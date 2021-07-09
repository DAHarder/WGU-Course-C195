package controller;

import dao.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import utilities.AlertMessages;
import utilities.TimeUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * Gui page - purpose is to allow the ability to add a new appointment to the appointment table on the database.
 */
public class AppointmentAddController implements Initializable {

    @FXML
    private TextField inputFieldTitle;

    @FXML
    private ComboBox<String> comboBoxType;

    @FXML
    private DatePicker dateTimeStart;

    @FXML
    private DatePicker dateTimeEnd;

    @FXML
    private ComboBox<String> comboBoxStartTime;

    @FXML
    private ComboBox<String> comboBoxEndTime;

    @FXML
    private TextField inputFieldLocation;

    @FXML
    private ComboBox<Contact> comboBoxContact;

    @FXML
    private ComboBox<Customer> comboBoxCustomer;

    @FXML
    private ComboBox<User> comboBoxUser;

    @FXML
    private TextArea inputFieldDescription;


    @FXML
    /**
     * Back button in Gui - changes scene back to main screen.
     *
     * @throws IOException due to JavaFX change scene
     */
    void btnBack(ActionEvent event) throws IOException {
        Stage stage;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/mainscreen.fxml"));
        loader.load();
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.setTitle("MAIN");
        stage.show();
    }

    /** Inserts new record into database.
     * Attempts to take all the fields and insert them into the Database as a new record
     * Checks for data validation and produces errors as needed.
     * @throws IOException due to JavaFX change scene
     */
    @FXML
    void btnSaveClose(ActionEvent event) throws IOException {
        //SET TIME VARIABLES
        LocalDateTime endDateTime=null;
        LocalDateTime startDateTime=null;

        if(dateTimeStart.getValue() != null && !comboBoxStartTime.getSelectionModel().isEmpty()){
            LocalTime start = LocalTime.parse(comboBoxStartTime.getValue(), TimeUtil.timeFormatter);
            startDateTime = LocalDateTime.of(dateTimeStart.getValue(), start);
        }
        if(dateTimeEnd.getValue() != null && !comboBoxEndTime.getSelectionModel().isEmpty()){
            LocalTime end = LocalTime.parse(comboBoxEndTime.getValue(), TimeUtil.timeFormatter);
            endDateTime = LocalDateTime.of(dateTimeEnd.getValue(), end);
        }

        //DATA VALIDATION - EMPTY FIELDS
        if(inputFieldTitle.getText().isEmpty()) { //Title field
            AlertMessages.dataValidationError(1, "Title");
            AlertMessages.highlightErrorField(inputFieldTitle);
            return;
        }
        else if(comboBoxType.getSelectionModel().isEmpty()){
            AlertMessages.dataValidationError(1, "Type");
            AlertMessages.highlightErrorField(comboBoxType);
            return;
        }
        else if(dateTimeStart.getValue() == null || comboBoxStartTime.getSelectionModel().isEmpty()){
            AlertMessages.dataValidationError(1, "Start Time");
            AlertMessages.highlightErrorField(dateTimeStart);
            AlertMessages.highlightErrorField(comboBoxStartTime);
            return;
        }
        else if(dateTimeEnd.getValue() == null || comboBoxEndTime.getSelectionModel().isEmpty()){
            AlertMessages.dataValidationError(1, "End Time");
            AlertMessages.highlightErrorField(dateTimeEnd);
            AlertMessages.highlightErrorField(comboBoxEndTime);
            return;
        }
        else if(comboBoxContact.getSelectionModel().isEmpty()){
            AlertMessages.dataValidationError(1, "Contact");
            AlertMessages.highlightErrorField(comboBoxContact);
            return;
        }
        else if(comboBoxCustomer.getSelectionModel().isEmpty()){
            AlertMessages.dataValidationError(1, "Customer");
            AlertMessages.highlightErrorField(comboBoxCustomer);
            return;
        }
        //DATA VALIDATION - TIME CHECK
        else if(startDateTime.isAfter(endDateTime)){
            AlertMessages.dataValidationError(2, "Start/End times");
            AlertMessages.highlightErrorField(dateTimeStart);
            AlertMessages.highlightErrorField(comboBoxStartTime);
            AlertMessages.highlightErrorField(dateTimeEnd);
            AlertMessages.highlightErrorField(comboBoxEndTime);
            return;
        }

        //DATA VALIDATION - TIME CHECK - BUSINESS HOURS - START
        else if(TimeUtil.localTimeToEST(startDateTime).toLocalTime().isBefore(LocalTime.of(8,0,0)) || TimeUtil.localTimeToEST(startDateTime).toLocalTime().isAfter(LocalTime.of(22,0,0))){
            AlertMessages.dataValidationError(3, "Start/End times");
            AlertMessages.highlightErrorField(comboBoxStartTime);
            return;
        }

        //DATA VALIDATION - TIME CHECK - BUSINESS HOURS - END
        else if(TimeUtil.localTimeToEST(endDateTime).toLocalTime().isBefore(LocalTime.of(8,0,0)) || TimeUtil.localTimeToEST(endDateTime).toLocalTime().isAfter(LocalTime.of(22,0,0))){
            AlertMessages.dataValidationError(3, "Start/End times");
            AlertMessages.highlightErrorField(comboBoxEndTime);
            return;
        }

        //DATA VALIDATION - CUSTOMER APPOINTMENTS OVERLAP
        Connection connection = DBConnection.openConnection();

        try {
        if(!AppointmentsDao.getAllAppointmentsByCustomerAndTimes(UserDao.loggedUser, comboBoxCustomer.getSelectionModel().getSelectedItem(), startDateTime.plusSeconds(1), endDateTime.minusSeconds(1), connection).isEmpty()) {
            AlertMessages.dataValidationError(4, "Start/End times");
            AlertMessages.highlightErrorField(dateTimeStart);
            AlertMessages.highlightErrorField(comboBoxStartTime);
            AlertMessages.highlightErrorField(dateTimeEnd);
            AlertMessages.highlightErrorField(comboBoxEndTime);
            DBConnection.closeConnection();
            return;
        }

        //ATTEMPT TO SAVE DATA TO DATABASE
        Appointment appointment = new Appointment();

        appointment.setTitle(inputFieldTitle.getText());
        appointment.setDescription(inputFieldDescription.getText());
        appointment.setLocation(inputFieldLocation.getText());
        appointment.setType(comboBoxType.getValue());
        appointment.setStart(startDateTime);
        appointment.setEnd(endDateTime);
        appointment.setCustomerID(comboBoxCustomer.getSelectionModel().getSelectedItem().getCustomerID());
        appointment.setUserID(comboBoxUser.getSelectionModel().getSelectedItem().getUserID());
        appointment.setContactID(comboBoxContact.getSelectionModel().getSelectedItem().getContactID());


            AppointmentsDao.insertAppointment(appointment, connection);
        } catch (SQLException E) {
            System.out.println(E.getMessage());
        }

        DBConnection.closeConnection();

        //Change scene back to mainscreen
        Stage stage;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/mainscreen.fxml"));
        loader.load();
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.setTitle("MAIN");
        stage.show();
    }


