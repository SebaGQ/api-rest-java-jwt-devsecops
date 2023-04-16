package com.medicalhourmanagement.medicalhourmanagement.appointment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.medicalhourmanagement.medicalhourmanagement.doctor.DoctorRest;
import com.medicalhourmanagement.medicalhourmanagement.patient.PatientRest;
import lombok.Data;

import java.time.LocalDateTime;


/**
 Esto es un DTO, se utilizan para mover los datos de una capa a otra.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AppointmentRest {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("doctor")
    private DoctorRest doctor;

    @JsonProperty("patient")
    private PatientRest patient;

    @JsonProperty("date")
    private LocalDateTime date;

    /**
     *  Con @JsonIgnoreProperties manejamos las referencias circulares
     *  La anotación @Data de lombok genera solo los métodos getter y setter faltantes, es decir, no sobrescribe los ya definidos.
     */
    @JsonIgnoreProperties({"appointments"})
    public DoctorRest getDoctor() {
        return doctor;
    }

    @JsonIgnoreProperties({"appointments"})
    public PatientRest getPatient() {
        return patient;
    }
}
