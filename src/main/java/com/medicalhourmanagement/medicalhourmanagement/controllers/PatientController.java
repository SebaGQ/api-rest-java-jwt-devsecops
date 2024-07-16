package com.medicalhourmanagement.medicalhourmanagement.controllers;

import com.medicalhourmanagement.medicalhourmanagement.dtos.PatientDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.request.ChangePasswordRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.NotFoundException;
import com.medicalhourmanagement.medicalhourmanagement.services.PatientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);
    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getPatients() {
        LOGGER.info("Received request to get all patients");
        List<PatientDTO> patients = patientService.getPatients();
        LOGGER.info("Returning {} patients", patients.size());
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        LOGGER.info("Received request to get patient with ID: {}", id);
        try {
            PatientDTO patient = patientService.getPatientById(id);
            LOGGER.info("Returning patient with ID: {}", id);
            return ResponseEntity.ok(patient);
        } catch (NotFoundException e) {
            LOGGER.warn("Patient not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PatientDTO> savePatient(@Valid @RequestBody PatientDTO patient) {
        LOGGER.info("Received request to save a new patient");
        try {
            PatientDTO savedPatient = patientService.savePatient(patient);
            LOGGER.info("Saved new patient with ID: {}", savedPatient.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Invalid patient data: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientDTO patientDTO) {
        LOGGER.info("Received request to update patient with ID: {}", id);
        try {
            PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);
            LOGGER.info("Updated patient with ID: {}", id);
            return ResponseEntity.ok(updatedPatient);
        } catch (NotFoundException e) {
            LOGGER.warn("Patient not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatientById(@PathVariable Long id) {
        LOGGER.info("Received request to delete patient with ID: {}", id);
        try {
            patientService.deletePatientById(id);
            LOGGER.info("Deleted patient with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            LOGGER.warn("Patient not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequestDTO request, Principal connectedUser) {
        LOGGER.info("Received request to change password for user: {}", connectedUser.getName());
        try {
            patientService.changePassword(request, connectedUser);
            LOGGER.info("Password changed successfully for user: {}", connectedUser.getName());
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            LOGGER.warn("Failed to change password: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}