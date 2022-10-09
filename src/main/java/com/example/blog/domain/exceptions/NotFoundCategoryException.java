package com.example.blog.domain.exceptions;

public class NotFoundCategoryException extends Exception {
    public NotFoundCategoryException() {
        super("Category not found");
    }
}
