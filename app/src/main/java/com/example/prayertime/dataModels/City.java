package com.example.prayertime.dataModels;

public class City {
    private int id;
    private String nameEn;
    private String nameAr;
    private String countryCode;
    private double latitude;
    private double longitude;
    private int altitude;

    // Constructors, getters, and setters
    public City() {}

    public City(int id, String nameEn, String nameAr, double latitude, double longitude, int altitude) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameAr = nameAr;
        this.countryCode = countryCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return nameEn  +" "  +
                nameAr ;
    }

    public City(int id, String nameEn, String countryCode) {
        this.id = id;
        this.nameEn = nameEn;
        this.countryCode = countryCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameEn() {
        return nameEn;
    }
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }



    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}

