package com.twitterclone.api.service;

import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
import com.twitterclone.api.repository.TweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TweetService {

    private final TweetRepository tweetRepository;

    public Tweet createTweet(String content, User user) {
        Tweet tweet = new Tweet();
        tweet.setContent(content);
        tweet.setUser(user);
        tweet.setCreatedAt(LocalDateTime.now());
        return tweetRepository.save(tweet);
    }

    public List<Tweet> getTweetsByUser(User user) {
        return tweetRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Tweet getTweetById(Long tweetId) {
        return tweetRepository.findById(tweetId).orElse(null);
    }

    public Tweet updateTweet(Long tweetId, String content, User user) {
        Tweet tweet = getTweetById(tweetId);
        if (tweet == null) {
            return null; 
        }
        
        if (!Objects.equals(tweet.getUser().getId(), user.getId())) {
            
            return null; 
        }
        tweet.setContent(content);
        return tweetRepository.save(tweet);
    }

    public boolean deleteTweet(Long tweetId, User user) {
        Tweet tweet = getTweetById(tweetId);
        if (tweet == null) {
            return false; 
        }
        
        if (!Objects.equals(tweet.getUser().getId(), user.getId())) {
            
            return false; 
        }
        tweetRepository.deleteById(tweetId);
        return true;
    }
}

