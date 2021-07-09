package dao;

import com.mysql.cj.protocol.Resultset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Contact;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Contacts Table Data Access Object Class
 */
public class ContactDao {
    /**
     * Queries the database and returns all the contacts in the contact table
     * @param connection Connection object to database
     * @return returns an observable list
     * @throws SQLException due to SQL query
     */
    public static ObservableList<Contact> getAllContacts(Connection connection) throws SQLException {
        String query = "SELECT * FROM contacts";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            ResultSet resultSet = ps.getResultSet();

            ObservableList<Contact> contactList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                Contact contacts = extractContactsFromResultSet(resultSet);
                contactList.add(contacts);
            }
            ps.close();

            return contactList;

    }
    /**
     * Queries the database and returns a single contact in the contact table by ContactID
     * @param ID Integer used to filter query by ContactID.
     * @param connection Connection object to database
     * @return returns a single contact object
     * @throws SQLException due to SQL query
     */
    public static Contact getContactFromID(int ID, Connection connection) throws SQLException {
        Contact returnContact = new Contact();

        String query = "SELECT * FROM contacts WHERE Contact_ID = ?";

        PreparedStatement ps = connection.prepareStatement(query);

        ps.setInt(1, ID);

        ps.execute();
        ResultSet resultSet = ps.getResultSet();

        if (resultSet.next()) {
            returnContact = extractContactsFromResultSet(resultSet);
        }

        ps.close();

        return returnContact;
    }

    /**
     * Inserts a contact record into the contacts table
     * @param contact contact object
     * @param connection connection object
     * @return returns boolean result based on insertion success
     * @throws SQLException due to SQL query
     */
    public static boolean insertContact(Contact contact, Connection connection) throws SQLException {
        String query = "INSERT INTO contacts (Contact_Name, Email) VALUES (?,?)";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, contact.getContactName());
        ps.setString(2, contact.getContactEmail());

        boolean result = ps.execute();

        ps.close();

        return result;
    }
    /**
     * Mdofies a contact record in the contacts table
     * @param contact contact object
     * @param connection connection object
     * @return returns boolean result based on modification success
     * @throws SQLException due to SQL query
     */
    public int modifyContact(Contact contact, Connection connection) throws SQLException {
        String query = "UPDATE contacts set Contact_Name = ?, Email = ? WHERE Contact_ID = ?";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, contact.getContactName());
        ps.setString(2, contact.getContactEmail());
        ps.setInt(3, contact.getContactID());

        int result = ps.executeUpdate();

        ps.close();

        return result;
    }
    /**
     * Deletes a contact record into the contacts table
     * @param contact contact object
     * @param connection connection object
     * @return returns boolean result based on deletion success
     * @throws SQLException due to SQL query
     */
    public static int deleteContact(Contact contact, Connection connection) throws SQLException {
        String query = "DELETE FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setInt(1, contact.getContactID());

        int result = ps.executeUpdate();

        ps.close();

        return result;
    }
    /**
     * Extracts the information from a ResultSet object into a single contact object.
     * @param resultSet ResultSet object
     * @return returns an appointment object
     * @throws SQLException due to SQL query
     */
    private static Contact extractContactsFromResultSet(ResultSet resultSet) throws SQLException{
        Contact contact = new Contact();

        contact.setContactID(resultSet.getInt("Contact_ID"));
        contact.setContactName(resultSet.getString("Contact_Name"));
        contact.setContactEmail(resultSet.getString("Email"));

        return contact;
    }
}
