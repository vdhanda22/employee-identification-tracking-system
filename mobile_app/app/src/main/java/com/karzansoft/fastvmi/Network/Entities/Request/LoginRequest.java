package com.karzansoft.fastvmi.Network.Entities.Request;

/**
 * Created by Yasir on 4/14/2016.
 */
public class LoginRequest {

    String tenancyName;
    String usernameOrEmailAddress;
    String password;

    public String getTenancyName() {
        return tenancyName;
    }

    public void setTenancyName(String tenancyName) {
        this.tenancyName = tenancyName;
    }

    public String getUsernameOrEmailAddress() {
        return usernameOrEmailAddress;
    }

    public void setUsernameOrEmailAddress(String usernameOrEmailAddress) {
        this.usernameOrEmailAddress = usernameOrEmailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
