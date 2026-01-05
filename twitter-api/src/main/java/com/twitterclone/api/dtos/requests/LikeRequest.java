package com.twitterclone.api.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeRequest {
    @NotNull
    private Long tweetId;
}
