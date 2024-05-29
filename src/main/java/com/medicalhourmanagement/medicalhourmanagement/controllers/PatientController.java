package com.medicalhourmanagement.medicalhourmanagement.controllers;

import com.medicalhourmanagement.medicalhourmanagement.dtos.PatientDTO;
import com.medicalhourmanagement.medicalhourmanagement.services.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService){
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getPatients() {
        List<PatientDTO> patients = patientService.getPatients();
        return ResponseEntity.status(HttpStatus.OK)
                .body(patients);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        PatientDTO patient = patientService.getPatientById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(patient);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> savePatient(@Valid @RequestBody PatientDTO patient) {
        PatientDTO savedPatient = patientService.savePatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedPatient);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientDTO patientDTO){
        PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedPatient);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePatientById(@PathVariable Long id) {
        patientService.deletePatientById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

}
