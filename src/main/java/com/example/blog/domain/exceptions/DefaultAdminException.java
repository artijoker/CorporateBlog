package com.example.blog.domain.exceptions;

public class DefaultAdminException extends Exception {
    public DefaultAdminException() {
        super("Cannot change or delete the default admin");
    }
}
