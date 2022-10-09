package com.example.blog.domain.services;

import com.example.blog.domain.entities.Account;
import com.example.blog.domain.exceptions.BannedAccountException;
import com.example.blog.domain.exceptions.DeletedAccountException;
import com.example.blog.domain.exceptions.InvalidLoginException;
import com.example.blog.domain.exceptions.InvalidPasswordException;
import com.example.blog.domain.repositories.IAccountRepository;
import com.example.blog.domain.repositories.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthorizationService  {

    private final IAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthorizationService(IAccountRepository accountRepository,
                               PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account Authorize(String login, String password)
            throws InvalidLoginException,
            InvalidPasswordException,
            DeletedAccountException,
            BannedAccountException {
        var account = accountRepository.findAccountByLogin(login);

        if (account.isEmpty())
            throw new InvalidLoginException();


        if (passwordEncoder.matches(password, account.get().getPasswordHash()))
            throw new InvalidPasswordException();

        if (account.get().getIsDeleted())
            throw new DeletedAccountException();

        if (account.get().getIsBanned())
            throw new BannedAccountException();


        //return token
        return account.get();
    }
}
