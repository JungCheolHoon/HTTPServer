package kr.co.mz.tutorial.jdbc.servlet.board;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.db.dao.BoardDao;
import kr.co.mz.tutorial.jdbc.file.FileService;

public class DeleteServlet extends HttpServlet {

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        var boardSeq = pathParts[1];
        var result = deleteBoard(Integer.parseInt(boardSeq));
        if (result != 0) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("게시글 삭제 성공 : " + result);
        }
    }

    private int deleteBoard(int boardSeq) {
        var dataSource = (DataSource) getServletContext().getAttribute("dataSource");
        Optional<List<String>> filePathOptional = new BoardDao(dataSource).deleteOne(boardSeq);
        int numberOfFiles = 0;
        if (filePathOptional.isPresent()) {
            List<String> filePathList = filePathOptional.get();
            for (String filePath : filePathList) {
                boolean flag = FileService.delete(filePath);
                if (flag) {
                    System.out.println("Success Delete File on Server Storage : " + filePath);
                }
                numberOfFiles++;
            }
        }
        return numberOfFiles;
    }
}
