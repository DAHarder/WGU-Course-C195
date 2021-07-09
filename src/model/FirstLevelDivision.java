package model;
/**
 * FirstLevelDivision class POJO for firstleveldivisions table in database
 */
public class FirstLevelDivision {
    private int divisionID;
    private String division;
    private int countryID;

    public FirstLevelDivision() {
    }

    public FirstLevelDivision(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }

    public FirstLevelDivision(String division, int countryID) {
        this.division = division;
        this.countryID = countryID;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    @Override
    public String toString(){
        return (division);
    }
}
