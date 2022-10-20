package com.example.blog.domain.services;

import com.example.blog.domain.entities.PostStatus;
import com.example.blog.domain.exceptions.NotFoundAccountException;
import com.example.blog.domain.exceptions.NotFoundCategoryException;
import com.example.blog.domain.exceptions.NotFoundPostException;
import com.example.blog.domain.repositories.IAccountRepository;
import com.example.blog.domain.repositories.ICategoryRepository;
import com.example.blog.domain.repositories.IPostRepository;
import com.example.blog.domain.entities.Post;
import com.example.blog.domain.repositories.IPostStatusRepository;
import com.example.blog.http.models.responses.PostResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final IPostRepository postRepository;
    private final IPostStatusRepository postStatusRepository;
    private final ICategoryRepository categoryRepository;
    private final IAccountRepository accountRepository;

    @Autowired
    public PostService(IPostRepository postRepository,
                       IPostStatusRepository postStatusRepository,
                       ICategoryRepository categoryRepository,
                       IAccountRepository accountRepository) {
        this.postRepository = postRepository;
        this.postStatusRepository = postStatusRepository;
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
    }

    public PostResponseModel getPostById(int postId) throws NotFoundPostException {
        var post = postRepository.findById(postId);
        if (post.isEmpty())
            throw new NotFoundPostException();

        return postToPostModel(post.get());
    }

    public List<PostResponseModel> getPublishedPosts() {
        return postRepository.getPostsByStatusId(3)
                .stream()
                .map(this::postToPostModel)
                .toList();
    }

    public List<PostResponseModel> getPendingPosts() {
        return postRepository.getPostsByStatusId(2)
                .stream()
                .map(this::postToPostModel)
                .toList();
    }


    public List<PostResponseModel> getDraftPostsByAccountId(int accountId) {
        return postRepository.getPostsByAccountIdAndStatusId(accountId, 1)
                .stream()
                .map(this::postToPostModel)
                .toList();
    }

    public List<PostResponseModel> getPendingPostsByAccountId(int accountId) {
        return postRepository.getPostsByAccountIdAndStatusId(accountId, 2)
                .stream()
                .map(this::postToPostModel)
                .toList();
    }

    public List<PostResponseModel> getPublishedPostsByAccountId(int accountId) {
        return postRepository.getPostsByAccountIdAndStatusId(accountId, 3)
                .stream()
                .map(this::postToPostModel)
                .toList();
    }

    public List<PostResponseModel> getRejectedPostsByAccountId(int accountId) {
        return postRepository.getPostsByAccountIdAndStatusId(accountId, 4)
                .stream()
                .map(this::postToPostModel)
                .toList();
    }

    public List<PostResponseModel> getPublishedPostsByCategoryId(int categoryId) {
        return postRepository.getPostsByCategoryIdAndStatusId(categoryId, 3)
                .stream()
                .map(this::postToPostModel)
                .toList();
    }

    public void addPost(int accountId,
                        String title,
                        String anons,
                        String fullText,
                        int categoryId,
                        int statusId) throws NotFoundAccountException, NotFoundCategoryException {

        var account = accountRepository.findById(accountId);
        if (account.isEmpty())
            throw new NotFoundAccountException();

        var category = categoryRepository.findById(categoryId);
        if (category.isEmpty())
            throw new NotFoundCategoryException();


        var post = new Post();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFullText(fullText);
        post.setAccount(account.get());
        post.setCategory(category.get());
        post.setStatus(postStatusRepository.getPostStatusById(statusId));

        postRepository.save(post);
    }

    public void addDraftPost(int accountId,
                             String title,
                             String anons,
                             String fullText,
                             int categoryId) throws NotFoundAccountException, NotFoundCategoryException {

        addPost(accountId, title, anons, fullText, categoryId, 1);
    }

    public void addPostAndSendToModeration(int accountId,
                                           String title,
                                           String anons,
                                           String fullText,
                                           int categoryId) throws NotFoundAccountException, NotFoundCategoryException {

        addPost(accountId, title, anons, fullText, categoryId, 2);
    }

    public void addPostAndPublished(int accountId,
                                    String title,
                                    String anons,
                                    String fullText,
                                    int categoryId) throws NotFoundAccountException, NotFoundCategoryException {

        addPost(accountId, title, anons, fullText, categoryId, 3);
    }


    public void updatePost(
            int postId,
            String title,
            String anons,
            String fullText,
            int categoryId) throws NotFoundPostException, NotFoundCategoryException {

        postRepository.save(
                editPost(postId,
                        title,
                        anons,
                        fullText,
                        categoryId,
                        Optional.of(postStatusRepository.getPostStatusById(1))
                ));

    }


    public void adminUpdatePost(
            int postId,
            String title,
            String anons,
            String fullText,
            int categoryId) throws NotFoundPostException, NotFoundCategoryException {
        postRepository.save(
                editPost(postId,
                        title,
                        anons,
                        fullText,
                        categoryId,
                        Optional.empty()
                ));

    }



    public void removePost(int postId) {
        postRepository.deleteById(postId);
    }

    public void sendPostModeration(int postId) throws NotFoundPostException {
        updatePostStatus(postId, 2);
    }

    public void sendPostToDraft(int postId) throws NotFoundPostException {
        updatePostStatus(postId, 1);
    }

    public void publishPost(int postId) throws NotFoundPostException {
        updatePostStatus(postId, 3);
    }

    public void rejectPost(int postId) throws NotFoundPostException {
        updatePostStatus(postId, 4);
    }

    public void updatePostStatus(int postId, int statusId) throws NotFoundPostException {
        var optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty())
            throw new NotFoundPostException();
        var post = optionalPost.get();
        post.setStatus(postStatusRepository.getPostStatusById(statusId));
        post.setLastChange(LocalDateTime.now());
        postRepository.save(post);
    }

    private Post editPost(
            int postId,
            String title,
            String anons,
            String fullText,
            int categoryId,
            Optional<PostStatus> status) throws NotFoundPostException, NotFoundCategoryException {
        var optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty())
            throw new NotFoundPostException();
        var post = optionalPost.get();

        var category = categoryRepository.findById(categoryId);
        if (category.isEmpty())
            throw new NotFoundCategoryException();

        if (post.getTitle() != title || post.getAnons() != anons ||
                post.getFullText() != fullText || post.getCategory().getId() != categoryId) {
            post.setTitle(title);
            post.setAnons(anons);
            post.setFullText(fullText);
            post.setCategory(category.get());
            post.setLastChange(LocalDateTime.now());
        }
        status.ifPresent(post::setStatus);
        return post;
    }

    private PostResponseModel postToPostModel(Post post) {
        var model = new PostResponseModel();
        model.setId(post.getId());
        model.setAccountId(post.getAccount().getId());
        model.setCategoryId(post.getCategory().getId());
        model.setStatusId(post.getStatus().getId());

        model.setTitle(post.getTitle());
        model.setAnons(post.getAnons());
        model.setFullText(post.getFullText());
        model.setLastChange(post.getLastChange());
        model.setAccountLogin(post.getAccount().getLogin());
        model.setCategoryName(post.getCategory().getName());
        return model;
    }
}
