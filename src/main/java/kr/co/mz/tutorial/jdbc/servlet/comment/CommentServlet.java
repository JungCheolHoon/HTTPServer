package kr.co.mz.tutorial.jdbc.servlet.comment;

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
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        var content = req.getParameter("content");
        var customerSeq = (int) getServletContext().getAttribute("customerSeq");
        var boardSeq = Integer.parseInt(req.getParameter("boardSeq"));
        try {
            var result = insertComment(content, customerSeq, boardSeq);
            if (result == 0) {
                System.out.println("Failed Insert One Comment");
            }
            resp.sendRedirect("/viewBoard?boardSeq=" + boardSeq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int insertComment(String content, int customerSeq, int boardSeq) throws SQLException {
        var dataSource = (DataSource) getServletContext().getAttribute("dataSource");
        return new CommentDao(dataSource).insertOne(content, customerSeq, boardSeq);
    }
}
