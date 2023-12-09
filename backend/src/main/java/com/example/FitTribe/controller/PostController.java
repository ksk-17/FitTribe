package com.example.FitTribe.controller;

import com.example.FitTribe.dto.PostDto;
import com.example.FitTribe.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {
  private final PostService postService;

  @GetMapping("/{id}")
  public ResponseEntity<Optional<PostDto>> getPostById(@PathVariable("id") String id) {
    return ResponseEntity.ok(postService.getPostById(id));
  }

  @GetMapping("/")
  public ResponseEntity<Page<PostDto>> getPosts(
      @PageableDefault(value = 10, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok(postService.getPosts(pageable));
  }

  @PostMapping("/create")
  public ResponseEntity<Void> createPost(@RequestBody PostDto postDto){
    postService.createPost(postDto);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PatchMapping("/update/{id}")
  public ResponseEntity<Void> updatePost(@PathVariable("id") String id, @RequestBody PostDto postDto){
    postService.updatePost(id, postDto);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> deletePost(@PathVariable("id") String id){
    postService.deletePost(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
