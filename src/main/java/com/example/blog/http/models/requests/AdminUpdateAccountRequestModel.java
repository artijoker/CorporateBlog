package com.example.blog.http.models.requests;

public class AdminUpdateAccountRequestModel extends UpdateAccountRequestModel {
    private int roleId;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
