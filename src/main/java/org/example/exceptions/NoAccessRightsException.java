package org.example.exceptions;

public class NoAccessRightsException extends RuntimeException {
    public NoAccessRightsException(String message) {
        super(message);
    }
}

