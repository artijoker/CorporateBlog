package com.example.blog.domain.exceptions;

public class InvalidLoginException extends Exception {
    public InvalidLoginException() {
        super("Invalid login");
    }
}
