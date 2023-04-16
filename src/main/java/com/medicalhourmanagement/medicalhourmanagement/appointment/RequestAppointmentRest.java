package com.medicalhourmanagement.medicalhourmanagement.appointment;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RequestAppointmentRest {
    @JsonProperty("doctor")
    private Long doctor;

    @JsonProperty("patient")
    private Long patient;

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

}
