package kr.co.mz.tutorial.jdbc.exception;

public class PartsIOException extends RuntimeException {

    public PartsIOException(Throwable t) {
        super("첨부파일 관련 처리에 오류가 발생하였습니다." + t.getMessage(), t);
    }
}
