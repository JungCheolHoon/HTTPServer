package kr.co.mz.tutorial.jdbc.exception;

public class WrongBoardInfoException extends AlertException {

    public WrongBoardInfoException() {
        super("잘못된 게시글의 정보입니다.", "http://localhost:8080/board");
    }
}
