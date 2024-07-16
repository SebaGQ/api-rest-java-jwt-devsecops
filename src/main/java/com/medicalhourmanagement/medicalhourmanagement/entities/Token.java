package com.medicalhourmanagement.medicalhourmanagement.entities;

import com.medicalhourmanagement.medicalhourmanagement.utils.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

  @Id
  @GeneratedValue
  public Long id;

  @Column(unique = true, name = "access_token")
  public String accessToken;

  @Enumerated(EnumType.STRING)
  public TokenType tokenType = TokenType.BEARER;

  private boolean revoked;

  private boolean expired;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  public User user;
}