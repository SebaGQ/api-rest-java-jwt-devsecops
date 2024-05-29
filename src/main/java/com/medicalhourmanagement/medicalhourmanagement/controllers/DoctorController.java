package com.medicalhourmanagement.medicalhourmanagement.controllers;

import com.medicalhourmanagement.medicalhourmanagement.dtos.DoctorDTO;
import com.medicalhourmanagement.medicalhourmanagement.services.DoctorService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getDoctors() {
        List<DoctorDTO> doctors = doctorService.getDoctors();
        return ResponseEntity.status(HttpStatus.OK)
                .body(doctors);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@NonNull @PathVariable final Long id) {
        DoctorDTO doctor = doctorService.getDoctorById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(doctor);
    }

    @PostMapping
    public ResponseEntity<DoctorDTO> saveDoctor(@NonNull @Valid @RequestBody final DoctorDTO doctor) {
        DoctorDTO savedDoctor = doctorService.saveDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedDoctor);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@NonNull @PathVariable final Long id, @NonNull @Valid @RequestBody final DoctorDTO doctorDTO){
        DoctorDTO updatedDoctor = doctorService.updateDoctor(id, doctorDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedDoctor);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteDoctorById(@NonNull @PathVariable final Long id){
        doctorService.deleteDoctorById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
