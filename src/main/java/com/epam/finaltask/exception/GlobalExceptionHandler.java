package com.epam.finaltask.exception;

import com.epam.finaltask.dto.RemoteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<RemoteResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new RemoteResponse(false, e.getErrorCode(), e.getMessage(), null));
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<RemoteResponse> handleEntityAlreadyExistsException(EntityAlreadyExistsException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new RemoteResponse(false, e.getErrorCode(), e.getMessage(), null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RemoteResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RemoteResponse(false, StatusCodes.VALUE_NOT_FOUND.name(),
                        e.getMessage(), null));
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<RemoteResponse> handleNumberFormatException(NumberFormatException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RemoteResponse(false, StatusCodes.NOT_A_NUMBER.name(),
                        e.getMessage(), null));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("succeeded", false);
        body.put("statusCode", StatusCodes.INVALID_DATA.name());

        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        body.put("statusMessage", errors);

        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(body, headers, status);
    }
}
