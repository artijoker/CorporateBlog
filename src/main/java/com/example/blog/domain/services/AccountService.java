package com.example.blog.domain.services;


import com.example.blog.domain.entities.Account;
import com.example.blog.domain.exceptions.*;
import com.example.blog.domain.repositories.IAccountRepository;
import com.example.blog.domain.repositories.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AccountService {

    protected IAccountRepository accountRepository;
    protected IRoleRepository roleRepository;
    protected PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(IAccountRepository accountRepository,
                          IRoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Iterable<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    public Iterable<Account> findAllBlockedAccounts() {
        return accountRepository.findAccountsByIsBannedTrue();
    }



    public void UserUpdateAccount(int accountId, String email, String login, String newPassword)
            throws Exception {
        boolean isModified = false;
        var accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty())
            throw new NotFoundAccountException();

        var account = accountOptional.get();

        if (account.getEmail() != email) {

            if (accountRepository.findAccountByEmail(email).isEmpty())
                throw new DuplicateEmailException();

            account.setEmail(email);
            isModified = true;
        }

        if (account.getLogin() != login) {
            if (accountRepository.findAccountByLogin(login).isEmpty())
                throw new DuplicateLoginException();
            account.setLogin(login);
            isModified = true;
        }

        if (newPassword.isEmpty()){
            account.setPasswordHash(passwordEncoder.encode(newPassword));
            isModified = true;
        }

        if (isModified){
            accountRepository.save(account);
        }
    }

    public void AdminUpdateAccount(int accountId, String email, String login, String newPassword, int roleId)
            throws Exception {
        boolean isModified = false;

        var accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty())
            throw new NotFoundAccountException();

        var account = accountOptional.get();

        if (account.getEmail() != email) {

            if (accountRepository.findAccountByEmail(email).isEmpty())
                throw new DuplicateEmailException();

            account.setEmail(email);
            isModified = true;
        }

        if (account.getLogin() != login) {
            if (accountRepository.findAccountByLogin(login).isEmpty())
                throw new DuplicateLoginException();
            account.setLogin(login);
            isModified = true;
        }

        if (!newPassword.isEmpty()){
            account.setPasswordHash(passwordEncoder.encode(newPassword));
            isModified = true;
        }

        if (account.getRole().getId() != roleId){
            var roleOptional = roleRepository.findById(roleId);
            if (roleOptional.isEmpty())
                throw new NotFoundRoleException();

            account.setRole(roleOptional.get());
            isModified = true;
        }

        if (isModified){
            accountRepository.save(account);
        }
    }

}
