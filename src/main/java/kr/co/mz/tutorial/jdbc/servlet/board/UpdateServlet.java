package kr.co.mz.tutorial.jdbc.servlet.board;

import static kr.co.mz.tutorial.jdbc.Constants.DATASOURCE_CONTEXT_KEY;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.db.dao.BoardDao;
import kr.co.mz.tutorial.jdbc.db.model.Board;
import kr.co.mz.tutorial.jdbc.file.FileService;

public class UpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var optionalUpdateView = Optional.ofNullable(req.getParameter("update"));
        var boardSeq = req.getParameter("boardSeq");
        if (optionalUpdateView.isPresent()) {
            int result = 0;
            try {
                result = updateBoard(
                    new Board(Integer.parseInt(boardSeq), req.getParameter("title"),
                        req.getParameter("content")
                        , req.getParameter("category")), req);
            } catch (ServletException | SQLException e) {
                System.out.println("Failed Update Board : " + e.getMessage());
                e.printStackTrace();
            }
            if (result == 1) {
                System.out.println("게시글 수정에 성공하였습니다.");
            } else {
                System.out.println("게시글 수정에 실패하였습니다.");
            }
            resp.sendRedirect("/board/" + boardSeq);
            return;
        }

        resp.setContentType("text/html; charset=UTF-8");
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
        out.println(
            "<form id=\"viewForm\" action=\"/board/" + boardSeq
                + "/update\" method=\"post\" accept-charset=\"UTF-8\" "
                + "enctype=\"multipart/form-data\">");
        out.println("<input type=\"hidden\" name=\"boardSeq\" value=\"" + req.getParameter("boardSeq") + "\">");
        out.println("<input type=\"hidden\" name=\"update\" value=\"1\">");
        out.println(
            "<div class=\"header\" name=\"title\"><input style='border:none;' class=\"header\" name=\"title\" value=\""
                + req.getParameter("title")
                + "\"/></div>");
        out.println("<div class=\"content\">");
        out.println(
            "<div class=\"author\">작성자: "
                + req.getParameter("customerName")
                + "</div>");
        out.println("    <select id=\"category\" name=\"category\">");
        out.println("      <option value=\"여행 경험 공유\">여행 경험 공유</option>");
        out.println("      <option value=\"여행지 추천\">여행지 추천</option>");
        out.println("      <option value=\"여행 계획 토론\">여행 계획 토론</option>");
        out.println("    </select><br>");
        out.println("<div class=\"date\">작성일: " + req.getParameter("modifiedTime") + "</div>");
        out.println("<div class=\"likes\">좋아요: " + req.getParameter("likesCount") + "</div>");
        out.println("<div class=\"likes\">첨부파일: ");
        if (!req.getParameter("fileCount").equals("0")) {
            for (int i = 1; i <= Integer.parseInt(req.getParameter("fileCount")); i++) {
                out.println("<span class=\"likes\">" + req.getParameter("fileName" + i) + " </span>");
            }
        }
        out.println("</div>");
        out.println("    <input type=\"file\" id=\"file\" name=\"file\" multiple><br>");
        out.println(
            "<div style='height:300px; border:0.5px solid;' class=\"body\"><textarea class=\"body\" style='border:none;' name=\"content\">"
                + req.getParameter("content") + "</textarea></div>");
        out.println("</form>");
        out.println("<div class=\"buttons\">");
        out.println("<a href=\"/board\">게시글 리스트로 돌아가기</a>");
        out.println(
            "<a style='float: right' href=\"#\" onclick=\"document.getElementById('viewForm').submit(); return false;\">확인</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");

        out.close();
    }

    private int updateBoard(Board board, HttpServletRequest req) throws ServletException, IOException, SQLException {
        var dataSource = (DataSource) getServletContext().getAttribute(DATASOURCE_CONTEXT_KEY);
        var fileService = new FileService();
        var parts = req.getParts();
        var writeYN = 0;
        var boardFileSet = fileService.upload(parts, writeYN);
        if (boardFileSet == null) {
            System.out.println("There are more than 3 attachments");
            return 0;
        }
        board.setBoardFileSet(boardFileSet);
        var optionalFilePathList = new BoardDao(dataSource).updateOne(board);
        if (optionalFilePathList.isPresent()) {
            var filePathList = optionalFilePathList.get();
            writeYN = 1;
            for (String filePath : filePathList) {
                fileService.delete(filePath);
            }
            fileService.upload(parts, writeYN);
        }
        return 1;
    }
}
