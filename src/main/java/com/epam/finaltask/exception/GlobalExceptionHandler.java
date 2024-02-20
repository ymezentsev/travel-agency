package com.epam.finaltask.exception;

import com.epam.finaltask.dto.RemoteResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public RemoteResponse notFoundException(BaseException ex) {
        log.error("The error occur with message={}", ex.getMessage());
        return RemoteResponse.create(false, ex.getErrorCode(), ex.getMessage(), null);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, InvalidDataException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RemoteResponse handleValidationExceptions(Exception ex) {
        String message;
        if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            message = methodArgumentNotValidException.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .orElse(methodArgumentNotValidException.getMessage());
        } else {
            message = ex.getMessage();
        }

        log.error("The error occur with message={}", message);
        return RemoteResponse.create(false, StatusCodes.INVALID_DATA.name(), message, null);
    }

}
