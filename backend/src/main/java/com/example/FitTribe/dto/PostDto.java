package com.example.FitTribe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private String id;
    private String user;
    private String message;
    private long likes;
    private Page<CommentDto> comments;
    private Date createdAt;
    private Date updatedAt;
}
