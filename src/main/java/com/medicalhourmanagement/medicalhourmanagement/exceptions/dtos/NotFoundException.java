package com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos;

import lombok.Data;

@Data
public class NotFoundException extends RuntimeException {

    private String code;
    private String message;

    public NotFoundException(String message) {
        super(message);
        this.code = "NOT_FOUND";
        this.message = message;
    }

}