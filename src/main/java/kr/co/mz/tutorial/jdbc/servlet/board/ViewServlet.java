package kr.co.mz.tutorial.jdbc.servlet.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Optional;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.db.dao.BoardDao;
import kr.co.mz.tutorial.jdbc.db.model.Board;
import kr.co.mz.tutorial.jdbc.db.model.BoardFile;
import kr.co.mz.tutorial.jdbc.db.model.Comment;

public class ViewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        Optional<Board> optionalBoard = Optional.empty();
        try {
            optionalBoard = viewBoard(Integer.parseInt(req.getParameter("boardSeq")));
        } catch (SQLException e) {
            System.out.println("The board does not exist : " + e.getMessage());
            e.printStackTrace();
        }
        if (optionalBoard.isEmpty()) {
            resp.sendRedirect("/main");
            return;
        }
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>게시글 보기</title>");
        out.println("<style>");
        out.println("/* CSS 코드 */");
        out.println("body {");
        out.println("  background-color: #f5f5f5;");
        out.println("  font-family: 'Arial', sans-serif;");
        out.println("}");

        out.println(".header {");
        out.println("  background-color: #f90;");
        out.println("  padding: 20px;");
        out.println("  color: #fff;");
        out.println("  font-size: 24px;");
        out.println("  font-weight: bold;");
        out.println("}");

        out.println(".content {");
        out.println("  background-color: #fff;");
        out.println("  padding: 20px;");
        out.println("}");

        out.println(".author {");
        out.println("  font-size: 18px;");
        out.println("  color: #666;");
        out.println("}");

        out.println(".date {");
        out.println("  font-size: 16px;");
        out.println("  color: #999;");
        out.println("}");

        out.println(".likes {");
        out.println("  font-size: 16px;");
        out.println("  color: #999;");
        out.println("}");

        out.println(".body {");
        out.println("  margin-top: 20px;");
        out.println("}");

        out.println(".comment-section {");
        out.println("  margin-top: 20px;");
        out.println("}");

        out.println(".comment {");
        out.println("  border: 1px solid #ccc;");
        out.println("  padding: 10px;");
        out.println("  margin-bottom: 10px;");
        out.println("}");

        out.println(".comment-form {");
        out.println("  margin-top: 20px;");
        out.println("}");

        out.println(".comment-form textarea {");
        out.println("  width: 100%;");
        out.println("  height: 80px;");
        out.println("}");

        out.println(".buttons {");
        out.println("  margin-top: 20px;");
        out.println("}");

        out.println(".buttons a,button {");
        out.println("  display: inline-block;");
        out.println("  padding: 8px 16px;");
        out.println("  background-color: #f90;");
        out.println("  color: #fff;");
        out.println("  text-decoration: none;");
        out.println("  border-radius: 4px;");
        out.println("  margin-right: 10px;");
        out.println("}");

        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        var board = optionalBoard.get();
        out.println("<form id=\"viewForm\" action=\"/updateBoard\" method=\"post\" accept-charset=\"UTF-8\">");
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
        out.println("<a href=\"/likesBoard?boardSeq=" + board.getSeq() + "&&likes=1\" class=\"likes-button\">👍</a>"
            + "</div>");
        out.println("<div class=\"likes\">첨부파일: ");
        var fileCount = 0;
        for (BoardFile boardFile : board.getBoardFileSet()) {
            fileCount++;
            out.println(
                "<a style=\"border-radius: 4px; background-color: white; border: none; color:#999;\" href=\"/download?fileUuid="
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
                "<div class=\"comment\"><span style='display:inline-block; width:1500px;'> " + comment.getContent()
                    + "</span> <span>작성자 : " + comment.getCustomerName() + "</span></div>");
        }
        out.println("</div>");
        out.println("<form action=\"/comment\" method=\"post\" accept-charset=\"UTF-8\">");
        out.println("<div class=\"comment-form\">");
        out.println("<input type=\"hidden\" name=\"boardSeq\" value=\"" + board.getSeq() + "\">");
        out.println("<span class=\"buttons\">");
        out.println("<input style=\"width:1660px;\" name=\"content\" placeholder=\"댓글을 입력하세요\"></input>");
        out.println(
            "<input style=\"border-radius: 4px; padding: 8px 16px;background-color: #f90; border: none; color:white;\" type=\"submit\" value=\"등록\">");
        out.println("</span>");
        out.println("</div>");
        out.println("</form>");
        out.println("<div class=\"buttons\">");
        out.println("<a href=\"/main\">게시글 리스트로 돌아가기</a>");
        out.println("<a id=\"deleteButton\" style='float: right' href=\"#\">게시글 삭제</a>");
        out.println(
            "<a style='float: right' href=\"#\" onclick=\"document.getElementById('viewForm').submit(); return false;\">게시글 수정</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("<script>");
        out.println("const deleteButton = document.getElementById('deleteButton');");
        out.println("const boardSeqInput = document.getElementById('boardSeq');");
        out.println("deleteButton.addEventListener('click', (event) => {");
        out.println("  event.preventDefault();"); // 기본 동작 방지
        out.println("  const boardSeq = boardSeqInput.value;");
        out.println("  const xhr = new XMLHttpRequest();");
        out.println("  xhr.open('DELETE', '/deleteBoard/' + boardSeq, true);");
        out.println("  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');");
        out.println("  xhr.onload = function () {");
        out.println("    if (xhr.status === 200) {");
        out.println("      console.log('게시글 삭제 성공');");
        out.println("      window.location.href = '/main';"); // 게시글 목록 페이지로 이동
        out.println("    } else {");
        out.println("      console.error('게시글 삭제 실패');");
        out.println("    }");
        out.println("  };");
        out.println("  xhr.send();");
        out.println("});");
        out.println("</script>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    private Optional<Board> viewBoard(int boardSeq) throws SQLException {
        var dataSource = (DataSource) getServletContext().getAttribute("dataSource");
        return new BoardDao(dataSource).findOne(boardSeq);
    }
}
