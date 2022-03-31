package com.example.demo.repository;

import com.example.demo.domain.Board;
import com.example.demo.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBoard(Board board);
}
