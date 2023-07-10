package kr.co.mz.tutorial.jdbc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.db.dao.BoardDao;
import kr.co.mz.tutorial.jdbc.db.model.Board;

public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Board> boardList;
        try {
            boardList = getBoardList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>게시판</title>");
        out.println("<style>");
        out.println("/* CSS 코드 */");
        out.println("h1 {");
        out.println("  color: white;");
        out.println("  font-family: 'Montserrat', sans-serif;");
        out.println("  background-color: #f90;");
        out.println("  text-align: center;");
        out.println("  font-size: 36px;");
        out.println("  font-weight: bold;");
        out.println("  height:50px;");
        out.println("}");

        out.println(".category {");
        out.println("  margin-bottom: 20px;");
        out.println("}");

        out.println("#post-table {");
        out.println("  width: 100%;");
        out.println("  border-collapse: collapse;");
        out.println("}");

        out.println("#post-table th,");
        out.println("#post-table td {");
        out.println("  padding: 8px;");
        out.println("  border: 1px solid #ccc;");
        out.println("}");

        out.println(".write-post {");
        out.println("  margin-top: 20px;");
        out.println("  text-align: right;");
        out.println("}");

        out.println(".write-post a {");
        out.println("  padding: 8px 16px;");
        out.println("  background-color: #f90;");
        out.println("  color: #fff;");
        out.println("  text-decoration: none;");
        out.println("  border-radius: 4px;");
        out.println("}");
        out.println("tr, td {");
        out.println("  text-align: center;");
        out.println("}");
        // 추가적인 CSS 코드 작성

        out.println("</style>");
        out.println("<script>");
        out.println("// JavaScript 코드");
        out.println("document.addEventListener('DOMContentLoaded', function() {");
        out.println("  // DOM이 로드된 후 실행될 JavaScript 코드");
        out.println("  // 카테고리 선택 드롭다운 메뉴의 이벤트 처리 로직");
        out.println("  var categoryDropdown = document.getElementById('category-dropdown');");
        out.println("  categoryDropdown.addEventListener('change', function() {");
        out.println("    var selectedCategory = categoryDropdown.value;");
        out.println("    console.log('선택한 카테고리:', selectedCategory);");
        out.println("    // 선택한 카테고리에 따라 동적으로 페이지를 로드하거나 필터링하는 로직을 추가하세요.");
        out.println("  });");
        out.println("});");
        out.println("</script>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>게 시 판</h1>");
        out.println("<div class=\"category\">");
        out.println("  <label for=\"category-dropdown\">카테고리:</label>");
        out.println("  <select id=\"category-dropdown\">");
        out.println("    <option value=\"all\">전체</option>");
        out.println("    <option value=\"experience\">여행 경험 공유</option>");
        out.println("    <option value=\"recommendation\">여행지 추천</option>");
        out.println("    <option value=\"discussion\">여행 계획 토론</option>");
        out.println("  </select>");
        out.println("</div>");
        out.println("<table id=\"post-table\">");
        out.println("  <thead>");
        out.println("  <tr>");
        out.println("    <th>글 번호</th>");
        out.println("    <th>제목</th>");
        out.println("    <th>작성자</th>");
        out.println("    <th>작성일</th>");
        out.println("  </tr>");
        out.println("  </thead>");
        out.println("  <tbody>");
        if (!boardList.isEmpty()) {
            var count = 1;
            for (Board board : boardList) {
                out.println("  <tr>");
                out.println("    <td>" + count + "</td>");
                out.println(
                    "    <td><a href=\"/viewBoard?boardSeq=" + board.getSeq() + "\">" + board.getTitle()
                        + "</a></td>");
                out.println("    <td>" + board.getCustomerName() + "</td>");
                out.println("    <td>" + board.getModifiedTime() + "</td>");
                out.println("  </tr>");
            }
        }
        out.println("  </tbody>");
        out.println("</table>");
        out.println("<div class=\"write-post\">");
        out.println("  <a href=\"/writeBoard\">글쓰기</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");

        out.close();
    }

    private List<Board> getBoardList() throws SQLException {
        var dataSource = (DataSource) getServletContext().getAttribute("dataSource");
        return new BoardDao(dataSource).getBoardList();
    }
}