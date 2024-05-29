package com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExceptionDTO {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ExceptionDTO() {
        this.timestamp = LocalDateTime.now();
    }
}
