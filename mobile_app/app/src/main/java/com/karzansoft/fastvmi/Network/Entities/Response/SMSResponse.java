package com.karzansoft.fastvmi.Network.Entities.Response;

/**
 * Created by Yasir on 11/13/2018.
 */

public class SMSResponse {

    String error_code;
    String status;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
