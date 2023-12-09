package com.example.FitTribe.service;

import com.example.FitTribe.dto.CommentDto;
import com.example.FitTribe.entity.Comment;
import com.example.FitTribe.entity.Post;
import com.example.FitTribe.entity.User;
import com.example.FitTribe.repository.CommentRepository;
import com.example.FitTribe.repository.PostRepository;
import com.example.FitTribe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;

  private final PostRepository postRepository;

  private final UserRepository userRepository;

  public Page<CommentDto> getCommentsByPostId(String postId, Pageable pageable) {
    Page<Comment> comments = commentRepository.findAll(pageable);
    Page<CommentDto> commentPage = comments.map(this::buildCommentDto);
    return commentPage;
  }

  public void createComment(String postId, CommentDto commentDto) {
    UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    User user =
        userRepository
            .findByUserName(userDetails.getUsername())
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "user not found, invalid token"));

    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "post not found, post id is invalid"));

    Comment comment =
        Comment.builder()
            .user(user)
            .post(post)
            .description(commentDto.getDescription())
            .likes(0)
            .build();
    commentRepository.save(comment);
  }

  public void updateComment(String commentId, CommentDto commentDto) {
    UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Comment comment =
        commentRepository
            .findById(commentId)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "comment not found, comment id is invalid"));

    if (!userDetails.getUsername().equals(comment.getUser().getUsername())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not matched");
    }

    comment.setDescription(commentDto.getDescription());
    commentRepository.save(comment);
  }

  public void deleteComment(String commentId) {
    UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Comment comment =
        commentRepository
            .findById(commentId)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "comment not found, comment id is invalid"));

    if (!userDetails.getUsername().equals(comment.getUser().getUsername())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not matched");
    }

    commentRepository.deleteById(commentId);
  }

  private CommentDto buildCommentDto(Comment comment) {
    return CommentDto.builder()
        .id(comment.getId())
        .user(comment.getUser().getUsername())
        .description(comment.getDescription())
        .likes(comment.getLikes())
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .build();
  }
}
