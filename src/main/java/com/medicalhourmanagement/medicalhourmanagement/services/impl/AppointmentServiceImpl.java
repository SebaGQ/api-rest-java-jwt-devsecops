    package com.medicalhourmanagement.medicalhourmanagement.services.impl;

    import com.medicalhourmanagement.medicalhourmanagement.dtos.RequestAppointmentDTO;
    import com.medicalhourmanagement.medicalhourmanagement.dtos.AppointmentDTO;
    import com.medicalhourmanagement.medicalhourmanagement.entities.Appointment;
    import com.medicalhourmanagement.medicalhourmanagement.entities.Doctor;
    import com.medicalhourmanagement.medicalhourmanagement.entities.Patient;
    import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.InternalServerErrorException;
    import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.NotFoundException;
    import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.RequestException;
    import com.medicalhourmanagement.medicalhourmanagement.repositories.AppointmentRepository;
    import com.medicalhourmanagement.medicalhourmanagement.services.AppointmentService;
    import com.medicalhourmanagement.medicalhourmanagement.services.DoctorService;
    import com.medicalhourmanagement.medicalhourmanagement.services.PatientService;
    import lombok.NonNull;
    import org.modelmapper.ModelMapper;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.time.Duration;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.stream.Collectors;

    /**
     * Los métodos no deben declarar las excepciones, ya que estas están siendo manejadas por el GlobalExceptionHandler.
     */
    @Service
    public class AppointmentServiceImpl implements AppointmentService {

        private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentServiceImpl.class);

        private final DoctorService doctorService;
        private final PatientService patientService;
        private final AppointmentRepository appointmentRepository;

        public AppointmentServiceImpl(DoctorService doctorService, PatientService patientService, AppointmentRepository appointmentRepository) {
            this.doctorService = doctorService;
            this.patientService = patientService;
            this.appointmentRepository = appointmentRepository;
        }

        public static final ModelMapper mapper = new ModelMapper();

        @Override
        public List<AppointmentDTO> getAppointments() {
            List<Appointment> appointments = appointmentRepository.findAll();
            return appointments.stream().map(this::convertToRest)
                    .collect(Collectors.toList());
        }

        /**
         * Se utiliza final y NonNull en la declaración de las variables de los métodos.
         * NonNull: En caso de que el valor sea null se arrojará una NullPointerException
         */
        @Override
        public AppointmentDTO getAppointmentById(@NonNull final Long id) {
            return convertToRest(getAppointmentByIdHelper(id));
        }

        @Override
        @Transactional
        public AppointmentDTO createAppointment(@NonNull final RequestAppointmentDTO createAppointmentRest, String email ) {
            Patient patient = mapper.map(patientService.getPatientById(createAppointmentRest.getPatient()), Patient.class);
            Doctor doctor = mapper.map(doctorService.getDoctorById(createAppointmentRest.getDoctor()), Doctor.class);

            if (patient.getEmail() != email){}


            validateTime(createAppointmentRest.getDate(), doctor.getId(),patient.getId());

            Appointment appointment = new Appointment();
            appointment.setDate(createAppointmentRest.getDate());
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);

            try {
                return convertToRest(appointmentRepository.save(appointment));
            } catch (Exception e) {
                throw new InternalServerErrorException("ERROR DURING APPOINTMENT SAVE");
            }
        }

        @Override
        @Transactional
        public AppointmentDTO updateAppointment(@NonNull final Long appointmentId, @NonNull final AppointmentDTO appointmentDTO) {
            Appointment existingAppointment = getAppointmentByIdHelper(appointmentId);
            try {
                Doctor doctor = mapper.map(doctorService.getDoctorById(appointmentDTO.getDoctor().getId()), Doctor.class);
                Patient patient = mapper.map(patientService.getPatientById(appointmentDTO.getPatient().getId()), Patient.class);

                existingAppointment.setDate(appointmentDTO.getDate());
                existingAppointment.setDoctor(doctor);
                existingAppointment.setPatient(patient);
                Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
                return convertToRest(updatedAppointment);
            } catch (NotFoundException e) {
                    throw e;
            }catch(Exception e) {
                throw new InternalServerErrorException("ERROR DURING APPOINTMENT ID: "+appointmentId+" UPDATE");
            }
        }

        @Override
        @Transactional
        public void deleteAppointmentById(@NonNull final Long id) {
            getAppointmentByIdHelper(id);
            appointmentRepository.deleteById(id);
        }

        private Appointment getAppointmentByIdHelper(@NonNull final Long id) {
            return appointmentRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("APPOINTMENT ID: " + id + " NOT FOUND"));
        }

        /**
         * Se realizan validaciones a la hora de la cita de una manera bastante simple
         *  Se valida que la hora de una cita debe estar entre 08 y 18 hrs.
         *  También se valida que una nueva cita no tope con otra.
         */
       private void validateTime(@NonNull final LocalDateTime appointmentTime, @NonNull final Long doctorId, @NonNull final Long patientId) {
            List<Appointment> appointments = appointmentRepository.findByDoctorIdOrPatientId(doctorId,patientId);

            int appointmentHour = appointmentTime.getHour();
            if (appointmentHour < 8 || appointmentHour >= 18) {
                throw new RequestException("APPOINTMENT TIME MUST BE BETWEEN 8 AM AND 6 PM");
            }

            for (Appointment existingAppointment : appointments) {
                long timeDifference = Math.abs(Duration.between(existingAppointment.getDate(), appointmentTime).toMinutes());
                if (timeDifference < 60) {
                        throw new RequestException("THE TIME OF THE APPOINTMENT MUST HAVE A MINIMUM 60 MINUTE GAP WITH OTHER APPOINTMENTS");
                }
            }
        }

        public AppointmentDTO convertToRest(Appointment appointment) {
            return mapper.map(appointment, AppointmentDTO.class);
        }

    }