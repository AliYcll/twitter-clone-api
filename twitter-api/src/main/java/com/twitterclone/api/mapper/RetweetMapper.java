package com.twitterclone.api.mapper;

import com.twitterclone.api.dtos.responses.RetweetResponse;
import com.twitterclone.api.model.Retweet;

public final class RetweetMapper {

    private RetweetMapper() {
    }

    public static RetweetResponse toResponse(Retweet retweet) {
        if (retweet == null) {
            return null;
        }
        return new RetweetResponse(
                retweet.getTweet() != null ? retweet.getTweet().getId() : null,
                retweet.getCreatedAt(),
                UserMapper.toSummary(retweet.getUser())
        );
    }
}
