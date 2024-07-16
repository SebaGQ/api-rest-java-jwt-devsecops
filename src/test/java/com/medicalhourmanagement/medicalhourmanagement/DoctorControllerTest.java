package com.medicalhourmanagement.medicalhourmanagement;

import com.medicalhourmanagement.medicalhourmanagement.controllers.DoctorController;
import com.medicalhourmanagement.medicalhourmanagement.dtos.DoctorDTO;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.NotFoundException;
import com.medicalhourmanagement.medicalhourmanagement.services.DoctorService;
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

class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDoctors() {
        List<DoctorDTO> doctors = List.of(new DoctorDTO(), new DoctorDTO());
        when(doctorService.getDoctors()).thenReturn(doctors);

        ResponseEntity<List<DoctorDTO>> response = doctorController.getDoctors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetDoctorById() {
        DoctorDTO doctor = new DoctorDTO();
        when(doctorService.getDoctorById(any(Long.class))).thenReturn(doctor);

        ResponseEntity<DoctorDTO> response = doctorController.getDoctorById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(doctor, response.getBody());
    }

    @Test
    void testGetDoctorByIdNotFound() {
        when(doctorService.getDoctorById(any(Long.class))).thenThrow(new NotFoundException("Doctor not found"));

        ResponseEntity<DoctorDTO> response = doctorController.getDoctorById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testSaveDoctor() {
        DoctorDTO doctor = new DoctorDTO();
        when(doctorService.saveDoctor(any(DoctorDTO.class))).thenReturn(doctor);

        ResponseEntity<DoctorDTO> response = doctorController.saveDoctor(doctor);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(doctor, response.getBody());
    }

    @Test
    void testSaveDoctorInvalid() {
        DoctorDTO doctor = new DoctorDTO();
        when(doctorService.saveDoctor(any(DoctorDTO.class))).thenThrow(new IllegalArgumentException("Invalid doctor data"));

        ResponseEntity<DoctorDTO> response = doctorController.saveDoctor(doctor);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateDoctor() {
        DoctorDTO doctor = new DoctorDTO();
        when(doctorService.updateDoctor(any(Long.class), any(DoctorDTO.class))).thenReturn(doctor);

        ResponseEntity<DoctorDTO> response = doctorController.updateDoctor(1L, doctor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(doctor, response.getBody());
    }

    @Test
    void testUpdateDoctorNotFound() {
        DoctorDTO doctor = new DoctorDTO();
        when(doctorService.updateDoctor(any(Long.class), any(DoctorDTO.class))).thenThrow(new NotFoundException("Doctor not found"));

        ResponseEntity<DoctorDTO> response = doctorController.updateDoctor(1L, doctor);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteDoctorById() {
        doNothing().when(doctorService).deleteDoctorById(any(Long.class));

        ResponseEntity<Void> response = doctorController.deleteDoctorById(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteDoctorByIdNotFound() {
        doThrow(new NotFoundException("Doctor not found")).when(doctorService).deleteDoctorById(any(Long.class));

        ResponseEntity<Void> response = doctorController.deleteDoctorById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}