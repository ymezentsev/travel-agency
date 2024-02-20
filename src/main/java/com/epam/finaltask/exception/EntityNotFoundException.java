package com.epam.finaltask.exception;

public class EntityNotFoundException extends BaseException{
    public EntityNotFoundException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
