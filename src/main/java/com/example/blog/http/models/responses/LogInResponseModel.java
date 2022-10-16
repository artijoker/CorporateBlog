package com.example.blog.http.models.responses;

public class LogInResponseModel extends ResponseModel<AccountResponseModel> {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
