package com.example.FitTribe.repository;

import com.example.FitTribe.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    @Query("select c from comments c where c.post = :postId")
    Page<Comment> findByPostId(@Param("postId") String postId, Pageable pageable);
}
