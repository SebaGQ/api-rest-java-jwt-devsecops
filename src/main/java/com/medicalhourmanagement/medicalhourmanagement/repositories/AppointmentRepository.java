package com.medicalhourmanagement.medicalhourmanagement.repositories;


import com.medicalhourmanagement.medicalhourmanagement.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByDoctorIdOrPatientId(Long doctorId,Long patientId);
}
