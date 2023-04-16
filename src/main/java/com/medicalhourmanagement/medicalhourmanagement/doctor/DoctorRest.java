package com.medicalhourmanagement.medicalhourmanagement.doctor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.medicalhourmanagement.medicalhourmanagement.appointment.AppointmentRest;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class DoctorRest {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("appointments")
    private List<AppointmentRest> appointments;
}
