package org.example.blogservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

public class ApiClientException extends RuntimeException {
    public ApiClientException(String message) {
        super(message);
    }
}
