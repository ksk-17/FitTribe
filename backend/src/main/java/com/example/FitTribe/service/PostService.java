package com.example.FitTribe.service;

import com.example.FitTribe.dto.CommentDto;
import com.example.FitTribe.dto.PostDto;
import com.example.FitTribe.entity.Comment;
import com.example.FitTribe.entity.Post;
import com.example.FitTribe.entity.User;
import com.example.FitTribe.repository.PostRepository;
import com.example.FitTribe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  private final UserRepository userRepository;

  private final CommentService commentService;

  public Optional<PostDto> getPostById(String id) {
    Post post =
        postRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "post not found, invalid id passed"));

    Page<CommentDto> comments =
        commentService.getCommentsByPostId(
            id, PageRequest.of(0, 10, Sort.by("createdAt").descending()));

    return Optional.ofNullable(buildPostDto(post, comments));
  }

  public Page<PostDto> getPosts(Pageable pageable) {
    Page<Post> page = postRepository.findAll(pageable);
    Page<PostDto> dtoPage =
        page.map(
            post -> {
              Page<CommentDto> comments =
                  commentService.getCommentsByPostId(
                      post.getId(), PageRequest.of(0, 10, Sort.by("createdAt").descending()));
              return buildPostDto(post, comments);
            });
    return dtoPage;
  }

  public void createPost(PostDto postDto) {
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
        Post.builder()
            .message(postDto.getMessage())
            .comments(new ArrayList<>())
            .likes(0)
            .user(user)
            .build();
    postRepository.save(post);
  }

  public void updatePost(String id, PostDto postDto) {
    UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Post post =
        postRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "post not found, invalid id passed"));

    if (!userDetails.getUsername().equals(post.getUser().getUsername())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not matched");
    }

    post.setMessage(postDto.getMessage());
    postRepository.save(post);
  }

  public void deletePost(String postId) {
    UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "post not found, invalid id passed"));

    if (!userDetails.getUsername().equals(post.getUser().getUsername())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not matched");
    }

    postRepository.deleteById(postId);
  }

  private PostDto buildPostDto(Post post, Page<CommentDto> comments) {
    return PostDto.builder()
        .id(post.getId())
        .user(post.getUser().getUsername())
        .message(post.getMessage())
        .likes(post.getLikes())
        .comments(comments)
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }
}
