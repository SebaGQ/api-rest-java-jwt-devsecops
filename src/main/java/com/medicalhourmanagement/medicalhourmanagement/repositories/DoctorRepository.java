package com.medicalhourmanagement.medicalhourmanagement.repositories;


import com.medicalhourmanagement.medicalhourmanagement.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long> {
}
