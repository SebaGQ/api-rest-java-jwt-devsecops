package com.medicalhourmanagement.medicalhourmanagement.services.impl;

import com.medicalhourmanagement.medicalhourmanagement.dtos.RequestAppointmentDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.AppointmentDTO;
import com.medicalhourmanagement.medicalhourmanagement.entities.Appointment;
import com.medicalhourmanagement.medicalhourmanagement.entities.Doctor;
import com.medicalhourmanagement.medicalhourmanagement.entities.Patient;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.InternalServerErrorException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.NotFoundException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.RequestException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.UnauthorizedAppointmentException;
import com.medicalhourmanagement.medicalhourmanagement.repositories.AppointmentRepository;
import com.medicalhourmanagement.medicalhourmanagement.services.AppointmentService;
import com.medicalhourmanagement.medicalhourmanagement.services.DoctorService;
import com.medicalhourmanagement.medicalhourmanagement.services.PatientService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentRepository appointmentRepository;
    private final ModelMapper mapper;

    @Override
    public List<AppointmentDTO> getAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public AppointmentDTO getAppointmentById(@NonNull final Long id) {
        return convertToDTO(getAppointmentByIdHelper(id));
    }

    @Override
    @Transactional
    public AppointmentDTO createAppointment(@NonNull final RequestAppointmentDTO createAppointmentRest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        boolean isAdminOrModerator = isAdminOrModerator(authentication.getAuthorities());

        Patient patient = getPatient(createAppointmentRest.getPatient());
        Doctor doctor = getDoctor(createAppointmentRest.getDoctor());

        validateUserAuthorization(email, isAdminOrModerator, patient);

        validateAppointmentTime(createAppointmentRest.getDate(), doctor.getId(), patient.getId());

        Appointment appointment = buildAppointment(createAppointmentRest.getDate(), doctor, patient);

        return saveAppointment(appointment);
    }

    @Override
    @Transactional
    public AppointmentDTO updateAppointment(@NonNull final Long id, @NonNull final AppointmentDTO appointmentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        boolean isAdminOrModerator = isAdminOrModerator(authentication.getAuthorities());

        Appointment existingAppointment = getAppointmentByIdHelper(id);
        Patient patient = existingAppointment.getPatient();
        Doctor doctor = existingAppointment.getDoctor();

        validateUserAuthorization(email, isAdminOrModerator, patient);

        updateExistingAppointment(existingAppointment, appointmentRequest, doctor, patient);

        validateAppointmentTime(appointmentRequest.getDate(), doctor.getId(), patient.getId());

        return saveAppointment(existingAppointment);
    }

    @Override
    @Transactional
    public void deleteAppointmentById(@NonNull final Long id) {
        getAppointmentByIdHelper(id);
        appointmentRepository.deleteById(id);
    }

    private boolean isAdminOrModerator(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
                        grantedAuthority.getAuthority().equals("ROLE_MODERATOR"));
    }

    private Patient getPatient(Long patientId) {
        return mapper.map(patientService.getPatientById(patientId), Patient.class);
    }

    private Doctor getDoctor(Long doctorId) {
        return mapper.map(doctorService.getDoctorById(doctorId), Doctor.class);
    }

    private void validateUserAuthorization(String email, boolean isAdminOrModerator, Patient patient) {
        if (!isAdminOrModerator && !patient.getEmail().equals(email)) {
            throw new UnauthorizedAppointmentException("You are not authorized to create/update an appointment for this patient");
        }
    }

    private void validateAppointmentTime(@NonNull final LocalDateTime appointmentTime, @NonNull final Long doctorId, @NonNull final Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdOrPatientId(doctorId, patientId);

        if (appointmentTime.getHour() < 8 || appointmentTime.getHour() >= 18) {
            throw new RequestException("APPOINTMENT TIME MUST BE BETWEEN 8 AM AND 6 PM");
        }

        appointments.forEach(existingAppointment -> {
            long timeDifference = Math.abs(Duration.between(existingAppointment.getDate(), appointmentTime).toMinutes());
            if (timeDifference < 60) {
                throw new RequestException("THE TIME OF THE APPOINTMENT MUST HAVE A MINIMUM 60 MINUTE GAP WITH OTHER APPOINTMENTS");
            }
        });
    }

    private Appointment buildAppointment(LocalDateTime date, Doctor doctor, Patient patient) {
        Appointment appointment = new Appointment();
        appointment.setDate(date);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        return appointment;
    }

    private AppointmentDTO saveAppointment(Appointment appointment) {
        try {
            return convertToDTO(appointmentRepository.save(appointment));
        } catch (Exception e) {
            throw new InternalServerErrorException("ERROR DURING APPOINTMENT SAVE");
        }
    }

    private void updateExistingAppointment(Appointment existingAppointment, AppointmentDTO appointmentRequest, Doctor doctor, Patient patient) {
        existingAppointment.setDate(appointmentRequest.getDate());
        existingAppointment.setDoctor(mapper.map(appointmentRequest.getDoctor(), Doctor.class));
        existingAppointment.setPatient(mapper.map(appointmentRequest.getPatient(), Patient.class));
    }

    private Appointment getAppointmentByIdHelper(@NonNull final Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("APPOINTMENT NOT FOUND"));
    }

    private AppointmentDTO convertToDTO(Appointment appointment) {
        return mapper.map(appointment, AppointmentDTO.class);
    }
}