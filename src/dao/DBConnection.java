package dao;


import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Used for connection and disconnection from the Database
 */
public class DBConnection {
    // JDBC URL Variables
    private static final String PROTOCOL = "jdbc";
    private static final String VENDORNAME = ":mysql:";
    private static final String DBName = "WJ06qAC";
    private static final String DBLOC = "//wgudb.ucertify.com/" + DBName;
    // JDBC URL Combined Variable
    private static final String JDBCADDRESS = PROTOCOL + VENDORNAME + DBLOC;
    // Login Credentials
    private static final String USERNAME = "U06qAC"; // Database username
    private static final String PASSWORD = "53688840846"; // Database password
    //Connection object
    protected static Connection conn = null;

    /**
     * Connects to database
     * @return connection object
     */
    public static Connection openConnection() {
        try{
            MysqlDataSource d = new MysqlDataSource();
            d.setUser(USERNAME);
            d.setPassword(PASSWORD);
            d.setURL(JDBCADDRESS);
            d.setDatabaseName(DBName);
            conn = d.getConnection();
            System.out.println("Connection successful");
        }
        catch(SQLException E) {
            System.out.println("Error: " + E.getMessage());
        }
        return conn;
    }

    /**
     * close database connection
     */
    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Database connection closed");
        }
        catch (SQLException E) {
            System.out.println("Error: " + E.getMessage());
        }
    }

}
