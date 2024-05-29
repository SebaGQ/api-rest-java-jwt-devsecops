package com.medicalhourmanagement.medicalhourmanagement.services.impl;

import com.medicalhourmanagement.medicalhourmanagement.dtos.ChangePasswordRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.entities.Patient;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.DuplicateKeyException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.NotFoundException;
import com.medicalhourmanagement.medicalhourmanagement.dtos.PatientDTO;
import com.medicalhourmanagement.medicalhourmanagement.repositories.PatientRepository;
import com.medicalhourmanagement.medicalhourmanagement.services.PatientService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    public final ModelMapper mapper;

    @Override
    public List<PatientDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(this::convertToRest).toList();
    }

    @Override
    public PatientDTO getPatientById(@NonNull final Long patientId) {
        Patient patient = getPatientByIdHelper(patientId);
        return convertToRest(patient);
    }

    @Override
    @Transactional
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
    public PatientDTO updatePatient(@NonNull final Long patientId, PatientDTO patientDTO){
        Patient existingPatient = getPatientByIdHelper(patientId);
        existingPatient.setFirstName(patientDTO.getFirstName());
        existingPatient.setLastName(patientDTO.getLastName());
        Patient updatedPatient = patientRepository.save(existingPatient);
        return convertToRest(updatedPatient);
    }

    @Override
    @Transactional
    public void deletePatientById(@NonNull final Long patientId)  {
        getPatientByIdHelper(patientId);
        patientRepository.deleteById(patientId);
    }

    public Patient getPatientByIdHelper(@NonNull final Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("PATIENT ID: " + patientId + " NOT FOUND"));
    }

    public PatientDTO convertToRest(Patient patient) {
        return mapper.map(patient, PatientDTO.class);
    }
    public Patient convertToEntity(PatientDTO patientDTO) {
        return mapper.map(patientDTO, Patient.class);
    }

    @Override
    public void changePassword(ChangePasswordRequestDTO request, Principal connectedUser) {

        var user = (Patient) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        patientRepository.save(user);
    }

    // Método para buscar un usuario por correo electrónico
    public Optional<Patient> findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }
}
