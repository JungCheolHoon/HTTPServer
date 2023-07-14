package kr.co.mz.tutorial.jdbc.service;

import java.sql.Connection;
import java.util.Optional;
import kr.co.mz.tutorial.jdbc.db.dao.LoginDao;
import kr.co.mz.tutorial.jdbc.db.model.Customer;
import kr.co.mz.tutorial.jdbc.exception.NoSuchCustomerFoundException;

public class CustomerService {

    private final Connection connection;

    public CustomerService(Connection connection) {
        this.connection = connection;
    }

    public Optional<Customer> findCustomer(String username) {
        var loginDao = new LoginDao(connection);
        return loginDao.findByUsername(username);
    }

    public void joinCustomer(Customer customer) {
        new LoginDao(connection).joinCustomer(customer);
    }

    public Customer findCustomer(String username, String password) {
        var loginDao = new LoginDao(connection);
        Optional<Customer> optionalCustomer = loginDao.findByUsername(username);
        return optionalCustomer
            .filter(customer2 -> password.equals(customer2.getPassword()))
            .orElseThrow(() -> new NoSuchCustomerFoundException(username));
    }

}
