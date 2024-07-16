package com.medicalhourmanagement.medicalhourmanagement.dtos.request;


import com.medicalhourmanagement.medicalhourmanagement.utils.constraints.PasswordConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

  private String firstname;
  private String lastname;
  private String email;
  @PasswordConstraint
  private String password;
}
