package org.localvendor.backend.exception.vendor_exceptions;

public class VendorNotFoundException extends RuntimeException {
    public VendorNotFoundException(String message) {
        super(message);
    }
}
