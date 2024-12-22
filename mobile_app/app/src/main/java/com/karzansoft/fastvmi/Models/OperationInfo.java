package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 4/17/2016.
 */
public class OperationInfo {
    String dateTime="";
    long km;
   // String fuel="";
    String notes="";
   // String  locationId="";
   // String  departmentId="";
  //  GeoLocation geoLocation;
  //  String  driverId="";
    String customerSignature="";
    String staffSignature="";
    String operationType;
   // String vehicleStatus="";
   // float fuelPercentage;
    float fuelLevel;
    boolean mobile;

    public float getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(float fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getKm() {
        return km;
    }

    public void setKm(long km) {
        this.km = km;
    }

  /*  public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }*/

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

  /*  public String  getLocationId() {
        return locationId;
    }

    public void setLocationId(String  locationId) {
        this.locationId = locationId;
    }

    public String  getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String  departmentId) {
        this.departmentId = departmentId;
    }*/

  /*  public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }
*/
  /*  public String  getDriverId() {
        return driverId;
    }

    public void setDriverId(String  driverId) {
        this.driverId = driverId;
    }*/

    public String getCustomerSignature() {
        return customerSignature;
    }

    public void setCustomerSignature(String customerSignature) {
        this.customerSignature = customerSignature;
    }

    public String getStaffSignature() {
        return staffSignature;
    }

    public void setStaffSignature(String staffSignature) {
        this.staffSignature = staffSignature;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    /* public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }*/

   /* public float getFuelPercentage() {
        return fuelPercentage;
    }

    public void setFuelPercentage(float fuelPercentage) {
        this.fuelPercentage = fuelPercentage;
    }*/
}
