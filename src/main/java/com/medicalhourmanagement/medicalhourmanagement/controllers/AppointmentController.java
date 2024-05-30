package com.medicalhourmanagement.medicalhourmanagement.controllers;

import com.medicalhourmanagement.medicalhourmanagement.dtos.AppointmentDTO;
import com.medicalhourmanagement.medicalhourmanagement.services.AppointmentService;
import com.medicalhourmanagement.medicalhourmanagement.dtos.RequestAppointmentDTO;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AppointmentDTO> getAppointmentById(@NonNull @PathVariable final Long id){
        AppointmentDTO appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(appointment);
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@NonNull @Valid @RequestBody final RequestAppointmentDTO requestAppointmentDTO){
        AppointmentDTO createdAppointment = appointmentService.createAppointment(requestAppointmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdAppointment);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@NonNull @PathVariable final Long id, @NonNull @Valid @RequestBody final AppointmentDTO appointmentRequest){
        AppointmentDTO updatedAppointment = appointmentService.updateAppointment(id, appointmentRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedAppointment);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteAppointment(@NonNull @PathVariable final Long id){
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
