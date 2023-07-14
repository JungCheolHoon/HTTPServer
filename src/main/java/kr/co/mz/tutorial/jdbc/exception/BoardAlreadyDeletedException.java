package kr.co.mz.tutorial.jdbc.exception;

public class BoardAlreadyDeletedException extends AlertException {

    public BoardAlreadyDeletedException() {
        super("이미 삭제된 게시글입니다.", "http://localhost:8080/board");
    }
}
