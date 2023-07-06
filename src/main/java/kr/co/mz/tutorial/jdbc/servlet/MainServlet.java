package kr.co.mz.tutorial.jdbc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"ko\">");
        out.println("<head>");
        out.println("<title>Simple Server Page</title>");
        out.println("<link rel=\"icon\" type=\"image/x-icon\" href=\"../static/favicon.ico\">");
        out.println("<style>");
        out.println("body {");
        out.println("  text-align: center;");
        out.println("  margin-top: 100px;");
        out.println("}");
        out.println("h1 {");
        out.println("  color: #333;");
        out.println("  font-family: 'Montserrat', sans-serif;");
        out.println("  font-size: 36px;");
        out.println("  font-weight: bold;");
        out.println("}");
        out.println(".hello {");
        out.println("  color: #FF6600;");
        out.println("  font-family: 'Pacifico', Montserrat;");
        out.println("  font-size: 36px;");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1><span class=\"hello\">Hello</span>, Server!</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}
