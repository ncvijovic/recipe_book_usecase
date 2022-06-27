package com.valcon.recipebook.exceptions;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorObject> handleEntityNotFound(NoSuchElementException ex) {
        log.error("Requested entity could not be found", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(ErrorObject.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> handleValidationFailures(MethodArgumentNotValidException ex) {
        log.error("Validation problem occurred.");
        ErrorObject errorObject = ErrorObject.builder().message("Validation errors occurred").errors(getValidationErrors(ex)).build();
        return ResponseEntity.badRequest().body(errorObject);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleAllOtherFailures(Exception ex) {
        log.error("Exception occurred.", ex);
        return ResponseEntity.internalServerError()
                             .body(ErrorObject.builder().message("Internal error occured. Contact maintainer.").build());
    }

    private List<String> getValidationErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                 .map(fe -> "Field " + fe.getField() + " of object " + fe.getObjectName() + " with value " + fe.getRejectedValue()
                         + " failed validation with message: " + fe.getDefaultMessage())
                 .toList();
    }
}
