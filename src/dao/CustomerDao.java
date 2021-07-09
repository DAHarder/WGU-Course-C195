package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Customers Table Data Access Object Class
 */
public class CustomerDao {
    /**
     * Queries the database and returns all the Customers in the customer table
     * @param connection Connection object to database
     * @return returns an observable list
     * @throws SQLException due to SQL query
     */
    public static ObservableList<Customer> getAllCustomers(Connection connection) throws SQLException {

        String query = "SELECT * FROM customers";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.execute();
       ResultSet resultSet = ps.getResultSet();

        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Customer customers = extractCustomersFromResultSet(resultSet);
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
    public static Customer getCustomerFromID(int ID, Connection connection) throws SQLException {
        Customer returnCustomer = new Customer();

        String query = "SELECT * FROM customers WHERE Customer_ID = ?";

        PreparedStatement ps = connection.prepareStatement(query);

        ps.setInt(1, ID);

        ps.execute();
        ResultSet resultSet = ps.getResultSet();

        if (resultSet.next()) {
            returnCustomer = extractCustomersFromResultSet(resultSet);
        }

        ps.close();

        return returnCustomer;
    }
    /**
     * Inserts a customer record into the customers table
     * @param customer contact object
     * @param connection connection object
     * @return returns boolean result based on insertion success
     * @throws SQLException due to SQL query
     */
    public static int insertCustomer(Customer customer, Connection connection) throws SQLException {
        String query = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, customer.getCustomerName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostalCode());
        ps.setString(4, customer.getPhone());
        ps.setString(5, customer.getCreatedBy());
        ps.setString(6, customer.getUpdatedBy());
        ps.setInt(7, customer.getDivisionID());

        int result = ps.executeUpdate();

        ps.close();

        return result;
    }
    /**
     * modifies a customer record into the customers table
     * @param customer contact object
     * @param connection connection object
     * @return returns boolean result based on modify success
     * @throws SQLException due to SQL query
     */
    public static int modifyCustomer(Customer customer, Connection connection) throws SQLException {
        String query = "UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, Last_Updated_By=?, Division_ID=? WHERE Customer_ID=?";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setInt(7,customer.getCustomerID());
        ps.setString(1, customer.getCustomerName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostalCode());
        ps.setString(4, customer.getPhone());
        ps.setString(5, customer.getUpdatedBy());
        ps.setInt(6, customer.getDivisionID());

        int result = ps.executeUpdate();

        ps.close();

        return result;
    }
    /**
     * deletes a customer record into the customers table
     * @param customer contact object
     * @param connection connection object
     * @return returns boolean result based on deletion success
     * @throws SQLException due to SQL query
     */
    public static int deleteCustomer(Customer customer, Connection connection) throws SQLException {
        String query = "DELETE FROM appointments WHERE Customer_ID=?";
        String query2 = "DELETE FROM customers WHERE Customer_ID=?";

        PreparedStatement ps = connection.prepareStatement(query);
        PreparedStatement ps2 = connection.prepareStatement(query2);

        ps.setInt(1, customer.getCustomerID());
        ps2.setInt(1, customer.getCustomerID());

        int result = ps.executeUpdate();

        int result2 = ps2.executeUpdate();

        ps.close();

        return result2;
    }
    /**
     * Extracts the information from a ResultSet object into a single Customer object.
     * @param resultSet ResultSet object
     * @return returns an appointment object
     * @throws SQLException due to SQL query
     */
    private static Customer extractCustomersFromResultSet(ResultSet resultSet) throws SQLException{
        Customer customer = new Customer();

        customer.setCustomerID(resultSet.getInt("Customer_ID"));
        customer.setCustomerName(resultSet.getString("Customer_Name"));
        customer.setAddress(resultSet.getString("Address"));
        customer.setPostalCode(resultSet.getString("Postal_Code"));
        customer.setPhone(resultSet.getString("Phone"));
        customer.setCreatedBy(resultSet.getString("Created_By"));
        customer.setUpdatedBy(resultSet.getString("Last_Updated_By"));
        customer.setDivisionID(resultSet.getInt("Division_ID"));

        return customer;
    }


}
