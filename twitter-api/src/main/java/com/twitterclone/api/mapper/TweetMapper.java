package com.twitterclone.api.mapper;

import com.twitterclone.api.dtos.responses.TweetResponse;
import com.twitterclone.api.model.Tweet;

public final class TweetMapper {

    private TweetMapper() {
    }

    public static TweetResponse toResponse(Tweet tweet) {
        if (tweet == null) {
            return null;
        }
        return new TweetResponse(
                tweet.getId(),
                tweet.getContent(),
                tweet.getCreatedAt(),
                UserMapper.toSummary(tweet.getUser()),
                false,
                false
        );
    }
}
