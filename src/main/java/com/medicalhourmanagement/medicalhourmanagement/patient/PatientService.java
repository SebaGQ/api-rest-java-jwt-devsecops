package com.medicalhourmanagement.medicalhourmanagement.patient;

import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PatientService {
    List<PatientRest> getPatients();

    PatientRest getPatientById(Long patientId);

    @Transactional
    PatientRest updatePatient(@NonNull Long patientId, @NonNull PatientRest patientRest);

    @Transactional
    void deletePatientById(Long patientId);

    @Transactional
    PatientRest savePatient(PatientRest paciente);
}
