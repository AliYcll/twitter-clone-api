package com.twitterclone.api.mapper;

import com.twitterclone.api.dtos.responses.CommentResponse;
import com.twitterclone.api.model.Comment;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static CommentResponse toResponse(Comment comment) {
        if (comment == null) {
            return null;
        }
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                UserMapper.toSummary(comment.getUser())
        );
    }
}
