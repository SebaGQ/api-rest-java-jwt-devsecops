package com.medicalhourmanagement.medicalhourmanagement.appointment;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/appointments")
public class AppointmentController {


    /**
     * En vez de hacer inyecciones con @Autowired, es recomendable hacerlas por constructor
     */
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public AppointmentResponse<List<AppointmentRest>> getAppointments() {
        return new AppointmentResponse<>("SUCCESS", String.valueOf(HttpStatus.OK), "APPOINTMENTS SUCCESSFULLY READED",
                appointmentService.getAppointments());
    }

    @GetMapping("/{id}")
    public AppointmentResponse<AppointmentRest> getAppointmentById(@PathVariable Long id){
        return new AppointmentResponse<>("SUCCESS", String.valueOf(HttpStatus.OK), "APPOINTMENT ID:"+id+" SUCCESSFULLY READED",
                appointmentService.getAppointmentById(id));
    }

    /**
     * La anotación Valid verifica que el parámetro sea válido
     * La gracia es que la validación se lleva a cabo antes de ejecutar el método.
     * Si la validación falla se arroja MethodArgumentNotValidException
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public AppointmentResponse<AppointmentRest> createAppointment(@Valid @RequestBody RequestAppointmentRest requestAppointmentRest){
        return new AppointmentResponse<>("SUCCESS", String.valueOf(HttpStatus.CREATED), "APPOINTMENT SUCCESSFULLY CREATED",
                appointmentService.createAppointment(requestAppointmentRest));
    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AppointmentResponse<AppointmentRest> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentRest appointmentRequest){
        return new AppointmentResponse<>("SUCCESS", String.valueOf(HttpStatus.OK), "APPOINTMENT ID:"+id+" SUCCESSFULLY UPDATED",
                appointmentService.updateAppointment(id, appointmentRequest));
    }

    @DeleteMapping(value = "/{id}")
    public AppointmentResponse<String> deleteAppointment(@PathVariable Long id){
        appointmentService.deleteAppointmentById(id);
        return new AppointmentResponse<>("DELETED", String.valueOf(HttpStatus.NO_CONTENT), "APPOINTMENT ID:"+id+" SUCCESSFULLY DELETED");
    }

}
