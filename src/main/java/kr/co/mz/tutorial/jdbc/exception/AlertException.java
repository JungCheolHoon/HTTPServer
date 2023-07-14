package kr.co.mz.tutorial.jdbc.exception;

public class AlertException extends RuntimeException {

    private final String redirectUrl;

    public AlertException(String message) {
        this(message, null);
    }

    public AlertException(String message, String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
    }

    public AlertException(Throwable t) {
        super(t);
        redirectUrl = null;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
