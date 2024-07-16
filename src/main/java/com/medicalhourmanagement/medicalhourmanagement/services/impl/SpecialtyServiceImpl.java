package com.medicalhourmanagement.medicalhourmanagement.services.impl;

import com.medicalhourmanagement.medicalhourmanagement.dtos.SpecialtyDTO;
import com.medicalhourmanagement.medicalhourmanagement.entities.Specialty;
import com.medicalhourmanagement.medicalhourmanagement.repositories.SpecialtyRepository;
import com.medicalhourmanagement.medicalhourmanagement.services.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialtyServiceImpl.class);

    private final SpecialtyRepository specialtyRepository;
    private final ModelMapper mapper;

    @Override
    public List<SpecialtyDTO> getAllSpecialties() {
        LOGGER.info("Fetching all specialties");
        List<Specialty> specialties = specialtyRepository.findAll();
        return specialties.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public SpecialtyDTO createSpecialty(SpecialtyDTO specialtyDTO) {
        LOGGER.info("Creating new specialty");
        Specialty specialty = convertToEntity(specialtyDTO);
        Specialty savedSpecialty = specialtyRepository.save(specialty);
        return convertToDTO(savedSpecialty);
    }

    private Specialty convertToEntity(SpecialtyDTO specialtyDTO) {
        return mapper.map(specialtyDTO, Specialty.class);
    }

    private SpecialtyDTO convertToDTO(Specialty specialty) {
        return mapper.map(specialty, SpecialtyDTO.class);
    }
}