package com.tpo.ecommerce.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        HttpStatus status = ex.getHttpStatus();
        return buildResponse(status, ex.getMessage(), request);
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleBadCredentials(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Mail o contrasenia incorrectos", request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex, HttpServletRequest request) {
        String message = StringUtils.hasText(ex.getMessage())
                ? ex.getMessage()
                : "No fue posible autenticar al usuario";
        return buildResponse(HttpStatus.UNAUTHORIZED, message, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "No tenes permisos para realizar esta accion", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = "Error de validacion en la solicitud";
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null && StringUtils.hasText(fieldError.getDefaultMessage())) {
            message = fieldError.getDefaultMessage();
        }
        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBind(BindException ex, HttpServletRequest request) {
        String message = "Formato de datos invalido";
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null && StringUtils.hasText(fieldError.getDefaultMessage())) {
            message = fieldError.getDefaultMessage();
        }
        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            ConstraintViolationException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex, HttpServletRequest request) {
        String message = StringUtils.hasText(ex.getMessage())
                ? ex.getMessage()
                : "Solicitud invalida";
        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        return buildResponse(
                HttpStatus.CONFLICT,
                "No se pudo completar la operacion por conflicto de datos",
                request
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String message = "Metodo HTTP no permitido para este endpoint";
        if (StringUtils.hasText(ex.getMethod())) {
            message = "Metodo HTTP no permitido: " + ex.getMethod();
        }
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, message, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex, HttpServletRequest request) {
        String message = StringUtils.hasText(ex.getMessage())
                ? ex.getMessage()
                : "Error interno del servidor";
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(
                new ErrorResponse(
                        LocalDateTime.now(),
                        status.value(),
                        status.name(),
                        message,
                        request.getRequestURI()
                )
        );
    }

    public record ErrorResponse(
            LocalDateTime timestamp,
            int statusCode,
            String httpStatus,
            String message,
            String path
    ) {
    }
}
