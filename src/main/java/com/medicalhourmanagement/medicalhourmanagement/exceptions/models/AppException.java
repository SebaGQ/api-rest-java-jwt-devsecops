package com.medicalhourmanagement.medicalhourmanagement.exceptions.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AppException extends Exception {

    private final String code;
    private final int responseCode;
    private final List<ExceptionDTO> errorList = new ArrayList<>();


}
