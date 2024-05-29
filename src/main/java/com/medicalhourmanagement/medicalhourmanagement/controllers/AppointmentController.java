package com.medicalhourmanagement.medicalhourmanagement.controllers;

import com.medicalhourmanagement.medicalhourmanagement.dtos.AppointmentDTO;
import com.medicalhourmanagement.medicalhourmanagement.services.AppointmentService;
import com.medicalhourmanagement.medicalhourmanagement.dtos.RequestAppointmentDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody RequestAppointmentDTO requestAppointmentDTO){
        AppointmentDTO createdAppointment = appointmentService.createAppointment(requestAppointmentDTO);
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
