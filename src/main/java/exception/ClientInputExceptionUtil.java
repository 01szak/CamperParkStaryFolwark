package exception;

public class ClientInputExceptionUtil {

	public static void ensure(boolean condition, String message) {
		if (condition) {
			throw new ClientInputException(message);
		}
	}
}
