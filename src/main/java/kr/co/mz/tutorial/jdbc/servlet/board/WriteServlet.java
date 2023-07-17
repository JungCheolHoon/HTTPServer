package kr.co.mz.tutorial.jdbc.servlet.board;

import static kr.co.mz.tutorial.jdbc.Constants.CUSTOMER_IN_SESSION;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.Constants;
import kr.co.mz.tutorial.jdbc.db.model.Board;
import kr.co.mz.tutorial.jdbc.db.model.Category;
import kr.co.mz.tutorial.jdbc.db.model.Customer;
import kr.co.mz.tutorial.jdbc.exception.DatabaseAccessException;
import kr.co.mz.tutorial.jdbc.exception.FileLimitException;
import kr.co.mz.tutorial.jdbc.exception.InputValidationException;
import kr.co.mz.tutorial.jdbc.file.FileService;
import kr.co.mz.tutorial.jdbc.service.BoardService;

public class WriteServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() {
        this.dataSource = (DataSource) getServletContext().getAttribute(Constants.DATASOURCE_CONTEXT_KEY);
    }

    private static final String PAGE_CONTENTS = """
        <!DOCTYPE html>
        <html>
        <head>
        <title>글쓰기</title>
        <style>
        /* CSS 코드 */
        h1 {
          color: white;
          font-family: 'Montserrat', sans-serif;
          background-color: #f90;
          text-align: center;
          font-size: 36px;
          font-weight: bold;
          height: 50px;
        }

        .write-form {
          margin: 20px auto;
          width: 500px;
        }

        .write-form label {
          display: block;
          margin-bottom: 10px;
        }

        .write-form input, .write-form textarea, .write-form select {
          width: 100%;
          padding: 8px;
          margin-bottom: 10px;
        }

        .write-form .button-container {
          text-align: center;
          margin-top: 20px;
        }

        .write-form .button-container button {
          padding: 8px 16px;
          background-color: #f90;
          color: #fff;
          text-decoration: none;
          border-radius: 4px;
          border: none;
        }

        /* 추가적인 CSS 코드 작성 */

        </style>
        </head>
        <body>
        <h1>게 시 판</h1>
        <div class="write-form">
          <form action="/board/write" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
            <label for="title">제목:</label>
            <input type="text" id="title" name="title"><br>
            <label for="content">내용:</label>
            <textarea id="content" name="content"></textarea><br>
            <label for="category">카테고리:</label>
            <select id="category" name="category">
              <option value="여행 경험 공유">여행 경험 공유</option>
              <option value="여행지 추천">여행지 추천</option>
              <option value="여행 계획 토론">여행 계획 토론</option>
            </select><br>
            <input type="file" id="file" name="file" multiple><br>
            <div class="button-container">
              <button type="submit">글쓰기</button>
              <button type="button" onclick="window.history.back()">뒤로가기</button>
            </div>
          </form>
        </div>
        </body>
        </html>
        """;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println(PAGE_CONTENTS);
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        var customer = (Customer) req.getSession().getAttribute(CUSTOMER_IN_SESSION);

        var board = validateInputParameter(req.getParameter("title"), req.getParameter("content"),
            req.getParameter("category"));

        var boardFileSet = new FileService().upload(req.getParts(), null, 1);

        if (boardFileSet == null) {
            throw new FileLimitException("http://localhost:8080/board/write");
        }

        board.setBoardFileSet(boardFileSet);
        try (var connection = dataSource.getConnection()) {
            var boardSeq = new BoardService(connection).write(board, customer);
            if (boardSeq != 0) {
                req.setAttribute("message", "게시글 작성이 완료되었습니다.");
                req.setAttribute("redirectUrl", "http://localhost:8080/board/view?boardSeq=" + boardSeq);
                req.getRequestDispatcher("/WEB-INF/jsp/redirect.jsp").forward(req, resp);
            }
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }

    private static Board validateInputParameter(String title, String content, String category) {
        if (title == null || title.length() < 1) {
            throw new InputValidationException("제목은 한 글자 이상이어야 합니다.");
        }
        if (content == null || content.length() < 1) {
            throw new InputValidationException("내용은 세 글자 이상이어야 합니다.");
        }
        if (category == null || (!category.equals(Category.experienceShare) && !category.equals(Category.recommendation)
            && !category.equals(Category.planDebate))) {
            throw new InputValidationException("내용은 세 글자 이상이어야 합니다.");
        }
        return new Board(title, content, category, null);
    }
}
