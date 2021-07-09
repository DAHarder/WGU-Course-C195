package controller;

import dao.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.AppointmentView;
import model.Contact;
import model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Inherited;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static dao.UserDao.loggedUser;
/** Login Gui page - purpose is to allow a user to log into the application for tracking and support purposes*/
public class LoginController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private Label label_username;

    @FXML
    private Label label_password;

    @FXML
    private TextField field_username;

    @FXML
    private PasswordField field_password;

    @FXML
    private Label label_userLocation;

    @FXML
    private Label label_errormsg;

    @FXML
    private Label label_location;

    /** Init method for the login gui screen.
     * From top to bottom:
     *
     * Populates the userlocation label from the language setting on the users computer (windows only?)
     * Automatically translates Japanese, English, and French using users computer language setting (windows only?)
     * Adds listeners to automatically change the fields back to default CSS style on focus
     *
     * */
    @Override
    public void initialize (URL url, ResourceBundle rb) {
        label_userLocation.setText(Locale.getDefault().getDisplayCountry());

        try {
            rb = ResourceBundle.getBundle("login", Locale.getDefault());
            if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("ja")) {
                label_username.setText(rb.getString("username"));
                label_password.setText(rb.getString("password"));
                label_location.setText(rb.getString("location"));
                label_errormsg.setText(rb.getString("loginerror"));
            }
        }
        catch (MissingResourceException E){
            System.out.println(E.getMessage());
        }
        field_username.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    field_username.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
                }
        });
        field_password.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field_password.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
            }
        });
    }

    /**
     * Login handler - Handles the login process for the application.  Using the username and password field, checks for a valid match from the database.
     * If a valid match is found it will continue to the next scene - the mainscreen.
     *
     * @throws IOException exception occurs during changing scenes - JavaFX issue
     * @throws SQLException exception in the case of a SQL error
     */
    @FXML
    void btn_Login_handler(ActionEvent event) throws IOException, SQLException {
        //Log sign in attempt
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yy H:mm");
        FileWriter fwriter = new FileWriter("src/login_activity.txt", true);
        PrintWriter loginAuditFile = new PrintWriter(fwriter);

        //Database connections and query
        Connection conn = DBConnection.openConnection();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE User_Name = ? AND Password = ?");
        ps.setString(1, field_username.getText().trim().toLowerCase());
        ps.setString(2, field_password.getText().trim().toLowerCase());

        ResultSet rs = DBQuery.query(ps);
        //Continue if username and password combo is found and log attempt
        if(rs != null && rs.next()) {
            loginAuditFile.println("Successful login\t" + "Logged time: " + dtf.format(LocalDateTime.now()) + "\t" + "User: " + field_username.getText() + "\t" + "Pass: " + field_password.getText());
            loginAuditFile.close();

            UserDao.loggedUser = new User();
            loggedUser.setUserID(rs.getInt(1));
            loggedUser.setUserName(rs.getString(2));

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/mainscreen.fxml"));
            loader.load();

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle("MAIN");
            stage.show();
        }
        //If no valid combination found, it will highlight fields red, provide an error, and log the attempt
        else {
            loginAuditFile.println("Failed login\t" + "Logged time: " + dtf.format(LocalDateTime.now()) + "\t" + "User: " + field_username.getText() + "\t" + "Pass: " + field_password.getText());
            loginAuditFile.close();

            label_errormsg.setVisible(true);
            field_username.setStyle("-fx-border-color: red");
            field_password.setStyle("-fx-border-color: red");
        }

        ps.close();
        DBConnection.closeConnection();


        }

}
