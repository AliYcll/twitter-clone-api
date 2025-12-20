package com.twitterclone.api.service;

import com.twitterclone.api.model.Comment;
import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
import com.twitterclone.api.repository.CommentRepository;
import com.twitterclone.api.repository.TweetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TweetRepository tweetRepository;

    @InjectMocks
    private CommentService commentService;

    private User tweetOwner;
    private User commentOwner;
    private User otherUser;
    private Tweet tweet;
    private Comment comment;

    @BeforeEach
    void setUp() {
        tweetOwner = new User();
        tweetOwner.setId(1L);

        commentOwner = new User();
        commentOwner.setId(2L);
        
        otherUser = new User();
        otherUser.setId(3L);

        tweet = new Tweet();
        tweet.setId(101L);
        tweet.setUser(tweetOwner);

        comment = new Comment();
        comment.setId(202L);
        comment.setUser(commentOwner);
        comment.setTweet(tweet);
        comment.setContent("A comment");
    }

    @Test
    void createComment_WhenTweetExists_ShouldSaveAndReturnComment() {
        when(tweetRepository.findById(101L)).thenReturn(Optional.of(tweet));
        when(commentRepository.save(any(Comment.class))).thenAnswer(i -> i.getArguments()[0]);

        Comment result = commentService.createComment(101L, "New comment", commentOwner);

        assertNotNull(result);
        assertEquals("New comment", result.getContent());
        assertEquals(commentOwner, result.getUser());
        assertEquals(tweet, result.getTweet());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void createComment_WhenTweetDoesNotExist_ShouldReturnNull() {
        when(tweetRepository.findById(999L)).thenReturn(Optional.empty());

        Comment result = commentService.createComment(999L, "New comment", commentOwner);

        assertNull(result);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void deleteComment_WhenUserIsCommentOwner_ShouldDeleteAndReturnTrue() {
        when(commentRepository.findById(202L)).thenReturn(Optional.of(comment));

        boolean result = commentService.deleteComment(202L, commentOwner);

        assertTrue(result);
        verify(commentRepository, times(1)).deleteById(202L);
    }

    @Test
    void deleteComment_WhenUserIsTweetOwner_ShouldDeleteAndReturnTrue() {
        when(commentRepository.findById(202L)).thenReturn(Optional.of(comment));

        boolean result = commentService.deleteComment(202L, tweetOwner);

        assertTrue(result);
        verify(commentRepository, times(1)).deleteById(202L);
    }

    @Test
    void deleteComment_WhenUserIsNeitherOwner_ShouldNotDeleteAndReturnFalse() {
        when(commentRepository.findById(202L)).thenReturn(Optional.of(comment));

        boolean result = commentService.deleteComment(202L, otherUser);

        assertFalse(result);
        verify(commentRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteComment_WhenCommentNotFound_ShouldReturnFalse() {
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        boolean result = commentService.deleteComment(999L, tweetOwner);

        assertFalse(result);
        verify(commentRepository, never()).deleteById(anyLong());
    }
}

