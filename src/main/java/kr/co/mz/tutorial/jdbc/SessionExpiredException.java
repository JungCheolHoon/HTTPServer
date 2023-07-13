package kr.co.mz.tutorial.jdbc;

import static kr.co.mz.tutorial.jdbc.Constants.LOGIN_REDIRECT_URL;

public class SessionExpiredException extends AlertException {

    public SessionExpiredException() {
        super("세션이 만료되었습니다.", LOGIN_REDIRECT_URL);
    }
}
