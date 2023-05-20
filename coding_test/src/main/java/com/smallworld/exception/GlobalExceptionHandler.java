package com.smallworld.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> defaultErrorHandler(
            HttpServletRequest req, HttpServletResponse response, Exception e) {
        ExceptionResponse obj = ExceptionDecorator.create(e);
        return new ResponseEntity<>(obj, obj.getStatus());
    }

}
