package com.medicalhourmanagement.medicalhourmanagement.controllers;

import com.medicalhourmanagement.medicalhourmanagement.constants.EndpointsConstants;
import com.medicalhourmanagement.medicalhourmanagement.dtos.ChangePasswordRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.PatientDTO;
import com.medicalhourmanagement.medicalhourmanagement.services.PatientService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = EndpointsConstants.ENDPOINT_PATIENTS)
@RequiredArgsConstructor
public class PatientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);
    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getPatients() {
        LOGGER.info("Received request to get all patients");
        List<PatientDTO> patients = patientService.getPatients();
        LOGGER.info("Returning {} patients", patients.size());
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@NonNull @PathVariable final Long id) {
        LOGGER.info("Received request to get patient with ID: {}", id);
        PatientDTO patient = patientService.getPatientById(id);
        LOGGER.info("Returning patient with ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(patient);
    }

    @PostMapping
    public ResponseEntity<PatientDTO> savePatient(@NonNull @Valid @RequestBody final PatientDTO patient) {
        LOGGER.info("Received request to save a new patient");
        PatientDTO savedPatient = patientService.savePatient(patient);
        LOGGER.info("Saved new patient with ID: {}", savedPatient.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@NonNull @PathVariable final Long id, @NonNull @Valid @RequestBody final PatientDTO patientDTO) {
        LOGGER.info("Received request to update patient with ID: {}", id);
        PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);
        LOGGER.info("Updated patient with ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPatient);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePatientById(@NonNull @PathVariable final Long id) {
        LOGGER.info("Received request to delete patient with ID: {}", id);
        patientService.deletePatientById(id);
        LOGGER.info("Deleted patient with ID: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping
    public ResponseEntity<Void> changePassword(@NonNull @RequestBody final ChangePasswordRequestDTO request, @NonNull final Principal connectedUser) {
        LOGGER.info("Received request to change password for user: {}", connectedUser.getName());
        patientService.changePassword(request, connectedUser);
        LOGGER.info("Password changed successfully for user: {}", connectedUser.getName());
        return ResponseEntity.ok().build();
    }
}
