package org.localvendor.authservice.exception;

public class PhoneNumberAlreadyExistException extends RuntimeException {
    public PhoneNumberAlreadyExistException(String message) {
        super(message);
    }
}
