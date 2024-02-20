package com.epam.finaltask.exception;

public class InvalidDataException extends BaseException{
    public InvalidDataException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
