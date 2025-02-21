package CPSF.com.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ClientInputException.class)
    public ResponseEntity<String> handleValidationException(ClientInputException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());

    }




}
