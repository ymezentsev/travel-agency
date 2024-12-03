package com.epam.finaltask.exception;

import com.epam.finaltask.dto.RemoteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<RemoteResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new RemoteResponse(false, StatusCodes.ENTITY_NOT_FOUND.name(),
                        e.getMessage(), Collections.EMPTY_LIST));
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<RemoteResponse> handleEntityAlreadyExistsException(EntityAlreadyExistsException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new RemoteResponse(false, StatusCodes.ENTITY_ALREADY_EXISTS.name(),
                        e.getMessage(), Collections.EMPTY_LIST));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RemoteResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RemoteResponse(false, StatusCodes.VALUE_NOT_FOUND.name(),
                        e.getMessage(), Collections.EMPTY_LIST));
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<RemoteResponse> handleNumberFormatException(NumberFormatException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RemoteResponse(false, StatusCodes.NOT_A_NUMBER.name(),
                        e.getMessage(), Collections.EMPTY_LIST));
    }

    //todo update errors and statusMessage
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("succeeded", false);
        body.put("statusCode", StatusCodes.INVALID_DATA.name());

        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(this::getErrorMessage)
                .toList();
        body.put("statusMessage", errors);
        body.put("errors", errors);

        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(body, headers, status);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField();
           //String message = e.getDefaultMessage();
           // return field + " " + message;
            System.out.println(e.getDefaultMessage());
            return e.getDefaultMessage();
        }
        return e.getDefaultMessage();
    }
}
