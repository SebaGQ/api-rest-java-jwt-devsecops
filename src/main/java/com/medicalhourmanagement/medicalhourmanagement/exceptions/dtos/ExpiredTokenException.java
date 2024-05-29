package com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String message) {
        super(message);
    }
}