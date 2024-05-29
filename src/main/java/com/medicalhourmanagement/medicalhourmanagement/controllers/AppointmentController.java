package com.medicalhourmanagement.medicalhourmanagement.controllers;

import com.medicalhourmanagement.medicalhourmanagement.dtos.AppointmentDTO;
import com.medicalhourmanagement.medicalhourmanagement.services.AppointmentService;
import com.medicalhourmanagement.medicalhourmanagement.dtos.RequestAppointmentDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;


    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAppointments() {
        List<AppointmentDTO> appointments = appointmentService.getAppointments();
        return ResponseEntity.status(HttpStatus.OK)
                .body(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id){
        AppointmentDTO appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(appointment);
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody RequestAppointmentDTO requestAppointmentDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AppointmentDTO createdAppointment = appointmentService.createAppointment(requestAppointmentDTO,email);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdAppointment);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentDTO appointmentRequest){
        AppointmentDTO updatedAppointment = appointmentService.updateAppointment(id, appointmentRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedAppointment);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id){
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

}
