package com.epam.finaltask.exception;

public class EntityNotFoundException extends BaseException {
    public EntityNotFoundException(String errorCode, String message) {
        super(errorCode, message);
    }
}
