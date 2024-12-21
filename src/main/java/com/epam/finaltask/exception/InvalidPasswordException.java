package com.epam.finaltask.exception;

public class InvalidPasswordException extends BaseException {
    public InvalidPasswordException(String errorCode, String message) {
        super(errorCode, message);
    }
}
