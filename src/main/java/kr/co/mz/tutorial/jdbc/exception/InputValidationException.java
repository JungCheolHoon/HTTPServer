package kr.co.mz.tutorial.jdbc.exception;

public class InputValidationException extends AlertException {

    public InputValidationException(String message) {
        super(message);
    }

    public InputValidationException(String message, String redirectUrl) {
        super(message, redirectUrl);
    }
}