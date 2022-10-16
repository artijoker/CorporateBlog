package com.example.blog.server.controllers;

import com.example.blog.domain.exceptions.*;
import com.example.blog.domain.services.AccountService;
import com.example.blog.domain.services.AuthorizationService;
import com.example.blog.domain.services.RegistrationService;
import com.example.blog.http.models.requests.AccountRequestModel;
import com.example.blog.http.models.responses.AccountResponseModel;
import com.example.blog.http.models.responses.LogInResponseModel;
import com.example.blog.http.models.responses.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final RegistrationService registrationService;
    private final AuthorizationService authorizationService;


    @Autowired
    public AccountController(AccountService accountService,
                             RegistrationService registrationService,
                             AuthorizationService authorizationService) {
        this.accountService = accountService;
        this.registrationService = registrationService;
        this.authorizationService = authorizationService;
    }


    @GetMapping("/get-accounts-that-have-published-posts")
    public ResponseModel<List<AccountResponseModel>> getAllAccounts() {
        var response = new ResponseModel<List<AccountResponseModel>>();
        response.setSucceeded(true);
        response.setResult(accountService.getLoginsThatHavePublishedPosts());

        return response;
    }

    @PostMapping("/registration")
    public LogInResponseModel RegisterAccount(@ModelAttribute AccountRequestModel model) {
        var response = new LogInResponseModel();
        try {
            registrationService.RegisterUserAccount(model.getEmail(), model.getLogin(), model.getPassword());
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

    @PostMapping("/login")
    public LogInResponseModel LogIn(@ModelAttribute AccountRequestModel model) {
        var response = new LogInResponseModel();
        try {
            response.setSucceeded(true);
            response.setResult(authorizationService.Authorize(model.getLogin(), model.getPassword()));
        } catch (InvalidLoginException | InvalidPasswordException e) {
            response.setSucceeded(false);
            response.setMessage("Неверный логин или пароль");
        } catch (BannedAccountException e) {
            response.setSucceeded(false);
            response.setMessage("Аккаунт заблокирован");
        } catch (DeletedAccountException e) {
            response.setSucceeded(false);
            response.setMessage("Аккаунт удален");
        }
        return response;
    }

    @PostMapping("/get-account")
    public ResponseModel<AccountResponseModel> getAccount(@RequestParam int accountId)
            throws NotFoundAccountException {
        var response = new ResponseModel<AccountResponseModel>();
        response.setSucceeded(true);
        response.setResult(accountService.getAccountByIdAndCountPublishedPosts(accountId));

        return response;
    }
}
