package com.example.blog.domain.repositories;

import com.example.blog.domain.entities.PostStatus;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IPostStatusRepository extends CrudRepository<PostStatus, Integer> {
   public PostStatus getPostStatusByName(String name);
   public PostStatus getPostStatusById(int id);
}
