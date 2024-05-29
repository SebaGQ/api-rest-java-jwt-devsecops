package com.medicalhourmanagement.medicalhourmanagement.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;


/**
 Esto es un DTO, se utilizan para mover los datos de una capa a otra.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AppointmentDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("doctor")
    private DoctorDTO doctor;

    @JsonProperty("patient")
    private PatientDTO patient;

    @JsonProperty("date")
    private LocalDateTime date;

    /**
     *  Con @JsonIgnoreProperties manejamos las referencias circulares
     *  La anotación @Data de lombok genera solo los métodos getter y setter faltantes, es decir, no sobrescribe los ya definidos.
     */
    @JsonIgnoreProperties({"appointments"})
    public DoctorDTO getDoctor() {
        return doctor;
    }

    @JsonIgnoreProperties({"appointments"})
    public PatientDTO getPatient() {
        return patient;
    }
}
