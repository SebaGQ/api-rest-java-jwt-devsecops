package com.medicalhourmanagement.medicalhourmanagement.repositories;

import com.medicalhourmanagement.medicalhourmanagement.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {
}
