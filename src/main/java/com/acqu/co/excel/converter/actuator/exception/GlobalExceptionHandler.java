package com.acqu.co.excel.converter.actuator.exception;

import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceStatus> globalExceptionHandler(Exception ex, WebRequest request) {
        return handleException(ex, request, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ServiceStatus> handleBadRequestException(BadRequestException ex, WebRequest request) {
        return handleException(ex, request, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ServiceStatus> handleException(Exception ex, WebRequest request, HttpStatus httpStatus) {
        LOGGER.error("An exception occurred", ex);

        ServiceStatus apiError = new ServiceStatus();
        apiError.setMessage(ex.getMessage());
        apiError.setStatusCode(httpStatus.value());
        apiError.setErrorDescription(getErrorDescription(ex));

        return new ResponseEntity<>(apiError, httpStatus);
    }

    private String getErrorDescription(Exception ex) {
        if (ex instanceof ConstraintViolationException) {
            return "Validation failed";
        } else if (ex instanceof MethodArgumentNotValidException) {
            return "Invalid request arguments";
        } else if (ex instanceof TypeMismatchException) {
            return "Invalid request type";
        }
        return "An unexpected error occurred";
    }
}