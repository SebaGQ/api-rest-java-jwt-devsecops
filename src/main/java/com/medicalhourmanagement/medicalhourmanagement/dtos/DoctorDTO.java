package com.medicalhourmanagement.medicalhourmanagement.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class DoctorDTO extends UserDTO {
    private String firstName;
    private String lastName;
}