package com.example.blog.http.models.responses;

import java.math.BigInteger;

public class CategoryResponseModel {
    private int id;
    private String name;
    private BigInteger quantityPublishedPosts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getQuantityPublishedPosts() {
        return quantityPublishedPosts;
    }

    public void setQuantityPublishedPosts(BigInteger quantityPublishedPosts) {
        this.quantityPublishedPosts = quantityPublishedPosts;
    }
}