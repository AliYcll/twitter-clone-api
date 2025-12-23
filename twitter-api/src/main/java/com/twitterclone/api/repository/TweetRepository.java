package com.twitterclone.api.repository;

import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findByUserOrderByCreatedAtDesc(User user);
    List<Tweet> findAllByOrderByCreatedAtDesc();
}

