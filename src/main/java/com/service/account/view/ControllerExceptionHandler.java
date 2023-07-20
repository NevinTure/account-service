package com.service.account.view;


import com.service.account.exceptions.*;
import com.service.account.util.ErrorMessage;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorMessage message = getErrorMessage(
                status.value(),
                "Bad Request",
                "",
                request);

        return new ResponseEntity<>(message, headers, status);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorMessage message = getErrorMessage(
                status.value(),
                "Bad Request",
                ex.getMessage(),
                request);

        return new ResponseEntity<>(message, headers, status);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<Object> handleEmailAlreadyUsed(
            Throwable ex, WebRequest request) {
        ErrorMessage message = getErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "User exist!",
                request);

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            PasswordChangeException.class,
            PaymentUpdateException.class,
            DateTimeParseException.class,
            ConstraintViolationException.class,
            AdministratorActionException.class,
            RoleManageException.class,
            LockedException.class
    })
    public ResponseEntity<Object> handleBadRequest(
            RuntimeException ex, WebRequest request) {
        ErrorMessage message = getErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request);

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UsernameNotFoundException.class, RoleNotFoundException.class})
    public ResponseEntity<Object> handleUsernameNotFound(RuntimeException ex, WebRequest request) {
        ErrorMessage errorMessage = getErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationError(AuthenticationException ex) {
        ErrorMessage message = new ErrorMessage(
                LocalDate.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage()
        );

        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }
    private ErrorMessage getErrorMessage(int errorCode, String errorMessage, String exMessage, WebRequest request) {
        return new ErrorMessage(
                LocalDate.now(),
                errorCode,
                errorMessage,
                exMessage,
                request.getDescription(false)
        );

    }
}
