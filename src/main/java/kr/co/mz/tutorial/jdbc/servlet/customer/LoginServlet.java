package kr.co.mz.tutorial.jdbc.servlet.customer;

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
import kr.co.mz.tutorial.jdbc.exception.DatabaseAccessException;
import kr.co.mz.tutorial.jdbc.exception.InputValidationException;
import kr.co.mz.tutorial.jdbc.service.CustomerService;

public class LoginServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() {
        this.dataSource = (DataSource) getServletContext().getAttribute(Constants.DATASOURCE_CONTEXT_KEY);
    }

    private static final String PAGE_CONTENTS = """
                <!DOCTYPE html>
                <html>
                <head>
                <title>Login</title>
                <style>
                .container {
                  max-width: 400px;
                  margin: 0 auto;
                  padding: 40px;
                  background-color: #fff;
                  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                  border-radius: 4px;
                }
                .form-group {
                  margin-bottom: 20px;
                }
                .form-group label {
                  display: block;
                  font-weight: bold;
                }
                .form-group input[type="text"],
                .form-group input[type="password"] {
                  width: 100%;
                  padding: 8px;
                  border: 1px solid #ccc;
                  border-radius: 4px;
                }
                .btn-container {
                  display: flex;
                  justify-content: space-between;
                  align-items: center;
                }
                .btn {
                  padding: 8px 16px;
                  background-color: #f90;
                  color: #fff;
                  text-decoration: none;
                  border-radius: 4px;
                  border: none;
                }
                .btn:hover {
                  background-color: #f60;
                }
                </style>
                </head>
                <body>
                <div class="container">
                <h2>Login</h2>
                <form action="/login" method="post" accept-charset="UTF-8">
                <div class="form-group">
                <label for="username">UserId:</label>
                <input type="text" id="username" name="username" required>
                </div>
                <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
                </div>
                <div class="btn-container">
                <input type="submit" value="Login" class="btn">
                <a href="/join" class="btn">Join</a>
                </div>
                </form>
                </div>
                </body>
                </html>
        """.stripIndent();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println(PAGE_CONTENTS);
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        validateInputParameter(username, password);
        try (var connection = dataSource.getConnection()) {
            var customer = new CustomerService(connection).findCustomer(username, password);
            req.getSession().setMaxInactiveInterval(1800);
            req.getSession().setAttribute(CUSTOMER_IN_SESSION, customer);
            req.setAttribute("message", "성공적으로 로그인 되었습니다.");
            req.setAttribute("redirectUrl", "http://localhost:8080/board");
            req.getRequestDispatcher("/WEB-INF/jsp/redirect.jsp").forward(req, resp);
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }

    private static void validateInputParameter(String username, String password) {
        if (username == null || username.length() < 3) {
            throw new InputValidationException("아이디는 세 글자 이상이어야 합니다.");
        }
        if (password == null || password.length() < 3) {
            throw new InputValidationException("비밀번호은 세 글자 이상이어야 합니다.");
        }
    }

}
