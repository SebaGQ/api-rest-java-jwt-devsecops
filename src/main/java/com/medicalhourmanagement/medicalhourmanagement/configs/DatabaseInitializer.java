package com.medicalhourmanagement.medicalhourmanagement.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initialize() {
        if (isDatabaseEmpty()) {
            insertDoctors();
            insertPatients();
            insertAdmin();
            insertAppointments();
        }
    }

    private boolean isDatabaseEmpty() {
        String checkDoctors = "SELECT COUNT(*) FROM DOCTORS";
        String checkPatients = "SELECT COUNT(*) FROM _patient";
        Integer doctorsCount = jdbcTemplate.queryForObject(checkDoctors, Integer.class);
        Integer patientsCount = jdbcTemplate.queryForObject(checkPatients, Integer.class);

        return (doctorsCount == 0 && patientsCount == 0);
    }

    private void insertDoctors() {
        insertDoctor("Juan", "Pérez");
        insertDoctor("María", "García");
        insertDoctor("José", "Martínez");
        insertDoctor("Antonio", "Gómez");
        insertDoctor("Laura", "Fernández");
        insertDoctor("Mónica", "López");
    }

    private void insertDoctor(String firstName, String lastName) {
        String checkAndInsertDoctor = "INSERT INTO DOCTORS (first_name, last_name) " +
                "SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM DOCTORS WHERE first_name = ? AND last_name = ?)";
        jdbcTemplate.update(checkAndInsertDoctor, firstName, lastName, firstName, lastName);
    }

    private void insertPatients() {
        insertPatient("Carlos", "Rodríguez");
        insertPatient("Ana", "Guzmán");
        insertPatient("Luis", "Ortiz");
        insertPatient("Sofía", "Torres");
        insertPatient("Javier", "Reyes");
        insertPatient("Marina", "Vargas");
    }

    private void insertPatient(String firstName, String lastName) {
        String email = firstName.toLowerCase() + "@email.com";
        String encodedPassword = passwordEncoder.encode("user1234");
        String role = "USER";
        String checkAndInsertPatient = "INSERT INTO _patient (first_name, last_name, email, password, role) " +
                "SELECT ?, ?, ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM _patient WHERE first_name = ? AND last_name = ?)";
        jdbcTemplate.update(checkAndInsertPatient, firstName, lastName, email, encodedPassword, role, firstName, lastName);
    }

    private void insertAdmin() {
        String firstName = "Admin";
        String lastName = "Admin";
        String email = "admin@email.com";
        String encodedPassword = passwordEncoder.encode("admin1234");
        String role = "ADMIN";
        String checkAndInsertAdmin = "INSERT INTO _patient (first_name, last_name, email, password, role) " +
                "SELECT ?, ?, ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM _patient WHERE email = ?)";
        jdbcTemplate.update(checkAndInsertAdmin, firstName, lastName, email, encodedPassword, role, email);
    }

    private void insertAppointments() {
        Long doctorId1 = getDoctorId("Juan", "Pérez");
        Long patientId1 = getPatientId("Carlos", "Rodríguez");
        insertAppointment("2023-05-01T10:00:00", doctorId1, patientId1);

        Long doctorId2 = getDoctorId("María", "García");
        Long patientId2 = getPatientId("Ana", "Guzmán");
        insertAppointment("2023-05-01T11:00:00", doctorId2, patientId2);

        Long doctorId3 = getDoctorId("José", "Martínez");
        Long patientId3 = getPatientId("Luis", "Ortiz");
        insertAppointment("2023-05-01T12:00:00", doctorId3, patientId3);

        Long doctorId4 = getDoctorId("Antonio", "Gómez");
        Long patientId4 = getPatientId("Sofía", "Torres");
        insertAppointment("2023-05-01T14:00:00", doctorId4, patientId4);

        Long doctorId5 = getDoctorId("Laura", "Fernández");
        Long patientId5 = getPatientId("Javier", "Reyes");
        insertAppointment("2023-05-01T15:00:00", doctorId5, patientId5);

        Long doctorId6 = getDoctorId("Mónica", "López");
        Long patientId6 = getPatientId("Marina", "Vargas");
        insertAppointment("2023-05-01T16:00:00", doctorId6, patientId6);
    }

    private void insertAppointment(String date, Long doctorId, Long patientId) {
        String checkAndInsertAppointment = "INSERT INTO APPOINTMENTS (date, doctor_id, patient_id) " +
                "SELECT ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM APPOINTMENTS WHERE date = ? AND doctor_id = ? AND patient_id = ?)";
        jdbcTemplate.update(checkAndInsertAppointment, date, doctorId, patientId, date, doctorId, patientId);
    }

    private Long getDoctorId(String firstName, String lastName) {
        String query = "SELECT id FROM DOCTORS WHERE first_name = ? AND last_name = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{firstName, lastName}, Long.class);
    }

    private Long getPatientId(String firstName, String lastName) {
        String query = "SELECT id FROM _patient WHERE first_name = ? AND last_name = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{firstName, lastName}, Long.class);
    }
}
