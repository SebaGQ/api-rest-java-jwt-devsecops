package com.medicalhourmanagement.medicalhourmanagement.services.impl;

import com.medicalhourmanagement.medicalhourmanagement.dtos.request.ChangePasswordRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.PatientDTO;
import com.medicalhourmanagement.medicalhourmanagement.entities.Patient;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.NotFoundException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.UnauthorizedAppointmentException;
import com.medicalhourmanagement.medicalhourmanagement.repositories.PatientRepository;
import com.medicalhourmanagement.medicalhourmanagement.services.PatientService;
import com.medicalhourmanagement.medicalhourmanagement.constants.ExceptionMessageConstants;
import com.medicalhourmanagement.medicalhourmanagement.constants.RoleConstants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientServiceImpl.class);

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Override
    @PreAuthorize("hasRole('" + RoleConstants.ROLE_ADMIN + "') or hasRole('" + RoleConstants.ROLE_MANAGER + "')")
    public List<PatientDTO> getPatients() {
        LOGGER.info("Fetching all patients");
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(this::convertToRest).toList();
    }

    @Override
    @PreAuthorize("hasRole('" + RoleConstants.ROLE_ADMIN + "') or hasRole('" + RoleConstants.ROLE_MANAGER + "')")
    public PatientDTO getPatientById(@NonNull final Long patientId) {
        LOGGER.info("Fetching patient with ID: {}", patientId);
        Patient patient = getPatientByIdHelper(patientId);
        validateUserAuthorization(patient.getEmail());
        return convertToRest(patient);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('" + RoleConstants.ROLE_ADMIN + "')")
    public PatientDTO savePatient(@NonNull final PatientDTO patientDTO) {
        LOGGER.info("Saving new patient");
        patientDTO.setId(null);
        Patient patientEntity = convertToEntity(patientDTO);
        Patient savedPatient = patientRepository.save(patientEntity);
        if (savedPatient == null) {
            LOGGER.error("Failed to save patient");
            throw new IllegalStateException("Failed to save patient");
        }
        LOGGER.info("Patient saved successfully with ID: {}", savedPatient.getId());
        return convertToRest(savedPatient);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('" + RoleConstants.ROLE_ADMIN + "')")
    public PatientDTO updatePatient(@NonNull final Long patientId, @NonNull final PatientDTO patientDTO) {
        LOGGER.info("Updating patient with ID: {}", patientId);
        Patient existingPatient = getPatientByIdHelper(patientId);
        existingPatient.setFirstName(patientDTO.getFirstName());
        existingPatient.setLastName(patientDTO.getLastName());
        Patient updatedPatient = patientRepository.save(existingPatient);
        if (updatedPatient == null) {
            LOGGER.error("Failed to update patient");
            throw new IllegalStateException("Failed to update patient");
        }
        LOGGER.info("Patient updated successfully with ID: {}", updatedPatient.getId());
        return convertToRest(updatedPatient);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('" + RoleConstants.ROLE_ADMIN + "')")
    public void deletePatientById(@NonNull final Long patientId) {
        LOGGER.info("Deleting patient with ID: {}", patientId);
        Patient patient = getPatientByIdHelper(patientId);
        if (patient == null) {
            LOGGER.error("Patient with ID: {} not found", patientId);
            throw new NotFoundException("Patient not found");
        }
        patientRepository.deleteById(patientId);
        LOGGER.info("Patient deleted successfully with ID: {}", patientId);
    }

    private Patient getPatientByIdHelper(@NonNull final Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> {
                    LOGGER.warn("Patient not found with ID: {}", patientId);
                    return new NotFoundException(ExceptionMessageConstants.PATIENT_NOT_FOUND_MSG);
                });
    }

    private void validateUserAuthorization(String patientEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdminOrManager = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(RoleConstants.ROLE_ADMIN) ||
                        grantedAuthority.getAuthority().equals(RoleConstants.ROLE_MANAGER));

        if (!isAdminOrManager && !patientEmail.equals(email)) {
            LOGGER.warn("Unauthorized access attempt by user {}", email);
            throw new UnauthorizedAppointmentException(ExceptionMessageConstants.UNAUTHORIZED_MSG);
        }
    }

    private PatientDTO convertToRest(Patient patient) {
        return mapper.map(patient, PatientDTO.class);
    }

    private Patient convertToEntity(PatientDTO patientDTO) {
        return mapper.map(patientDTO, Patient.class);
    }

    @Override
    public void changePassword(ChangePasswordRequestDTO request, Principal connectedUser) {
        var user = (Patient) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            LOGGER.warn("Wrong current password for user {}", user.getEmail());
            throw new IllegalStateException(ExceptionMessageConstants.WRONG_PASSWORD_MSG);
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            LOGGER.warn("New password and confirmation do not match for user {}", user.getEmail());
            throw new IllegalStateException(ExceptionMessageConstants.WRONG_CONF_PASSWORD_MSG);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        patientRepository.save(user);
        LOGGER.info("Password changed successfully for user {}", user.getEmail());
    }
}
