package com.example.blog.http.models.requests;

public class UpdateCategoryRequestModel extends AddingCategoryRequestModel {
    private int categoryId;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
