package com.medicalhourmanagement.medicalhourmanagement.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {

  @JsonProperty("Token")
  private String accessToken;
  @JsonProperty("RefreshToken")
  private String refreshToken;
}
