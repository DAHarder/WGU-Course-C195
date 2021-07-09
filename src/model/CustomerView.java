package model;

public class CustomerView extends Customer{
    private String division;
    private String Country;
    /**
     * CustomerView class POJO for CustomerView tableview in database.
     * Used to populate the table in the Edit Customers scene.
     * Used to display the division and country by name instead of ID.
     */
    public CustomerView() {
    }

    public CustomerView(String division, String country) {
        this.division = division;
        Country = country;
    }

    public CustomerView(String customerName, String address, String postalCode, String phone, String createdBy, String updatedBy, Integer divisionID, String division, String country) {
        super(customerName, address, postalCode, phone, createdBy, updatedBy, divisionID);
        this.division = division;
        Country = country;
    }

    public CustomerView(Integer customerID, String customerName, String address, String postalCode, String phone, String createdBy, String updatedBy, Integer divisionID, String division, String country) {
        super(customerID, customerName, address, postalCode, phone, createdBy, updatedBy, divisionID);
        this.division = division;
        Country = country;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }
}
