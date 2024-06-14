package com.ra.project_md04_api.advice;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.FAILED)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .data(errors)
                        .build()
        );
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.FAILED)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .data(ex.getMessage())
                        .build(),
                ex.getHttpStatus()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.FAILED)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .data(ex.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
