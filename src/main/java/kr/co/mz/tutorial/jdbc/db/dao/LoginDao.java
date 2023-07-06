package kr.co.mz.tutorial.jdbc.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.db.model.Customer;

public class LoginDao {

    DataSource dataSource;

    public LoginDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean existCustomer(String username, String password) throws SQLException {
        var query = "select exists (select 1 from customer where customer_id=? and password=?)";
        try (var connection = dataSource.getConnection()
            ; PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }
        return false;
    }

    public int joinCustomer(Customer customer) throws SQLException {
        var query = "insert into Customer(customer_id,password,name,address) values(?,?,?,?)";
        try (var connection = dataSource.getConnection()
            ; PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, customer.getCustomerId());
            ps.setString(2, customer.getPassword());
            ps.setString(3, customer.getName());
            ps.setString(4, customer.getAddress());
            int result = ps.executeUpdate();
            return result;
        }
    }

}
