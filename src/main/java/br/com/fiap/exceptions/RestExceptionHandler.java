package br.com.fiap.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class RestExceptionHandler {
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<RestValidationError>> constraintViolationExceptionHandler(ConstraintViolationException e){
        List<RestValidationError> errors = new ArrayList<>();

        e.getConstraintViolations().forEach((v) -> { 
            errors.add(new RestValidationError(v.getPropertyPath().toString(), v.getMessage()));
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
