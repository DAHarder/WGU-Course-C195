package controller;

import dao.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.AppointmentView;
import model.Contact;
import utilities.ReportUtilities;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Gui Page - Mainscreen - the purpose of this is to be the mainscreen of the application.
 * It holds the appointments and all the functional buttons to navigate around the application
 */
public class MainscreenController implements Initializable {
    Stage stage;
    Parent scene;

    private static int monthCount = 0;
    private static int weekCount = 0;
    private static boolean month = false;
    private static boolean week = false;

    @FXML
    private Label labelUserName;

    @FXML
    private ComboBox<Contact> comboBoxContacts;

    @FXML
    private TableView<AppointmentView> tableAppointments;

    @FXML
    private TableColumn<AppointmentView, Integer> tblColumnID;

    @FXML
    private TableColumn<AppointmentView, String> tblColumnTitle;

    @FXML
    private TableColumn<AppointmentView, String> tblColumnDescription;

    @FXML
    private TableColumn<AppointmentView, String> tblColumnLocation;

    @FXML
    private TableColumn<AppointmentView, String> tblColumnContact;

    @FXML
    private TableColumn<AppointmentView, String> tblColumnType;

    @FXML
    private TableColumn<AppointmentView, LocalDateTime> tblColumnStart;

    @FXML
    private TableColumn<AppointmentView, LocalDateTime> tblColumnEnd;

    @FXML
    private TableColumn<AppointmentView, String> tblColumnCustomer;

    @FXML
    private Label labelTotalAppCount;

    @FXML
    private Label labelUniqueCustomersCount;

    @FXML
    private Label labelTodaysDate;

    /**
     * Filters the appointments table to show all objects.
     *  queries the database then loads the results into the table
     */
    @FXML
    void btnALL(ActionEvent event) {
        month = false;
        week = false;
        //Connect to database and query for appointments to load into the tableAppointments
        Connection connection = DBConnection.openConnection();
        try {
            tableAppointments.setItems(AppointmentViewDao.getAllAppointments(UserDao.loggedUser, connection));
        } catch (SQLException E) {
            System.out.println(E.getMessage());
        }

        DBConnection.closeConnection();

        //Report number of appointments
        labelTotalAppCount.setText(ReportUtilities.appointmentReport(tableAppointments));

        //Report total unique customers
        Set<String> customerReport = new HashSet<>();
        for (Appointment appointment: tableAppointments.getItems()) {
            customerReport.add(String.valueOf(appointment.getCustomerID()));
        }
        labelUniqueCustomersCount.setText(String.valueOf(customerReport.size()));

        //Set tableview sort default
        tableAppointments.getSortOrder().add(tblColumnStart);
        tableAppointments.sort();

    }

    /**
     * Attempts to delete the selected Item in the appointemnts table.
     */
    @FXML
    void btnDelete(ActionEvent event) {
        Connection connection = DBConnection.openConnection();
        //Attempt to delete appointment.  If fails, produces error
        if (tableAppointments.getSelectionModel().getSelectedIndex() != -1) {
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to Delete the following appointment: "
                + "ID: " + tableAppointments.getSelectionModel().getSelectedItem().getAppointmentID() + "\n"
                + "Title: " + tableAppointments.getSelectionModel().getSelectedItem().getTitle()+"?\n"
                + "Type: " + tableAppointments.getSelectionModel().getSelectedItem().getType());
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Appointment appointment = tableAppointments.getSelectionModel().getSelectedItem();
                try {
                    if (AppointmentsDao.deleteAppointment(appointment, connection)==0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Deletion Error");
                        alert.setContentText("Unable to delete item");
                        alert.show();
                    }
                    else
                        tableAppointments.setItems(AppointmentViewDao.getAllAppointments(UserDao.loggedUser, connection));
                } catch (SQLException E) {
                    System.out.println(E.getMessage());
                }
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Data Selection Error");
            alert.setContentText("Please select an item to Delete");
            alert.show();
        }

        DBConnection.closeConnection();

        //Report number of appointments
        labelTotalAppCount.setText(ReportUtilities.appointmentReport(tableAppointments));

        //Report total unique customers
        Set<String> customerReport = new HashSet<>();
        for (Appointment appointment: tableAppointments.getItems()) {
            customerReport.add(String.valueOf(appointment.getCustomerID()));
        }
        labelUniqueCustomersCount.setText(String.valueOf(customerReport.size()));

