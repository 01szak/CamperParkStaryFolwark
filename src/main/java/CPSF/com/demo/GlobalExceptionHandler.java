package CPSF.com.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ClientInputException.class)
    public ResponseEntity<String> handleValidationException(ClientInputException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());

    }    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());

    }




}
