package com.example.blog.domain.repositories;

import com.example.blog.domain.entities.Account;

public interface IAccountsAndPostsNumber
{
    String getLogin();
    String getEmail();
    String getRole();
    int getNumber();
}
