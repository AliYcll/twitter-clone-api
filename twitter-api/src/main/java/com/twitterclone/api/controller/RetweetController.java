package com.twitterclone.api.controller;

import com.twitterclone.api.dtos.requests.RetweetRequest;
import com.twitterclone.api.dtos.responses.RetweetResponse;
import com.twitterclone.api.dtos.responses.TweetResponse;
import com.twitterclone.api.mapper.RetweetMapper;
import com.twitterclone.api.mapper.TweetMapper;
import com.twitterclone.api.model.Retweet;
import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
import com.twitterclone.api.repository.LikeRepository;
import com.twitterclone.api.repository.RetweetRepository;
import com.twitterclone.api.service.RetweetService;
import com.twitterclone.api.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/retweets")
@AllArgsConstructor
public class RetweetController {

    private final RetweetService retweetService;
    private final UserService userService;
    private final LikeRepository likeRepository;
    private final RetweetRepository retweetRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userService.findByEmail(currentPrincipalName);
    }

    private TweetResponse toTweetResponse(Tweet tweet, User currentUser) {
        TweetResponse response = TweetMapper.toResponse(tweet);
        if (tweet == null || currentUser == null) {
            return response;
        }
        boolean liked = likeRepository.findByUserAndTweet(currentUser, tweet).isPresent();
        boolean retweeted = retweetRepository.findByUserAndTweet(currentUser, tweet).isPresent();
        response.setLikedByCurrentUser(liked);
        response.setRetweetedByCurrentUser(retweeted);
        return response;
    }

    @PostMapping
    public ResponseEntity<RetweetResponse> createRetweet(@Valid @RequestBody RetweetRequest request) {
        User currentUser = getCurrentUser();
        Retweet newRetweet = retweetService.createRetweet(request.getTweetId(), currentUser);
        if (newRetweet == null) {
            
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(RetweetMapper.toResponse(newRetweet));
    }

    @DeleteMapping("/{tweetId}")
    public ResponseEntity<Void> deleteRetweet(@PathVariable Long tweetId) {
        User currentUser = getCurrentUser();
        boolean deleted = retweetService.deleteRetweet(tweetId, currentUser);
        if (!deleted) {
            
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<List<TweetResponse>> getMyRetweets() {
        User currentUser = getCurrentUser();
        List<Tweet> tweets = retweetService.getRetweetedTweets(currentUser);
        List<TweetResponse> response = tweets.stream()
                .map(tweet -> toTweetResponse(tweet, currentUser))
                .toList();
        return ResponseEntity.ok(response);
    }
}

