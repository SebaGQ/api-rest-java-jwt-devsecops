package com.medicalhourmanagement.medicalhourmanagement.services.impl;

import com.medicalhourmanagement.medicalhourmanagement.entities.Doctor;
import com.medicalhourmanagement.medicalhourmanagement.dtos.DoctorDTO;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.DuplicateKeyException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.InternalServerErrorException;
import com.medicalhourmanagement.medicalhourmanagement.repositories.DoctorRepository;
import com.medicalhourmanagement.medicalhourmanagement.services.DoctorService;
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
    public List<DoctorDTO> getDoctors() {
        final List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream().map(this::convertToRest)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorDTO getDoctorById(@NonNull final Long doctorId) {
        final Doctor doctor = getDoctorByIdHelper(doctorId);
        return convertToRest(doctor);
    }

    @Override
    @Transactional
    public DoctorDTO saveDoctor(@NonNull final DoctorDTO doctorDTO) {
        if (doctorDTO.getId() != null) {
            Doctor existingDoctor = getDoctorByIdHelper(doctorDTO.getId());
            throw new DuplicateKeyException("THERE IS ALREADY A DOCTOR WITH ID: "+existingDoctor.getId());
        }
        try {
            Doctor doctorEntity = convertToEntity(doctorDTO);
            Doctor savedDoctor = doctorRepository.save(doctorEntity);
            return convertToRest(savedDoctor);
        } catch (Exception e) {
            throw new InternalServerErrorException("ERROR DURING DOCTOR SAVE");
        }
    }

    @Override
    @Transactional
    public DoctorDTO updateDoctor(@NonNull final Long doctorId, @NonNull final DoctorDTO doctorDTO) {
        getDoctorByIdHelper(doctorId);
        Doctor doctorEntity = convertToEntity(doctorDTO);
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

    public DoctorDTO convertToRest(Doctor doctor) {
        return mapper.map(doctor, DoctorDTO.class);
    }

    public Doctor convertToEntity(DoctorDTO doctorDTO) {
        return mapper.map(doctorDTO, Doctor.class);
    }
}
