package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 4/6/2016.
 */
public class AccessoryItem {
    private String name;
    private boolean status;
    private int id;
    private int checklistId;

    public AccessoryItem(){}
    public AccessoryItem(String name, boolean status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(int checklistId) {
        this.checklistId = checklistId;
    }
}
