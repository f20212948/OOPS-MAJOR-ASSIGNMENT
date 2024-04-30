package com.major.Major_Assignment.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        // Check if the cause is a DateTimeParseException
        if (ex.getCause() instanceof DateTimeParseException) {
            // Set the custom error message
            errorResponse.put("Error", "Invalid Date/ Time");
        } else {
            // If the cause is not a DateTimeParseException, check if the error message contains the specific string
            if (ex.getMessage().contains("Cannot deserialize value of type `java.time.LocalDate`")) {
                // Set the custom error message
                errorResponse.put("Error", "Invalid Date/ Time");
            } else {
                // If the error message does not contain the specific string, set the original error message
                errorResponse.put("Error", ex.getMessage());
            }
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}