package com.medicalhourmanagement.medicalhourmanagement.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_patient")
public class Patient extends User {

  private String firstName;
  private String lastName;

  @Column(unique = true)
  private String rut;

  @Column(unique = true)
  private String phoneNumber;

  private String address;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "patient")
  @JsonManagedReference("patient-appointments")
  private List<Appointment> appointments;
}