package com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}