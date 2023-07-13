package kr.co.mz.tutorial.jdbc.servlet.comment;

import static kr.co.mz.tutorial.jdbc.Constants.DATASOURCE_CONTEXT_KEY;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.db.dao.CommentDao;

public class CommentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var content = req.getParameter("content");
        var customerSeq = (int) getServletContext().getAttribute("customerSeq");
        var uriArr = req.getRequestURI().split("/");
        var boardSeq = Integer.parseInt(uriArr[1]);
        try {
            var result = insertComment(content, customerSeq, boardSeq);
            if (result == 0) {
                System.out.println("Failed Insert One Comment");
            }
            resp.sendRedirect("/board/*?boardSeq=" + boardSeq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int insertComment(String content, int customerSeq, int boardSeq) throws SQLException {
        var dataSource = (DataSource) getServletContext().getAttribute(DATASOURCE_CONTEXT_KEY);
        return new CommentDao(dataSource).insertOne(content, customerSeq, boardSeq);
    }
}
