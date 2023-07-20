package com.service.account.exceptions;

public class PasswordChangeException extends RuntimeException {
    public PasswordChangeException(String message) {
        super(message);
    }
}
