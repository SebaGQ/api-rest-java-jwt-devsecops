package com.medicalhourmanagement.medicalhourmanagement.doctor;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService){
        this.doctorService = doctorService;
    }

    @GetMapping
    public DoctorResponse<List<DoctorRest>> getDoctors() {
        return new DoctorResponse<>("SUCCESS", String.valueOf(HttpStatus.OK), "DOCTORS SUCCESSFULLY READED",
                doctorService.getDoctors());
    }

    @GetMapping(value = "/{id}")
    public DoctorResponse<DoctorRest> getDoctorById(@PathVariable Long id) {
        return new DoctorResponse<>("SUCCESS", String.valueOf(HttpStatus.OK), "DOCTOR ID: " + id + " SUCCESSFULLY READED",
                doctorService.getDoctorById(id));
    }




    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public DoctorResponse<DoctorRest> saveDoctor(@Valid @RequestBody DoctorRest doctor) {
        return new DoctorResponse<>("SUCCESS", String.valueOf(HttpStatus.CREATED), "DOCTOR SUCCESSFULLY SAVED",
                doctorService.saveDoctor(doctor));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DoctorResponse<DoctorRest> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorRest doctorRest){
        return new DoctorResponse<>("SUCCESS", String.valueOf(HttpStatus.OK), "DOCTOR ID: "+id+" SUCCESSFULLY UPDATED",
                doctorService.updateDoctor(id, doctorRest));
    }

    @DeleteMapping(value = "/{id}")
    public DoctorResponse<String> deleteDoctorById(@PathVariable Long id){
        doctorService.deleteDoctorById(id);
        return new DoctorResponse<>("DELETED", String.valueOf(HttpStatus.NO_CONTENT), "DOCTOR ID: "+id+" SUCCESSFULLY DELETED");
    }
}
