package com.medicalhourmanagement.medicalhourmanagement.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientDTO extends UserDTO {
    private String firstName;
    private String lastName;
    private String rut;
    private String phoneNumber;
    private String address;
}