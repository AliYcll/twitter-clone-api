package com.twitterclone.api.repository;

import com.twitterclone.api.model.Like;
import com.twitterclone.api.model.LikeId;
import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {
    Optional<Like> findByUserAndTweet(User user, Tweet tweet);
    void deleteByTweet(Tweet tweet);
}
