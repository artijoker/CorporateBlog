package com.example.blog.domain.services;

import com.example.blog.domain.entities.Category;
import com.example.blog.domain.exceptions.DefaultCategoryException;
import com.example.blog.domain.exceptions.DuplicateCategoryException;
import com.example.blog.domain.exceptions.NotFoundCategoryException;
import com.example.blog.domain.repositories.ICategoryRepository;
import com.example.blog.http.models.responses.CategoryResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private final ICategoryRepository categoryRepository;

    @Autowired
    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponseModel> getCategories() {
        var categoryModels = new ArrayList<CategoryResponseModel>();
        categoryRepository.findAll().forEach(c ->{
            var model = new CategoryResponseModel();
            model.setId(c.getId());
            model.setName(c.getName());
            categoryModels.add(model);
        });
        return categoryModels;
    }
    public List<CategoryResponseModel> getCategoriesAndCountPublishedPostsForEach() {
        var categoryModels = new ArrayList<CategoryResponseModel>();
        categoryRepository.getCategoriesAndCountPostsForEachByStatusId(3).forEach(objects ->{
            var model = new CategoryResponseModel();
            model.setId((Integer)(objects[0]));
            model.setName((String)(objects[1]));
            model.setQuantityPublishedPosts((BigInteger)(objects[2]));
            categoryModels.add(model);
        });
        return categoryModels;
    }

    public void AddCategory(String name) throws DuplicateCategoryException {
        if (categoryRepository.findCategoriesByName(name).isPresent())
            throw new DuplicateCategoryException();

        var category = new Category();
        category.setName(name);
        categoryRepository.save(category);
    }

    public void UpdateCategory(int categoryId, String name) throws DefaultCategoryException, DuplicateCategoryException, NotFoundCategoryException {
        if (categoryId == 1)
            throw new DefaultCategoryException();

        if (categoryRepository.findCategoriesByName(name).isPresent())
            throw new DuplicateCategoryException();

        var categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty())
            throw new NotFoundCategoryException();

        var category = categoryOptional.get();
        category.setName(name);
        categoryRepository.save(category);
    }

    public void RemoveCategory(int categoryId) throws DefaultCategoryException {
        if (categoryId == 1)
            throw new DefaultCategoryException();

        categoryRepository.deleteById(categoryId);
    }


}
