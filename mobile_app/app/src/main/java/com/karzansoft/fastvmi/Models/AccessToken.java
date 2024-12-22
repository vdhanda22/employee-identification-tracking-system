package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 5/15/2016.
 */
public class AccessToken {

    String baseUrl="";
    String token="";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
