package com.medicalhourmanagement.medicalhourmanagement.services.impl;

import com.medicalhourmanagement.medicalhourmanagement.dtos.ChangePasswordRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.PatientDTO;
import com.medicalhourmanagement.medicalhourmanagement.entities.Patient;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.DuplicateKeyException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.NotFoundException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.UnauthorizedAppointmentException;
import com.medicalhourmanagement.medicalhourmanagement.repositories.PatientRepository;
import com.medicalhourmanagement.medicalhourmanagement.services.PatientService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public List<PatientDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(this::convertToRest).toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public PatientDTO getPatientById(@NonNull final Long patientId) {
        Patient patient = getPatientByIdHelper(patientId);
        validateUserAuthorization(patient.getEmail());
        return convertToRest(patient);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PatientDTO savePatient(@NonNull final PatientDTO patientDTO) {
        if (patientDTO.getId() != null) {
            Patient existingPatient = getPatientByIdHelper(patientDTO.getId());
            throw new DuplicateKeyException("THERE IS ALREADY A PATIENT WITH ID: " + existingPatient.getId());
        }
        Patient patientEntity = convertToEntity(patientDTO);
        Patient savedPatient = patientRepository.save(patientEntity);
        return convertToRest(savedPatient);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PatientDTO updatePatient(@NonNull final Long patientId, @NonNull final PatientDTO patientDTO) {
        Patient existingPatient = getPatientByIdHelper(patientId);
        existingPatient.setFirstName(patientDTO.getFirstName());
        existingPatient.setLastName(patientDTO.getLastName());
        Patient updatedPatient = patientRepository.save(existingPatient);
        return convertToRest(updatedPatient);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePatientById(@NonNull final Long patientId) {
        getPatientByIdHelper(patientId);
        patientRepository.deleteById(patientId);
    }

    private Patient getPatientByIdHelper(@NonNull final Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("PATIENT ID: " + patientId + " NOT FOUND"));
    }

    private void validateUserAuthorization(String patientEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdminOrManager = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
                        grantedAuthority.getAuthority().equals("ROLE_MANAGER"));

        if (!isAdminOrManager && !patientEmail.equals(email)) {
            throw new UnauthorizedAppointmentException("You are not authorized to access this patient's information");
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
            throw new IllegalStateException("Wrong password");
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        patientRepository.save(user);
    }

}
