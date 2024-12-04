package com.epam.finaltask.exception;

public class EntityAlreadyExistsException extends BaseException {
    public EntityAlreadyExistsException(String errorCode, String message) {
        super(errorCode, message);
    }
}
