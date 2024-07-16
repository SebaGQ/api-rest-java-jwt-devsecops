package com.medicalhourmanagement.medicalhourmanagement.controllers;

import com.medicalhourmanagement.medicalhourmanagement.dtos.SpecialtyDTO;
import com.medicalhourmanagement.medicalhourmanagement.services.SpecialtyService;
import com.medicalhourmanagement.medicalhourmanagement.utils.constants.EndpointsConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndpointsConstants.ENDPOINT_SPECIALTIES_PATTERN)
@RequiredArgsConstructor
public class SpecialtyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialtyController.class);
    private final SpecialtyService specialtyService;

    @GetMapping
    public ResponseEntity<List<SpecialtyDTO>> getAllSpecialties() {
        LOGGER.info("Received request to get all specialties");
        List<SpecialtyDTO> specialties = specialtyService.getAllSpecialties();
        LOGGER.info("Returning {} specialties", specialties.size());
        return ResponseEntity.ok(specialties);
    }

    @PostMapping
    public ResponseEntity<SpecialtyDTO> createSpecialty(@RequestBody SpecialtyDTO specialtyDTO) {
        LOGGER.info("Received request to create a new specialty");
        try {
            SpecialtyDTO savedSpecialty = specialtyService.createSpecialty(specialtyDTO);
            LOGGER.info("Saved new specialty with ID: {}", savedSpecialty.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSpecialty);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Invalid specialty data: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}