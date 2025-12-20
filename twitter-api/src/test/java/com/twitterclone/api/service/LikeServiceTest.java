package com.twitterclone.api.service;

import com.twitterclone.api.model.Like;
import com.twitterclone.api.model.LikeId;
import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
import com.twitterclone.api.repository.LikeRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private TweetRepository tweetRepository;

    @InjectMocks
    private LikeService likeService;

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
    void likeTweet_WhenTweetExistsAndNotLiked_ShouldSaveAndReturnLike() {
        when(tweetRepository.findById(101L)).thenReturn(Optional.of(tweet));
        when(likeRepository.findByUserAndTweet(user, tweet)).thenReturn(Optional.empty());
        when(likeRepository.save(any(Like.class))).thenAnswer(i -> i.getArguments()[0]);

        Like result = likeService.likeTweet(101L, user);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(tweet, result.getTweet());
        verify(likeRepository, times(1)).save(any(Like.class));
    }

    @Test
    void likeTweet_WhenTweetExistsAndAlreadyLiked_ShouldReturnNull() {
        Like existingLike = new Like(new LikeId(user.getId(), tweet.getId()), user, tweet);
        when(tweetRepository.findById(101L)).thenReturn(Optional.of(tweet));
        when(likeRepository.findByUserAndTweet(user, tweet)).thenReturn(Optional.of(existingLike));

        Like result = likeService.likeTweet(101L, user);

        assertNull(result);
        verify(likeRepository, never()).save(any(Like.class));
    }

    @Test
    void likeTweet_WhenTweetDoesNotExist_ShouldReturnNull() {
        when(tweetRepository.findById(999L)).thenReturn(Optional.empty());

        Like result = likeService.likeTweet(999L, user);

        assertNull(result);
        verify(likeRepository, never()).save(any(Like.class));
    }

    @Test
    void dislikeTweet_WhenLikeExists_ShouldDeleteAndReturnTrue() {
        Like existingLike = new Like(new LikeId(user.getId(), tweet.getId()), user, tweet);
        when(tweetRepository.findById(101L)).thenReturn(Optional.of(tweet));
        when(likeRepository.findByUserAndTweet(user, tweet)).thenReturn(Optional.of(existingLike));
        doNothing().when(likeRepository).delete(existingLike);

        boolean result = likeService.dislikeTweet(101L, user);

        assertTrue(result);
        verify(likeRepository, times(1)).delete(existingLike);
    }

    @Test
    void dislikeTweet_WhenLikeDoesNotExist_ShouldReturnFalse() {
        when(tweetRepository.findById(101L)).thenReturn(Optional.of(tweet));
        when(likeRepository.findByUserAndTweet(user, tweet)).thenReturn(Optional.empty());

        boolean result = likeService.dislikeTweet(101L, user);

        assertFalse(result);
        verify(likeRepository, never()).delete(any(Like.class));
    }
}

