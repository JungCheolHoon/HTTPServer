package kr.co.mz.tutorial.jdbc.servlet.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Optional;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.Constants;
import kr.co.mz.tutorial.jdbc.db.dao.BoardFileDao;
import kr.co.mz.tutorial.jdbc.db.model.BoardFile;
import kr.co.mz.tutorial.jdbc.file.FileService;

public class FileDownloadServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() {
        this.dataSource = (DataSource) getServletContext().getAttribute(Constants.DATASOURCE_CONTEXT_KEY);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fileUuid = req.getParameter("fileUuid");
        Optional<BoardFile> optionalBoardFile = Optional.empty();
        try {
            optionalBoardFile = findFilePath(fileUuid);
        } catch (SQLException e) {
            System.out.println("Failed Finding By FileUuid : " + e.getMessage());
            e.printStackTrace();
        }
        if (optionalBoardFile.isPresent()) {
            var boardFile = optionalBoardFile.get();
            resp.setContentType(new FileService().getMimeType(boardFile.getFileName()));
            String encodeFileName = URLEncoder.encode(boardFile.getFileName(), StandardCharsets.UTF_8);
            String header = "attachment; filename=" + encodeFileName;
            resp.setHeader("Content-Disposition", header);
            try (
                var fileInputStream = new FileInputStream(boardFile.getFilePath());
                var outputStream = resp.getOutputStream();
            ) {
                byte[] buffer = new byte[4096];
                int byteCount = -1;
                while ((byteCount = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, byteCount);
                }
            }
        }
    }

    private Optional<BoardFile> findFilePath(String fileUuid) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            return new BoardFileDao(connection).findOneFromFileUuid(fileUuid);
        }
    }
}
