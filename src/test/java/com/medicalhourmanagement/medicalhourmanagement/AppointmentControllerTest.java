package com.medicalhourmanagement.medicalhourmanagement;

import com.medicalhourmanagement.medicalhourmanagement.controllers.AppointmentController;
import com.medicalhourmanagement.medicalhourmanagement.dtos.AppointmentDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.request.RequestAppointmentDTO;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.NotFoundException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.UnauthorizedAppointmentException;
import com.medicalhourmanagement.medicalhourmanagement.services.AppointmentService;
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

class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAppointments() {
        List<AppointmentDTO> appointments = List.of(new AppointmentDTO(), new AppointmentDTO());
        when(appointmentService.getAppointments()).thenReturn(appointments);

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getAppointments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetAppointmentById() {
        AppointmentDTO appointment = new AppointmentDTO();
        when(appointmentService.getAppointmentById(any(Long.class))).thenReturn(appointment);

        ResponseEntity<AppointmentDTO> response = appointmentController.getAppointmentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointment, response.getBody());
    }

    @Test
    void testGetAppointmentByIdNotFound() {
        when(appointmentService.getAppointmentById(any(Long.class))).thenThrow(new NotFoundException("Appointment not found"));

        ResponseEntity<AppointmentDTO> response = appointmentController.getAppointmentById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCreateAppointment() {
        RequestAppointmentDTO requestAppointmentDTO = new RequestAppointmentDTO();
        AppointmentDTO createdAppointment = new AppointmentDTO();
        when(appointmentService.createAppointment(any(RequestAppointmentDTO.class))).thenReturn(createdAppointment);

        ResponseEntity<AppointmentDTO> response = appointmentController.createAppointment(requestAppointmentDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdAppointment, response.getBody());
    }

    @Test
    void testCreateAppointmentUnauthorized() {
        RequestAppointmentDTO requestAppointmentDTO = new RequestAppointmentDTO();
        when(appointmentService.createAppointment(any(RequestAppointmentDTO.class))).thenThrow(new UnauthorizedAppointmentException("Unauthorized"));

        ResponseEntity<AppointmentDTO> response = appointmentController.createAppointment(requestAppointmentDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCreateAppointmentInvalid() {
        RequestAppointmentDTO requestAppointmentDTO = new RequestAppointmentDTO();
        when(appointmentService.createAppointment(any(RequestAppointmentDTO.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<AppointmentDTO> response = appointmentController.createAppointment(requestAppointmentDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateAppointment() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        when(appointmentService.updateAppointment(any(Long.class), any(AppointmentDTO.class))).thenReturn(appointmentDTO);

        ResponseEntity<AppointmentDTO> response = appointmentController.updateAppointment(1L, appointmentDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointmentDTO, response.getBody());
    }

    @Test
    void testUpdateAppointmentNotFound() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        when(appointmentService.updateAppointment(any(Long.class), any(AppointmentDTO.class))).thenThrow(new NotFoundException("Appointment not found"));

        ResponseEntity<AppointmentDTO> response = appointmentController.updateAppointment(1L, appointmentDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateAppointmentUnauthorized() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        when(appointmentService.updateAppointment(any(Long.class), any(AppointmentDTO.class))).thenThrow(new UnauthorizedAppointmentException("Unauthorized"));

        ResponseEntity<AppointmentDTO> response = appointmentController.updateAppointment(1L, appointmentDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteAppointment() {
        doNothing().when(appointmentService).deleteAppointmentById(any(Long.class));

        ResponseEntity<Void> response = appointmentController.deleteAppointment(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteAppointmentNotFound() {
        doThrow(new NotFoundException("Appointment not found")).when(appointmentService).deleteAppointmentById(any(Long.class));

        ResponseEntity<Void> response = appointmentController.deleteAppointment(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteAppointmentUnauthorized() {
        doThrow(new UnauthorizedAppointmentException("Unauthorized")).when(appointmentService).deleteAppointmentById(any(Long.class));

        ResponseEntity<Void> response = appointmentController.deleteAppointment(1L);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}