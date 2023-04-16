package com.medicalhourmanagement.medicalhourmanagement.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AppointmentResponse<T> implements Serializable {

    private String status;

    private String code;

    private String message;

    private T data;

    public AppointmentResponse(String status, String code, String message) {
    }
}
