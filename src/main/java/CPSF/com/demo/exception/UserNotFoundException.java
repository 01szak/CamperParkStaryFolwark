package CPSF.com.demo.exception;

public class UserNotFoundException extends AuthenticationException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
