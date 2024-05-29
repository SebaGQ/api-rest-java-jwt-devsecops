package com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos;

public class UnauthorizedAppointmentException extends RuntimeException {
    public UnauthorizedAppointmentException(String message) {
        super(message);
    }
}
