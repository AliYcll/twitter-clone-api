package com.twitterclone.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {

    @EmbeddedId
    private LikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") 
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tweetId") 
    @JoinColumn(name = "tweet_id", nullable = false)
    @JsonIgnore
    private Tweet tweet;
}

