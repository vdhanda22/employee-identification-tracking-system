package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 17/12/2018.
 */

public class VehicleServiceAlert {
    long id;
    float kms;
    String dueDate;
    float dueKms;
    String dueReason ="";
    float lastServiceKms;
    VehicleService dueService;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getKms() {
        return kms;
    }

    public void setKms(float kms) {
        this.kms = kms;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public float getDueKms() {
        return dueKms;
    }

    public void setDueKms(float dueKms) {
        this.dueKms = dueKms;
    }

    public String getDueReason() {
        return dueReason;
    }

    public void setDueReason(String dueReason) {
        this.dueReason = dueReason;
    }

    public float getLastServiceKms() {
        return lastServiceKms;
    }

    public void setLastServiceKms(float lastServiceKms) {
        this.lastServiceKms = lastServiceKms;
    }

    public VehicleService getDueService() {
        return dueService;
    }

    public void setDueService(VehicleService dueService) {
        this.dueService = dueService;
    }
}
