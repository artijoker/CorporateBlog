package com.example.blog.server.controllers;

import com.example.blog.domain.entities.Post;
import com.example.blog.domain.exceptions.NotFoundPostException;
import com.example.blog.http.models.responses.ResponseModel;
import com.example.blog.domain.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService _postService;

    @Autowired
    public PostController(PostService postService) {
        _postService = postService;
    }

    @GetMapping("/get-posts")
    public ResponseModel<List<Post>> posts() {
        var response = new ResponseModel<List<Post>>();
        response.succeeded = true;
        response.statusCode = 200;
        response.result = _postService.getPublishedPosts();

        return response;
    }

    @GetMapping("/get-post/{id}")
    public ResponseModel<Post> postDetails(@PathVariable(value = "id") int id) {
        try {
            var post = _postService.getById(id);

            var response = new ResponseModel<Post>();
            response.succeeded = true;
            response.result = post;
            return response;

        } catch (NotFoundPostException ex) {
            var response = new ResponseModel<Post>();
            response.succeeded = false;
            response.message = "Error! Post not found";
            return response;
        }
    }

    @GetMapping("/all-posts-by-author")
    public ResponseModel<List<Post>> allPostsByAuthor(@RequestParam int accountId) {

        var response = new ResponseModel<List<Post>>();
        response.succeeded = true;
        response.statusCode = 200;
        response.result =  _postService.getPublishedPostsByAccountId(accountId);

        return response;
    }

    @GetMapping("/all-posts-in-category")
    public ResponseModel<List<Post>> allPostsInCategory(@RequestParam int categoryId) {

        var response = new ResponseModel<List<Post>>();
        response.succeeded = true;
        response.statusCode = 200;
        response.result =  _postService.getPublishedPostsByCategoryId(categoryId);

        return response;
    }

}
