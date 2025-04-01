package com.userService.exceptions;

import com.userService.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<ApiResponse> handleResponseException(ResourceException resourceException){
        ApiResponse response =  ApiResponse
                .builder()
                .message(resourceException.getMessage())
                .success(true)
                .status(HttpStatus.NOT_FOUND)
                .build();

        return new ResponseEntity<>(response , HttpStatus.NOT_FOUND);
    }
}
