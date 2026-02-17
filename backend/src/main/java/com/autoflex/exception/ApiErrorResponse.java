package com.autoflex.exception;

public class ApiErrorResponse {
    private int status;
    private String message;
    private String description;

    public ApiErrorResponse(int status, String message, String description) {
        this.status = status;
        this.message = message;
        this.description = description;
    }

    // Getters and Setters

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
