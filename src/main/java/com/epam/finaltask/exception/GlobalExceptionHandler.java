package com.epam.finaltask.exception;

import com.epam.finaltask.dto.RemoteResponse;
import com.epam.finaltask.model.enums.StatusCodes;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RemoteResponse> handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RemoteResponse(false, StatusCodes.VALUE_NOT_FOUND.name(),
                        e.getMessage(), null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RemoteResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RemoteResponse(false, StatusCodes.VALUE_NOT_FOUND.name(),
                        e.getMessage(), null));
    }

    @ExceptionHandler(InvalidVoucherOperationException.class)
    public ResponseEntity<RemoteResponse> handleInvalidVoucherOperationException(InvalidVoucherOperationException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new RemoteResponse(false, StatusCodes.INVALID_VOUCHER_OPERATION.name(),
                        e.getMessage(), null));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RemoteResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new RemoteResponse(false, StatusCodes.FORBIDDEN.name(),
                        e.getMessage(), null));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<RemoteResponse> handleInvalidPasswordException(InvalidPasswordException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new RemoteResponse(false, StatusCodes.FORBIDDEN.name(),
                        e.getMessage(), null));
    }

    @ExceptionHandler(TokenAlreadyConfirmedException.class)
    public ResponseEntity<RemoteResponse> handleTokenAlreadyConfirmedException(TokenAlreadyConfirmedException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new RemoteResponse(false, e.getErrorCode(), e.getMessage(), null));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<RemoteResponse> handleTokenExpiredException(TokenExpiredException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new RemoteResponse(false, e.getErrorCode(), e.getMessage(), null));
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

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<RemoteResponse> handleAuthenticationException(AuthenticationException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new RemoteResponse(false, StatusCodes.INVALID_CREDENTIALS.name(),
                        e.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RemoteResponse> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new RemoteResponse(false, StatusCodes.INTERNAL_SERVER_ERROR.name(),
                        e.getMessage(), null));
    }
}
