package com.medicalhourmanagement.medicalhourmanagement.constants;

public class EndpointsConstants {

    // Constructor privado para evitar la instanciación
    private EndpointsConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    //Los pattern son usados para la configuracion de spring security
    public static final String ENDPOINT_BASE_API = "/api/v1";
    public static final String ENDPOINT_AUTH = ENDPOINT_BASE_API + "/auth";
    public static final String ENDPOINT_AUTH_PATTERN = ENDPOINT_AUTH + "/**";
    public static final String ENDPOINT_LOGOUT = ENDPOINT_AUTH + "/logout";
    public static final String ENDPOINT_ACTUATOR = "/actuator";
    public static final String ENDPOINT_ACTUATOR_PATTERN = ENDPOINT_ACTUATOR + "/**";
    public static final String ENDPOINT_DOCTORS = ENDPOINT_BASE_API + "/doctors";
    public static final String ENDPOINT_DOCTORS_PATTERN = ENDPOINT_DOCTORS + "/**";
    public static final String ENDPOINT_PATIENTS = ENDPOINT_BASE_API + "/patients";
    public static final String ENDPOINT_PATIENTS_PATTERN = ENDPOINT_PATIENTS + "/**";
    public static final String ENDPOINT_APPOINTMENTS = ENDPOINT_BASE_API + "/appointments";
    public static final String ENDPOINT_APPOINTMENTS_PATTERN = ENDPOINT_APPOINTMENTS + "/**";
}
