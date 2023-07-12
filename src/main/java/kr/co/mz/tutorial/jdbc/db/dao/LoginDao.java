package kr.co.mz.tutorial.jdbc.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import kr.co.mz.tutorial.jdbc.db.model.Customer;

public class LoginDao {

    private final DataSource dataSource;

    public LoginDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int existCustomer(String username, String password) throws SQLException {
        var query = "select seq from customer where customer_id=? and password=?";
        try (
            var connection = dataSource.getConnection();
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
            var connection = dataSource.getConnection();
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

}
