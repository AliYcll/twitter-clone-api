package com.twitterclone.api.service;

import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
import com.twitterclone.api.repository.TweetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;

    @InjectMocks
    private TweetService tweetService;

    private User user1;
    private User user2;
    private Tweet tweet;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        tweet = new Tweet();
        tweet.setId(101L);
        tweet.setUser(user1);
        tweet.setContent("Original content");
        tweet.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createTweet_ShouldSaveAndReturnTweet() {
        com.twitterclone.api.dtos.TweetRequest request = new com.twitterclone.api.dtos.TweetRequest();
        request.setContent("Original content");
        
        when(tweetRepository.save(any(Tweet.class))).thenAnswer(i -> {
            Tweet savedTweet = i.getArgument(0);
            savedTweet.setId(101L); 
            return savedTweet;
        });

        Tweet result = tweetService.createTweet(request, user1);

        assertNotNull(result);
        assertEquals("Original content", result.getContent());
        assertEquals(user1, result.getUser());
        verify(tweetRepository, times(1)).save(any(Tweet.class));
    }

    @Test
    void updateTweet_WhenUserIsOwner_ShouldUpdateAndReturnTweet() {
        when(tweetRepository.findById(101L)).thenReturn(Optional.of(tweet));
        when(tweetRepository.save(any(Tweet.class))).thenAnswer(i -> i.getArguments()[0]);

        Tweet result = tweetService.updateTweet(101L, "Updated content", user1);

        assertNotNull(result);
        assertEquals("Updated content", result.getContent());
        verify(tweetRepository, times(1)).findById(101L);
        verify(tweetRepository, times(1)).save(tweet);
    }

    @Test
    void updateTweet_WhenUserIsNotOwner_ShouldReturnNull() {
        when(tweetRepository.findById(101L)).thenReturn(Optional.of(tweet));

        Tweet result = tweetService.updateTweet(101L, "Updated content", user2);

        assertNull(result);
        verify(tweetRepository, times(1)).findById(101L);
        verify(tweetRepository, never()).save(any(Tweet.class));
    }
    
    @Test
    void updateTweet_WhenTweetNotFound_ShouldReturnNull() {
        when(tweetRepository.findById(999L)).thenReturn(Optional.empty());

        Tweet result = tweetService.updateTweet(999L, "content", user1);

        assertNull(result);
        verify(tweetRepository, times(1)).findById(999L);
        verify(tweetRepository, never()).save(any(Tweet.class));
    }

    @Test
    void deleteTweet_WhenUserIsOwner_ShouldDeleteTweetAndReturnTrue() {
        when(tweetRepository.findById(101L)).thenReturn(Optional.of(tweet));
        doNothing().when(tweetRepository).deleteById(101L);

        boolean result = tweetService.deleteTweet(101L, user1);

        assertTrue(result);
        verify(tweetRepository, times(1)).findById(101L);
        verify(tweetRepository, times(1)).deleteById(101L);
    }

    @Test
    void deleteTweet_WhenUserIsNotOwner_ShouldNotDeleteAndReturnFalse() {
        when(tweetRepository.findById(101L)).thenReturn(Optional.of(tweet));

        boolean result = tweetService.deleteTweet(101L, user2);

        assertFalse(result);
        verify(tweetRepository, times(1)).findById(101L);
        verify(tweetRepository, never()).deleteById(anyLong());
    }
    
    @Test
    void deleteTweet_WhenTweetNotFound_ShouldReturnFalse() {
        when(tweetRepository.findById(999L)).thenReturn(Optional.empty());

        boolean result = tweetService.deleteTweet(999L, user1);

        assertFalse(result);
        verify(tweetRepository, times(1)).findById(999L);
        verify(tweetRepository, never()).deleteById(anyLong());
    }
}

