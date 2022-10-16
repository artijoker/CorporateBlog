package com.example.blog.http.models.responses;
import java.math.BigInteger;
import java.time.LocalDateTime;

public class AccountResponseModel {
    private int id;
    private int roleId;
    private String login;

    private String email;
    private Boolean isBanned;
    private Boolean isDeleted;
    private LocalDateTime registered;
    private BigInteger quantityPosts;
    private String roleName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(Boolean banned) {
        isBanned = banned;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }

    public BigInteger getQuantityPosts() {
        return quantityPosts;
    }

    public void setQuantityPosts(BigInteger quantityPosts) {
        this.quantityPosts = quantityPosts;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
