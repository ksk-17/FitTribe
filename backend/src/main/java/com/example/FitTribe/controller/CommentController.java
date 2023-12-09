package com.example.FitTribe.controller;

import com.example.FitTribe.dto.CommentDto;
import com.example.FitTribe.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
  private final CommentService commentService;

  @GetMapping("/{postId}")
  public ResponseEntity<Page<CommentDto>> getCommentsByPostId(
      @PathVariable("postId") String postId,
      @PageableDefault(value = 10, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.ok(commentService.getCommentsByPostId(postId, pageable));
  }

  @PostMapping("/create/{postId}")
  public ResponseEntity<Void> createComment(
      @PathVariable("postId") String postId, @RequestBody CommentDto commentDto) {
    commentService.createComment(postId, commentDto);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PatchMapping("/update/{id}")
  public ResponseEntity<Void> updateComment(
      @PathVariable("id") String commentId, @RequestBody CommentDto commentDto) {
    commentService.updateComment(commentId, commentDto);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> deleteComment(@PathVariable("id") String commentId) {
    commentService.deleteComment(commentId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
