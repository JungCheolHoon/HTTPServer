package kr.co.mz.tutorial.jdbc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import kr.co.mz.tutorial.jdbc.DatabaseAccessException;
import kr.co.mz.tutorial.jdbc.db.model.Customer;

public class LoginDao {

    private final Connection connection;

    public LoginDao(Connection connection) {
        this.connection = connection;
    }

    public int existCustomer(String username, String password) throws SQLException {
        var query = "select seq from customer where customer_id=? and password=?";
        System.out.println("QUERY:::" + query);
        try (
            PreparedStatement ps = connection.prepareStatement(query)
        ) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                System.out.println("Successful Find One Customer! PK : " + resultSet.getInt(1));
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    public int joinCustomer(Customer customer) throws SQLException {
        var query = "insert into Customer(customer_id,password,name,address) values(?,?,?,?)";
        try (
            PreparedStatement ps = connection.prepareStatement(query)
        ) {
            ps.setString(1, customer.getCustomerId());
            ps.setString(2, customer.getPassword());
            ps.setString(3, customer.getName());
            ps.setString(4, customer.getAddress());
            int result = ps.executeUpdate();
            if (result != 0) {
                System.out.println("Successful Insert One Customer! Rows : " + result);
            }
            return result;
        }
    }

    public Optional<Customer> findByUsername(String username) {
        final String query = "select * from customer where customer_id = ?";

        try (
            PreparedStatement ps = connection.prepareStatement(query)
        ) {
            ps.setString(1, username);
            var rs = ps.executeQuery();
            Customer customer = null;
            if (rs.next()) {
                customer = Customer.fromResultSet(rs);
            }
            return Optional.ofNullable(customer);
        } catch (SQLException sqle) {
            throw new DatabaseAccessException("데이터베이스 관련 처리에 오류가 발생하였습니다:" + sqle.getMessage(), sqle);
        }
    }
}
