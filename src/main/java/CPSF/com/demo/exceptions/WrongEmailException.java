package CPSF.com.demo.exceptions;

public class WrongEmailException extends Throwable {
    public WrongEmailException(String message) {
        super(message);
    }
}
