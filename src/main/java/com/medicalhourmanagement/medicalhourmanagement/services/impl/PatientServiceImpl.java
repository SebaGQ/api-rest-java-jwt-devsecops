package com.medicalhourmanagement.medicalhourmanagement.services.impl;

import com.medicalhourmanagement.medicalhourmanagement.exceptions.models.DuplicateKeyException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.models.NotFoundException;
import com.medicalhourmanagement.medicalhourmanagement.entities.Patient;
import com.medicalhourmanagement.medicalhourmanagement.dtos.PatientDTO;
import com.medicalhourmanagement.medicalhourmanagement.repositories.PatientRepository;
import com.medicalhourmanagement.medicalhourmanagement.services.PatientService;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public static final ModelMapper mapper = new ModelMapper();

    @Override
    public List<PatientDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(this::convertToRest)
                .collect(Collectors.toList());
    }

    @Override
    public PatientDTO getPatientById(@NonNull final Long patientId) {
        Patient patient = getPatientByIdHelper(patientId);
        return convertToRest(patient);
    }

    @Override
    @Transactional
    public PatientDTO savePatient(@NonNull final PatientDTO patientDTO) {
        if (patientDTO.getId() != null) {
            Patient existingPatient = getPatientByIdHelper(patientDTO.getId());
            throw new DuplicateKeyException("THERE IS ALREADY A PATIENT WITH ID: " + existingPatient.getId());
        }
        Patient patientEntity = convertToEntity(patientDTO);
        Patient savedPatient = patientRepository.save(patientEntity);
        return convertToRest(savedPatient);
    }

    @Override
    @Transactional
    public PatientDTO updatePatient(@NonNull final Long patientId, @NonNull final PatientDTO patientDTO){
        Patient existingPatient = getPatientByIdHelper(patientId);
        existingPatient.setFirstName(patientDTO.getFirstName());
        existingPatient.setLastName(patientDTO.getLastName());
        Patient updatedPatient = patientRepository.save(existingPatient);
        return convertToRest(updatedPatient);
    }

    @Override
    @Transactional
    public void deletePatientById(@NonNull final Long patientId)  {
        getPatientByIdHelper(patientId);
        patientRepository.deleteById(patientId);
    }

    public Patient getPatientByIdHelper(@NonNull final Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("PATIENT ID: " + patientId + " NOT FOUND"));
    }

    public PatientDTO convertToRest(Patient patient) {
        return mapper.map(patient, PatientDTO.class);
    }
    public Patient convertToEntity(PatientDTO patientDTO) {
        return mapper.map(patientDTO, Patient.class);
    }
}
