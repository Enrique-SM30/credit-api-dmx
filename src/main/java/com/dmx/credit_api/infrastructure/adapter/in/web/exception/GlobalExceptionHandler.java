package com.dmx.credit_api.infrastructure.adapter.in.web.exception;

import com.dmx.credit_api.domain.exception.CreditApplicationNotFoundException;
import com.dmx.credit_api.domain.exception.InvalidStatusTransitionException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Formato estándar de error
    public record ErrorResponse(
            OffsetDateTime timestamp,
            int status,
            String error,
            String code,
            String message,
            String path
    ) {}

    //Metodo para contruir respuesta
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String code, String message, HttpServletRequest request){
        ErrorResponse body = new ErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                code,
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }

    //ERROR 404: Error solicitud no encontrada
    @ExceptionHandler(CreditApplicationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(CreditApplicationNotFoundException ex, HttpServletRequest request){
        return buildResponse(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), request);
    }

    //ERROR 409: Error de cambio de status
    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTransition(InvalidStatusTransitionException ex, HttpServletRequest request){
        return buildResponse(HttpStatus.CONFLICT, "INVALID_STATUS_TRANSITION", ex.getMessage(), request);
    }

    //ERROR 400: Error de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request){

        String message = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining("; "));

        return buildResponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message, request);
    }

    //ERROR 400: JSON malformado o enum invalido
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(Exception ex, HttpServletRequest request){
        return buildResponse(HttpStatus.BAD_REQUEST, "INVALID_REQUEST_BODY", "El cuerpo de la solicitud invalido o contiene valores no permitidos", request);
    }

    //ERROR 500: Error general
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Error interno del servidor", request);
    }
}
