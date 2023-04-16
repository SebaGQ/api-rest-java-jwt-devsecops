package com.medicalhourmanagement.medicalhourmanagement.patient;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PatientResponse<T> implements Serializable {

    private String status;

    private String code;

    private String message;

    private T data;

    public PatientResponse(String deleted, String code, String message) {
    }
}
