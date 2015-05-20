package com.sjsu.proj.accsensor;

/**
 * Created by Ashik on 4/15/2015.
 */
public class GPSData {
    private String stringLatitude;
    private String stringLongitude;
    private String sCountry;
    private String sCity;
    private String sPostalCode;
    private String sAddressLine;

    public String getStringLatitude() {
        return stringLatitude;
    }

    public void setStringLatitude(String stringLatitude) {
        this.stringLatitude = stringLatitude;
    }

    public String getStringLongitude() {
        return stringLongitude;
    }

    public void setStringLongitude(String stringLongitude) {
        this.stringLongitude = stringLongitude;
    }

    public String getsCountry() {
        return sCountry;
    }

    public void setsCountry(String sCountry) {
        this.sCountry = sCountry;
    }

    public String getsCity() {
        return sCity;
    }

    public void setsCity(String sCity) {
        this.sCity = sCity;
    }

    public String getsPostalCode() {
        return sPostalCode;
    }

    public void setsPostalCode(String sPostalCode) {
        this.sPostalCode = sPostalCode;
    }

    public String getsAddressLine() {
        return sAddressLine;
    }

    public void setsAddressLine(String sAddressLine) {
        this.sAddressLine = sAddressLine;
    }
}
