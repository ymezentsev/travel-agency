package com.epam.finaltask.exception;

public class EntityAlreadyExistsException extends BaseException{
    public EntityAlreadyExistsException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
