package com.epam.finaltask.exception;

public class TokenAlreadyConfirmedException extends BaseException {
    public TokenAlreadyConfirmedException(String errorCode, String message) {
        super(errorCode, message);
    }
}
