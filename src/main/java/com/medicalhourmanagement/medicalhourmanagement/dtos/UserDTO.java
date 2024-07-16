package com.medicalhourmanagement.medicalhourmanagement.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.medicalhourmanagement.medicalhourmanagement.utils.enums.Role;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    private String email;
    private Role role;
}