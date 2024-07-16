package com.medicalhourmanagement.medicalhourmanagement.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "DOCTORS")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Doctor extends User {

    @Size(min = 1, max = 50, message = "FIRST NAME MUST BE BETWEEN {min} AND {max} CHARACTERS LONG")
    @NotBlank(message = "FIRST NAME CANNOT BE NULL")
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Size(min = 1, max = 50, message = "LAST NAME MUST BE BETWEEN {min} AND {max} CHARACTERS LONG")
    @NotBlank(message = "LAST NAME CANNOT BE NULL")
    @Column(name = "LAST_NAME")
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "doctor")
    @JsonManagedReference("doctor-appointments")
    private List<Appointment> appointments;
    @ManyToMany
    @JoinTable(
            name = "doctor_specialties",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id")
    )
    private Set<Specialty> specialties;
}