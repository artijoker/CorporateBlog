package com.example.blog.domain.exceptions;

public class DuplicateLoginException extends Exception {
    public DuplicateLoginException() {
        super("Duplicate login");
    }
}