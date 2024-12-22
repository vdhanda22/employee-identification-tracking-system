package com.karzansoft.fastvmi.Network.Entities;

import java.util.ArrayList;

/**
 * Created by Yasir on 4/14/2016.
 */
public class ErrorField {
    String message;
    ArrayList<String> members;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }
}
