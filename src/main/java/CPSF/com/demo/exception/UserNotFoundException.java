package CPSF.com.demo.exception;

public class UserNotFoundException extends AuthorizationException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
