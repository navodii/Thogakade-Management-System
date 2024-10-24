package controller.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerController implements CustomerService {
    @Override
    public boolean addCustomer(Customer customer) {
        String SQL = "INSERT INTO customer VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            return CrudUtil.execute(
                    SQL,
                    customer.getId(),
                    customer.getTitle(),
                    customer.getName(),
                    customer.getDob(),
                    customer.getSalary(),
                    customer.getAddress(),
                    customer.getCity(),
                    customer.getProvince(),
                    customer.getPostalCode()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        return false;
    }

    @Override
    public Customer searchCustomer(String id) {
        return null;
    }

    @Override
    public boolean deleteCustomer(String id) {
        return false;
    }

    @Override
    public ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> observableList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet =CrudUtil.execute("SELECT * FROM customer");
            while (resultSet.next()){
                observableList.add(new Customer(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDate(4).toLocalDate(),
                        resultSet.getDouble(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9)
                ));
            }
            return observableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<String> getCustomerIdes(){
        ObservableList<Customer> allCustomers = getAllCustomers();
        ObservableList<String> idList = FXCollections.observableArrayList();

        allCustomers.forEach(customer -> {
            idList.add(customer.getId());
        });
        return idList;
    }
}
