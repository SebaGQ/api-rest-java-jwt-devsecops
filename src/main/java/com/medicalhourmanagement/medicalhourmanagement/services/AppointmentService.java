package com.medicalhourmanagement.medicalhourmanagement.services;

import com.medicalhourmanagement.medicalhourmanagement.dtos.RequestAppointmentDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.AppointmentDTO;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AppointmentService {

    List<AppointmentDTO> getAppointments();

    AppointmentDTO getAppointmentById(@NonNull Long id) ;

    /**
     * Los métodos que realizan modificaciones en la base de datos deben anotarse con @Transactional para asegurar consistencia en la BD.
     * Si un método falla durante su ejecución se realiza un rollback.
     */
    @Transactional
    AppointmentDTO updateAppointment(@NonNull Long appointmentId, @NonNull AppointmentDTO appointmentDTO);

    @Transactional
    void deleteAppointmentById(@NonNull Long id);

    @Transactional  //Recibe un DTO de request
    AppointmentDTO createAppointment(@NonNull RequestAppointmentDTO createAppointmentRest, String email);
}
