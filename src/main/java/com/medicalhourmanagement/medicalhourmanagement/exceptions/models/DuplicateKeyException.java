package com.medicalhourmanagement.medicalhourmanagement.exceptions.models;


import lombok.Data;

@Data
public class DuplicateKeyException extends RuntimeException {

    private String message;

    public DuplicateKeyException(String message) {
        super(message);
        this.message = message;
    }

}