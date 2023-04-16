package com.medicalhourmanagement.medicalhourmanagement.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class DoctorResponse<T> implements Serializable {

    private String status;

    private String code;

    private String message;

    private T data;


    public DoctorResponse(String deleted, String code, String message) {
    }
}
