package com.example.blog.server.controllers;

import com.example.blog.domain.exceptions.NotFoundAccountException;
import com.example.blog.domain.exceptions.NotFoundCategoryException;
import com.example.blog.domain.exceptions.NotFoundPostException;
import com.example.blog.http.models.requests.AddingPostRequestsModel;
import com.example.blog.http.models.requests.UpdatePostRequestModel;
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
    public ResponseModel<List<PostResponseModel>> getPosts() {
        var response = new ResponseModel<List<PostResponseModel>>();
        response.setSucceeded(true);
        response.setResult( _postService.getPublishedPosts());

        return response;
    }

    @PostMapping("/get-post")
    public ResponseModel<PostResponseModel> getPostById(@RequestParam int postId) {
        var response = new ResponseModel<PostResponseModel>();
        try {
            var post = _postService.getPostById(postId);
            response.setSucceeded(true);
            response.setResult(post);
        } catch (NotFoundPostException ex) {
            response.setSucceeded(false);
            response.setMessage("Статья не найдена");
        }
        return response;
    }

    @PostMapping("/get-posts-by-category")
    public ResponseModel<List<PostResponseModel>> getPublishedPostsByCategoryId(@RequestParam int categoryId) {

        var response = new ResponseModel<List<PostResponseModel>>();
        response.setSucceeded(true);
        response.setResult(_postService.getPublishedPostsByCategoryId(categoryId));

        return response;
    }

    @PostMapping("/get-draft-posts-by-account")
    public ResponseModel<List<PostResponseModel>> getDraftPostsByAccountId(@RequestParam int accountId) {

        var response = new ResponseModel<List<PostResponseModel>>();
        response.setSucceeded(true);
        response.setResult(_postService.getDraftPostsByAccountId(accountId));

        return response;
    }

    @PostMapping("/get-pending-posts-by-account")
    public ResponseModel<List<PostResponseModel>> getPendingPostsByAccountId(@RequestParam int accountId) {

        var response = new ResponseModel<List<PostResponseModel>>();
        response.setSucceeded(true);
        response.setResult(_postService.getPendingPostsByAccountId(accountId));

        return response;
    }

    @PostMapping("/get-published-posts-by-account")
    public ResponseModel<List<PostResponseModel>> getPublishedPostsByAccountId(@RequestParam int accountId) {

        var response = new ResponseModel<List<PostResponseModel>>();
        response.setSucceeded(true);
        response.setResult(_postService.getPublishedPostsByAccountId(accountId));

        return response;
    }

    @PostMapping("/get-rejected-posts-by-account")
    public ResponseModel<List<PostResponseModel>> getRejectedPostsByAccountId(@RequestParam int accountId) {

        var response = new ResponseModel<List<PostResponseModel>>();
        response.setSucceeded(true);
        response.setResult(_postService.getRejectedPostsByAccountId(accountId));

        return response;
    }

    @PostMapping("/add-post")
    public ResponseModel<?> addDraftPost(@ModelAttribute AddingPostRequestsModel model){
        var response = new ResponseModel<>();
        try{
            _postService.addDraftPost(model.getAccountId(),
                    model.getTitle(),
                    model.getAnons(),
                    model.getFullText(),
                    model.getCategoryId()
            );
            response.setSucceeded(true);
        } catch (NotFoundAccountException e) {
            response.setSucceeded(false);
            response.setMessage("Учетная запись не найдена");
        } catch (NotFoundCategoryException e) {
            response.setSucceeded(false);
            response.setMessage("Категория не найдена");
        }
        return response;
    }

    @PostMapping("/add-post-and-send-to-moderation")
    public ResponseModel<?> addPostAndSendToModeration(@ModelAttribute AddingPostRequestsModel model){
        var response = new ResponseModel<>();
        try{
            _postService.addPostAndSendToModeration(model.getAccountId(),
                    model.getTitle(),
                    model.getAnons(),
                    model.getFullText(),
                    model.getCategoryId()
            );
            response.setSucceeded(true);
        } catch (NotFoundAccountException e) {
            response.setSucceeded(false);
            response.setMessage("Учетная запись не найдена");
        } catch (NotFoundCategoryException e) {
            response.setSucceeded(false);
            response.setMessage("Категория не найдена");
        }
        return response;
    }

    @PostMapping("/send-post-moderation")
    public ResponseModel<?> sendPostModeration(@RequestParam int postId){
        var response = new ResponseModel<>();
        try{
            _postService.sendPostModeration(postId);
            response.setSucceeded(true);
        } catch (NotFoundPostException e) {
            response.setSucceeded(false);
            response.setMessage("Статья не найдена");
        }
        return response;
    }

    @PostMapping("/send-post-to-draft")
    public ResponseModel<?> sendPostToDraft(@RequestParam int postId){
        var response = new ResponseModel<>();
        try{
            _postService.sendPostToDraft(postId);
            response.setSucceeded(true);
        } catch (NotFoundPostException e) {
            response.setSucceeded(false);
            response.setMessage("Статья не найдена");
        }
        return response;
    }

    @PostMapping("/update-post")
    public ResponseModel<?> updatePost(@ModelAttribute UpdatePostRequestModel model){
        var response = new ResponseModel<>();
        try{
            _postService.updatePost(model.getPostId(),
                    model.getTitle(),
                    model.getAnons(),
                    model.getFullText(),
                    model.getCategoryId()
            );
            response.setSucceeded(true);
        } catch (NotFoundCategoryException e) {
            response.setSucceeded(false);
            response.setMessage("Категория не найдена");
        } catch (NotFoundPostException e) {
            response.setSucceeded(false);
            response.setMessage("Статья не найдена");
        }
        return response;
    }

    @PostMapping("/delete-post")
    public ResponseModel<?> removePost(@RequestParam int postId){
        _postService.removePost(postId);
        var response = new ResponseModel<>();
        response.setSucceeded(true);

        return response;
    }
}
