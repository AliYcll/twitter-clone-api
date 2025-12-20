package com.twitterclone.api.controller;

import com.twitterclone.api.dtos.LikeRequest;
import com.twitterclone.api.model.Like;
import com.twitterclone.api.model.User;
import com.twitterclone.api.service.LikeService;
import com.twitterclone.api.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final UserService userService;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userService.findByEmail(currentPrincipalName);
    }

    @PostMapping("/like")
    public ResponseEntity<Like> likeTweet(@Valid @RequestBody LikeRequest request) {
        User currentUser = getCurrentUser();
        Like newLike = likeService.likeTweet(request.getTweetId(), currentUser);
        if (newLike == null) {
            
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(newLike);
    }

    @PostMapping("/dislike")
    public ResponseEntity<Void> dislikeTweet(@Valid @RequestBody LikeRequest request) {
        User currentUser = getCurrentUser();
        boolean disliked = likeService.dislikeTweet(request.getTweetId(), currentUser);
        if (!disliked) {
            
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }
}

