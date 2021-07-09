package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.Customer;
import model.FirstLevelDivision;

import java.rmi.ConnectIOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * FirstLevelDivisions Table Data Access Object Class
 */
public class FirstLevelDivisionDao {
    /**
     * Query database for all first level division data
     * @param connection connection object
     * @return Observable list with first level division data records
     * @throws SQLException due to SQL query
     */
    public static ObservableList<FirstLevelDivision> getAllDivisions(Connection connection) throws SQLException {
        String query = "SELECT * FROM first_level_divisions";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.execute();
        ResultSet resultSet = ps.getResultSet();

        ObservableList<FirstLevelDivision> divisionList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            FirstLevelDivision division = extractFromResultSet(resultSet);
            divisionList.add(division);
        }
        ps.close();
        return divisionList;
    }
    /**
     * Query database for a single first level division record by the country ID
     * @param connection connection object
     * @return Observable list with first level division data records
     * @throws SQLException due to SQL query
     */
    public static ObservableList<FirstLevelDivision> getDivisionByCountryID(int ID, Connection connection) throws SQLException {
        String query = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID=?";

        PreparedStatement ps = connection.prepareStatement(query);

        ps.setInt(1, ID);

        ps.execute();
        ResultSet resultSet = ps.getResultSet();

        ObservableList<FirstLevelDivision> divisionList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            FirstLevelDivision division = extractFromResultSet(resultSet);
            divisionList.add(division);
        }

        ps.close();

        return divisionList;
    }

    /**
     * Extracts from the resultSet and places data into a single first level division object
     * @param resultSet resultset object
     * @return returns firstleveldivision object
     * @throws SQLException due to SQL query
     */
    private static FirstLevelDivision extractFromResultSet(ResultSet resultSet) throws SQLException{
        FirstLevelDivision division = new FirstLevelDivision();

        division.setDivisionID(resultSet.getInt("Division_ID"));
        division.setDivision(resultSet.getString("Division"));
        division.setCountryID(resultSet.getInt("COUNTRY_ID"));

        return division;
    }

}
