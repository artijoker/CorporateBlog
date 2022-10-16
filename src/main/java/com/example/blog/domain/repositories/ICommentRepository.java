package com.example.blog.domain.repositories;

import com.example.blog.domain.entities.*;

import com.example.blog.domain.entities.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ICommentRepository extends CrudRepository<Comment, Integer> {
    List<Comment> findCommentsByAccountId(int accountId);
    List<Comment> findCommentsByPostId(int postId);
    List<Comment> findCommentsByPostIdAndAccountId(int postId, int accountId);
}
