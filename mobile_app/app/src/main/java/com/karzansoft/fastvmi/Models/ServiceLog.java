package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 17/12/2018.
 */

public class ServiceLog {

      int tenantId;
      int vehicleId;
      int serviceId;
      int workshopId;
      String dueDate;
      float dueKMs;
      String dueReason;
      String  date;
      float kMs;
      String notes;
      boolean isHistorical;
      Integer id;
      VehicleService service;


    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(int workshopId) {
        this.workshopId = workshopId;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public float getDueKMs() {
        return dueKMs;
    }

    public void setDueKMs(float dueKMs) {
        this.dueKMs = dueKMs;
    }

    public String getDueReason() {
        return dueReason;
    }

    public void setDueReason(String dueReason) {
        this.dueReason = dueReason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getkMs() {
        return kMs;
    }

    public void setkMs(float kMs) {
        this.kMs = kMs;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isHistorical() {
        return isHistorical;
    }

    public void setHistorical(boolean historical) {
        isHistorical = historical;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public VehicleService getService() {
        return service;
    }

    public void setService(VehicleService service) {
        this.service = service;
    }
}
