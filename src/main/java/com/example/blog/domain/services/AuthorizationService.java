package com.example.blog.domain.services;

import com.example.blog.domain.exceptions.BannedAccountException;
import com.example.blog.domain.exceptions.DeletedAccountException;
import com.example.blog.domain.exceptions.InvalidLoginException;
import com.example.blog.domain.exceptions.InvalidPasswordException;
import com.example.blog.domain.repositories.IAccountRepository;
import com.example.blog.http.models.responses.AccountResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService  {

    private final IAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthorizationService(IAccountRepository accountRepository,
                               PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AccountResponseModel Authorize(String login, String password)
            throws InvalidLoginException,
            InvalidPasswordException,
            DeletedAccountException,
            BannedAccountException {
        var accountOptional = accountRepository.findAccountByLogin(login);

        if (accountOptional.isEmpty())
            throw new InvalidLoginException();

        var account = accountOptional.get();

        if (passwordEncoder.matches(password, account.getPasswordHash()))
            throw new InvalidPasswordException();

        if (account.getIsDeleted())
            throw new DeletedAccountException();

        if (account.getIsBanned())
            throw new BannedAccountException();


        var model = new AccountResponseModel();
        model.setId(account.getId());
        model.setLogin(account.getLogin());
        model.setEmail(account.getEmail());
        model.setRegistered(account.getRegistered());

        return model;
        //return token
    }
}
