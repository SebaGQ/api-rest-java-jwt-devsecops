package com.medicalhourmanagement.medicalhourmanagement.services;

import com.medicalhourmanagement.medicalhourmanagement.dtos.SpecialtyDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SpecialtyService {
    List<SpecialtyDTO> getAllSpecialties();

    @Transactional
    SpecialtyDTO createSpecialty(SpecialtyDTO specialtyDTO);
}