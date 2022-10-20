package com.example.blog.server.controllers.admin;

import com.example.blog.domain.exceptions.*;
import com.example.blog.domain.services.AccountService;
import com.example.blog.domain.services.RegistrationService;
import com.example.blog.http.models.requests.AddingAccountRequestModel;
import com.example.blog.http.models.requests.AdminUpdateAccountRequestModel;
import com.example.blog.http.models.responses.AccountResponseModel;
import com.example.blog.http.models.responses.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/accounts")
public class AdminAccountController {

    private final AccountService accountService;
    private final RegistrationService registrationService;

    @Autowired
    public AdminAccountController(AccountService accountService, RegistrationService registrationService) {
        this.accountService = accountService;
        this.registrationService = registrationService;
    }

    @GetMapping("/get-all-account")
    public ResponseModel<List<AccountResponseModel>> getAllAccounts() {
        var response = new ResponseModel<List<AccountResponseModel>>();
        response.setSucceeded(true);
        response.setResult(accountService.getAccountsAndCountPublishedPostsForEach());

        return response;
    }

    @PostMapping("/add-account")
    public ResponseModel<?> addAccount(@ModelAttribute AddingAccountRequestModel model) {
        var response = new ResponseModel<>();
        try {
            registrationService.AddAccount(model.getEmail(), model.getLogin(), model.getPassword(), model.getRoleId());
            response.setSucceeded(true);
        } catch (DuplicateEmailException ex) {
            response.setSucceeded(false);
            response.setMessage("Пользователь с таким email уже существует");

        } catch (DuplicateLoginException ex) {
            response.setSucceeded(false);
            response.setMessage("Пользователь с таким логином уже существует");
        }
        return response;
    }

    @PostMapping("/update-account")
    public ResponseModel<?> updateAccount(@ModelAttribute AdminUpdateAccountRequestModel model) {
        var response = new ResponseModel<>();
        try {
            accountService.adminUpdateAccount(
                    model.getAccountId(),
                    model.getEmail(),
                    model.getLogin(),
                    model.getNewPassword(),
                    model.getRoleId()
            );
            response.setSucceeded(true);
        } catch (DuplicateEmailException ex) {
            response.setSucceeded(false);
            response.setMessage("Пользователь с таким email или уже существует");
        } catch (DuplicateLoginException ex) {
            response.setSucceeded(false);
            response.setMessage("Пользователь с таким логином уже существует");
        } catch (NotFoundAccountException ex) {
            response.setSucceeded(false);
            response.setMessage("Учетная запись не найдена");
        } catch (DefaultAdminException ex) {
            response.setSucceeded(false);
            response.setMessage("Нельзя изменить роль у супер администратора");
        }
        return response;
    }

    @PostMapping("/banned-account")
    public ResponseModel<?> bannedAccount(@RequestParam int accountId) {
        var response = new ResponseModel<>();
        try {
            accountService.banAccount(accountId);
            response.setSucceeded(true);
        } catch (NotFoundAccountException ex) {
            response.setSucceeded(false);
            response.setMessage("Учетная запись не найдена");
        } catch (DefaultAdminException ex) {
            response.setSucceeded(false);
            response.setMessage("Нельзя заблокировать супер администратора");
        }
        return response;
    }

    @PostMapping("/unlock-account")
    public ResponseModel<?> unlockAccount(@RequestParam int accountId) {
        var response = new ResponseModel<>();
        try {
            accountService.unlockAccount(accountId);
            response.setSucceeded(true);
        } catch (NotFoundAccountException ex) {
            response.setSucceeded(false);
            response.setMessage("Учетная запись не найдена");
        }
        return response;
    }
}
