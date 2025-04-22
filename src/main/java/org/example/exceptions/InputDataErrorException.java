package org.example.exceptions;

public class InputDataErrorException extends RuntimeException {
    public InputDataErrorException(String validationMessage) {
        super(validationMessage);
    }
}
