package com.medicalhourmanagement.medicalhourmanagement.dtos;


import com.medicalhourmanagement.medicalhourmanagement.auth.constraints.PasswordConstraint;
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
