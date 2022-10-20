package com.example.blog.http.models.requests;

public class RegistrationRequestModel extends LogInRequestModel {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
