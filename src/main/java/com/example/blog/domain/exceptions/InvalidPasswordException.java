package com.example.blog.domain.exceptions;

public class InvalidPasswordException  extends Exception {
    public InvalidPasswordException() {
        super("Invalid password");
    }
}
