package com.medicalhourmanagement.medicalhourmanagement.controllers;

import com.medicalhourmanagement.medicalhourmanagement.constants.EndpointsConstants;
import com.medicalhourmanagement.medicalhourmanagement.dtos.AppointmentDTO;
import com.medicalhourmanagement.medicalhourmanagement.services.AppointmentService;
import com.medicalhourmanagement.medicalhourmanagement.dtos.RequestAppointmentDTO;
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
        return ResponseEntity.status(HttpStatus.OK)
                .body(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@NonNull @PathVariable final Long id){
        LOGGER.info("Received request to get appointment with ID: {}", id);
        AppointmentDTO appointment = appointmentService.getAppointmentById(id);
        LOGGER.info("Returning appointment with ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(appointment);
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@NonNull @Valid @RequestBody final RequestAppointmentDTO requestAppointmentDTO){
        LOGGER.info("Received request to create a new appointment");
        AppointmentDTO createdAppointment = appointmentService.createAppointment(requestAppointmentDTO);
        LOGGER.info("Created new appointment with ID: {}", createdAppointment.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdAppointment);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@NonNull @PathVariable final Long id, @NonNull @Valid @RequestBody final AppointmentDTO appointmentRequest){
        LOGGER.info("Received request to update appointment with ID: {}", id);
        AppointmentDTO updatedAppointment = appointmentService.updateAppointment(id, appointmentRequest);
        LOGGER.info("Updated appointment with ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedAppointment);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteAppointment(@NonNull @PathVariable final Long id){
        LOGGER.info("Received request to delete appointment with ID: {}", id);
        appointmentService.deleteAppointmentById(id);
        LOGGER.info("Deleted appointment with ID: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
