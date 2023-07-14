package kr.co.mz.tutorial.jdbc.exception;

public class InvalidUpdateCode extends AlertException {

    public InvalidUpdateCode(String boardSeq) {
        super("적합하지 않은 게시글 업데이트 요청 코드입니다.", "http://localhost:8080/board/view?boardSeq=" + boardSeq);
    }
}
