package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 4/21/2016.
 */
public class VehicleStatus {

    String name="";
    String id="";

    public VehicleStatus(){}
    public VehicleStatus(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
