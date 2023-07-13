package kr.co.mz.tutorial.jdbc.servlet.customer;

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
import kr.co.mz.tutorial.jdbc.DatabaseAccessException;
import kr.co.mz.tutorial.jdbc.InputValidationException;
import kr.co.mz.tutorial.jdbc.NoSuchCustomerFoundException;
import kr.co.mz.tutorial.jdbc.db.dao.LoginDao;
import kr.co.mz.tutorial.jdbc.db.model.Customer;

public class LoginServlet extends HttpServlet {

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

        // input validation
        validateInputParameter(username, password);
//        if (!inputValidationResult) {
//            req.setAttribute("message", "입력 정보가 올바르지 않습니다.");
//            req.setAttribute("redirectUrl", "http://localhost:8080/login");
//            req.getRequestDispatcher("/WEB-INF/jsp/redirect.jsp").forward(req, resp);
//        }

        // 로그인 처리 로직
        var customer = findCustomer(username, password);
        req.getSession().setAttribute(CUSTOMER_IN_SESSION, customer);
        req.setAttribute("message", "성공적으로 로그인 되었습니다.");
        req.setAttribute("redirectUrl", "http://localhost:8080/board");
        req.getRequestDispatcher("/WEB-INF/jsp/redirect.jsp").forward(req, resp);
    }

    private static void validateInputParameter(String username, String password) {
        if (username == null || username.length() < 3) {
            throw new InputValidationException("사용자명은 세 글자 이상이어야 합니다.");
        }
        if (password == null || password.length() < 3) {
            throw new InputValidationException("비밀번호은 세 글자 이상이어야 합니다.");
        }
    }

    private Customer findCustomer(String username, String password) {
        var dataSource = (DataSource) getServletContext().getAttribute(DATASOURCE_CONTEXT_KEY);
        try (
            var connection = dataSource.getConnection();
        ) {
            var loginDao = new LoginDao(connection);
            Optional<Customer> optionalCustomer = loginDao.findByUsername(username);
            return optionalCustomer
                .filter(customer2 -> password.equals(customer2.getPassword()))
                .orElseThrow(() -> new NoSuchCustomerFoundException(username));
        } catch (SQLException sqle) {
            throw new DatabaseAccessException("데이터베이스 관련 처리에 오류가 발생하였습니다:" + sqle.getMessage(), sqle);
        }
    }
}
