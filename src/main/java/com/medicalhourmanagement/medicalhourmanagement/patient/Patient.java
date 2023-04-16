package com.medicalhourmanagement.medicalhourmanagement.patient;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.medicalhourmanagement.medicalhourmanagement.appointment.Appointment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="PATIENT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 50, message = "FIRST NAME MUST BE BETWEEN {min} AND {max} CHARACTERS LONG")
    @NotBlank(message = "FIRST NAME CANNOT BE NULL")
    @Column(name ="FIRST_NAME")
    private String firstName;


    @Size(min = 1, max = 50, message = "LAST NAME MUST BE BETWEEN {min} AND {max} CHARACTERS LONG")
    @NotBlank(message = "LAST NAME CANNOT BE NULL")
    @Column(name ="LAST_NAME")
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "patient")
    @JsonManagedReference("patient-appointments")
    private List<Appointment> appointments;

}
