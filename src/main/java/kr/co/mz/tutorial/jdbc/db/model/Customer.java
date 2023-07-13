package kr.co.mz.tutorial.jdbc.db.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {

    private int seq;
    private String customerId;
    private String password;
    private String name;
    private String address;

    public Customer(String customerId, String password, String name, String address) {
        this.customerId = customerId;
        this.password = password;
        this.name = name;
        this.address = address;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Customer fromResultSet(ResultSet rs) throws SQLException {
        var customer = new Customer(
            rs.getString("customer_id"),
            rs.getString("name"),
            rs.getString("password"),
            rs.getString("address")
        );
        customer.setSeq(rs.getInt("seq"));

        return customer;
    }

}
