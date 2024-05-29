package com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos;

import lombok.Data;

@Data
public class RequestException extends RuntimeException{

    private final String message;
    public RequestException( String message) {
        this.message = message;
    }

}
