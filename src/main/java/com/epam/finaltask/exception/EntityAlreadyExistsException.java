package com.epam.finaltask.exception;

import lombok.Getter;

@Getter
public class EntityAlreadyExistsException extends RuntimeException{
    private String errorCode;

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
