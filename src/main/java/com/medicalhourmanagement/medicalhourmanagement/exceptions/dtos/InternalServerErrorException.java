package com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos;

import lombok.Data;

@Data
public class InternalServerErrorException extends RuntimeException {

    private String code;
    private String message;

    public InternalServerErrorException(String message) {
        this.message = message;
    }

}