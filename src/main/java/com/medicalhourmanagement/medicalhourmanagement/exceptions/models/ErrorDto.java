package com.medicalhourmanagement.medicalhourmanagement.exceptions.models;


import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorDto implements Serializable {

    private String code;

    private String message;

    public ErrorDto(String code, String message) {
        this.code = code;
        this.message = message;
    }


}