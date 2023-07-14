package kr.co.mz.tutorial.jdbc.exception;

public class FailedWriteCommentException extends AlertException {

    public FailedWriteCommentException(int boardSeq) {
        super("댓글 작성에 실패하셨습니다.", "http://localhost:8080/board/view?boardSeq=" + boardSeq);
    }
}
