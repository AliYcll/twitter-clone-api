package com.twitterclone.api.service;

import com.twitterclone.api.model.Comment;
import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
import com.twitterclone.api.repository.CommentRepository;
import com.twitterclone.api.repository.TweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;

    public Comment createComment(Long tweetId, String content, User user) {
        Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
        if (tweetOptional.isEmpty()) {
            return null; 
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setTweet(tweetOptional.get());
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, String content, User user) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isEmpty()) {
            return null; 
        }

        Comment comment = commentOptional.get();
        
        if (!Objects.equals(comment.getUser().getId(), user.getId())) {
            return null; 
        }

        comment.setContent(content);
        return commentRepository.save(comment);
    }

    public boolean deleteComment(Long commentId, User user) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isEmpty()) {
            return false; 
        }

        Comment comment = commentOptional.get();
        Tweet tweet = comment.getTweet();

        
        boolean isCommentOwner = Objects.equals(comment.getUser().getId(), user.getId());
        boolean isTweetOwner = Objects.equals(tweet.getUser().getId(), user.getId());

        if (!isCommentOwner && !isTweetOwner) {
            return false; 
        }

        commentRepository.deleteById(commentId);
        return true;
    }
}

