package org.example.blogservice.controller.advice;

import org.example.blogservice.exception.BlogNotFoundException;
import org.example.blogservice.dto.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BlogControllerAdvice {
    @ExceptionHandler(BlogNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleBlogNotFoundException(BlogNotFoundException blogNotFoundException) {
        final ExceptionResponse response = new ExceptionResponse(blogNotFoundException.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
