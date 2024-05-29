package com.medicalhourmanagement.medicalhourmanagement.services;

import com.medicalhourmanagement.medicalhourmanagement.dtos.ChangePasswordRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.PatientDTO;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

public interface PatientService {
    List<PatientDTO> getPatients();

    PatientDTO getPatientById(Long patientId);

    @Transactional
    PatientDTO updatePatient(@NonNull Long patientId, @NonNull PatientDTO patientDTO);

    @Transactional
    void deletePatientById(Long patientId);

    @Transactional
    PatientDTO savePatient(PatientDTO paciente);

    void changePassword(ChangePasswordRequestDTO request, Principal connectedUser);
}
