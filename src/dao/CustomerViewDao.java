package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.CustomerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * CustomerView TableView Data Access Object Class.
 * For use with the applications table.
 */
public class CustomerViewDao {
    /**
     * Queries the database and returns all the Customers in the customerview tableview
     * @param connection Connection object to database
     * @return returns an observable list
     * @throws SQLException due to SQL query
     */
    public static ObservableList<CustomerView> getAllCustomers(Connection connection) throws SQLException {
        String query = "SELECT * FROM customersview";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.execute();
        ResultSet resultSet = ps.getResultSet();

        ObservableList<CustomerView> customerList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            CustomerView customers = extractCustomersFromResultSet(resultSet);
            customerList.add(customers);
        }
        ps.close();
        return customerList;
    }
    /**
     * Queries the database and returns a single customer in the customer table by customerID
     * @param ID Integer used to filter query by customerID.
     * @param connection Connection object to database
     * @return returns a single contact object
     * @throws SQLException due to SQL query
     */
    public static ObservableList<CustomerView> getCustomersFromID(int ID, Connection connection) throws SQLException {
        String query = "SELECT * FROM customersview WHERE Customer_ID=?";

        PreparedStatement ps = connection.prepareStatement(query);

        ps.setInt(1, ID);

        ps.execute();
        ResultSet resultSet = ps.getResultSet();

        ObservableList<CustomerView> customerList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            CustomerView customers = extractCustomersFromResultSet(resultSet);
            customerList.add(customers);
        }
        ps.close();
        return customerList;
    }
    /**
     * Queries the database and returns a single customer in the customer table by customerID
     * @param name String used to filter query by CustomerName.
     * @param connection Connection object to database
     * @return returns a single contact object
     * @throws SQLException due to SQL query
     */
    public static ObservableList<CustomerView> getCustomersFromName(String name, Connection connection) throws SQLException {
        String query = "SELECT * FROM customersview WHERE Customer_Name LIKE ?";

        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, "%"+name+"%");

        ps.execute();
        ResultSet resultSet = ps.getResultSet();

        ObservableList<CustomerView> customerList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            CustomerView customers = extractCustomersFromResultSet(resultSet);
            customerList.add(customers);
        }
        ps.close();
        return customerList;
    }
    /**
     * Extracts the information from a ResultSet object into a single Customer object.
     * @param resultSet ResultSet object
     * @return returns an appointment object
     * @throws SQLException due to SQL query
     */
    private static CustomerView extractCustomersFromResultSet(ResultSet resultSet) throws SQLException{
        CustomerView customer = new CustomerView();

        customer.setCustomerID(resultSet.getInt("Customer_ID"));
        customer.setCustomerName(resultSet.getString("Customer_Name"));
        customer.setAddress(resultSet.getString("Address"));
        customer.setPostalCode(resultSet.getString("Postal_Code"));
        customer.setPhone(resultSet.getString("Phone"));
        customer.setCountry(resultSet.getString("Country"));
        customer.setDivision(resultSet.getString("Division"));

        return customer;
    }
}
