package com.twitterclone.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {
    @NotNull
    private Long tweetId;

    @NotBlank
    @Size(max = 280)
    private String content;
}

