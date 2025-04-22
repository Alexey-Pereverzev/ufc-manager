package org.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.example.constants.InfoMessage.*;

@ControllerAdvice
public class GlobalExceptionsHandler {
    @ExceptionHandler
    public ResponseEntity<AppError> handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new AppError(RESOURCE_NOT_FOUND_CODE.getValue(), e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleInvalidEnumValueException(InvalidEnumValueException e) {
        return new ResponseEntity<>(new AppError(INVALID_PARAMETER_CODE.getValue(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleInputDataErrorException(InputDataErrorException e) {
        return new ResponseEntity<>(new AppError(INPUT_DATA_ERROR_CODE.getValue(),
                e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ResponseEntity<>(new AppError(USER_EXISTS_CODE.getValue(), e.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return new ResponseEntity<>(new AppError(UNAUTHORIZED_CODE.getValue(), e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleNoAccessRightsException(NoAccessRightsException e) {
        return new ResponseEntity<>(new AppError(NO_ACCESS_CODE.getValue(), e.getMessage()),
                HttpStatus.FORBIDDEN);
    }
}
