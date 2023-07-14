package kr.co.mz.tutorial.jdbc.servlet.comment;

import static kr.co.mz.tutorial.jdbc.Constants.CUSTOMER_IN_SESSION;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.Constants;
import kr.co.mz.tutorial.jdbc.db.model.Customer;
import kr.co.mz.tutorial.jdbc.exception.DatabaseAccessException;
import kr.co.mz.tutorial.jdbc.exception.FailedWriteCommentException;
import kr.co.mz.tutorial.jdbc.exception.InputValidationException;
import kr.co.mz.tutorial.jdbc.exception.WrongBoardInfoException;
import kr.co.mz.tutorial.jdbc.service.CommentService;

public class WriteServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() {
        this.dataSource = (DataSource) getServletContext().getAttribute(Constants.DATASOURCE_CONTEXT_KEY);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        var content = req.getParameter("content");
        var customer = (Customer) req.getSession().getAttribute(CUSTOMER_IN_SESSION);

        try (var connection = dataSource.getConnection()) {
            var boardSeqStr = req.getParameter("boardSeq");
            var boardSeq = validateParameter(content, boardSeqStr);
            var result = new CommentService(connection).insertComment(content, customer.getSeq(),
                boardSeq);
            if (result == 0) {
                throw new FailedWriteCommentException(boardSeq);
            }
            req.setAttribute("message", "댓글 작성에 성공하셨습니다.");
            req.setAttribute("redirectUrl", "http://localhost:8080/board/view?boardSeq=" + boardSeq);
            req.getRequestDispatcher("/WEB-INF/jsp/redirect.jsp").forward(req, resp);
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }

    private static int validateParameter(String content, String boardSeqStr) {
        var boardSeq = 0;
        try {
            boardSeq = Integer.parseInt(boardSeqStr);
        } catch (NumberFormatException | NullPointerException nfe) {
            throw new WrongBoardInfoException();
        }
        if (content == null || content.equals("")) {
            throw new InputValidationException("댓글을 입력해주세요",
                "http://localhost:8080/board/view?boardSeq=" + boardSeqStr);
        }
        return boardSeq;
    }
}
