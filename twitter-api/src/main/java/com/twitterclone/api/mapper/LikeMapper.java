package com.twitterclone.api.mapper;

import com.twitterclone.api.dtos.responses.LikeResponse;
import com.twitterclone.api.model.Like;

public final class LikeMapper {

    private LikeMapper() {
    }

    public static LikeResponse toResponse(Like like) {
        if (like == null) {
            return null;
        }
        return new LikeResponse(
                like.getTweet() != null ? like.getTweet().getId() : null,
                UserMapper.toSummary(like.getUser())
        );
    }
}
