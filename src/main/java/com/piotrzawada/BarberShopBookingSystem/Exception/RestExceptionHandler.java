package com.piotrzawada.BarberShopBookingSystem.Exception;

import com.piotrzawada.BarberShopBookingSystem.Dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * Exception handler for REST controllers, providing consistent handling of specific exceptions and generating
 * appropriate response entities.
 * @author Piotr Zawada
 * @version 1.1
 * */
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * * Handles the exception raised when method arguments fail validation.
     *
     * @param exception MethodArgumentNotValidException.class representing the validation failure.
     * @return Response Entity with message and HttpStatus
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getFieldError();
        String errorMessage = fieldError.getDefaultMessage();

        Response response = new Response(errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

