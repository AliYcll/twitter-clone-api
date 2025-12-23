package com.twitterclone.api.controller;

import com.twitterclone.api.dtos.TweetRequest;
import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
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

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userService.findByEmail(currentPrincipalName);
    }

    @GetMapping
    public ResponseEntity<List<Tweet>> getAllTweets() {
        List<Tweet> tweets = tweetService.getAllTweets();
        return ResponseEntity.ok(tweets);
    }

    @PostMapping
    public ResponseEntity<Tweet> createTweet(@Valid @RequestBody TweetRequest request) {
        User currentUser = getCurrentUser();
        Tweet newTweet = tweetService.createTweet(request, currentUser);
        return ResponseEntity.ok(newTweet);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Tweet>> getTweetsByUserId(@PathVariable Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<Tweet> tweets = tweetService.getTweetsByUser(user);
        return ResponseEntity.ok(tweets);
    }

    @GetMapping("/{tweetId}")
    public ResponseEntity<Tweet> getTweetById(@PathVariable Long tweetId) {
        Tweet tweet = tweetService.getTweetById(tweetId);
        if (tweet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tweet);
    }

    @PutMapping("/{tweetId}")
    public ResponseEntity<Tweet> updateTweet(@PathVariable Long tweetId, @Valid @RequestBody TweetRequest request) {
        User currentUser = getCurrentUser();
        Tweet updatedTweet = tweetService.updateTweet(tweetId, request.getContent(), currentUser);
        if (updatedTweet == null) {
            
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(updatedTweet);
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

