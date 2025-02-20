package CPSF.com.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationExceptions.class)
    public ResponseEntity<String> handleValidationException(ValidationExceptions exception) {
        System.out.println("🚀 Obsłużono ValidationExceptions: " + exception.getMessage());
        return ResponseEntity.badRequest().body(exception.getMessage());

    }




}
