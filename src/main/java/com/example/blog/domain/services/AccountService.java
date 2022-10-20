package com.example.blog.domain.services;


import com.example.blog.domain.exceptions.*;
import com.example.blog.domain.repositories.IAccountRepository;
import com.example.blog.domain.repositories.IRoleRepository;
import com.example.blog.http.models.responses.AccountResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    public List<AccountResponseModel> findAllAccounts() {
        var accounts = new ArrayList<AccountResponseModel>();
        accountRepository.findAll().forEach(account -> {
            var model = new AccountResponseModel();
            model.setId(account.getId());
            model.setLogin(account.getLogin());
            model.setEmail(account.getEmail());
            model.setRegistered(account.getRegistered());
            model.setIsBanned(account.getIsBanned());
            model.setIsDeleted(account.getIsDeleted());
            model.setRoleName(account.getRole().getName());
            model.setRoleId(account.getRole().getId());
            accounts.add(model);
        });
        return accounts;
    }

    public List<AccountResponseModel> findAllBlockedAccounts() {
        var accounts = new ArrayList<AccountResponseModel>();
        accountRepository.findAccountsByIsBannedTrue().forEach(account -> {
            var model = new AccountResponseModel();
            model.setId(account.getId());
            model.setLogin(account.getLogin());
            model.setEmail(account.getEmail());
            model.setRegistered(account.getRegistered());
            model.setIsBanned(account.getIsBanned());
            model.setIsDeleted(account.getIsDeleted());
            model.setRoleName(account.getRole().getName());
            model.setRoleId(account.getRole().getId());
            accounts.add(model);
        });
        return accounts;
    }

    public AccountResponseModel getAccountById(int accountId) throws NotFoundAccountException {

        var optional = accountRepository.findById(accountId);

        if (optional.isEmpty())
            throw new NotFoundAccountException();

        var account = optional.get();

        var model = new AccountResponseModel();
        model.setId(account.getId());
        model.setLogin(account.getLogin());
        model.setEmail(account.getEmail());
        model.setRegistered(account.getRegistered());
        return model;
    }

    public AccountResponseModel getAccountByIdAndCountPublishedPosts(int accountId) throws NotFoundAccountException {

        var optional = accountRepository.getAccountByIdAndCountPostsByStatusId(accountId, 3);

        if (optional.isEmpty())
            throw new NotFoundAccountException();

        var objects = optional.get();

        var model = new AccountResponseModel();
        model.setId((int) objects[0]);
        model.setLogin((String) objects[1]);
        model.setEmail((String) objects[2]);
        model.setRegistered(((Timestamp) objects[3]).toLocalDateTime());
        model.setQuantityPosts((BigInteger) objects[4]);
        return model;
    }

    public List<AccountResponseModel> getAccountsAndCountPublishedPostsForEach() {
        var accountModels = new ArrayList<AccountResponseModel>();
        accountRepository.getAccountsAndCountPostsForEachByStatusId(3).forEach(objects -> {
            var model = new AccountResponseModel();
            model.setId((int) objects[0]);
            model.setLogin((String) objects[1]);
            model.setEmail((String) objects[2]);
            model.setRegistered(((Timestamp) objects[3]).toLocalDateTime());
            model.setIsBanned((boolean) objects[4]);
            model.setIsDeleted((boolean) objects[5]);
            model.setRoleName((String) objects[6]);
            model.setRoleId((int) objects[7]);
            model.setQuantityPosts((BigInteger) objects[8]);
            accountModels.add(model);
        });
        return accountModels;
    }

    public List<AccountResponseModel> getLoginsThatHavePublishedPosts() {
        var accountModels = new ArrayList<AccountResponseModel>();
        accountRepository.getLoginsAndCountPostsForEachByStatusId(3).forEach(objects -> {
            if (((BigInteger) objects[2]).longValue() > 0) {
                var model = new AccountResponseModel();
                model.setId((int) objects[0]);
                model.setLogin((String) objects[1]);
                model.setQuantityPosts((BigInteger) objects[2]);
                accountModels.add(model);
            }
        });
        return accountModels;
    }

    public void updateAccount(int accountId, String email, String login, String newPassword)
            throws NotFoundAccountException, DuplicateEmailException, DuplicateLoginException {
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

        if (newPassword.isEmpty()) {
            account.setPasswordHash(passwordEncoder.encode(newPassword));
            isModified = true;
        }

        if (isModified) {
            accountRepository.save(account);
        }
    }

    public void adminUpdateAccount(int accountId,
                                   String email,
                                   String login,
                                   String newPassword,
                                   int roleId)
            throws NotFoundAccountException,
            DuplicateEmailException,
            DuplicateLoginException,
            DefaultAdminException {
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

        if (!newPassword.isEmpty()) {
            account.setPasswordHash(passwordEncoder.encode(newPassword));
            isModified = true;
        }

        if (account.getRole().getId() != roleId) {
            if (accountId == 1)
                throw new DefaultAdminException();

            var role = roleRepository.getRoleById(roleId);
            account.setRole(role);
            isModified = true;
        }

        if (isModified) {
            accountRepository.save(account);
        }
    }

    public void removeAccount(int accountId) throws DefaultAdminException {
        if (accountId == 1)
            throw new DefaultAdminException();
        accountRepository.deleteById(accountId);
    }

    public void banAccount(int accountId) throws DefaultAdminException, NotFoundAccountException {
        if (accountId == 1)
            throw new DefaultAdminException();

        var accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty())
            throw new NotFoundAccountException();

        var account = accountOptional.get();

        account.setIsBanned(true);

        accountRepository.save(account);
    }

    public void unlockAccount(int accountId) throws NotFoundAccountException{

        var accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty())
            throw new NotFoundAccountException();

        var account = accountOptional.get();
        account.setIsBanned(false);

        accountRepository.save(account);
    }

}
