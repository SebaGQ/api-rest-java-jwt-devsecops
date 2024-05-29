package com.medicalhourmanagement.medicalhourmanagement.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="DOCTORS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * NotBlank maneja que la cadena no sea null, esté vacía o compuesta solo de espacios en blanco
     *
     */
    @Size(min = 1, max = 50, message = "FIRST NAME MUST BE BETWEEN {min} AND {max} CHARACTERS LONG")
    @NotBlank(message = "FIRST NAME CANNOT BE NULL")
    @Column(name ="FIRST_NAME")
    private String firstName;

    @Size(min = 1, max = 50, message = "LAST NAME MUST BE BETWEEN {min} AND {max} CHARACTERS LONG")
    @NotBlank(message = "LAST NAME CANNOT BE NULL")
    @Column(name ="LAST_NAME")
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "doctor")
    @JsonManagedReference("doctor-appointments")
    private List<Appointment> appointments;

}
