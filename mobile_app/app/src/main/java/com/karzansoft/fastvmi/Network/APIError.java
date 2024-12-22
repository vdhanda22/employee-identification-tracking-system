package com.karzansoft.fastvmi.Network;

/**
 * Created by Yasir on 5/10/2016.
 */
public class APIError {
    private int statusCode;
    private String message;
    private String Message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
