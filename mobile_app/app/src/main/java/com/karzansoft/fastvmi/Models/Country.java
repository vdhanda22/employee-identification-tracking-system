package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 3/1/2017.
 */
public class Country {
    private int id;
    private String country_name;
    private String country_code;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCountry_name() {
        return country_name;
    }
    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
    public String getCountry_code() {
        return country_code;
    }
    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

}

