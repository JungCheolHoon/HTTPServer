package kr.co.mz.tutorial.jdbc;

public class NoSuchCustomerFoundException extends AlertException {

    public NoSuchCustomerFoundException(String username) {
        super("사용자를 찾을 수 없습니다: " + username);
    }
}
