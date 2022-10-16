package com.example.blog.http.models.responses;

public class ResponseModel<TObject> {
    private boolean succeeded;
    private boolean bug;
    private int statusCode;
    private String message;
    private TObject result;

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public boolean isBug() {
        return bug;
    }

    public void setBug(boolean bug) {
        this.bug = bug;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TObject getResult() {
        return result;
    }

    public void setResult(TObject result) {
        this.result = result;
    }
}
