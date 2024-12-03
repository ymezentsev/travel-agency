package com.epam.finaltask.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private String statusCode;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String statusCode, String message) {
        this(message);
        this.statusCode = statusCode;
    }
}
