package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.AppointmentView;
import model.Contact;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class AppointmentViewDao {
    Connection connection = null;
    PreparedStatement ptmt = null;
    static ResultSet resultSet = null;

    /**
     * Queries the database and returns all the appointments in the Appointmentview tableview
     * @param user User object
     * @param connection Connection object to database
     * @return returns an observable list for use to populate the appointments Table on the mainscreen
     * @throws SQLException due to SQL query
     */
    public static ObservableList<AppointmentView> getAllAppointments(User user, Connection connection) throws SQLException {

        String query = "SELECT * FROM AppointmentsView WHERE User_ID=?";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, user.getUserID());
            ps.execute();
            resultSet = ps.getResultSet();

            ObservableList<AppointmentView> appointmentList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                AppointmentView appointments = extractAppointmentsFromResultSet(resultSet);
                appointmentList.add(appointments);
            }
            ps.close();

            return appointmentList;
    }
    /**
     * Queries the database and returns all the appointments in the Appointmentview tableview by the current Month
     * @param user User object
     * @param connection Connection object to database
     * @return returns an observable list for use to populate the appointments Table on the mainscreen
     * @throws SQLException due to SQL query
     */
    public static ObservableList<AppointmentView> getAllAppointmentsByMonth(User user, Connection connection, int month) throws SQLException {

        String query = "SELECT * FROM AppointmentsView WHERE User_ID=? AND MONTH(Start) = MONTH(now()) + ?";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, user.getUserID());
        ps.setInt(2, month);
        ps.execute();
        resultSet = ps.getResultSet();

        ObservableList<AppointmentView> appointmentList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            AppointmentView appointments = extractAppointmentsFromResultSet(resultSet);
            appointmentList.add(appointments);
        }
        ps.close();

        return appointmentList;
    }
    /**
     * Queries the database and returns all the appointments in the Appointmentview tableview by the current week
     * @param user User object
     * @param connection Connection object to database
     * @param week integer used to go forward or backward a week.  Used with the btnNext and btnPrevious on mainscreen
     * @return returns an observable list for use to populate the appointments Table on the mainscreen
     * @throws SQLException due to SQL query
     */
    public static ObservableList<AppointmentView> getAllAppointmentsByWeek(User user, Connection connection, int week) throws SQLException {

        String query = "SELECT * FROM AppointmentsView WHERE User_ID=? AND WEEK(Start) = WEEK(now(), 0) + ?";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, user.getUserID());
        ps.setInt(2, week);
        ps.execute();
        resultSet = ps.getResultSet();

        ObservableList<AppointmentView> appointmentList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            AppointmentView appointments = extractAppointmentsFromResultSet(resultSet);
            appointmentList.add(appointments);
        }
        ps.close();

        return appointmentList;
    }
    /**
     * Queries the database and returns all the appointments in the Appointmentview tableview by a Contacts.
     * Used with the contacts combo box on the mainscreen
     * @param user User object
     * @param connection Connection object to database
     * @param contact contact object
     * @return returns an observable list for use to populate the appointments Table on the mainscreen
     * @throws SQLException due to SQL query
     */
    public static ObservableList<AppointmentView> getAllAppointmentsByContact(User user, Connection connection, Contact contact) throws SQLException {

        String query = "SELECT * FROM AppointmentsView WHERE User_ID=? AND Contact_ID = ?";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, user.getUserID());
        ps.setInt(2, contact.getContactID());
        ps.execute();
        resultSet = ps.getResultSet();

        ObservableList<AppointmentView> appointmentList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            AppointmentView appointments = extractAppointmentsFromResultSet(resultSet);
            appointmentList.add(appointments);
        }
        ps.close();

        return appointmentList;
    }
    /**
     * Extracts the information from a ResultSet object into a single appointment object.
     * @param resultSet ResultSet object
     * @return returns an appointment object
     * @throws SQLException due to SQL query
     */
    private static AppointmentView extractAppointmentsFromResultSet(ResultSet resultSet) throws SQLException{
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyy HH:mm");

        AppointmentView appointment = new AppointmentView();

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
        appointment.setCustomerName(resultSet.getString("Customer_Name"));
        appointment.setContactName(resultSet.getString("Contact_Name"));

        return appointment;
    }

}

