package utilities;

import javafx.scene.control.*;

import java.time.LocalDateTime;

public class AlertMessages {
    /**
     * Used as databalidation within all the fields in the appointment add and modify scenes.
     * Further used to provide error alerts upon validation failures.
     * @param code integer used to pick a case.
     * @param field String used to edit the error message dynamically.
     */
        public static void dataValidationError (int code, String field){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error saving Item");
            alert.setHeaderText("Cannot save Item");
            switch (code) {
                case 1: {
                    alert.setContentText("The "+field+" Field cannot be empty and must be a valid input.");
                    break;
                }
                case 2: {
                    alert.setContentText("The Start Date must come before the End Date.");
                    break;
                }
                case 3: {
                    alert.setContentText("The Start Time must be within the main office's business hours (8am - 10pm EST) (08:00 - 22:00))");
                    break;
                }
                case 4: {
                    alert.setContentText("The set time overlaps with an existing appointment for that Customer");
                    break;
                }
                case 5: {
                    alert.setContentText("The End Time must be within the main office's business hours (8am - 10pm EST) (08:00 - 22:00))");
                    break;
                }
            }
            alert.showAndWait();
        }

    /**
     * highlights the field in which the datavalidation failed
     * @param field Textfield to change the style
     */
    public static void highlightErrorField (TextField field) {
        field.setStyle("-fx-border-color: red");
    }
    /**
     * highlights the field in which the datavalidation failed
     * @param field Textfield to change the style
     */
    public static void highlightErrorField (DatePicker field) {
        field.setStyle("-fx-border-color: red");
    }
    /**
     * highlights the field in which the datavalidation failed
     * @param field Textfield to change the style
     */
    public static void highlightErrorField (ComboBox field) {
        field.setStyle("-fx-border-color: red");
    }

    }