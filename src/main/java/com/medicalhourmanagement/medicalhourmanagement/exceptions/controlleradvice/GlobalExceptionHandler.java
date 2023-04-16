package com.medicalhourmanagement.medicalhourmanagement.exceptions.controlleradvice;

import com.medicalhourmanagement.medicalhourmanagement.exceptions.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador Global de Excepciones
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Object> handleAppException(AppException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getResponseCode());
        ErrorDto error = new ErrorDto("P-501", ex.getMessage());
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorDto error = new ErrorDto("P-404", ex.getMessage());
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<Object> handleRequestException(RequestException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDto error = new ErrorDto("P-400", ex.getMessage());
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDto error = new ErrorDto("P-400", ex.getMessage());
        return new ResponseEntity<>(error, status);
    }
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Object> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDto error = new ErrorDto("P-400", ex.getMessage());
        return new ResponseEntity<>(error, status);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorDto error = new ErrorDto("P-666", ex.getMessage());
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Object> handleInternalServerErrorException(InternalServerErrorException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorDto error = new ErrorDto("P-500", ex.getMessage());
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorDto error = new ErrorDto("P-500", ex.getMessage());
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorDto error = new ErrorDto("P-409", ex.getMessage());
        return new ResponseEntity<>(error, status);
    }

}
