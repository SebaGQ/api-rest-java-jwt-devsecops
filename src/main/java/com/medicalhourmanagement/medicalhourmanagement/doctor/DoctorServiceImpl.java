package com.medicalhourmanagement.medicalhourmanagement.doctor;

import com.medicalhourmanagement.medicalhourmanagement.exceptions.models.DuplicateKeyException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.models.InternalServerErrorException;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class DoctorServiceImpl implements DoctorService {


    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository){
        this.doctorRepository = doctorRepository;
    }

    public static final ModelMapper mapper = new ModelMapper();

    @Override
    public List<DoctorRest> getDoctors() {
        final List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream().map(this::convertToRest)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorRest getDoctorById(@NonNull final Long doctorId) {
        final Doctor doctor = getDoctorByIdHelper(doctorId);
        return convertToRest(doctor);
    }

    @Override
    @Transactional
    public DoctorRest saveDoctor(@NonNull final DoctorRest doctorRest) {
        if (doctorRest.getId() != null) {
            Doctor existingDoctor = getDoctorByIdHelper(doctorRest.getId());
            throw new DuplicateKeyException("THERE IS ALREADY A DOCTOR WITH ID: "+existingDoctor.getId());
        }
        try {
            Doctor doctorEntity = convertToEntity(doctorRest);
            Doctor savedDoctor = doctorRepository.save(doctorEntity);
            return convertToRest(savedDoctor);
        } catch (Exception e) {
            throw new InternalServerErrorException("ERROR DURING DOCTOR SAVE");
        }
    }

    @Override
    @Transactional
    public DoctorRest updateDoctor(@NonNull final Long doctorId, @NonNull final DoctorRest doctorRest) {
        getDoctorByIdHelper(doctorId);
        Doctor doctorEntity = convertToEntity(doctorRest);
        doctorEntity.setId(doctorId);
        Doctor updatedDoctor = doctorRepository.save(doctorEntity);
        return convertToRest(updatedDoctor);
    }

    @Override
    @Transactional
    public void deleteDoctorById(@NonNull final Long doctorId) {
        getDoctorByIdHelper(doctorId);
        doctorRepository.deleteById(doctorId);
    }

    private Doctor getDoctorByIdHelper(@NonNull Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow();
    }

    public DoctorRest convertToRest(Doctor doctor) {
        return mapper.map(doctor, DoctorRest.class);
    }

    public Doctor convertToEntity(DoctorRest doctorRest) {
        return mapper.map(doctorRest, Doctor.class);
    }
}
