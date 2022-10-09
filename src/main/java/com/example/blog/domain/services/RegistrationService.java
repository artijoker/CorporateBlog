package com.example.blog.domain.services;

import com.example.blog.domain.entities.Account;
import com.example.blog.domain.exceptions.DuplicateEmailException;
import com.example.blog.domain.exceptions.DuplicateLoginException;
import com.example.blog.domain.exceptions.NotFoundRoleException;
import com.example.blog.domain.repositories.IAccountRepository;
import com.example.blog.domain.repositories.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

public class RegistrationService {

    private final IAccountRepository accountRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(IAccountRepository accountRepository,
                          IRoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void RegisterAccount(String email, String login, String password) throws NotFoundRoleException, DuplicateEmailException, DuplicateLoginException {
        var role = roleRepository.findRoleByName("user");
        if (role.isEmpty())
            throw new NotFoundRoleException();

        if (accountRepository.findAccountByEmail(email).isPresent())
            throw new DuplicateEmailException();

        if (accountRepository.findAccountByLogin(login).isPresent())
            throw new DuplicateLoginException();

        var account = new Account();
        account.setEmail(email);
        account.setLogin(login);
        account.setPasswordHash(passwordEncoder.encode(password));
        account.setIsBanned(false);
        account.setIsDeleted(false);
        account.setRegistered(LocalDate.now());
        account.setRole(role.get());

        accountRepository.save(account);
        //return token
    }

    public void AddAccount(String email, String login, String password, int roleId)
            throws Exception {
        var role = roleRepository.findById(roleId);

        if (role.isEmpty())
            throw new NotFoundRoleException();

        if (accountRepository.findAccountByEmail(email).isPresent())
            throw new DuplicateEmailException();

        if (accountRepository.findAccountByLogin(login).isPresent())
            throw new DuplicateLoginException();

        var account = new Account();
        account.setEmail(email);
        account.setLogin(login);
        account.setPasswordHash(passwordEncoder.encode(password));
        account.setRole(role.get());
        account.setIsBanned(false);
        account.setIsDeleted(false);
        account.setRegistered(LocalDate.now());

        accountRepository.save(account);
    }
}
