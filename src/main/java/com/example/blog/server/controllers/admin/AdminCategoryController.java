package com.example.blog.server.controllers.admin;

import com.example.blog.domain.exceptions.DefaultCategoryException;
import com.example.blog.domain.exceptions.DuplicateCategoryException;
import com.example.blog.domain.exceptions.NotFoundCategoryException;
import com.example.blog.domain.services.CategoryService;
import com.example.blog.http.models.responses.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/categories")
public class AdminCategoryController {

        private final CategoryService categoryService;

        @Autowired
        public AdminCategoryController(CategoryService categoryService) {
            this.categoryService = categoryService;
        }

        @GetMapping("/add-category")
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

        @GetMapping("/edit-category")
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

        @GetMapping("/delete-category")
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
