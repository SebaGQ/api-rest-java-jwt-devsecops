package com.medicalhourmanagement.medicalhourmanagement.appointment;

import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AppointmentService {

    List<AppointmentRest> getAppointments();

    AppointmentRest getAppointmentById(@NonNull Long id) ;

    /**
     * Los métodos que realizan modificaciones en la base de datos deben anotarse con @Transactional para asegurar consistencia en la BD.
     * Si un método falla durante su ejecución se realiza un rollback.
     */
    @Transactional
    AppointmentRest updateAppointment(@NonNull Long appointmentId, @NonNull AppointmentRest appointmentRest);

    @Transactional
    void deleteAppointmentById(@NonNull Long id);

    @Transactional  //Recibe un DTO de request
    AppointmentRest createAppointment(@NonNull RequestAppointmentRest createAppointmentRest);
}
