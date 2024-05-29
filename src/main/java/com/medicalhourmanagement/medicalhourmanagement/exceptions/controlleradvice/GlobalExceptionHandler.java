package com.medicalhourmanagement.medicalhourmanagement.exceptions.controlleradvice;

import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ExceptionDTO> handleAppException(AppException ex, WebRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getResponseCode());
        ExceptionDTO error = new ExceptionDTO();
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setPath(request.getDescription(false).substring(4)); // Remover "uri="

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleNotFoundException(NotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionDTO error = new ExceptionDTO();
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setPath(request.getDescription(false).substring(4));

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<ExceptionDTO> handleRequestException(RequestException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionDTO error = new ExceptionDTO();
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setPath(request.getDescription(false).substring(4));

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionDTO error = new ExceptionDTO();
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setPath(request.getDescription(false).substring(4));

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ExceptionDTO> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionDTO error = new ExceptionDTO();
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setPath(request.getDescription(false).substring(4));

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDTO> handleRuntimeException(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionDTO error = new ExceptionDTO();
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setPath(request.getDescription(false).substring(4));

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ExceptionDTO> handleInternalServerErrorException(InternalServerErrorException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionDTO error = new ExceptionDTO();
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setPath(request.getDescription(false).substring(4));

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionDTO> handleNullPointerException(NullPointerException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionDTO error = new ExceptionDTO();
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setPath(request.getDescription(false).substring(4));

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ExceptionDTO> handleDuplicateKeyException(DuplicateKeyException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ExceptionDTO error = new ExceptionDTO();
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setPath(request.getDescription(false).substring(4));

        return new ResponseEntity<>(error, status);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleGenericException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionDTO error = new ExceptionDTO();
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setPath(request.getDescription(false).substring(4));

        return new ResponseEntity<>(error, status);
    }
}
