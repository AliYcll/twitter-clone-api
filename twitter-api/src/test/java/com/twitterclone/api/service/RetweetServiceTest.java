package com.twitterclone.api.service;

import com.twitterclone.api.model.Retweet;
import com.twitterclone.api.model.RetweetId;
import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
import com.twitterclone.api.repository.RetweetRepository;
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
class RetweetServiceTest {

    @Mock
    private RetweetRepository retweetRepository;

    @Mock
    private TweetRepository tweetRepository;

    @InjectMocks
    private RetweetService retweetService;

    private User user;
    private Tweet tweet;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        tweet = new Tweet();
        tweet.setId(101L);
    }

    @Test
    void createRetweet_WhenTweetExistsAndNotRetweeted_ShouldSaveAndReturnRetweet() {
        when(tweetRepository.findById(101L)).thenReturn(Optional.of(tweet));
        when(retweetRepository.findByUserAndTweet(user, tweet)).thenReturn(Optional.empty());
        when(retweetRepository.save(any(Retweet.class))).thenAnswer(i -> {
            Retweet rt = i.getArgument(0);
            rt.setCreatedAt(LocalDateTime.now());
            return rt;
        });

        Retweet result = retweetService.createRetweet(101L, user);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(tweet, result.getTweet());
        assertNotNull(result.getCreatedAt());
        verify(retweetRepository, times(1)).save(any(Retweet.class));
    }

    @Test
    void createRetweet_WhenAlreadyRetweeted_ShouldReturnNull() {
        Retweet existingRetweet = new Retweet(new RetweetId(user.getId(), tweet.getId()), user, tweet, LocalDateTime.now());
        when(tweetRepository.findById(101L)).thenReturn(Optional.of(tweet));
        when(retweetRepository.findByUserAndTweet(user, tweet)).thenReturn(Optional.of(existingRetweet));

        Retweet result = retweetService.createRetweet(101L, user);

        assertNull(result);
        verify(retweetRepository, never()).save(any(Retweet.class));
    }
    
    @Test
    void deleteRetweet_WhenRetweetExists_ShouldDeleteAndReturnTrue() {
        Retweet existingRetweet = new Retweet(new RetweetId(user.getId(), tweet.getId()), user, tweet, LocalDateTime.now());
        when(tweetRepository.findById(101L)).thenReturn(Optional.of(tweet));
        when(retweetRepository.findByUserAndTweet(user, tweet)).thenReturn(Optional.of(existingRetweet));
        doNothing().when(retweetRepository).delete(existingRetweet);

        boolean result = retweetService.deleteRetweet(101L, user);

        assertTrue(result);
        verify(retweetRepository, times(1)).delete(existingRetweet);
    }

    @Test
    void deleteRetweet_WhenRetweetDoesNotExist_ShouldReturnFalse() {
        when(tweetRepository.findById(101L)).thenReturn(Optional.of(tweet));
        when(retweetRepository.findByUserAndTweet(user, tweet)).thenReturn(Optional.empty());

        boolean result = retweetService.deleteRetweet(101L, user);

        assertFalse(result);
        verify(retweetRepository, never()).delete(any(Retweet.class));
    }
}

