package controller;

import dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import utilities.AlertMessages;
import utilities.TimeUtil;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Customers Gui Page - purpose is to allow the ability to ADD, MODIFY, DELETE, and create NEW customer records into and from the Database
 */
public class CustomersController implements Initializable {
    Stage stage;
    Parent scene;

    private static ObservableList<FirstLevelDivision> states = FXCollections.observableArrayList();

    @FXML
    private TableView<CustomerView> tableCustomers;

    @FXML
    private TableColumn<CustomerView, Integer> tblColumnID;

    @FXML
    private TableColumn<CustomerView, String> tblColumnName;

    @FXML
    private TableColumn<CustomerView, String> tblColumnAddress;

    @FXML
    private TableColumn<CustomerView, String> tblColumnPostal;

    @FXML
    private TableColumn<CustomerView, String> tblColumnPhone;

    @FXML
    private TextField inputfieldSearch;

    @FXML
    private Label labelCustomerID;

    @FXML
    private TextField inputfieldCustomerName;

    @FXML
    private TextField inputFieldAddress;

    @FXML
    private TableColumn<CustomerView, String> tblColumnStateProvince;

    @FXML
    private TableColumn<CustomerView, String> tblColumnCountry;

    @FXML
    private TextField inputFieldPostal;

    @FXML
    private ComboBox<Country> comboBoxCountry;

    @FXML
    private ComboBox<FirstLevelDivision> comboBoxState;

    @FXML
    private TextField inputFieldPhone;

    /**
     * Back button in Gui - changes scene back to main screen
     *
     * @throws IOException due to JavaFX change scene
     */
    @FXML
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

