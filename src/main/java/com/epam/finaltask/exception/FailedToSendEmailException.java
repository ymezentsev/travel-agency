package com.epam.finaltask.exception;

public class FailedToSendEmailException extends BaseException {
    public FailedToSendEmailException(String errorCode, String message) {
        super(errorCode, message);
    }
}
