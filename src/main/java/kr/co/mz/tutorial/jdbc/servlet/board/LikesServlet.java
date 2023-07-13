package kr.co.mz.tutorial.jdbc.servlet.board;

import static kr.co.mz.tutorial.jdbc.Constants.DATASOURCE_CONTEXT_KEY;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.db.dao.BoardDao;

public class LikesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("likes").equals("1")) {
            var boardSeq = req.getParameter("boardSeq");
            int result = likesCount(Integer.parseInt(boardSeq));
            if (result != 0) {
                resp.sendRedirect("/board/" + boardSeq);
            }
        }
    }

    private int likesCount(int boardSeq) {
        var dataSource = (DataSource) getServletContext().getAttribute(DATASOURCE_CONTEXT_KEY);
        return new BoardDao(dataSource).updateOneOfLikesCount(
            boardSeq, (int) getServletContext().getAttribute("customerSeq")
        );
    }
}
