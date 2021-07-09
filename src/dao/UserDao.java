package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * User Table Data Access Object Class
 */
public class UserDao {
    public static User loggedUser;
    public static boolean logged = false;

    Connection connection = null;
    PreparedStatement ptmt = null;
    static ResultSet resultSet = null;

    public static ObservableList<User> getAllUsers(Connection connection) throws SQLException {
        String query = "SELECT * FROM users";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            resultSet = ps.getResultSet();

            ObservableList<User> userList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                User users = extractUserFromResultSet(resultSet);
                userList.add(users);
            }
            ps.close();
            return userList;
    }

    /**
     * Queries database for a single user.
     * @param userID integer used for userID.
     * @param connection connection object
     * @return returns user object
     */
    public static User getUser(Integer userID, Connection connection) throws SQLException {
        User user = new User();
        String query = "SELECT * FROM users WHERE User_ID=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, userID);
        ps.execute();
        resultSet = ps.getResultSet();

        if (resultSet.next()){
            user = extractUserFromResultSet(resultSet);
        }
        resultSet.close();

        return user;
    }

    /**
     * Extracts the information from a ResultSet object into a single User object.
     * @param resultSet ResultSet object
     * @return returns an appointment object
     * @throws SQLException due to SQL query
     */
    private static User extractUserFromResultSet(ResultSet resultSet) throws SQLException{
        User user = new User();

        user.setUserID(resultSet.getInt("User_ID"));
        user.setUserName(resultSet.getString("User_Name"));
        user.setPassword(resultSet.getString("Password"));

        return user;
    }
}
