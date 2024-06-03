package com.medicalhourmanagement.medicalhourmanagement.controllers;

import com.medicalhourmanagement.medicalhourmanagement.constants.EndpointsConstants;
import com.medicalhourmanagement.medicalhourmanagement.dtos.DoctorDTO;
import com.medicalhourmanagement.medicalhourmanagement.services.DoctorService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = EndpointsConstants.ENDPOINT_DOCTORS)
@RequiredArgsConstructor
public class DoctorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorController.class);
    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getDoctors() {
        LOGGER.info("Received request to get all doctors");
        List<DoctorDTO> doctors = doctorService.getDoctors();
        LOGGER.info("Returning {} doctors", doctors.size());
        return ResponseEntity.status(HttpStatus.OK).body(doctors);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@NonNull @PathVariable final Long id) {
        LOGGER.info("Received request to get doctor with ID: {}", id);
        DoctorDTO doctor = doctorService.getDoctorById(id);
        LOGGER.info("Returning doctor with ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(doctor);
    }

    @PostMapping
    public ResponseEntity<DoctorDTO> saveDoctor(@NonNull @Valid @RequestBody final DoctorDTO doctor) {
        LOGGER.info("Received request to save a new doctor");
        DoctorDTO savedDoctor = doctorService.saveDoctor(doctor);
        LOGGER.info("Saved new doctor with ID: {}", savedDoctor.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@NonNull @PathVariable final Long id, @NonNull @Valid @RequestBody final DoctorDTO doctorDTO) {
        LOGGER.info("Received request to update doctor with ID: {}", id);
        DoctorDTO updatedDoctor = doctorService.updateDoctor(id, doctorDTO);
        LOGGER.info("Updated doctor with ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDoctor);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteDoctorById(@NonNull @PathVariable final Long id) {
        LOGGER.info("Received request to delete doctor with ID: {}", id);
        doctorService.deleteDoctorById(id);
        LOGGER.info("Deleted doctor with ID: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
