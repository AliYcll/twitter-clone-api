package com.twitterclone.api.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RetweetRequest {
    @NotNull
    private Long tweetId;
}

