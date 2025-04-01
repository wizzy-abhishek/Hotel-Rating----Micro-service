package com.hotel.exception;

public class ResourceException extends RuntimeException{

    public ResourceException(String message) {
        super(message);
    }

    public ResourceException() {
        super("Resource not found!");
    }
}
