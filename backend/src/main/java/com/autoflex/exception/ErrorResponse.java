package com.autoflex.exception;

public class ErrorResponse {

    public String error;
    public String details;

    public ErrorResponse(String error, String details) {
        this.error = error;
        this.details = details;
    }
}
