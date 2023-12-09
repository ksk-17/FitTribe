package com.example.FitTribe.repository;

import com.example.FitTribe.entity.Post;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface PostRepository
    extends JpaRepository<Post, String>, PagingAndSortingRepository<Post, String> {
}
