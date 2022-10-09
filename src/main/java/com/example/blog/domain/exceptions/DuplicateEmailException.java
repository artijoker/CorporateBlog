package com.example.blog.domain.exceptions;

public class DuplicateEmailException extends Exception {
    public DuplicateEmailException() {
        super("Duplicate email");
    }
}
