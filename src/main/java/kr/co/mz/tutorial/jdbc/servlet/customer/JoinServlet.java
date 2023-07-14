package kr.co.mz.tutorial.jdbc.servlet.customer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.Constants;
import kr.co.mz.tutorial.jdbc.db.model.Customer;
import kr.co.mz.tutorial.jdbc.exception.CustomerExistsException;
import kr.co.mz.tutorial.jdbc.exception.DatabaseAccessException;
import kr.co.mz.tutorial.jdbc.exception.InputValidationException;
import kr.co.mz.tutorial.jdbc.service.CustomerService;

public class JoinServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() {
        this.dataSource = (DataSource) getServletContext().getAttribute(Constants.DATASOURCE_CONTEXT_KEY);
    }

    private static final String PAGE_CONTENTS = """
        <!DOCTYPE html>
        <html>
        <head>
        <title>회원가입</title>
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
        .center-text {
            text-align: center;
        }
        </style>
        </head>
        <body>
        <div class="container">
        <h2 class="center-text">Join</h2></br>
        <form action="/join" method="post" accept-charset="UTF-8">
        <div class="form-group">
        <label for="username">ID:</label>
        <input type="text" id="username" name="username" required>
        </div>
        <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>
        </div>
        <div class="form-group">
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" required>
        </div>
        <div class="form-group">
        <label for="address">Address:</label>
        <input type="text" id="address" name="address" required>
        </div>
        <div class="btn-container">
        <a href="/login" class="btn">Login</a>
        <input type="submit" value="Join" class="btn">
        </div>
        </form>
        </div>
        </body>
        </html>
        """;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println(PAGE_CONTENTS);
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var customer = validateInputParameter(req.getParameter("username"), req.getParameter("password")
            , req.getParameter("name"), req.getParameter("address"));
        try (var connection = dataSource.getConnection()) {
            var customerService = new CustomerService(connection);
            if (customerService.findCustomer(customer.getCustomerId()).isPresent()) {
                throw new CustomerExistsException();
            }
            customerService.joinCustomer(customer);
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
        req.setAttribute("message", "성공적으로 회원가입 되었습니다.");
        req.setAttribute("redirectUrl", "http://localhost:8080/login");
        req.getRequestDispatcher("/WEB-INF/jsp/redirect.jsp").forward(req, resp);
    }

    private static Customer validateInputParameter(String username, String password, String name, String address) {
        if (username == null || username.length() < 3) {
            throw new InputValidationException("아이디 세 글자 이상이어야 합니다.");
        }
        if (password == null || password.length() < 3) {
            throw new InputValidationException("비밀번호는 세 글자 이상이어야 합니다.");
        }
        if (name == null || name.length() < 2) {
            throw new InputValidationException("이름은 두 글자 이상이어야 합니다.");
        }
        if (address == null || address.length() < 5) {
            throw new InputValidationException("주소는 다섯 글자 이상이어야 합니다.");
        }
        return new Customer(username, password, name, address);
    }

}
