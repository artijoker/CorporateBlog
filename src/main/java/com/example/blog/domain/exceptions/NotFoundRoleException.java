package com.example.blog.domain.exceptions;

public class NotFoundRoleException  extends Exception {
    public NotFoundRoleException() {
        super("Role not found");
    }
}
