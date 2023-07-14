package kr.co.mz.tutorial.jdbc.servlet.comment;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.Constants;
import kr.co.mz.tutorial.jdbc.exception.DatabaseAccessException;
import kr.co.mz.tutorial.jdbc.exception.WrongBoardInfoException;
import kr.co.mz.tutorial.jdbc.service.CommentService;

public class DeleteServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        this.dataSource = (DataSource) getServletContext().getAttribute(Constants.DATASOURCE_CONTEXT_KEY);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var boardSeq = validateParameter(req.getParameter("boardSeq"));
        var commentSeq = validateParameter(req.getParameter("commentSeq"));
        try (var connection = dataSource.getConnection()) {
            var result = new CommentService(connection).deleteComment(commentSeq);
            req.setAttribute("message", "댓글이 삭제되지 않았습니다.");
            if (result == 1) {
                req.setAttribute("message", "댓글이 삭제되었습니다.");
            }
            req.setAttribute("redirectUrl", "http://localhost:8080/board/view?boardSeq=" + boardSeq);
            req.getRequestDispatcher("/WEB-INF/jsp/redirect.jsp").forward(req, resp);
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }

    private static int validateParameter(String parameter) {
        try {
            return Integer.parseInt(parameter);
        } catch (NumberFormatException | NullPointerException nfe) {
            throw new WrongBoardInfoException();
        }
    }
}
