package com.example.blog.http.models.responses;

public class ResponseModel<TObject> {
    public boolean succeeded;
    public boolean bug;
    public int statusCode;
    public String message;
    public TObject result;
}
