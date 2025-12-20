package com.twitterclone.api.controller;

import com.twitterclone.api.dtos.CommentRequest;
import com.twitterclone.api.model.Comment;
import com.twitterclone.api.model.User;
import com.twitterclone.api.service.CommentService;
import com.twitterclone.api.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userService.findByEmail(currentPrincipalName);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@Valid @RequestBody CommentRequest request) {
        User currentUser = getCurrentUser();
        Comment newComment = commentService.createComment(request.getTweetId(), request.getContent(), currentUser);
        if (newComment == null) {
            
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(newComment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody String content) {
        User currentUser = getCurrentUser();
        Comment updatedComment = commentService.updateComment(commentId, content, currentUser);
        if (updatedComment == null) {
            
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        User currentUser = getCurrentUser();
        boolean deleted = commentService.deleteComment(commentId, currentUser);
        if (!deleted) {
            
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.noContent().build();
    }
}

