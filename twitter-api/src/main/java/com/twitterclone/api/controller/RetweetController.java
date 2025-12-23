package com.twitterclone.api.controller;

import com.twitterclone.api.dtos.RetweetRequest;
import com.twitterclone.api.model.Retweet;
import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
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

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userService.findByEmail(currentPrincipalName);
    }

    @PostMapping
    public ResponseEntity<Retweet> createRetweet(@Valid @RequestBody RetweetRequest request) {
        User currentUser = getCurrentUser();
        Retweet newRetweet = retweetService.createRetweet(request.getTweetId(), currentUser);
        if (newRetweet == null) {
            
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(newRetweet);
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
    public ResponseEntity<List<Tweet>> getMyRetweets() {
        User currentUser = getCurrentUser();
        List<Tweet> tweets = retweetService.getRetweetedTweets(currentUser);
        return ResponseEntity.ok(tweets);
    }
}

