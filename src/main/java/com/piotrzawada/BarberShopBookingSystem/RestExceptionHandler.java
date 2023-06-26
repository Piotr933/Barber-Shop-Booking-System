package com.piotrzawada.BarberShopBookingSystem;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getFieldError();
        String errorMessage = fieldError.getDefaultMessage();

        Response response = new Response(errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }
}

