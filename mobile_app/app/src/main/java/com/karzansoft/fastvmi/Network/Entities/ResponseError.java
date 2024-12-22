package com.karzansoft.fastvmi.Network.Entities;

import java.util.ArrayList;

/**
 * Created by Yasir on 4/14/2016.
 */
public class ResponseError {
    int code;
    String message="";
    String details="";
    ArrayList<ErrorField> validationErrors;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ArrayList<ErrorField> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(ArrayList<ErrorField> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
