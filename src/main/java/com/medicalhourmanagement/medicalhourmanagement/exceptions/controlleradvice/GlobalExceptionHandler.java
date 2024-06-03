package com.medicalhourmanagement.medicalhourmanagement.exceptions.controlleradvice;

import com.medicalhourmanagement.medicalhourmanagement.constants.ExceptionMessageConstants;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Manejador Global de Excepciones
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<ExceptionDTO> buildResponseEntity(HttpStatus status, String message, WebRequest request) {
        ExceptionDTO error = new ExceptionDTO();
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(message);
        error.setPath(request.getDescription(false).substring(4)); // Remover "uri="
        return new ResponseEntity<>(error, status);
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionDTO> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        LOGGER.error("AuthenticationException: {}", ex.getMessage(), ex);
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return buildResponseEntity(status, ex.getMessage(), request);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ExceptionDTO> handleAppException(AppException ex, WebRequest request) {
        LOGGER.error("AppException: {}", ex.getMessage(), ex);
        HttpStatus status = HttpStatus.valueOf(ex.getResponseCode());
        return buildResponseEntity(status, ex.getMessage(), request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleNotFoundException(NotFoundException ex, WebRequest request) {
        LOGGER.warn("NotFoundException: {}", ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<ExceptionDTO> handleRequestException(RequestException ex, WebRequest request) {
        LOGGER.warn("RequestException: {}", ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        LOGGER.warn("MethodArgumentNotValidException: {}", ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ExceptionDTO> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        LOGGER.warn("HttpMediaTypeNotSupportedException: {}", ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ExceptionDTO> handleDuplicateKeyException(DuplicateKeyException ex, WebRequest request) {
        LOGGER.warn("DuplicateKeyException: {}", ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(UnauthorizedAppointmentException.class)
    public ResponseEntity<ExceptionDTO> handleUnauthorizedAppointmentException(UnauthorizedAppointmentException ex, WebRequest request) {
        LOGGER.warn("UnauthorizedAppointmentException: {}", ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ExceptionDTO> handleInternalServerErrorException(InternalServerErrorException ex, WebRequest request) {
        LOGGER.error("InternalServerErrorException: {}", ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionMessageConstants.INTERNAL_SERVER_ERROR_MSG, request);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionDTO> handleNullPointerException(NullPointerException ex, WebRequest request) {
        LOGGER.error("NullPointerException: {}", ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionMessageConstants.INTERNAL_SERVER_ERROR_MSG, request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDTO> handleRuntimeException(RuntimeException ex, WebRequest request) {
        LOGGER.error("RuntimeException: {}", ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionMessageConstants.INTERNAL_SERVER_ERROR_MSG, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleGenericException(Exception ex, WebRequest request) {
        LOGGER.error("Exception: {}", ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionMessageConstants.INTERNAL_SERVER_ERROR_MSG, request);
    }
}
