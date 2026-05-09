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

    // Maneja las excepciones propias de la aplicacion, como recursos no encontrados,
    // solicitudes invalidas, duplicados o usuarios no autorizados.
    // Salta cuando algun servicio o controlador lanza una ApiException o una de sus clases hijas.
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        HttpStatus status = ex.getHttpStatus();
        return buildResponse(status, ex.getMessage(), request);
    }

    // Maneja errores de login con credenciales incorrectas o usuarios inexistentes.
    // Salta cuando Spring Security no puede validar el mail o la contrasenia ingresados.
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleBadCredentials(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Mail o contrasenia incorrectos", request);
    }

    // Maneja errores generales de autenticacion.
    // Salta cuando falla la autenticacion por una causa distinta a credenciales incorrectas.
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex, HttpServletRequest request) {
        String message = StringUtils.hasText(ex.getMessage())
                ? ex.getMessage()
                : "No fue posible autenticar al usuario";
        return buildResponse(HttpStatus.UNAUTHORIZED, message, request);
    }

    // Maneja accesos rechazados por falta de permisos.
    // Salta cuando el usuario esta autenticado, pero no tiene autorizacion para usar el endpoint.
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "No tenes permisos para realizar esta accion", request);
    }

    // Maneja errores de validacion en el cuerpo de la request.
    // Salta cuando un DTO anotado con @Valid no cumple sus restricciones.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = "Error de validacion en la solicitud";
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null && StringUtils.hasText(fieldError.getDefaultMessage())) {
            message = fieldError.getDefaultMessage();
        }
        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    // Maneja errores al convertir o validar parametros enlazados a objetos.
    // Salta cuando Spring no puede hacer el binding de datos enviados en la request.
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBind(BindException ex, HttpServletRequest request) {
        String message = "Formato de datos invalido";
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null && StringUtils.hasText(fieldError.getDefaultMessage())) {
            message = fieldError.getDefaultMessage();
        }
        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    // Maneja solicitudes mal formadas o con parametros invalidos.
    // Salta cuando faltan parametros, hay tipos incorrectos, JSON invalido,
    // restricciones violadas o argumentos no aceptados por la aplicacion.
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

    // Maneja conflictos de integridad en la base de datos.
    // Salta cuando una operacion viola una restriccion, por ejemplo una clave unica o foranea.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        return buildResponse(
                HttpStatus.CONFLICT,
                "No se pudo completar la operacion por conflicto de datos",
                request
        );
    }

    // Maneja metodos HTTP no permitidos para un endpoint.
    // Salta cuando se llama una ruta existente con un metodo incorrecto, por ejemplo POST en vez de GET.
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String message = "Metodo HTTP no permitido para este endpoint";
        if (StringUtils.hasText(ex.getMethod())) {
            message = "Metodo HTTP no permitido: " + ex.getMethod();
        }
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, message, request);
    }

    // Maneja cualquier error no contemplado por los handlers anteriores.
    // Salta como ultima barrera cuando ocurre una excepcion inesperada en la aplicacion. -> tira 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex, HttpServletRequest request) {
        String message = StringUtils.hasText(ex.getMessage())
                ? ex.getMessage()
                : "Error interno del servidor";
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, request);
    }

    // Construye una respuesta de error uniforme para todos los handlers.
    // Se usa cada vez que hay que devolver status, mensaje, fecha y path de la request.
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