    /**
     * Attempts to delete the selected record using the tablecustomers table.
     *
     */
    @FXML
    void btnDelete(ActionEvent event) {
        Connection connection = DBConnection.openConnection();
        //Make sure an item is selected
        if (tableCustomers.getSelectionModel().getSelectedIndex() != -1) {
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to Delete the customer: " +tableCustomers.getSelectionModel().getSelectedItem().getCustomerName()+"?");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Customer customer = tableCustomers.getSelectionModel().getSelectedItem();
                try {
                    if (CustomerDao.deleteCustomer(customer, connection)==0) { //Attempt to delete the record
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Deletion Error");
                        alert.setContentText("Unable to delete item");
                        alert.show();
                    }
                    else
                        tableCustomers.setItems(CustomerViewDao.getAllCustomers(connection)); //If successful, refresh the table with a call to the database
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

        clearFields();
    }

    /**
     * Used to clear all the fields in the current scene to blank
     */
    @FXML
    void btnNewCustomer(ActionEvent event) {
        clearFields();
    }

    /**
     * Attempts to take all the fields and insert them into the Database as a new record for the Customer Table
     * Checks for data validation and produces errors as needed
     * @throws IOException due to JavaFX change scene
     */
    @FXML
    void btnSave(ActionEvent event) throws IOException {
        if(labelCustomerID.getText().equals("Auto Generated")) {
            //DATA VALIDATION - EMPTY FIELDS
            if (inputfieldCustomerName.getText().isEmpty()) { //Customer name field
                AlertMessages.dataValidationError(1, "Customer name");
                AlertMessages.highlightErrorField(inputfieldCustomerName);
                return;
            }
            else if (inputFieldAddress.getText().isEmpty()) { //Customer Address field
                AlertMessages.dataValidationError(1, "Customer address");
                AlertMessages.highlightErrorField(inputFieldAddress);
                return;
            }
            else if(comboBoxCountry.getSelectionModel().isEmpty()){
                AlertMessages.dataValidationError(1, "Country");
                AlertMessages.highlightErrorField(comboBoxCountry);
                return;
            }
            else if(comboBoxState.getSelectionModel().isEmpty()){
                AlertMessages.dataValidationError(1, "State");
                AlertMessages.highlightErrorField(comboBoxState);
                return;
            }
            else if (inputFieldPostal.getText().isEmpty()) { //Customer Address field
                AlertMessages.dataValidationError(1, "Postal Code");
                AlertMessages.highlightErrorField(inputFieldPostal);
                return;
            }
            else if (inputFieldPhone.getText().isEmpty()) { //Customer Address field
                AlertMessages.dataValidationError(1, "Customer Phone number");
                AlertMessages.highlightErrorField(inputFieldPhone);
                return;
            }
            addCustomer();
        }
        else {
            //DATA VALIDATION - EMPTY FIELDS
            if (inputfieldCustomerName.getText().isEmpty()) { //Customer name field
                AlertMessages.dataValidationError(1, "Customer name");
                AlertMessages.highlightErrorField(inputfieldCustomerName);
                return;
            }
            else if (inputFieldAddress.getText().isEmpty()) { //Customer Address field
                AlertMessages.dataValidationError(1, "Customer address");
                AlertMessages.highlightErrorField(inputFieldAddress);
                return;
            }
            else if(comboBoxCountry.getSelectionModel().isEmpty()){
                AlertMessages.dataValidationError(1, "Country");
                AlertMessages.highlightErrorField(comboBoxCountry);
                return;
            }
            else if(comboBoxState.getSelectionModel().isEmpty()){
                AlertMessages.dataValidationError(1, "State");
                AlertMessages.highlightErrorField(comboBoxState);
                return;
            }
            else if (inputFieldPostal.getText().isEmpty()) { //Customer Address field
                AlertMessages.dataValidationError(1, "Postal Code");
                AlertMessages.highlightErrorField(inputFieldPostal);
                return;
            }
            else if (inputFieldPhone.getText().isEmpty()) { //Customer Address field
                AlertMessages.dataValidationError(1, "Customer Phone number");
                AlertMessages.highlightErrorField(inputFieldPhone);
                return;
            }
            updateCustomer();
        }
    }

    /**
     * Filters the Customers table in the scene to 'search' for the input in the search field.
     * Searches by ID or Names
     */
    @FXML
    void Search(ActionEvent event) {
        Connection connection = DBConnection.openConnection();
        try {
            if(inputfieldSearch.getText().isEmpty()) {
                tableCustomers.setItems(CustomerViewDao.getAllCustomers(connection));
            }
            else
                if ((inputfieldSearch.getText().matches("[0-9]+")))
                    tableCustomers.setItems(CustomerViewDao.getCustomersFromID(Integer.parseInt(inputfieldSearch.getText()), connection));
                else
                    tableCustomers.setItems(CustomerViewDao.getCustomersFromName(inputfieldSearch.getText(), connection));
            }
        catch (SQLException E) {
            System.out.println(E.getMessage());
        }
        DBConnection.closeConnection();
    }

    /**
     * Filters the State Combo Box from the selection of the below combo box (country)
     */
    @FXML
    void comboBoxCountrySelect(ActionEvent event) {
        ObservableList<FirstLevelDivision> stateList = FXCollections.observableArrayList();

        if (!comboBoxCountry.getSelectionModel().isEmpty()) {
            for (FirstLevelDivision fld:states) {
                if (fld.getCountryID() == comboBoxCountry.getSelectionModel().getSelectedItem().getCountryID()) {
                    stateList.add(fld);
                }
            }
            comboBoxState.setItems(stateList);
        }
    }

    /**
     * Attempts to insert a NEW customer record into the Customer Table on the Database using the fields on the current scene
     */
    private void addCustomer() {
        Connection connection = DBConnection.openConnection();

        Customer customer = new Customer();

        customer.setCustomerName(inputfieldCustomerName.getText());
        customer.setAddress(inputFieldAddress.getText());
        customer.setPostalCode(inputFieldPostal.getText());
        customer.setPhone(inputFieldPhone.getText());
        customer.setUpdatedBy(UserDao.loggedUser.getUserName());
        customer.setCreatedBy(UserDao.loggedUser.getUserName());
        customer.setDivisionID(comboBoxState.getSelectionModel().getSelectedItem().getDivisionID());
        // Attempt to insert customer record into database
        try {
            int insert = CustomerDao.insertCustomer(customer, connection);
            tableCustomers.setItems(CustomerViewDao.getAllCustomers(connection)); //Refresh table using results from a query to the database

            if (insert != 0)
                clearFields();

        } catch (SQLException E) {
            System.out.println(E.getMessage());
        }

        DBConnection.closeConnection();
    }
    /**
     * Attempts to UPDATE a customer record into the Customer Table on the Database using the fields on the current scene
     */
    private void updateCustomer() {
        Connection connection = DBConnection.openConnection();

        Customer customer = new Customer();

        customer.setCustomerID(Integer.valueOf(labelCustomerID.getText()));
        customer.setCustomerName(inputfieldCustomerName.getText());
        customer.setAddress(inputFieldAddress.getText());
        customer.setPostalCode(inputFieldPostal.getText());
        customer.setPhone(inputFieldPhone.getText());
        customer.setUpdatedBy(UserDao.loggedUser.getUserName());
        customer.setDivisionID(comboBoxState.getSelectionModel().getSelectedItem().getDivisionID());
        // Attempt to insert customer record into database
        try {
            int update = CustomerDao.modifyCustomer(customer, connection);
            tableCustomers.setItems(CustomerViewDao.getAllCustomers(connection)); //Refresh table using results from a query to the database

            if (update != 0)
                clearFields();

        } catch (SQLException E) {
            System.out.println(E.getMessage());
        }
        DBConnection.closeConnection();
    }

    /**
     * Updates all the fields in the scene using the data from the SELECTED customer record in the Table on the current scene
     */
    private void selectUpdate() {
        labelCustomerID.setText(String.valueOf(tableCustomers.getSelectionModel().getSelectedItem().getCustomerID()));
        inputfieldCustomerName.setText(tableCustomers.getSelectionModel().getSelectedItem().getCustomerName());
        inputFieldAddress.setText(tableCustomers.getSelectionModel().getSelectedItem().getAddress());;
        inputFieldPostal.setText(tableCustomers.getSelectionModel().getSelectedItem().getPostalCode());;
        inputFieldPhone.setText(tableCustomers.getSelectionModel().getSelectedItem().getPhone());

        for (Country country: comboBoxCountry.getItems()) {
            if(tableCustomers.getSelectionModel().getSelectedItem().getCountry().equals(country.getCountry())) {
                comboBoxCountry.setValue(country);
                break;
            }
        }
        for (FirstLevelDivision fld: comboBoxState.getItems()) {
            if (tableCustomers.getSelectionModel().getSelectedItem().getDivision().equals(fld.getDivision())) {
                comboBoxState.setValue(fld);
                break;
            }
        }

    }

    /**
     * Clears all the fields on the scene to blank
     * Used on the New Customer button
     */
    private void clearFields(){
        labelCustomerID.setText("Auto Generated");
        inputfieldCustomerName.clear();
        inputFieldAddress.clear();
        inputFieldPostal.clear();
        comboBoxCountry.getSelectionModel().clearSelection();
        comboBoxState.getSelectionModel().clearSelection();
        inputFieldPhone.clear();
    }

    /**
     * Init method for the scene.
     * Populates the Country and State combo boxes
     * Populates the TableCustomers table on the scene
     * Adds listeners to reset the STYLE on all fields if they are focused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = DBConnection.openConnection();

        //populate table and combo boxes
        try {
            states = FirstLevelDivisionDao.getAllDivisions(connection);

            comboBoxCountry.setItems(CountryDao.getAllCountries(connection));
            comboBoxState.setItems(states);

            tableCustomers.setItems(CustomerViewDao.getAllCustomers(connection));
        } catch (SQLException E) {
            System.out.println(E.getMessage());
        }

        tblColumnID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        tblColumnName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tblColumnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tblColumnPostal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        tblColumnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tblColumnCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        tblColumnStateProvince.setCellValueFactory(new PropertyValueFactory<>("division"));

        DBConnection.closeConnection();

        tableCustomers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectUpdate();
            }
        });
        // Automatically select the first item in the table
        tableCustomers.getSelectionModel().selectFirst();


        //Listeners to reset fields back to the default style after they are focused
        inputfieldCustomerName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                inputfieldCustomerName.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
            }
        });
        inputFieldAddress.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                inputFieldAddress.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
            }
        });
        comboBoxCountry.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                comboBoxCountry.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
            }
        });
        comboBoxState.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                comboBoxState.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
            }
        });
        inputFieldPostal.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                inputFieldPostal.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
            }
        });
        inputFieldPhone.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                inputFieldPhone.setStyle("-fx-border-color: Black; -fx-border-radius: 6");
            }
        });

    }
}
