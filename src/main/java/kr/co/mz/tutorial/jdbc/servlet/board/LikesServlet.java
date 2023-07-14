package kr.co.mz.tutorial.jdbc.servlet.board;

import static kr.co.mz.tutorial.jdbc.Constants.CUSTOMER_IN_SESSION;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.Constants;
import kr.co.mz.tutorial.jdbc.db.model.Customer;
import kr.co.mz.tutorial.jdbc.exception.DatabaseAccessException;
import kr.co.mz.tutorial.jdbc.exception.WrongBoardInfoException;
import kr.co.mz.tutorial.jdbc.service.BoardService;

public class LikesServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() {
        this.dataSource = (DataSource) getServletContext().getAttribute(Constants.DATASOURCE_CONTEXT_KEY);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var customer = (Customer) req.getSession().getAttribute(CUSTOMER_IN_SESSION);
        var boardSeq = req.getParameter("boardSeq");
        if (req.getParameter("likes").equals("1")) {
            try (var connection = dataSource.getConnection()) {
                int result = new BoardService(connection).likesCount(customer, Integer.parseInt(boardSeq));
                if (result != 0) {
                    resp.sendRedirect("/board/view?boardSeq=" + boardSeq);
                }
            } catch (NumberFormatException nfe) {
                throw new WrongBoardInfoException();
            } catch (SQLException sqle) {
                throw new DatabaseAccessException(sqle);
            }
        }
    }
}
