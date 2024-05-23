package org.example.blogservice.exception;

import org.example.blogservice.dto.response.ExceptionResponse;
import org.example.blogservice.dto.response.ValidationExceptionResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiClientException.class)
    public ResponseEntity<ExceptionResponse> handleApiClientException(ApiClientException exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(), 400);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        final List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        final List<String> errorStringsList = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();

        final ValidationExceptionResponse response = new ValidationExceptionResponse(errorStringsList, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(), 400);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
