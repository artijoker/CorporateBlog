package com.example.blog.domain.exceptions;

public class BannedAccountException extends Exception {
    public BannedAccountException() {
        super("Account banned");
    }
}
