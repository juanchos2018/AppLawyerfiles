package com.document.lawyerfiles.Clases;

public class ClsCountries {

    String Country;
    String NewConfirmed;
    String TotalConfirmed;
    String NewDeaths;
    String TotalDeaths;
    String NewRecovered;
    String TotalRecovered;
    String Date;


    public ClsCountries(){

    }
    public ClsCountries(String country, String newConfirmed) {
        Country = country;
        NewConfirmed = newConfirmed;
    }



    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getNewConfirmed() {
        return NewConfirmed;
    }

    public void setNewConfirmed(String newConfirmed) {
        NewConfirmed = newConfirmed;
    }

    public String getTotalConfirmed() {
        return TotalConfirmed;
    }

    public void setTotalConfirmed(String totalConfirmed) {
        TotalConfirmed = totalConfirmed;
    }

    public String getNewDeaths() {
        return NewDeaths;
    }

    public void setNewDeaths(String newDeaths) {
        NewDeaths = newDeaths;
    }

    public String getTotalDeaths() {
        return TotalDeaths;
    }

    public void setTotalDeaths(String totalDeaths) {
        TotalDeaths = totalDeaths;
    }

    public String getNewRecovered() {
        return NewRecovered;
    }

    public void setNewRecovered(String newRecovered) {
        NewRecovered = newRecovered;
    }

    public String getTotalRecovered() {
        return TotalRecovered;
    }

    public void setTotalRecovered(String totalRecovered) {
        TotalRecovered = totalRecovered;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
