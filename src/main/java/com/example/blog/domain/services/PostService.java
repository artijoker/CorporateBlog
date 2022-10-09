package com.example.blog.domain.services;

import com.example.blog.domain.exceptions.NotFoundAccountException;
import com.example.blog.domain.exceptions.NotFoundCategoryException;
import com.example.blog.domain.exceptions.NotFoundPostException;
import com.example.blog.domain.repositories.IAccountRepository;
import com.example.blog.domain.repositories.ICategoryRepository;
import com.example.blog.domain.repositories.IPostRepository;
import com.example.blog.domain.entities.Post;
import com.example.blog.domain.repositories.IPostStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public List<Post> getPublishedPosts() {
        return new ArrayList<>(postRepository.getPostsByStatusId(3));
    }

    public List<Post> getPendingPosts() {
        return new ArrayList<>(postRepository.getPostsByStatusId(2));
    }


    public List<Post> getDraftPostsByAccountId(int accountId){
        return new ArrayList<>(postRepository.getPostsByAccountIdAndStatusId(accountId, 1));
    }
    public List<Post> getPendingPostsByAccountId(int accountId){
        return new ArrayList<>(postRepository.getPostsByAccountIdAndStatusId(accountId, 2));
    }
    public List<Post> getPublishedPostsByAccountId(int accountId) {
        return new ArrayList<>(postRepository.getPostsByAccountIdAndStatusId(accountId, 3));

    }
    public  List<Post> getRejectedPostsByAccountId(int accountId){
        return new ArrayList<>(postRepository.getPostsByAccountIdAndStatusId(accountId, 4));
    }
    public List<Post> getPublishedPostsByCategoryId(int categoryId) {
        return new ArrayList<>(postRepository.getPostsByCategoryIdAndStatusId(categoryId, 3));
    }
    public void addPost(int accountId,
                        String title,
                        String anons,
                        String fullText,
                        boolean isAllowCommenting,
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
        post.setIsAllowCommenting(isAllowCommenting);
        post.setAccount(account.get());
        post.setCategory(category.get());
        post.setStatus(postStatusRepository.getPostStatusById(statusId));

        postRepository.save(post);
    }

    public void addDraftPost(int accountId,
                             String title,
                             String anons,
                             String fullText,
                             boolean isAllowCommenting,
                             int categoryId) throws NotFoundAccountException, NotFoundCategoryException {

        addPost(accountId, title, anons, fullText, isAllowCommenting, categoryId, 1);
    }

    public void addPostAndSendToModeration(int accountId,
                                           String title,
                                           String anons,
                                           String fullText,
                                           boolean isAllowCommenting,
                                           int categoryId) throws NotFoundAccountException, NotFoundCategoryException {

        addPost(accountId, title, anons, fullText, isAllowCommenting, categoryId, 2);
    }

    public void addPostAndPublished(int accountId,
                                    String title,
                                    String anons,
                                    String fullText,
                                    boolean isAllowCommenting,
                                    int categoryId) throws NotFoundAccountException, NotFoundCategoryException {

        addPost(accountId, title, anons, fullText, isAllowCommenting, categoryId, 3);
    }

    public Post getById(int id) throws NotFoundPostException {
        var post = postRepository.findById(id);
        if (post.isEmpty())
            throw new NotFoundPostException();

        return post.get();
    }





    public void userUpdatePost(
            int postId,
            String title,
            String anons,
            String fullText,
            boolean isAllowCommenting,
            int categoryId) throws NotFoundPostException, NotFoundCategoryException {
        var optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty())
            throw new  NotFoundPostException();
        var post = optionalPost.get();

        var category = categoryRepository.findById(categoryId);
        if (category.isEmpty())
            throw new NotFoundCategoryException();

        if (post.getTitle() != title || post.getAnons() != anons ||
                post.getFullText() != fullText || post.getCategory().getId() != categoryId)
        {
            post.setTitle(title);
            post.setAnons(anons);
            post.setFullText(fullText);
            post.setIsAllowCommenting(isAllowCommenting);
            post.setCategory(category.get());

            post.setStatus(postStatusRepository.getPostStatusById(1));

            post.setLastChange(LocalDate.now());

            postRepository.save(post);
        }
    }


    public void adminUpdatePost(
            int postId,
            String title,
            String anons,
            String fullText,
            boolean isAllowCommenting,
            int categoryId) throws NotFoundPostException, NotFoundCategoryException {
        var optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty())
            throw new  NotFoundPostException();
        var post = optionalPost.get();

        var category = categoryRepository.findById(categoryId);
        if (category.isEmpty())
            throw new NotFoundCategoryException();

        if (post.getTitle() != title || post.getAnons() != anons ||
                post.getFullText() != fullText || post.getCategory().getId() != categoryId)
        {
            post.setTitle(title);
            post.setAnons(anons);
            post.setFullText(fullText);
            post.setIsAllowCommenting(isAllowCommenting);
            post.setCategory(category.get());

            post.setLastChange(LocalDate.now());

            postRepository.save(post);
        }
    }

    public void removePost(int postId) {
        postRepository.deleteById(postId);

    }

    public void sendPostModeration(int postId) throws NotFoundPostException {
        updatePostStatus(postId, 2);
    }

    public void  sendPostToDraft(int postId) throws NotFoundPostException {
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
            throw new  NotFoundPostException();
        var post = optionalPost.get();
        post.setStatus(postStatusRepository.getPostStatusById(statusId));
        post.setLastChange(LocalDate.now());
        postRepository.save(post);
    }

}
