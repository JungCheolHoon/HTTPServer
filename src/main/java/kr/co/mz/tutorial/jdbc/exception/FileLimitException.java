package kr.co.mz.tutorial.jdbc.exception;

public class FileLimitException extends AlertException {

    public FileLimitException(String redirectUrl) {
        super("최대로 업로드할 수 있는 파일의 개수는 3개입니다.", redirectUrl);
    }
}
