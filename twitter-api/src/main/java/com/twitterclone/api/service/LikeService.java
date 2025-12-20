package com.twitterclone.api.service;

import com.twitterclone.api.model.Like;
import com.twitterclone.api.model.LikeId;
import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
import com.twitterclone.api.repository.LikeRepository;
import com.twitterclone.api.repository.TweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final TweetRepository tweetRepository;

    public Like likeTweet(Long tweetId, User user) {
        Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
        if (tweetOptional.isEmpty()) {
            return null; 
        }
        Tweet tweet = tweetOptional.get();

        
        if (likeRepository.findByUserAndTweet(user, tweet).isPresent()) {
            return null; 
        }

        Like like = new Like();
        like.setId(new LikeId(user.getId(), tweet.getId()));
        like.setUser(user);
        like.setTweet(tweet);

        return likeRepository.save(like);
    }

    public boolean dislikeTweet(Long tweetId, User user) {
        Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
        if (tweetOptional.isEmpty()) {
            return false; 
        }
        Tweet tweet = tweetOptional.get();

        Optional<Like> likeOptional = likeRepository.findByUserAndTweet(user, tweet);
        if (likeOptional.isEmpty()) {
            return false; 
        }

        likeRepository.delete(likeOptional.get());
        return true;
    }
}

