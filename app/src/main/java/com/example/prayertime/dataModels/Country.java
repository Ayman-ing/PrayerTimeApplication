package com.example.prayertime.dataModels;

public class Country {
    private String code;
    private String nameEn;
    private String nameAr;

    // Constructors, getters, and setters
    public Country() {}

    @Override
    public String toString() {
        return
                 nameEn
                 + " "  +nameAr
                ;
    }

    public Country(String code, String nameEn) {
        this.code = code;
        this.nameEn = nameEn;
    }

    public Country(String countryCode, String nameEN, String nameAR ) {
        code=countryCode;
        nameEn=nameEN;
        nameAr=nameAR;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameAr() {
        return nameAr;
    }
}

