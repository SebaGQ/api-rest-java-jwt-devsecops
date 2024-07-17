package com.medicalhourmanagement.medicalhourmanagement.configs;

import com.medicalhourmanagement.medicalhourmanagement.entities.Doctor;
import com.medicalhourmanagement.medicalhourmanagement.entities.Patient;
import com.medicalhourmanagement.medicalhourmanagement.utils.enums.Role;
import com.medicalhourmanagement.medicalhourmanagement.repositories.DoctorRepository;
import com.medicalhourmanagement.medicalhourmanagement.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;




/*
Esta clase tiene propositos de prueba, esto en producción es ilegalXD
 */
@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Inicializar doctores
            Doctor doctor = Doctor.builder()
                    .firstName("Doctor John")
                    .lastName("Doctor Doe")
                    .email("admin@email.com")
                    .password(passwordEncoder.encode("admin12345"))
                    .role(Role.ADMIN)
                    .build();
            doctorRepository.save(doctor);


        // Inicializar pacientes

            Patient patient = Patient.builder()
                    .firstName("Jane")
                    .lastName("Doe")
                    .email("doctor@email.com")
                    .password(passwordEncoder.encode("user12345"))
                    .rut("12345678-9")
                    .phoneNumber("123456789")
                    .address("123 Main St")
                    .build();
            patientRepository.save(patient);

    }
}