    /**
     * Init method for the scene.
     * Does multiple tasks:
     * Sets the Combo Box Types to a select items (**** In the future use a database table for this??***)
     * Populates the Combo Box start and end times.
     * Populates the Customer and Contact Combo Boxes from the database.
     * Adds listeners to reset the fields to the base CSS style upon focus. ***
     *
     * Justification for lambas: Overview for all lambas as they provde the same service:
     * provides ability to add functionality to the addListener method utilizign the
     * focusedProperty to edit the field upon focus.  Used as Lambda to override method in an easy to read format.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = DBConnection.openConnection();
            //Populate the contact and customer combo boxes from database
            comboBoxContact.setItems(ContactDao.getAllContacts(connection));
            comboBoxCustomer.setItems(CustomerDao.getAllCustomers(connection));
            comboBoxUser.setItems(UserDao.getAllUsers(connection));

        } catch (SQLException | NullPointerException E) {
            System.out.println(E.getMessage());
        }
        //Populate the Type combo box
        comboBoxType.getItems().addAll("De-Briefing", "Meeting", "Luncheon", "Customer interview");
        //Populate the start and end times
        comboBoxStartTime.setItems(TimeUtil.populateTime());
        comboBoxEndTime.setItems(TimeUtil.populateTime());

        DBConnection.closeConnection();

        //Listeners to reset fields back to the default style after they are focused
        inputFieldTitle.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                inputFieldTitle.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
            }
        });
        comboBoxType.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                comboBoxType.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
            }
        });
        dateTimeStart.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                dateTimeStart.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
            }
        });
        comboBoxStartTime.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                comboBoxStartTime.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
            }
        });
        dateTimeEnd.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                dateTimeEnd.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
            }
        });
        comboBoxEndTime.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                comboBoxEndTime.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
            }
        });
        comboBoxContact.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                comboBoxContact.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
            }
        });
        comboBoxCustomer.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                comboBoxCustomer.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
            }
        });
    }
}
