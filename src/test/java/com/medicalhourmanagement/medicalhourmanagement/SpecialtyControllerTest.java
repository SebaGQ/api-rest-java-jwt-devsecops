package com.medicalhourmanagement.medicalhourmanagement;

import com.medicalhourmanagement.medicalhourmanagement.controllers.SpecialtyController;
import com.medicalhourmanagement.medicalhourmanagement.dtos.SpecialtyDTO;
import com.medicalhourmanagement.medicalhourmanagement.services.SpecialtyService;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SpecialtyControllerTest {

    @Mock
    private SpecialtyService specialtyService;

    @InjectMocks
    private SpecialtyController specialtyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSpecialties() {
        List<SpecialtyDTO> specialties = List.of(new SpecialtyDTO(), new SpecialtyDTO());
        when(specialtyService.getAllSpecialties()).thenReturn(specialties);

        ResponseEntity<List<SpecialtyDTO>> response = specialtyController.getAllSpecialties();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testCreateSpecialty() {
        SpecialtyDTO specialty = new SpecialtyDTO();
        when(specialtyService.createSpecialty(any(SpecialtyDTO.class))).thenReturn(specialty);

        ResponseEntity<SpecialtyDTO> response = specialtyController.createSpecialty(specialty);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(specialty, response.getBody());
    }

    @Test
    void testCreateSpecialtyInvalid() {
        SpecialtyDTO specialty = new SpecialtyDTO();
        when(specialtyService.createSpecialty(any(SpecialtyDTO.class))).thenThrow(new IllegalArgumentException("Invalid specialty data"));

        ResponseEntity<SpecialtyDTO> response = specialtyController.createSpecialty(specialty);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}