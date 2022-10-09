package com.example.blog.domain.exceptions;

public class NotFoundAccountException  extends Exception {
    public NotFoundAccountException() {
        super("Account not found");
    }
}
