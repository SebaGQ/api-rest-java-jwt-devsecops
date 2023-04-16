package com.medicalhourmanagement.medicalhourmanagement.patient;

import com.medicalhourmanagement.medicalhourmanagement.exceptions.models.DuplicateKeyException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.models.NotFoundException;
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
    public List<PatientRest> getPatients(){
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(this::convertToRest)
                .collect(Collectors.toList());
    }

    @Override
    public PatientRest getPatientById(@NonNull final Long patientId) {
        Patient patient = getPatientByIdHelper(patientId);
        return convertToRest(patient);
    }

    @Override
    @Transactional
    public PatientRest savePatient(@NonNull final PatientRest patientRest) {
        if (patientRest.getId() != null) {
            Patient existingPatient = getPatientByIdHelper(patientRest.getId());
            throw new DuplicateKeyException("THERE IS ALREADY A PATIENT WITH ID: " + existingPatient.getId());
        }
        Patient patientEntity = convertToEntity(patientRest);
        Patient savedPatient = patientRepository.save(patientEntity);
        return convertToRest(savedPatient);
    }

    @Override
    @Transactional
    public PatientRest updatePatient(@NonNull final Long patientId, @NonNull final PatientRest patientRest){
        Patient existingPatient = getPatientByIdHelper(patientId);
        existingPatient.setFirstName(patientRest.getFirstName());
        existingPatient.setLastName(patientRest.getLastName());
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

    public PatientRest convertToRest(Patient patient) {
        return mapper.map(patient, PatientRest.class);
    }
    public Patient convertToEntity(PatientRest patientRest) {
        return mapper.map(patientRest, Patient.class);
    }
}
