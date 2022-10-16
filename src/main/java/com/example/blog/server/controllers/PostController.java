package com.example.blog.server.controllers;

import com.example.blog.domain.exceptions.NotFoundPostException;
import com.example.blog.http.models.responses.PostResponseModel;
import com.example.blog.http.models.responses.ResponseModel;
import com.example.blog.domain.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResponseModel<List<PostResponseModel>> posts() {
        var response = new ResponseModel<List<PostResponseModel>>();
        response.setSucceeded(true);
        response.setResult( _postService.getPublishedPosts());

        return response;
    }

    @GetMapping("/get-post/{id}")
    public ResponseModel<PostResponseModel> postDetails(@PathVariable(value = "id") int id) {
        try {
            var post = _postService.getPostById(id);

            var response = new ResponseModel<PostResponseModel>();
            response.setSucceeded(true);
            response.setResult(post);
            return response;

        } catch (NotFoundPostException ex) {
            var response = new ResponseModel<PostResponseModel>();
            response.setSucceeded(false);
            response.setMessage("Статья не найдена");
            return response;
        }
    }

    @GetMapping("/get-published-posts-by-account")
    public ResponseModel<List<PostResponseModel>> getPublishedPostsByAccount(@RequestParam int accountId) {

        var response = new ResponseModel<List<PostResponseModel>>();
        response.setSucceeded(true);
        response.setResult(_postService.getPublishedPostsByAccountId(accountId));

        return response;
    }

    @GetMapping("/get-published-posts-by-category")
    public ResponseModel<List<PostResponseModel>> publishedPostsByCategory(@RequestParam int categoryId) {

        var response = new ResponseModel<List<PostResponseModel>>();
        response.setSucceeded(true);
        response.setResult(_postService.getPublishedPostsByCategoryId(categoryId));

        return response;
    }
}
