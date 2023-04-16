package com.medicalhourmanagement.medicalhourmanagement.doctor;

import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DoctorService {
    List<DoctorRest> getDoctors();

    DoctorRest getDoctorById(@NonNull Long doctorId);

    @Transactional
    DoctorRest saveDoctor(@NonNull DoctorRest doctorRest);

    @Transactional
    DoctorRest updateDoctor(@NonNull Long doctorId, @NonNull DoctorRest doctorRest);

    @Transactional
    void deleteDoctorById(@NonNull Long doctorId);
}
