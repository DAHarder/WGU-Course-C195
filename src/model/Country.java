package model;

public class Country {

    private int countryID;
    private String country;
    /**
     * country class POJO for country table in database
     */
    public Country() {
    }

    public Country(String country) {
        this.country = country;
    }

    public Country(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString(){
        return (country);
    }
}
