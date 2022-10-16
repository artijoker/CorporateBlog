package com.example.blog.server.controllers;

import com.example.blog.domain.exceptions.DefaultCategoryException;
import com.example.blog.domain.exceptions.DuplicateCategoryException;
import com.example.blog.domain.exceptions.NotFoundCategoryException;
import com.example.blog.domain.services.CategoryService;
import com.example.blog.http.models.responses.CategoryResponseModel;
import com.example.blog.http.models.responses.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/get-categories")
    public ResponseModel<List<CategoryResponseModel>> getCategories() {
        var response = new ResponseModel<List<CategoryResponseModel>>();
        response.setSucceeded(true);
        response.setResult(categoryService.getCategoriesAndCountPublishedPostsForEach());

        return response;
    }

    @PostMapping("/add-category")
    public ResponseModel<?> AddCategory(@RequestParam String name) {
        var response = new ResponseModel<>();
        try {
            categoryService.AddCategory(name);
            response.setSucceeded(true);
        } catch (DuplicateCategoryException ex) {
            response.setSucceeded(false);
            response.setMessage("Категоря с таким именем уже существует");
        }
        return response;
    }

    @PostMapping("/edit-category")
    public ResponseModel<?> UpdateCategory(@RequestParam int id,
                                           @RequestParam String name) {
        var response = new ResponseModel<>();
        try {
            categoryService.UpdateCategory(id, name);
            response.setSucceeded(true);
        } catch (DuplicateCategoryException ex) {
            response.setSucceeded(false);
            response.setMessage("Категоря с таким именем уже существует");
        } catch (DefaultCategoryException e) {
            response.setSucceeded(false);
            response.setMessage("Нельзя редактировать категорию 'по умолчанию'");
        } catch (NotFoundCategoryException e) {
            response.setSucceeded(false);
            response.setMessage("Категоря не найдена");
        }
        return response;
    }

    @PostMapping("/delete-category")
    public ResponseModel<?> RemoveCategory(@RequestParam int id) {
        var response = new ResponseModel<>();

        try {
            categoryService.RemoveCategory(id);
            response.setSucceeded(true);
        } catch (DefaultCategoryException e) {
            response.setSucceeded(false);
            response.setMessage("Нельзя удалять категорию 'по умолчанию'");
        }
        return response;
    }
}
