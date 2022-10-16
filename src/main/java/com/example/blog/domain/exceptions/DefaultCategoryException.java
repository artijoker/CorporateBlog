package com.example.blog.domain.exceptions;

public class DefaultCategoryException  extends Exception {
    public DefaultCategoryException() {
        super("Cannot change or delete the default category");
    }
}
