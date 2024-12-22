package com.karzansoft.fastvmi.Models;

import java.util.List;

/**
 * Created by Yasir on 17/12/2018.
 */

public class VehicleService {

    int id;
    String name = "";
    float dueOnKMs;
    float dueOnMonths;
    String notes;
    int modelId;
    List<ServiceItem> serviceItems;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDueOnKMs() {
        return dueOnKMs;
    }

    public void setDueOnKMs(float dueOnKMs) {
        this.dueOnKMs = dueOnKMs;
    }

    public float getDueOnMonths() {
        return dueOnMonths;
    }

    public void setDueOnMonths(float dueOnMonths) {
        this.dueOnMonths = dueOnMonths;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public List<ServiceItem> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(List<ServiceItem> serviceItems) {
        this.serviceItems = serviceItems;
    }
}
