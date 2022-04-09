package com.one.miniproject.repository;

import com.one.miniproject.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoard_Boardid(Long id);
}