        //Set tableview sort default
        tableAppointments.getSortOrder().add(tblColumnStart);
        tableAppointments.sort();
    }

    /**
     * Changes the scene to the Appointment Modify scene.
     * Uses the sendAppointment method to send the information from the selected Item in the table to the new scene.
     * @throws IOException due to changing the scene to appointmentModify.fxml
     */
    @FXML
    void btnEdit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/appointmentModify.fxml"));
        loader.load();

        AppointmentModifyController AMC = loader.getController();
        AMC.sendAppointment(tableAppointments.getSelectionModel().getSelectedItem()); //Sends data to the new scene

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.setTitle("EDIT APPOINTMENT");
        stage.show();
    }

    /**
     * Changes the scene to the Edit Customers scene.
     * @throws IOException due to changing the scene to customers.fxml
     */
    @FXML
    void btnEditCustomers(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/customers.fxml"));
        loader.load();
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.setTitle("EDIT CUSTOMERS");
        stage.show();
    }

    /**
     * Filters the table to view only the appointments within the current month.
     * Functions with the next and previous buttons
     */
    @FXML
    void btnMonth(ActionEvent event) {
        month = true;
        week = false;
        monthCount = 0;

        Connection connection = DBConnection.openConnection();
        try {
            tableAppointments.setItems(AppointmentViewDao.getAllAppointmentsByMonth(UserDao.loggedUser, connection, monthCount));
        } catch (SQLException E) {
            System.out.println(E.getMessage());
        }

        DBConnection.closeConnection();

        //Report number of appointments
        labelTotalAppCount.setText(ReportUtilities.appointmentReport(tableAppointments));

        //Report total unique customers
        Set<String> customerReport = new HashSet<>();
        for (Appointment appointment: tableAppointments.getItems()) {
            customerReport.add(String.valueOf(appointment.getCustomerID()));
        }
        labelUniqueCustomersCount.setText(String.valueOf(customerReport.size()));

        //Set tableview sort default
        tableAppointments.getSortOrder().add(tblColumnStart);
        tableAppointments.sort();

    }

    /**
     * Changes the scene to the appointments Add scene.
     * @throws IOException due to changing the scene to the appointmentAdd.fxml
     */
    @FXML
    void btnNewAppointment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/appointmentAdd.fxml"));
        loader.load();
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.setTitle("NEW APPOINTMENT");
        stage.show();
    }

    /**
     * Filters the appointment table - filters either one week or one month into the future.
     * Depends on the whether the button week or month was clicked first.
     * If neither was clicked, the button should do nothing
     */
    @FXML
    void btnNext(ActionEvent event) {
        if (month) {
            monthCount++;

            Connection connection = DBConnection.openConnection();
            try {
                tableAppointments.setItems(AppointmentViewDao.getAllAppointmentsByMonth(UserDao.loggedUser, connection, monthCount));
            } catch (SQLException E) {
                System.out.println(E.getMessage());
            }

            DBConnection.closeConnection();

            //Report number of appointments
            labelTotalAppCount.setText(ReportUtilities.appointmentReport(tableAppointments));

            //Report total unique customers
            Set<String> customerReport = new HashSet<>();
            for (Appointment appointment: tableAppointments.getItems()) {
                customerReport.add(String.valueOf(appointment.getCustomerID()));
            }
            labelUniqueCustomersCount.setText(String.valueOf(customerReport.size()));

            //Set tableview sort default
            tableAppointments.getSortOrder().add(tblColumnStart);
            tableAppointments.sort();
        }
        else if(week) {
            weekCount++;

            Connection connection = DBConnection.openConnection();
            try {
                tableAppointments.setItems(AppointmentViewDao.getAllAppointmentsByWeek(UserDao.loggedUser, connection, weekCount));
            } catch (SQLException E) {
                System.out.println(E.getMessage());
            }

            DBConnection.closeConnection();

            //Report number of appointments
            labelTotalAppCount.setText(ReportUtilities.appointmentReport(tableAppointments));

            //Report total unique customers
            Set<String> customerReport = new HashSet<>();
            for (Appointment appointment: tableAppointments.getItems()) {
                customerReport.add(String.valueOf(appointment.getCustomerID()));
            }
            labelUniqueCustomersCount.setText(String.valueOf(customerReport.size()));

            //Set tableview sort default
            tableAppointments.getSortOrder().add(tblColumnStart);
            tableAppointments.sort();

        }
    }
    /**
     * Filters the appointment table - filters either one week or one month into the past.
     * Depends on the whether the button week or month was clicked first.
     * If neither was clicked, the button should do nothing
     */
    @FXML
    void btnPrevious(ActionEvent event) {
        if (month) {
            monthCount--;

            Connection connection = DBConnection.openConnection();
            try {
                tableAppointments.setItems(AppointmentViewDao.getAllAppointmentsByMonth(UserDao.loggedUser, connection, monthCount));
            } catch (SQLException E) {
                System.out.println(E.getMessage());
            }

            DBConnection.closeConnection();

            //Report number of appointments
            labelTotalAppCount.setText(ReportUtilities.appointmentReport(tableAppointments));

            //Report total unique customers
            Set<String> customerReport = new HashSet<>();
            for (Appointment appointment: tableAppointments.getItems()) {
                customerReport.add(String.valueOf(appointment.getCustomerID()));
            }
            labelUniqueCustomersCount.setText(String.valueOf(customerReport.size()));

            //Set tableview sort default
            tableAppointments.getSortOrder().add(tblColumnStart);
            tableAppointments.sort();
        }
        else if(week) {
            weekCount--;

            Connection connection = DBConnection.openConnection();
            try {
                tableAppointments.setItems(AppointmentViewDao.getAllAppointmentsByWeek(UserDao.loggedUser, connection, weekCount));
            } catch (SQLException E) {
                System.out.println(E.getMessage());
            }

            DBConnection.closeConnection();

            //Report number of appointments
            labelTotalAppCount.setText(ReportUtilities.appointmentReport(tableAppointments));

            //Report total unique customers
            Set<String> customerReport = new HashSet<>();
            for (Appointment appointment: tableAppointments.getItems()) {
                customerReport.add(String.valueOf(appointment.getCustomerID()));
            }
            labelUniqueCustomersCount.setText(String.valueOf(customerReport.size()));

            //Set tableview sort default
            tableAppointments.getSortOrder().add(tblColumnStart);
            tableAppointments.sort();
        }
    }

    /**
     * Resets the selection on the Contacts combo box and calls the btnAll method.
     */
    @FXML
    void btnResetFilter(ActionEvent event) {
        comboBoxContacts.getSelectionModel().clearSelection();
        btnALL(event);
    }

    /**
     * Filters the table to view only the appointments within the current week.
     * Functions with the next and previous buttons
     */
    @FXML
    void btnWeek(ActionEvent event) {
        month = false;
        week = true;
        weekCount = 0;

        Connection connection = DBConnection.openConnection();
        try {
            tableAppointments.setItems(AppointmentViewDao.getAllAppointmentsByWeek(UserDao.loggedUser, connection, weekCount));
        } catch (SQLException E) {
            System.out.println(E.getMessage());
        }

        DBConnection.closeConnection();

        //Report number of appointments
        labelTotalAppCount.setText(ReportUtilities.appointmentReport(tableAppointments));

        //Report total unique customers
        Set<String> customerReport = new HashSet<>();
        for (Appointment appointment: tableAppointments.getItems()) {
            customerReport.add(String.valueOf(appointment.getCustomerID()));
        }
        labelUniqueCustomersCount.setText(String.valueOf(customerReport.size()));

        //Set tableview sort default
        tableAppointments.getSortOrder().add(tblColumnStart);
        tableAppointments.sort();

    }

    /**
     * Filters the appointments table based upon the current selected contact from the Combo box.
     * Filter is based on a contactID = contactID.
     */
    @FXML
    void comboBoxContacts(ActionEvent event) {
        Contact contact = comboBoxContacts.getSelectionModel().getSelectedItem();

        if (!comboBoxContacts.getSelectionModel().isEmpty()) {
            Connection connection = DBConnection.openConnection();
            try {
                tableAppointments.setItems(AppointmentViewDao.getAllAppointmentsByContact(UserDao.loggedUser, connection, contact));
            } catch (SQLException E) {
                System.out.println(E.getMessage());
            }

            DBConnection.closeConnection();

            //Report number of appointments
            labelTotalAppCount.setText(ReportUtilities.appointmentReport(tableAppointments));

            //Report total unique customers
            Set<String> customerReport = new HashSet<>();
            for (Appointment appointment: tableAppointments.getItems()) {
                customerReport.add(String.valueOf(appointment.getCustomerID()));
            }
            labelUniqueCustomersCount.setText(String.valueOf(customerReport.size()));

            //Set tableview sort default
            tableAppointments.getSortOrder().add(tblColumnStart);
            tableAppointments.sort();

        }
    }

    /**
     *Init method for the scene.
     * the method does the following:
     * Displays the user name and date using a label.
     * populates the appointments table and Contacts combo box.
     * Provides reports on the appointments table.
     * Pops up a dialog box if any appointments are within 15 minutes, within the past 15 minutes, or neither.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = DBConnection.openConnection();

        labelUserName.setText(UserDao.loggedUser.getUserName().substring(0, 1).toUpperCase() + UserDao.loggedUser.getUserName().substring(1));

        DateTimeFormatter df = DateTimeFormatter.ofPattern("EEEE, MMMM d, y");
        labelTodaysDate.setText(df.format(LocalDateTime.now()));

        try {
            tableAppointments.setItems(AppointmentViewDao.getAllAppointments(UserDao.loggedUser, connection));
        } catch (SQLException E) {
            System.out.println(E.getMessage());
        }

        tblColumnID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        tblColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tblColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tblColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tblColumnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tblColumnStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        tblColumnEnd.setCellValueFactory(new PropertyValueFactory<>("end"));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yy h:mm a");

        tblColumnStart.setCellFactory(column -> new TableCell<AppointmentView, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty)
                    setText(null);
                else
                    setText(dtf.format(item));
            }
    });

        tblColumnEnd.setCellFactory(column -> new TableCell<AppointmentView, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty)
                    setText(null);
                else
                    setText(dtf.format(item));
            }
        });

        tblColumnContact.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        tblColumnCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        comboBoxContacts.setPromptText("Select a Contact");
        comboBoxContacts.setVisibleRowCount(10);

        try {
            comboBoxContacts.setItems(ContactDao.getAllContacts(connection));
        } catch (SQLException E) {
            System.out.println(E.getMessage());
        }

        DBConnection.closeConnection();

        //Report number of appointments
        labelTotalAppCount.setText(ReportUtilities.appointmentReport(tableAppointments));

        //Report total unique customers
        Set<String> customerReport = new HashSet<>();
        for (Appointment appointment: tableAppointments.getItems()) {
            customerReport.add(String.valueOf(appointment.getCustomerID()));
        }
        labelUniqueCustomersCount.setText(String.valueOf(customerReport.size()));

        //Set tableview sort default
        tableAppointments.getSortOrder().add(tblColumnStart);
        tableAppointments.sort();

        //Appointment Alerts
        if(UserDao.logged == false) {
            int i = 0;
            for (AppointmentView appointment:tableAppointments.getItems()) {
                i++;
                if (appointment.getStart().isBefore(LocalDateTime.now().plusMinutes(15)) && appointment.getStart().isAfter(LocalDateTime.now().plusSeconds(1))) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Appointment Alert");
                    alert.setHeaderText("You have an upcoming Appointment!");
                    alert.setContentText("You have an appointment in " + (appointment.getStart().toLocalTime().getMinute() - LocalDateTime.now().toLocalTime().getMinute()) + " minutes!\n\n"
                            + "ID: " + appointment.getAppointmentID() + "\n" + "Title: " + appointment.getTitle() + "\n" + "Time: " + dtf.format(appointment.getStart()));
                    alert.show();
                    break;
                } else if (appointment.getStart().isBefore(LocalDateTime.now().minusSeconds(1)) && appointment.getStart().isAfter(LocalDateTime.now().minusMinutes(15))) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Appointment Alert");
                    alert.setHeaderText("You have a missed Appointment!");
                    alert.setContentText("You have an appointment that was scheduled " + (LocalDateTime.now().toLocalTime().getMinute() - appointment.getStart().toLocalTime().getMinute()) + " minutes ago!\n\n"
                            + "ID: " + appointment.getAppointmentID() + "\n" + "Title: " + appointment.getTitle() + "\n" + "Time: " + dtf.format(appointment.getStart()));
                    alert.show();
                    break;
                } else if (i == tableAppointments.getItems().size()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Appointment Information");
                    alert.setHeaderText("You have no upcoming appointments");
                    alert.show();
                    break;
                }
            }
            UserDao.logged = true;
        }


    }

}
