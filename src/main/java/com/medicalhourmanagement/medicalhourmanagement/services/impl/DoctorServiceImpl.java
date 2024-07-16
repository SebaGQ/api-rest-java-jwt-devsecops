package com.medicalhourmanagement.medicalhourmanagement.services.impl;

import com.medicalhourmanagement.medicalhourmanagement.dtos.DoctorDTO;
import com.medicalhourmanagement.medicalhourmanagement.entities.Doctor;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.InternalServerErrorException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.NotFoundException;
import com.medicalhourmanagement.medicalhourmanagement.repositories.DoctorRepository;
import com.medicalhourmanagement.medicalhourmanagement.services.DoctorService;
import com.medicalhourmanagement.medicalhourmanagement.utils.constants.ExceptionMessageConstants;
import com.medicalhourmanagement.medicalhourmanagement.utils.constants.RoleConstants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorServiceImpl.class);

    private final DoctorRepository doctorRepository;
    private final ModelMapper mapper;

    @Override
    public List<DoctorDTO> getDoctors() {
        LOGGER.info("Fetching all doctors");
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream().map(this::convertToRest).toList();
    }

    @Override
    public DoctorDTO getDoctorById(@NonNull final Long doctorId) {
        LOGGER.info("Fetching doctor with ID: {}", doctorId);
        Doctor doctor = getDoctorByIdHelper(doctorId);
        return convertToRest(doctor);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('" + RoleConstants.ROLE_ADMIN + "')")
    public DoctorDTO saveDoctor(@NonNull final DoctorDTO doctorDTO) {
        LOGGER.info("Saving new doctor");
        doctorDTO.setId(null);
        try {
            Doctor doctorEntity = convertToEntity(doctorDTO);
            Doctor savedDoctor = doctorRepository.save(doctorEntity);
            LOGGER.info("Doctor saved successfully with ID: {}", savedDoctor.getId());
            return convertToRest(savedDoctor);
        } catch (Exception e) {
            LOGGER.error("Error occurred during doctor save", e);
            throw new InternalServerErrorException(ExceptionMessageConstants.INTERNAL_SERVER_ERROR_MSG);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('" + RoleConstants.ROLE_ADMIN + "')")
    public DoctorDTO updateDoctor(@NonNull final Long doctorId, @NonNull final DoctorDTO doctorDTO) {
        LOGGER.info("Updating doctor with ID: {}", doctorId);
        getDoctorByIdHelper(doctorId);
        Doctor doctorEntity = convertToEntity(doctorDTO);
        doctorEntity.setId(doctorId);
        Doctor updatedDoctor = doctorRepository.save(doctorEntity);
        LOGGER.info("Doctor updated successfully with ID: {}", updatedDoctor.getId());
        return convertToRest(updatedDoctor);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('" + RoleConstants.ROLE_ADMIN + "')")
    public void deleteDoctorById(@NonNull final Long doctorId) {
        LOGGER.info("Deleting doctor with ID: {}", doctorId);
        getDoctorByIdHelper(doctorId);
        doctorRepository.deleteById(doctorId);
        LOGGER.info("Doctor deleted successfully with ID: {}", doctorId);
    }

    private Doctor getDoctorByIdHelper(@NonNull Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> {
                    LOGGER.warn("Doctor not found with ID: {}", doctorId);
                    return new NotFoundException(ExceptionMessageConstants.DOCTOR_NOT_FOUND_MSG);
                });
    }

    private DoctorDTO convertToRest(Doctor doctor) {
        return mapper.map(doctor, DoctorDTO.class);
    }

    private Doctor convertToEntity(DoctorDTO doctorDTO) {
        return mapper.map(doctorDTO, Doctor.class);
    }
}
