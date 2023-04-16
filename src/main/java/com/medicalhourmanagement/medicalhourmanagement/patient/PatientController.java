package com.medicalhourmanagement.medicalhourmanagement.patient;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public PatientResponse<List<PatientRest>> getPatients() {
        return new PatientResponse<>("", String.valueOf(HttpStatus.OK), "PATIENTS SUCCESSFULLY READED",
                patientService.getPatients());
    }

    @GetMapping(value = "/{patientId}")
    public PatientResponse<PatientRest> getPatientById(@PathVariable Long id) {
        return new PatientResponse<>("SUCCESS", String.valueOf(HttpStatus.OK), "PATIENT ID: "+id+" SUCCESSFULLY READED",
                patientService.getPatientById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PatientResponse<PatientRest> savePatient(@Valid @RequestBody PatientRest patient) {
        return new PatientResponse<>("SUCCESS", String.valueOf(HttpStatus.CREATED), "PATIENT SUCCESSFULLY SAVED",
                patientService.savePatient(patient));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PatientResponse<PatientRest> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRest patientRest){
        return new PatientResponse<>("SUCCESS", String.valueOf(HttpStatus.OK), "PATIENT ID: "+id+" SUCCESSFULLY UPDATED",
                patientService.updatePatient(id, patientRest));
    }

    @DeleteMapping(value = "/{id}")
    public PatientResponse<String> deletePatientById(@PathVariable Long id) {
        patientService.deletePatientById(id);
        return new PatientResponse<>("DELETED", String.valueOf(HttpStatus.NO_CONTENT), "PATIENT ID: "+id+" SUCCESSFULLY DELETED");
    }

}
