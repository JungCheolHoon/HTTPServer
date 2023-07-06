package kr.co.mz.tutorial.jdbc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.db.dao.LoginDao;
import kr.co.mz.tutorial.jdbc.db.model.Customer;

public class JoinServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>회원가입</title>");
        out.println("<style>");
        out.println(".container {");
        out.println("    max-width: 400px;");
        out.println("    margin: 0 auto;");
        out.println("    padding: 40px;");
        out.println("    background-color: #fff;");
        out.println("    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);");
        out.println("    border-radius: 4px;");
        out.println("}");
        out.println(".form-group {");
        out.println("    margin-bottom: 20px;");
        out.println("}");
        out.println(".form-group label {");
        out.println("    display: block;");
        out.println("    font-weight: bold;");
        out.println("}");
        out.println(".form-group input[type=\"text\"],");
        out.println(".form-group input[type=\"password\"] {");
        out.println("    width: 100%;");
        out.println("    padding: 8px;");
        out.println("    border: 1px solid #ccc;");
        out.println("    border-radius: 4px;");
        out.println("}");
        out.println(".btn-container {");
        out.println("    display: flex;");
        out.println("    justify-content: space-between;");
        out.println("    align-items: center;");
        out.println("}");
        out.println(".btn {");
        out.println("    padding: 8px 16px;");
        out.println("    background-color: #f90;");
        out.println("    color: #fff;");
        out.println("    text-decoration: none;");
        out.println("    border-radius: 4px;");
        out.println("    border: none;");
        out.println("}");
        out.println(".btn:hover {");
        out.println("    background-color: #f60;");
        out.println("}");
        out.println(".center-text {");
        out.println("    text-align: center;");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class=\"container\">");
        out.println("<h2 class=\"center-text\">Join</h2></br>");
        out.println("<form action=\"/join\" method=\"post\" accept-charset=\"UTF-8\">");
        out.println("<div class=\"form-group\">");
        out.println("<label for=\"username\">ID:</label>");
        out.println("<input type=\"text\" id=\"username\" name=\"username\" required>");
        out.println("</div>");
        out.println("<div class=\"form-group\">");
        out.println("<label for=\"password\">Password:</label>");
        out.println("<input type=\"password\" id=\"password\" name=\"password\" required>");
        out.println("</div>");
        out.println("<div class=\"form-group\">");
        out.println("<label for=\"name\">Name:</label>");
        out.println("<input type=\"text\" id=\"name\" name=\"name\" required>");
        out.println("</div>");
        out.println("<div class=\"form-group\">");
        out.println("<label for=\"address\">Address:</label>");
        out.println("<input type=\"text\" id=\"address\" name=\"address\" required>");
        out.println("</div>");
        out.println("<div class=\"btn-container\">");
        out.println("<a href=\"/login\" class=\"btn\">Login</a>");
        out.println("<input type=\"submit\" value=\"Join\" class=\"btn\">");
        out.println("</div>");
        out.println("</form>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var customer = new Customer(req.getParameter("username"), req.getParameter("password")
            , req.getParameter("name"), req.getParameter("address"));
        try {
            if (joinCustomer(customer) == 1) {
                resp.sendRedirect("/login");
            } else {
                resp.sendRedirect("/join");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int joinCustomer(Customer customer) throws SQLException {
        var dataSource = (DataSource) getServletContext().getAttribute("dataSource");
        return new LoginDao(dataSource).joinCustomer(customer);
    }
}
