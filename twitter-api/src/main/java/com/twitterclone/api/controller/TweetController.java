package com.twitterclone.api.controller;

import com.twitterclone.api.dtos.requests.TweetRequest;
import com.twitterclone.api.dtos.responses.TweetResponse;
import com.twitterclone.api.mapper.TweetMapper;
import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
import com.twitterclone.api.repository.LikeRepository;
import com.twitterclone.api.repository.RetweetRepository;
import com.twitterclone.api.service.TweetService;
import com.twitterclone.api.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tweets")
@AllArgsConstructor
public class TweetController {

    private final TweetService tweetService;
    private final UserService userService;
    private final LikeRepository likeRepository;
    private final RetweetRepository retweetRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userService.findByEmail(currentPrincipalName);
    }

    private User getCurrentUserOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String currentPrincipalName = authentication.getName();
        if (currentPrincipalName == null || "anonymousUser".equals(currentPrincipalName)) {
            return null;
        }
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

    private List<TweetResponse> toTweetResponses(List<Tweet> tweets, User currentUser) {
        return tweets.stream()
                .map(tweet -> toTweetResponse(tweet, currentUser))
                .toList();
    }

    @GetMapping
    public ResponseEntity<List<TweetResponse>> getAllTweets() {
        User currentUser = getCurrentUserOrNull();
        List<Tweet> tweets = tweetService.getAllTweets();
        return ResponseEntity.ok(toTweetResponses(tweets, currentUser));
    }

    @PostMapping
    public ResponseEntity<TweetResponse> createTweet(@Valid @RequestBody TweetRequest request) {
        User currentUser = getCurrentUser();
        Tweet newTweet = tweetService.createTweet(request, currentUser);
        return ResponseEntity.ok(toTweetResponse(newTweet, currentUser));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TweetResponse>> getTweetsByUserId(@PathVariable Long userId) {
        User currentUser = getCurrentUserOrNull();
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<Tweet> tweets = tweetService.getTweetsByUser(user);
        return ResponseEntity.ok(toTweetResponses(tweets, currentUser));
    }

    @GetMapping("/{tweetId}")
    public ResponseEntity<TweetResponse> getTweetById(@PathVariable Long tweetId) {
        User currentUser = getCurrentUserOrNull();
        Tweet tweet = tweetService.getTweetById(tweetId);
        if (tweet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toTweetResponse(tweet, currentUser));
    }

    @PutMapping("/{tweetId}")
    public ResponseEntity<TweetResponse> updateTweet(@PathVariable Long tweetId, @Valid @RequestBody TweetRequest request) {
        User currentUser = getCurrentUser();
        Tweet updatedTweet = tweetService.updateTweet(tweetId, request.getContent(), currentUser);
        if (updatedTweet == null) {
            
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(toTweetResponse(updatedTweet, currentUser));
    }

    @DeleteMapping("/{tweetId}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long tweetId) {
        User currentUser = getCurrentUser();
        boolean deleted = tweetService.deleteTweet(tweetId, currentUser);
        if (!deleted) {
             
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.noContent().build();
    }
}

