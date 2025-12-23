package com.twitterclone.api.repository;

import com.twitterclone.api.model.Comment;
import com.twitterclone.api.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTweetOrderByCreatedAtDesc(Tweet tweet);
    void deleteByTweet(Tweet tweet);
}

