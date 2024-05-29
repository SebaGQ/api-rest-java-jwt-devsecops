package com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class AppException extends RuntimeException {

    private final String code;
    private final int responseCode;
    private final List<ExceptionDTO> errorList = new ArrayList<>();

    public AppException(String code, int responseCode, String message) {
        super(message);
        this.code = code;
        this.responseCode = responseCode;
    }
}
