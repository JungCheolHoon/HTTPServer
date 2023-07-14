package kr.co.mz.tutorial.jdbc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import kr.co.mz.tutorial.jdbc.db.model.Customer;
import kr.co.mz.tutorial.jdbc.exception.DatabaseAccessException;

public class LoginDao {

    private final Connection connection;

    public LoginDao(Connection connection) {
        this.connection = connection;
    }

    public void joinCustomer(Customer customer) {
        var query = "insert into Customer(customer_id,password,name,address) values(?,?,?,?)";
        System.out.println("Query : " + query);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, customer.getCustomerId());
            ps.setString(2, customer.getPassword());
            ps.setString(3, customer.getName());
            ps.setString(4, customer.getAddress());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }

    public Optional<Customer> findByUsername(String username) {
        final String query = "select * from customer where customer_id = ?";
        System.out.println("Query : " + query);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            var rs = ps.executeQuery();
            Customer customer = null;
            if (rs.next()) {
                customer = Customer.fromResultSet(rs);
            }
            return Optional.ofNullable(customer);
        } catch (SQLException sqle) {
            throw new DatabaseAccessException(sqle);
        }
    }

}
