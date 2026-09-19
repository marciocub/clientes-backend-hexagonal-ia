package com.escuela.infrastructure.in;

import com.escuela.domain.exception.ClienteNoEncontradoException;
import com.escuela.domain.exception.CredencialesInvalidasException;
import com.escuela.domain.exception.EmailDuplicadoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Adaptador de entrada que mapea excepciones de dominio a codigos HTTP:
 * - MethodArgumentNotValidException -&gt; 400 Bad Request
 * - CredencialesInvalidasException   -&gt; 401 Unauthorized
 * - ClienteNoEncontradoException      -&gt; 404 Not Found
 * - EmailDuplicadoException          -&gt; 409 Conflict
 * - IllegalArgumentException         -&gt; 400 Bad Request (ej. estado invalido)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Cuerpo de error uniforme para toda la API. */
    public record ErrorRespuesta(int status, String error, String mensaje, LocalDateTime fecha) {
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> manejarValidaciones(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest()
                .body(new ErrorRespuesta(400, "Bad Request", mensaje, LocalDateTime.now()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuesta> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorRespuesta(400, "Bad Request",
                        "El cuerpo de la peticion no es un JSON valido", LocalDateTime.now()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRespuesta> manejarArgumentoInvalido(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorRespuesta(400, "Bad Request", ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorRespuesta> manejarCredencialesInvalidas(CredencialesInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorRespuesta(401, "Unauthorized", ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> manejarClienteNoEncontrado(ClienteNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorRespuesta(404, "Not Found", ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<ErrorRespuesta> manejarEmailDuplicado(EmailDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorRespuesta(409, "Conflict", ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> manejarErrorGenerico(Exception ex) {
        LOG.error("Error interno no esperado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorRespuesta(500, "Internal Server Error",
                        "Ocurrio un error interno en el servidor", LocalDateTime.now()));
    }
}
