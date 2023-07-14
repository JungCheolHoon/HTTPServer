package kr.co.mz.tutorial.jdbc.exception;

public class DatabaseAccessException extends RuntimeException {

    public DatabaseAccessException(String message) {
        super(message);
    }

    public DatabaseAccessException(Throwable t) {
        super("데이터베이스 관련 처리에 오류가 발생하였습니다: " + t.getMessage(), t);
    }
}
