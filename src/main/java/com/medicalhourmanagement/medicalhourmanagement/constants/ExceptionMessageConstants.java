package com.medicalhourmanagement.medicalhourmanagement.constants;

public class ExceptionMessageConstants {

    /**
     * AUTH
     */
    public static final String UNAUTHORIZED_MSG = "You are not authorized";
    public static final String WRONG_PASSWORD_MSG = "Wrong password";
    public static final String WRONG_CONF_PASSWORD_MSG = "Password are not the same";
    public static final String EXPIRED_TOKEN_MSG = "Token has expired";
    public static final String INVALID_TOKEN_MSG = "Token is invalid";
    public static final String USER_NOT_FOUND_MSG = "User not found";
    public static final String ROLE_NOT_FOUND_MSG = "Role not found";


    /**
     * APPOINTMENTS
     */
    public static final String TIME_CONFLICT_MSG = "Appointments must be at least 60 minutes apart";
    public static final String TIME_INVALID_MSG = "Appointments must be between 8 AM and 6 PM";
    public static final String APPOINTMENT_NOT_FOUND_MSG = "Appointment not found";


    /**
     *  PATIENTS
     */
    public static final String PATIENT_NOT_FOUND_MSG = "Patient not found";

    /**
     * DOCTORS
     */
    public static final String DOCTOR_NOT_FOUND_MSG = "Doctor not found";


    /**
    *   API RESPONSES
     */
    public static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred during appointment save";
}