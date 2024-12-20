package com.epam.finaltask.exception;

public class InvalidVoucherOperationException extends BaseException {
    public InvalidVoucherOperationException(String errorCode, String message) {
        super(errorCode, message);
    }
}
