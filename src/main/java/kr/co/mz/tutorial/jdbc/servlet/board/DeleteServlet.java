package kr.co.mz.tutorial.jdbc.servlet.board;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.db.dao.BoardDao;

public class DeleteServlet extends HttpServlet {

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        var boardSeq = pathParts[1];

        var result = deleteBoard(Integer.parseInt(boardSeq));
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("게시글 삭제 성공 : " + result);

    }

    private int deleteBoard(int boardSeq) {
        var dataSource = (DataSource) getServletContext().getAttribute("dataSource");
        return new BoardDao(dataSource).deleteBoard(boardSeq);
    }
}
