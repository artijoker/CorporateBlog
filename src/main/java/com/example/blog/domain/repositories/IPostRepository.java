package com.example.blog.domain.repositories;

import com.example.blog.domain.entities.Account;
import com.example.blog.domain.entities.Category;
import com.example.blog.domain.entities.Post;
import com.example.blog.domain.entities.PostStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
public interface IPostRepository extends CrudRepository<Post, Integer> {

    List<Post> getPostsByStatusId(Integer statusId);
    List<Post> getPostsByCategoryId(Integer categoryId);
    List<Post> getPostsByAccountId(Integer accountId);


    List<Post> getPostsByAccountIdAndStatusId( Integer accountId, Integer statusId);
    List<Post> getPostsByCategoryIdAndStatusId(Integer categoryId, Integer statusId);

    List<Post> getPostsByStatus(PostStatus status);
    List<Post> getPostsByCategory(Category category);
    List<Post> getPostsByAccount(Account account);

    List<Post> getPostsByAccountAndStatus(Account account, PostStatus status);
    List<Post> getPostsByCategoryAndStatus(Category category,PostStatus status);


    int countPostByCategoryIdAndStatusId(Integer categoryId, Integer statusId);
    int countPostByAccountIdAndStatusId( Integer accountId, Integer statusId);
}
