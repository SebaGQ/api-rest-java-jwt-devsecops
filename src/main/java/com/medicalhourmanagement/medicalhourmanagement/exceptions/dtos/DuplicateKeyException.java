package com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos;


import lombok.Data;

@Data
public class DuplicateKeyException extends RuntimeException {

    private final String message;

    public DuplicateKeyException(String message) {
        super(message);
        this.message = message;
    }

}