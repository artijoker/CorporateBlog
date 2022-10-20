package com.example.blog.server.controllers.admin;

import com.example.blog.domain.exceptions.NotFoundAccountException;
import com.example.blog.domain.exceptions.NotFoundCategoryException;
import com.example.blog.domain.exceptions.NotFoundPostException;
import com.example.blog.domain.services.PostService;
import com.example.blog.http.models.requests.AddingPostRequestsModel;
import com.example.blog.http.models.requests.UpdatePostRequestModel;
import com.example.blog.http.models.responses.PostResponseModel;
import com.example.blog.http.models.responses.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("admin/posts")
public class AdminPostController {
    private final PostService _postService;

    @Autowired
    public AdminPostController(PostService postService) {
        _postService = postService;
    }

    @GetMapping("/get-pending-posts")
    public ResponseModel<List<PostResponseModel>> getPendingPosts() {
        var response = new ResponseModel<List<PostResponseModel>>();
        response.setSucceeded(true);
        response.setResult( _postService.getPendingPosts());

        return response;
    }

    @PostMapping("/add-post-and-published")
    public ResponseModel<?> addPostAndPublished(@ModelAttribute AddingPostRequestsModel model){
        var response = new ResponseModel<>();
        try{
            _postService.addPostAndPublished(model.getAccountId(),
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

    @PostMapping("/publish-post")
    public ResponseModel<?> publishPost(@RequestParam int postId){
        var response = new ResponseModel<>();
        try{
            _postService.publishPost(postId);
            response.setSucceeded(true);
        } catch (NotFoundPostException e) {
            response.setSucceeded(false);
            response.setMessage("Статья не найдена");
        }
        return response;
    }

    @PostMapping("/reject-post")
    public ResponseModel<?> rejectPost(@RequestParam int postId){
        var response = new ResponseModel<>();
        try{
            _postService.rejectPost(postId);
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
            _postService.adminUpdatePost(model.getPostId(),
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
}
