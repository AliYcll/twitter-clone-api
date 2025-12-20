package com.twitterclone.api.repository;

import com.twitterclone.api.model.Retweet;
import com.twitterclone.api.model.RetweetId;
import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetweetRepository extends JpaRepository<Retweet, RetweetId> {
    Optional<Retweet> findByUserAndTweet(User user, Tweet tweet);
}

