package com.karzansoft.fastvmi.Network.Entities.Response;

/**
 * Created by Yasir on 5/5/2016.
 */
public class LoginResponse {
    String result="";
    int staffId=-1;
    String userName;
    //    String tenant="";
    boolean rememberMe;
    Integer locationId;

    public String getToken() {
        return result;
    }

    public void setToken(String token) {
        this.result = token;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
/*

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
*/

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public Integer getLocationId() {
//        if (locationId==null)
//            return 0;
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }
}
