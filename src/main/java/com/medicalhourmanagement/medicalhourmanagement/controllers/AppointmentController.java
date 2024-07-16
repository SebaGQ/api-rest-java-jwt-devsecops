package com.medicalhourmanagement.medicalhourmanagement.controllers;

import com.medicalhourmanagement.medicalhourmanagement.utils.constants.EndpointsConstants;
import com.medicalhourmanagement.medicalhourmanagement.dtos.AppointmentDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.request.RequestAppointmentDTO;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.NotFoundException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.UnauthorizedAppointmentException;
import com.medicalhourmanagement.medicalhourmanagement.services.AppointmentService;
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
@RequestMapping(EndpointsConstants.ENDPOINT_APPOINTMENTS)
@RequiredArgsConstructor
public class AppointmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentController.class);
    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAppointments() {
        LOGGER.info("Received request to get all appointments");
        List<AppointmentDTO> appointments = appointmentService.getAppointments();
        LOGGER.info("Returning {} appointments", appointments.size());
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@NonNull @PathVariable final Long id) {
        LOGGER.info("Received request to get appointment with ID: {}", id);
        try {
            AppointmentDTO appointment = appointmentService.getAppointmentById(id);
            LOGGER.info("Returning appointment with ID: {}", id);
            return ResponseEntity.ok(appointment);
        } catch (NotFoundException e) {
            LOGGER.warn("Appointment not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@NonNull @Valid @RequestBody final RequestAppointmentDTO requestAppointmentDTO) {
        LOGGER.info("Received request to create a new appointment");
        try {
            AppointmentDTO createdAppointment = appointmentService.createAppointment(requestAppointmentDTO);
            LOGGER.info("Created new appointment with ID: {}", createdAppointment.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAppointment);
        } catch (UnauthorizedAppointmentException e) {
            LOGGER.warn("Unauthorized appointment creation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Invalid appointment data: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@NonNull @PathVariable final Long id, @NonNull @Valid @RequestBody final AppointmentDTO appointmentRequest) {
        LOGGER.info("Received request to update appointment with ID: {}", id);
        try {
            AppointmentDTO updatedAppointment = appointmentService.updateAppointment(id, appointmentRequest);
            LOGGER.info("Updated appointment with ID: {}", id);
            return ResponseEntity.ok(updatedAppointment);
        } catch (NotFoundException e) {
            LOGGER.warn("Appointment not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedAppointmentException e) {
            LOGGER.warn("Unauthorized appointment update: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteAppointment(@NonNull @PathVariable final Long id) {
        LOGGER.info("Received request to delete appointment with ID: {}", id);
        try {
            appointmentService.deleteAppointmentById(id);
            LOGGER.info("Deleted appointment with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            LOGGER.warn("Appointment not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedAppointmentException e) {
            LOGGER.warn("Unauthorized appointment deletion: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}