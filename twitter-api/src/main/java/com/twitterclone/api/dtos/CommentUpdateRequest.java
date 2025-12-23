package com.twitterclone.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequest {
    @NotBlank(message = "Comment content cannot be empty")
    @Size(min = 1, max = 280, message = "Comment content must be between 1 and 280 characters")
    private String content;
}
