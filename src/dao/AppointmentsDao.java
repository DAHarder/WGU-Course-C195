package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;
import model.User;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Appointments Table Data Access Object Class
 */
public class AppointmentsDao {

    Connection connection = null;
    PreparedStatement ptmt = null;
    static ResultSet resultSet = null;

    /**
     * Queries the database and returns all the appointments in the Appointment table
     * @param user User object
     * @param connection Connection object to database
     * @return returns an observable list
     * @throws SQLException due to SQL query
     */
    public static ObservableList<Appointment> getAllAppointments(User user, Connection connection) throws SQLException {
        String query = "SELECT * FROM appointments WHERE User_ID=?";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, user.getUserID());
            ps.execute();
            resultSet = ps.getResultSet();

            ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                Appointment appointments = extractAppointmentsFromResultSet(resultSet);
                appointmentList.add(appointments);
            }
            ps.close();
            return appointmentList;
    }

    /**
     * Queries the database and returns an observable list - it is used in Adding appointments as Validation to prevent appointments from having overlapping times.
     * @param user User object
     * @param customer Customer object
     * @param start time object for the start
     * @param end time object for the end
     * @param connection connection object
     * @return returns an observable list from the below query
     * @throws SQLException due to SQL query to database
     */
    public static ObservableList<Appointment> getAllAppointmentsByCustomerAndTimes(User user, Customer customer, LocalDateTime start, LocalDateTime end, Connection connection) throws SQLException {
        String query = "SELECT * FROM appointments WHERE User_ID=? AND Customer_ID=? AND ((Start between ? and ? OR End between ? and ?) OR (? between Start AND End OR ? between Start AND End)) ";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, user.getUserID());
        ps.setInt(2, customer.getCustomerID());
        ps.setTimestamp(3, Timestamp.valueOf(start));
        ps.setTimestamp(4, Timestamp.valueOf(end));
        ps.setTimestamp(5, Timestamp.valueOf(start));
        ps.setTimestamp(6, Timestamp.valueOf(end));
        ps.setTimestamp(7, Timestamp.valueOf(start));
        ps.setTimestamp(8, Timestamp.valueOf(end));

        ps.execute();
        resultSet = ps.getResultSet();

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Appointment appointments = extractAppointmentsFromResultSet(resultSet);
            appointmentList.add(appointments);
        }
        ps.close();
        return appointmentList;
    }
    /**
     * Queries the database and returns an observable list - it is used in the Modifying appointments as Validation to prevent appointments from having overlapping times.
     * The query also removes the current appointment to allow for modification.
     * @param user User object
     * @param customer Customer object
     * @param start time object for the start
     * @param end time object for the end
     * @param connection connection object
     * @return returns an observable list from the below query
     * @throws SQLException due to SQL query to database
     */
    public static ObservableList<Appointment> getAllAppointmentsByCustomerAndTimesModify(User user, Customer customer, int appointmentID, LocalDateTime start, LocalDateTime end, Connection connection) throws SQLException {
        String query = "SELECT * FROM appointments WHERE User_ID=? AND Customer_ID=? AND ((Start between ? and ? OR End between ? and ?) OR (? between Start AND End OR ? between Start AND End)) AND NOT Appointment_ID=?";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, user.getUserID());
        ps.setInt(2, customer.getCustomerID());
        ps.setTimestamp(3, Timestamp.valueOf(start));
        ps.setTimestamp(4, Timestamp.valueOf(end));
        ps.setTimestamp(5, Timestamp.valueOf(start));
        ps.setTimestamp(6, Timestamp.valueOf(end));
        ps.setTimestamp(7, Timestamp.valueOf(start));
        ps.setTimestamp(8, Timestamp.valueOf(end));
        ps.setInt(9, appointmentID);

        ps.execute();
        resultSet = ps.getResultSet();

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Appointment appointments = extractAppointmentsFromResultSet(resultSet);
            appointmentList.add(appointments);
        }
        ps.close();
        return appointmentList;
    }

    /**
     * Inserts an appointment record into the appointment table on database
     * @param appointment appointment object
     * @param connection connection object
     * @return returns an integer result to verify the insert query success
     * @throws SQLException due to SQL query
     */
    public static int insertAppointment(Appointment appointment, Connection connection) throws SQLException {
        String query = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Created_By, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
        ps.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
        ps.setString(7, UserDao.loggedUser.getUserName());
        ps.setString(8, UserDao.loggedUser.getUserName());
        ps.setInt(9, appointment.getCustomerID());
        ps.setInt(10, appointment.getUserID());
        ps.setInt(11, appointment.getContactID());

        int result = ps.executeUpdate();

        ps.close();
        DBConnection.closeConnection();

        return result;
    }

    /**
     * Modify an appointment record in the appointment table on database
     * @param appointment appointment object
     * @param connection connection object
     * @return returns an integer result to verify the insert query success
     * @throws SQLException due to SQL query
     */
    public static int modifyAppointment(Appointment appointment, Connection connection) throws SQLException {

        String query = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Created_By=?, Last_Updated_By=?, Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID=?";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
        ps.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
        ps.setString(7, UserDao.loggedUser.getUserName());
        ps.setString(8, UserDao.loggedUser.getUserName());
        ps.setInt(9, appointment.getCustomerID());
        ps.setInt(10, appointment.getUserID());
        ps.setInt(11, appointment.getContactID());
        ps.setInt(12, appointment.getAppointmentID());

        int result = ps.executeUpdate();

        ps.close();

        return result;
    }

    /**
     * Delete an appointment record in the appointment table on database
     * @param appointment appointment object
     * @param connection connection object
     * @return returns an integer result to verify the insert query success
     * @throws SQLException due to SQL query
     */
    public static int deleteAppointment(Appointment appointment, Connection connection) throws SQLException {

        String query = "DELETE FROM appointments WHERE Appointment_ID=?";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setInt(1, appointment.getAppointmentID());

        int result = ps.executeUpdate();

        ps.close();

        return result;
    }

    /**
     * Extracts the information from a ResultSet object into a single appointment object.
     * @param resultSet ResultSet object
     * @return returns an appointment object
     * @throws SQLException due to SQL query
     */
    private static Appointment extractAppointmentsFromResultSet(ResultSet resultSet) throws SQLException{
        Appointment appointment = new Appointment();

        appointment.setAppointmentID(resultSet.getInt("Appointment_ID"));
        appointment.setTitle(resultSet.getString("Title"));
        appointment.setDescription(resultSet.getString("Description"));
        appointment.setLocation(resultSet.getString("Location"));
        appointment.setType(resultSet.getString("Type"));
        appointment.setStart(resultSet.getTimestamp("Start").toLocalDateTime());
        appointment.setEnd(resultSet.getTimestamp("End").toLocalDateTime());
        appointment.setCustomerID(resultSet.getInt("Customer_ID"));
        appointment.setUserID(resultSet.getInt("User_ID"));
        appointment.setContactID(resultSet.getInt("Contact_ID"));

        return appointment;
    }

}
