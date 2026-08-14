package CPSF.com.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserInputException.class)
    public ResponseEntity<String> handleValidationException(UserInputException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationExceptionException(AuthenticationException ex) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
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

    @ExceptionHandler(ClientSideException.class)
    public ResponseEntity<String> handleClientSideException(ClientSideException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
