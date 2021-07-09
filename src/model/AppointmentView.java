package model;

import java.time.LocalDateTime;
/**
 * AppointmentView class POJO for appointmentsview tableview in database.
 * Used for populating the Table in the Mainscreen of the application.
 * Used for displaying the Customer and Contact names instead of IDs.
 */
public class AppointmentView extends Appointment{
    private String customerName;
    private String contactName;

    public AppointmentView() {
    }

    public AppointmentView(Integer appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, String updatedBy, Integer customerID, Integer userID, Integer contactID, String customerName, String contactName) {
        super(appointmentID, title, description, location, type, start, end, updatedBy, customerID, userID, contactID);
        this.customerName = customerName;
        this.contactName = contactName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
