package com.medicalhourmanagement.medicalhourmanagement.controllers;

import com.medicalhourmanagement.medicalhourmanagement.dtos.ChangePasswordRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.PatientDTO;
import com.medicalhourmanagement.medicalhourmanagement.services.PatientService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getPatients() {
        List<PatientDTO> patients = patientService.getPatients();
        return ResponseEntity.status(HttpStatus.OK)
                .body(patients);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@NonNull @PathVariable final Long id) {
        PatientDTO patient = patientService.getPatientById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(patient);
    }

    @PostMapping
    public ResponseEntity<PatientDTO> savePatient(@NonNull @Valid @RequestBody final PatientDTO patient) {
        PatientDTO savedPatient = patientService.savePatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedPatient);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@NonNull @PathVariable final Long id, @NonNull @Valid @RequestBody final PatientDTO patientDTO){
        PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedPatient);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePatientById(@NonNull @PathVariable final Long id) {
        patientService.deletePatientById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PatchMapping
    public ResponseEntity<Void> changePassword(@NonNull @RequestBody final ChangePasswordRequestDTO request, @NonNull final Principal connectedUser) {
        patientService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
