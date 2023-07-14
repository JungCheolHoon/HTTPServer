package kr.co.mz.tutorial.jdbc.servlet.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Optional;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.Constants;
import kr.co.mz.tutorial.jdbc.db.model.Board;
import kr.co.mz.tutorial.jdbc.db.model.BoardFile;
import kr.co.mz.tutorial.jdbc.db.model.Comment;
import kr.co.mz.tutorial.jdbc.exception.BoardAlreadyDeletedException;
import kr.co.mz.tutorial.jdbc.exception.DatabaseAccessException;
import kr.co.mz.tutorial.jdbc.exception.WrongBoardInfoException;
import kr.co.mz.tutorial.jdbc.service.BoardService;

public class ViewServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() {
        this.dataSource = (DataSource) getServletContext().getAttribute(Constants.DATASOURCE_CONTEXT_KEY);
    }

    public static final String TOP_CONTENTS = """
        <!DOCTYPE html>
        <html>
        <head>
        <title>게시글 보기</title>
        <style>
        /* CSS 코드 */
        body {
          background-color: #f5f5f5;
          font-family: 'Arial', sans-serif;
        }

        .header {
          background-color: #f90;
          padding: 20px;
          color: #fff;
          font-size: 24px;
          font-weight: bold;
        }

        .content {
          background-color: #fff;
          padding: 20px;
        }

        .author {
          font-size: 18px;
          color: #666;
        }

        .date {
          font-size: 16px;
          color: #999;
        }

        .likes {
          font-size: 16px;
          color: #999;
        }

        .body {
          margin-top: 20px;
        }

        .comment-section {
          margin-top: 20px;
        }

        .comment {
          border: 1px solid #ccc;
          padding: 10px;
          margin-bottom: 10px;
        }

        .comment-form {
          margin-top: 20px;
        }

        .comment-form textarea {
          width: 100%;
          height: 80px;
        }

        .buttons {
          margin-top: 20px;
        }

        .buttons a,button {
          display: inline-block;
          padding: 8px 16px;
          background-color: #f90;
          color: #fff;
          text-decoration: none;
          border-radius: 4px;
          margin-right: 10px;
        }

        </style>
        </head>
        <body>
        """;

    private static final String BOTTOM_CONTENTS = """
        <span class="buttons">
            <input style="width:1660px;" name="content" placeholder="댓글을 입력하세요"></input>
            <input style="border-radius: 4px; padding: 8px 16px;background-color: #f90; border: none; color:white;" type="submit" value="등록">
            </span>
            </div>
            </form>
            <div class="buttons">
            <a href="/board">게시글 리스트로 돌아가기</a>
            <a id="deleteButton" style='float: right' href="#">게시글 삭제</a>
            <a style='float: right' href="#" onclick="document.getElementById('viewForm').submit(); return false;">게시글 수정</a>
            </div>
            </div>
            <script>
            const deleteButton = document.getElementById('deleteButton');
            const boardSeqInput = document.getElementsByName('boardSeq')[0];
            deleteButton.addEventListener('click', (event) => {
              event.preventDefault(); // 기본 동작 방지
              const boardSeq = boardSeqInput.value;
              const xhr = new XMLHttpRequest();
              xhr.open('DELETE', '/board/delete/' + boardSeq, true);
              xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
              xhr.onload = function () {
                if (xhr.status === 200) {
                  alert('게시글이 삭제되었습니다.');
                  window.location.href = '/board'; // 게시글 목록 페이지로 이동
                } else {
                  alert('게시글 삭제가 실패되었습니다.');
                  window.location.href = '/board';
                }
              };
              xhr.send();
            });
            </script>
            </body>
            </html>
            """;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var boardSeq = validateParameter(req.getParameter("boardSeq"));
        Optional<Board> optionalBoard;
        try (var connection = dataSource.getConnection()) {
            optionalBoard = new BoardService(connection).view(boardSeq);
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
        if (optionalBoard.isEmpty()) {
            throw new BoardAlreadyDeletedException();
        }
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println(TOP_CONTENTS);
        var board = optionalBoard.get();
        out.println("<form id=\"viewForm\" action=\"/board/update\" method=\"post\" accept-charset=\"UTF-8\">");
        out.println("<input type=\"hidden\" id=\"boardSeq\" name=\"boardSeq\" value=\"" + board.getSeq() + "\">");
        out.println(
            "<div class=\"header\" ><input style='border:none;' class=\"header\" name=\"title\" value=\""
                + board.getTitle()
                + "\" readonly/></div>");
        out.println("<div class=\"content\">");
        out.println(
            "<div class=\"author\">작성자: <input class=\"author\" style='border:none;' name=\"customerName\" value=\""
                + board.getCustomerName()
                + "\"readonly/></div>");
        out.println(
            "<div class=\"author\">카테고리: <input class=\"author\" style='border:none;' name=\"category\" value=\""
                + board.getCategory()
                + "\"readonly/></div>");
        out.println(
            "<div class=\"date\">작성일: <input class=\"date\" style='border:none;' name=\"modifiedTime\" value=\""
                + board.getModifiedTime()
                + "\"readonly/></div>");
        out.println("<div class=\"likes\">좋아요: <span class=\"likes-count\">");
        out.println("<input class=\"date\" style='border:none;' name=\"likesCount\" value=\""
            + board.getLikesCount()
            + "\"readonly/>" + "</span>");
        out.println("<a href=\"/board/likes?likes=1&&boardSeq=" + boardSeq + "\" class=\"likes-button\">👍</a>"
            + "</div>");
        out.println("<div class=\"likes\">첨부파일: ");
        var fileCount = 0;
        for (BoardFile boardFile : board.getBoardFileSet()) {
            fileCount++;
            out.println(
                "<a style=\"border-radius: 4px; background-color: white; border: none; color:#999;\" href=\"/board/download?fileUuid="
                    + boardFile.getFileUuid() + "\">" + boardFile.getFileName()
                    + "</a> ");
            out.println(
                "<input type=\"hidden\" name=\"fileName" + fileCount + "\" value=\"" + boardFile.getFileName() + "\">");
        }
        out.println(
            "<input type=\"hidden\" name=\"fileCount\" value=\"" + fileCount + "\">");
        out.println("</div>");
        out.println(
            "<div style='height:300px; border:0.5px solid;' class=\"body\"><textarea class=\"body\" style='border:none;' name=\"content\" readonly='readonly'>"
                + board.getContent() + "</textarea></div>");
        out.println("</form>");
        out.println("<div class=\"comment-section\">");
        out.println(
            "<input style='text-align:center;font-size:16px;width:100%; height:30px;color:white;height:30px;background-color:#f90; border:none;' value='&nbsp댓글 목록' disabled/>");
        for (Comment comment : board.getCommentSet()) {
            out.println(
                "<div class=\"comment\"><span style='display:inline-block; width:1400px;'> " + comment.getContent()
                    + "</span> <span>작성자 : " + comment.getCustomerName()
                    + "</span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<a href=\"/board/view/comment/delete?boardSeq="
                    + boardSeq
                    + "&&commentSeq="
                    + comment.getSeq() + "\">삭제</a></div>");
        }
        out.println("</div>");
        out.println("<form action=\"/board/view/comment/write\" method=\"post\" accept-charset=\"UTF-8\">");
        out.println("<div class=\"comment-form\">");
        out.println("<input type=\"hidden\" name=\"boardSeq\" value=\"" + board.getSeq() + "\">");
        out.println(BOTTOM_CONTENTS);
        out.close();
    }

    private static int validateParameter(String boardSeqStr) {
        try {
            return Integer.parseInt(boardSeqStr);
        } catch (NumberFormatException | NullPointerException nfe) {
            throw new WrongBoardInfoException();
        }
    }
}
