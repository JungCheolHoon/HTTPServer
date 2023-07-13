package kr.co.mz.tutorial.jdbc.servlet.board;

import static kr.co.mz.tutorial.jdbc.Constants.CUSTOMER_IN_SESSION;
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
import kr.co.mz.tutorial.jdbc.SessionExpiredException;
import kr.co.mz.tutorial.jdbc.db.dao.BoardDao;
import kr.co.mz.tutorial.jdbc.db.model.Board;
import kr.co.mz.tutorial.jdbc.db.model.Customer;
import kr.co.mz.tutorial.jdbc.file.FileService;

public class WriteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>글쓰기</title>");
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

        out.println(".write-form {");
        out.println("  margin: 20px auto;");
        out.println("  width: 500px;");
        out.println("}");

        out.println(".write-form label {");
        out.println("  display: block;");
        out.println("  margin-bottom: 10px;");
        out.println("}");

        out.println(".write-form input, .write-form textarea, .write-form select {");
        out.println("  width: 100%;");
        out.println("  padding: 8px;");
        out.println("  margin-bottom: 10px;");
        out.println("}");

        out.println(".write-form .button-container {");
        out.println("  text-align: center;");
        out.println("  margin-top: 20px;");
        out.println("}");

        out.println(".write-form .button-container button {");
        out.println("  padding: 8px 16px;");
        out.println("  background-color: #f90;");
        out.println("  color: #fff;");
        out.println("  text-decoration: none;");
        out.println("  border-radius: 4px;");
        out.println("  border: none;");
        out.println("}");

        out.println("/* 추가적인 CSS 코드 작성 */");

        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>게 시 판</h1>");
        out.println("<div class=\"write-form\">");
        out.println(
            "  <form action=\"/board/write\" method=\"post\" enctype=\"multipart/form-data\" accept-charset=\"UTF-8\">");
        out.println("    <label for=\"title\">제목:</label>");
        out.println("    <input type=\"text\" id=\"title\" name=\"title\"><br>");
        out.println("    <label for=\"content\">내용:</label>");
        out.println("    <textarea id=\"content\" name=\"content\"></textarea><br>");
        out.println("    <label for=\"category\">카테고리:</label>");
        out.println("    <select id=\"category\" name=\"category\">");
        out.println("      <option value=\"여행 경험 공유\">여행 경험 공유</option>");
        out.println("      <option value=\"여행지 추천\">여행지 추천</option>");
        out.println("      <option value=\"여행 계획 토론\">여행 계획 토론</option>");
        out.println("    </select><br>");
        out.println("    <input type=\"file\" id=\"file\" name=\"file\" multiple><br>");
        out.println("    <div class=\"button-container\">");
        out.println("      <button type=\"submit\">글쓰기</button>");
        out.println("      <button type=\"button\" onclick=\"window.history.back()\">뒤로가기</button>");
        out.println("    </div>");
        out.println("  </form>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        var customer = (Customer) req.getSession().getAttribute(CUSTOMER_IN_SESSION);
        if (customer == null) {
            throw new SessionExpiredException();
        }
        if (Optional.ofNullable(getServletContext().getAttribute("customerSeq")).isEmpty()) {
            resp.sendRedirect("/board");
            return;
        }
        var title = req.getParameter("title");
        var content = req.getParameter("content");
        var category = req.getParameter("category");
        var boardFileSet = new FileService().upload(req.getParts(), 1);
        if (boardFileSet == null) {
            resp.sendRedirect("/board/write");
            return;
        }
        try {
            int result = insertBoard(new Board(title, content, category, boardFileSet), customer);
            if (result == 0) {
                System.out.println("There are more than 3 attachments");
                resp.sendRedirect("/board");
                return;
            }
            resp.sendRedirect("/board/" + result);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    private int insertBoard(Board board, Customer customer)
        throws SQLException {
        var dataSource = (DataSource) getServletContext().getAttribute(DATASOURCE_CONTEXT_KEY);
        board.setCustomerSeq(customer.getSeq());
        return new BoardDao(dataSource).insertOne(board);
    }
}
