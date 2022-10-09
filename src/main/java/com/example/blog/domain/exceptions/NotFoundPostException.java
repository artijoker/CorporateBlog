package com.example.blog.domain.exceptions;

public class NotFoundPostException extends Exception {
    public NotFoundPostException() {
        super("Post not found");
    }
}
