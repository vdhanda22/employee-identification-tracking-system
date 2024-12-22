package com.karzansoft.fastvmi.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yasir on 3/1/2017.
 */
public class CountryResult {

    List<Country> countryList;

    private CountryResult() {
        countryList = new ArrayList<Country>();
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }
}
