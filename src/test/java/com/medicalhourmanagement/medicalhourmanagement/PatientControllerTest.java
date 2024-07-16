package com.medicalhourmanagement.medicalhourmanagement;

import com.medicalhourmanagement.medicalhourmanagement.controllers.PatientController;
import com.medicalhourmanagement.medicalhourmanagement.dtos.PatientDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.request.ChangePasswordRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.services.PatientService;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.NotFoundException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.UnauthorizedAppointmentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.ServletException;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPatients() {
        List<PatientDTO> patients = List.of(new PatientDTO(), new PatientDTO());
        when(patientService.getPatients()).thenReturn(patients);

        ResponseEntity<List<PatientDTO>> response = patientController.getPatients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetPatientById() {
        PatientDTO patient = new PatientDTO();
        when(patientService.getPatientById(any(Long.class))).thenReturn(patient);

        ResponseEntity<PatientDTO> response = patientController.getPatientById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patient, response.getBody());
    }

    @Test
    void testGetPatientByIdNotFound() {
        when(patientService.getPatientById(any(Long.class))).thenThrow(new NotFoundException("Patient not found"));

        ResponseEntity<PatientDTO> response = patientController.getPatientById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSavePatient() {
        PatientDTO patient = new PatientDTO();
        when(patientService.savePatient(any(PatientDTO.class))).thenReturn(patient);

        ResponseEntity<PatientDTO> response = patientController.savePatient(patient);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(patient, response.getBody());
    }

    @Test
    void testSavePatientInvalid() {
        PatientDTO patient = new PatientDTO();
        when(patientService.savePatient(any(PatientDTO.class))).thenThrow(new IllegalStateException("Failed to save patient"));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            patientController.savePatient(patient);
        });

        assertEquals("Failed to save patient", exception.getMessage());
    }

    @Test
    void testUpdatePatient() {
        PatientDTO patient = new PatientDTO();
        when(patientService.updatePatient(any(Long.class), any(PatientDTO.class))).thenReturn(patient);

        ResponseEntity<PatientDTO> response = patientController.updatePatient(1L, patient);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patient, response.getBody());
    }

    @Test
    void testUpdatePatientNotFound() {
        when(patientService.updatePatient(any(Long.class), any(PatientDTO.class))).thenThrow(new NotFoundException("Patient not found"));

        ResponseEntity<PatientDTO> response = patientController.updatePatient(1L, new PatientDTO());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeletePatientById() {
        doNothing().when(patientService).deletePatientById(any(Long.class));

        ResponseEntity<Void> response = patientController.deletePatientById(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeletePatientByIdNotFound() {
        doThrow(new NotFoundException("Patient not found")).when(patientService).deletePatientById(any(Long.class));

        ResponseEntity<Void> response = patientController.deletePatientById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testChangePassword() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer refresh-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO();
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("test@example.com");

        doNothing().when(patientService).changePassword(any(ChangePasswordRequestDTO.class), any(Principal.class));

        ResponseEntity<Void> responseEntity = patientController.changePassword(changePasswordRequestDTO, principal);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(patientService, times(1)).changePassword(any(ChangePasswordRequestDTO.class), any(Principal.class));
    }

    @Test
    void testChangePasswordInvalid() throws IOException, ServletException {
        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO();
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("test@example.com");

        doThrow(new IllegalStateException("Invalid password")).when(patientService).changePassword(any(ChangePasswordRequestDTO.class), any(Principal.class));

        ResponseEntity<Void> responseEntity = patientController.changePassword(changePasswordRequestDTO, principal);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
