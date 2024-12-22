package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 4/17/2016.
 */
public class MarkType {
    String name="";
    int surfaceTypeId;
    int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSurfaceTypeId() {
        return surfaceTypeId;
    }

    public void setSurfaceTypeId(int surfaceTypeId) {
        this.surfaceTypeId = surfaceTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
