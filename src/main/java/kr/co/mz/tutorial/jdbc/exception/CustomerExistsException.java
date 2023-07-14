package kr.co.mz.tutorial.jdbc.exception;

public class CustomerExistsException extends AlertException {

    public CustomerExistsException() {
        super("중복된 아이디가 이미 존재합니다.", "http://localhost:8080/join");
    }
}
