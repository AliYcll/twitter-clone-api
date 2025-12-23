package com.twitterclone.api.service;

import com.twitterclone.api.model.Retweet;
import com.twitterclone.api.model.RetweetId;
import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
import com.twitterclone.api.repository.RetweetRepository;
import com.twitterclone.api.repository.TweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetweetService {

    private final RetweetRepository retweetRepository;
    private final TweetRepository tweetRepository;

    public Retweet createRetweet(Long tweetId, User user) {
        Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
        if (tweetOptional.isEmpty()) {
            return null; 
        }
        Tweet tweet = tweetOptional.get();

        
        Optional<Retweet> existingRetweet = retweetRepository.findByUserAndTweet(user, tweet);
        if (existingRetweet.isPresent()) {
            return existingRetweet.get();
        }

        Retweet retweet = new Retweet();
        retweet.setId(new RetweetId(user.getId(), tweet.getId()));
        retweet.setUser(user);
        retweet.setTweet(tweet);
        retweet.setCreatedAt(LocalDateTime.now());

        return retweetRepository.save(retweet);
    }

    public boolean deleteRetweet(Long tweetId, User user) {
        Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
        if (tweetOptional.isEmpty()) {
            return false; 
        }
        Tweet tweet = tweetOptional.get();

        Optional<Retweet> retweetOptional = retweetRepository.findByUserAndTweet(user, tweet);
        if (retweetOptional.isEmpty()) {
            return false; 
        }

        retweetRepository.delete(retweetOptional.get());
        return true;
    }

    public List<Tweet> getRetweetedTweets(User user) {
        return retweetRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(Retweet::getTweet)
                .collect(Collectors.toList());
    }
}

