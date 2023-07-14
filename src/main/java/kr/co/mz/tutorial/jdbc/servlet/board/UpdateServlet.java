package kr.co.mz.tutorial.jdbc.servlet.board;

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
import kr.co.mz.tutorial.jdbc.exception.DatabaseAccessException;
import kr.co.mz.tutorial.jdbc.exception.InputValidationException;
import kr.co.mz.tutorial.jdbc.exception.WrongBoardInfoException;
import kr.co.mz.tutorial.jdbc.service.BoardService;

public class UpdateServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() {
        this.dataSource = (DataSource) getServletContext().getAttribute(Constants.DATASOURCE_CONTEXT_KEY);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Board board = validateParameter(req.getParameter("boardSeq"),
            req.getParameter("title"),
            req.getParameter("content")
            , req.getParameter("category"));
        var update = req.getParameter("update");
        int result;
        if (update != null && update.equals("1")) {
            try (var connection = dataSource.getConnection()) {
                result = new BoardService(connection).modify(board, req.getParts());
            } catch (NumberFormatException nfe) {
                throw new WrongBoardInfoException();
            } catch (SQLException sqle) {
                throw new DatabaseAccessException(sqle);
            }
            if (result == 1) {
                req.setAttribute("message", "게시글 수정에 성공하였습니다.");
                req.setAttribute("redirectUrl", "http://localhost:8080/board/view?boardSeq=" + board.getSeq());
            } else {
                req.setAttribute("message", "게시글 수정에 실패하였습니다.");
                req.setAttribute("redirectUrl", "http://localhost:8080/board/view?boardSeq=" + board.getSeq());
            }
            req.getRequestDispatcher("/WEB-INF/jsp/redirect.jsp").forward(req, resp);
            return;
        }

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println(ViewServlet.TOP_CONTENTS);
        out.println(
            "<form id=\"viewForm\" action=\"/board/update\" method=\"post\" accept-charset=\"UTF-8\" "
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

    private static Board validateParameter(String boardSeqStr, String title, String content,
        String category) {
        int boardSeq;
        try {
            boardSeq = Integer.parseInt(boardSeqStr);
        } catch (NumberFormatException nfe) {
            throw new WrongBoardInfoException();
        }
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
        return new Board(boardSeq, title, content, category);
    }


}
