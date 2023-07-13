package kr.co.mz.tutorial.jdbc;

public class AlertException extends RuntimeException {

    private final String redirectUrl;

    public AlertException(String message) {
        this(message, null);
    }

    public AlertException(String message, String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
