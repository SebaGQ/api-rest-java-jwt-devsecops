package com.medicalhourmanagement.medicalhourmanagement.services;

import com.medicalhourmanagement.medicalhourmanagement.dtos.DoctorDTO;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DoctorService {
    List<DoctorDTO> getDoctors();

    DoctorDTO getDoctorById(@NonNull Long doctorId);

    @Transactional
    DoctorDTO saveDoctor(@NonNull DoctorDTO doctorDTO);

    @Transactional
    DoctorDTO updateDoctor(@NonNull Long doctorId, @NonNull DoctorDTO doctorDTO);

    @Transactional
    void deleteDoctorById(@NonNull Long doctorId);
}
