package com.smartshop.smartshop.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;

public class ApiErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public OffsetDateTime timestamp;
    public int status;
    public String error;
    public String message;
    public String path;

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(OffsetDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
