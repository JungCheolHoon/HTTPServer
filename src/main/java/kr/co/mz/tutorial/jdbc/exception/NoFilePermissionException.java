package kr.co.mz.tutorial.jdbc.exception;

public class NoFilePermissionException extends RuntimeException {

    public NoFilePermissionException(Throwable t) {
        super("파일 삭제에 대한 권한이 없습니다 : " + t.getMessage(), t);
    }
}
