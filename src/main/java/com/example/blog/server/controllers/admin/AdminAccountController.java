package com.example.blog.server.controllers.admin;

import com.example.blog.domain.services.AccountService;
import com.example.blog.http.models.responses.AccountResponseModel;
import com.example.blog.http.models.responses.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin/accounts")
public class AdminAccountController{

        private final AccountService accountService;

        @Autowired
        public AdminAccountController(AccountService accountService){
            this.accountService = accountService;
        }


        @GetMapping("/get-all-account")
        public ResponseModel<List<AccountResponseModel>> getAllAccounts() {
            var response = new ResponseModel<List<AccountResponseModel>>();
            response.setSucceeded(true);
            response.setResult(accountService.getAccountsAndCountPublishedPostsForEach());

            return response;
        }
}
