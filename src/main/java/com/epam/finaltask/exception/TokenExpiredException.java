package com.epam.finaltask.exception;

public class TokenExpiredException extends BaseException {
    public TokenExpiredException(String errorCode, String message) {
        super(errorCode, message);
    }
}
