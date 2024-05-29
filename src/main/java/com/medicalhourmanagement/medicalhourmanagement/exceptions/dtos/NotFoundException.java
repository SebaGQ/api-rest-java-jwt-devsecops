package com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos;

import lombok.Data;

@Data
public class NotFoundException extends RuntimeException {

    private final String message;

    public NotFoundException(String message) {
        super(message);
        this.message = message;
    }

}