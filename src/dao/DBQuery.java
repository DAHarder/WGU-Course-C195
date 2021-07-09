package dao;

import java.sql.*;

/**
 * Used with login - returns database query resultSet.
 */
public class DBQuery {
    private static PreparedStatement statement; // Statement variable
    private static ResultSet resultSet;

    public static ResultSet query(PreparedStatement statement) {
        try {
            statement.execute();
            resultSet = statement.getResultSet();
            return resultSet;
        } catch (SQLException E) {
            System.out.println("ERROR: " + E.getMessage());
        }
        return null;
    }

}
