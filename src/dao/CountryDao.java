package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Country Table Data Access Object Class
 */
public class CountryDao {
    /**
     * Queries the database and returns all the Countries in the Country table
     * @param connection Connection object to database
     * @return returns an observable list
     * @throws SQLException due to SQL query
     */
    public static ObservableList<Country> getAllCountries(Connection connection) throws SQLException {

        String query = "SELECT * FROM countries";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.execute();
        ResultSet resultSet = ps.getResultSet();

        ObservableList<Country> countryList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Country country = extractFromResultSet(resultSet);
            countryList.add(country);
        }
        ps.close();
        return countryList;

    }
    /**
     * Queries the database and returns a Country in the Country table by the Country Name
     * @param name String for the country name
     * @param connection Connection object to database
     * @return returns an observable list
     * @throws SQLException due to SQL query
     */
    public static Country getCountryByName(String name, Connection connection) throws SQLException {

        String query = "SELECT * FROM countries WHERE Country=?";

        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, name);

        ps.execute();

        ResultSet resultSet = ps.getResultSet();

        Country country = extractFromResultSet(resultSet);


        ps.close();
        return country;

    }
    /**
     * Queries the database and returns a Country in the Country table by the Country ID
     * @param connection Connection object to database
     * @return returns an observable list
     * @throws SQLException due to SQL query
     */
    public static Country getCountryFromID(int ID, Connection connection) throws SQLException {
        Country country = new Country();

        String query = "SELECT * FROM countries WHERE Country_ID=?";

        PreparedStatement ps = connection.prepareStatement(query);

        ps.setInt(1, ID);

        ps.execute();
        ResultSet resultSet = ps.getResultSet();

        country = extractFromResultSet(resultSet);

        ps.close();

        return country;

    }
    /**
     * Extracts the information from a ResultSet object into a single Country object.
     * @param resultSet ResultSet object
     * @return returns an appointment object
     * @throws SQLException due to SQL query
     */
    private static Country extractFromResultSet(ResultSet resultSet) throws SQLException{
        Country country = new Country();

        country.setCountryID(resultSet.getInt("Country_ID"));
        country.setCountry(resultSet.getString("Country"));

        return country;
    }

}
