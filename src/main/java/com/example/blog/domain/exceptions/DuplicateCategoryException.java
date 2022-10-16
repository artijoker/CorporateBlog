package com.example.blog.domain.exceptions;

public class DuplicateCategoryException extends Exception {
    public DuplicateCategoryException() {
        super("Duplicate category");
    }
}
