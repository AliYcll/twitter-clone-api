package com.twitterclone.api.mapper;

import com.twitterclone.api.dtos.responses.UserSummaryResponse;
import com.twitterclone.api.model.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserSummaryResponse toSummary(User user) {
        if (user == null) {
            return null;
        }
        return new UserSummaryResponse(user.getId(), user.getPublicUsername());
    }
}
