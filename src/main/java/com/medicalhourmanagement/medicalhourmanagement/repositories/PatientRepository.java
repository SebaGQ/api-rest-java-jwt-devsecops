package com.medicalhourmanagement.medicalhourmanagement.repositories;

import com.medicalhourmanagement.medicalhourmanagement.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

  Optional<Patient> findByEmail(String email);


}
