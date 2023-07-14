package kr.co.mz.tutorial.jdbc.servlet.board;

import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.Constants;
import kr.co.mz.tutorial.jdbc.exception.DatabaseAccessException;
import kr.co.mz.tutorial.jdbc.service.BoardService;

public class DeleteServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() {
        this.dataSource = (DataSource) getServletContext().getAttribute(Constants.DATASOURCE_CONTEXT_KEY);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        var boardSeq = pathParts[1];
        try (var connection = dataSource.getConnection()) {
            var result = new BoardService(connection).delete(Integer.parseInt(boardSeq));
            resp.setStatus(HttpServletResponse.SC_OK);
            if (result == 0) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }
}
