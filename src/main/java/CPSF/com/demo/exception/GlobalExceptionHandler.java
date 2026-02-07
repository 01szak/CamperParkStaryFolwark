package CPSF.com.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientInputException.class)
    public ResponseEntity<String> handleValidationException(ClientInputException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var error = new StringBuilder();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(e -> {
                    error
                            .append(e.getField())
                            .append(" ")
                            .append(e.getDefaultMessage());
                });

        return ResponseEntity.badRequest().body(error.toString());
    }

}
