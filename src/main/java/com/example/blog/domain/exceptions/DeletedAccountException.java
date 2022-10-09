package com.example.blog.domain.exceptions;

public class DeletedAccountException extends Exception {
    public DeletedAccountException() {
        super("Account deleted");
    }
}
