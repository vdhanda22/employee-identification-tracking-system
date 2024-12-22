package com.karzansoft.fastvmi.Models;

import java.util.ArrayList;

/**
 * Created by Yasir on 4/12/2016.
 */
public class Vehicle {

    String modelId="";
    String model="";
    String make="";
    int id;
    String registrationNo="";
    String fleetCode="";
    String plateCode="";
    String plateNo="";
    String modelYear="";
    String statusId = "";
    String locationId;
    String departmentId = "";
    String stateId="";
    String vehicleTypeId="";
    String state="";
    String vehicleType="";
    String status = "";
    String location;
    String department = "";
    TariffGroup tariffGroup;
    Long tariffGroupId;
    int ownership = 1;
    // float fuelPercentage;
    int kMs;
    float fuelLevel;
    ArrayList<MarkDetail> marks;

    public Vehicle(){}

    public Vehicle(String fleetNo, String regNo, String make, String model, String year) {
        this.fleetCode = fleetNo;
        this.registrationNo = regNo;
        this.modelYear = year;
    }

    public float getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(float fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public int  getId() {
        return id;
    }

    public void setId(int  vehicleId) {
        this.id = vehicleId;
    }

  /*  public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }*/

    public String getFleetCode() {
        return fleetCode;
    }

    public void setFleetCode(String fleetCode) {
        this.fleetCode = fleetCode;
    }

    public String getPlateCode() {
        return plateCode;
    }

    public void setPlateCode(String plateCode) {
        this.plateCode = plateCode;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String  getStatusId() {
        return statusId;
    }

    public void setStatusId(String  statusId) {
        this.statusId = statusId;
    }

    public String  getLocationId() {
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
    }

    public String  getStateId() {
        return stateId;
    }

    public void setStateId(String  stateId) {
        this.stateId = stateId;
    }

    public String  getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String  vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

   /* public float getFuelPercentage() {
        return fuelPercentage;
    }

    public void setFuelPercentage(float fuelPercentage) {
        this.fuelPercentage = fuelPercentage;
    }*/

    public int getkMs() {
        return kMs;
    }

    public void setkMs(int kMs) {
        this.kMs = kMs;
    }

    public ArrayList<MarkDetail> getMarks() {
        return marks;
    }

    public void setMarks(ArrayList<MarkDetail> marks) {
        this.marks = marks;
    }

    public TariffGroup getTariffGroup() {
        return tariffGroup;
    }

    public void setTariffGroup(TariffGroup tariffGroup) {
        this.tariffGroup = tariffGroup;
    }

    public Long getTariffGroupId() {
        return tariffGroupId;
    }

    public void setTariffGroupId(Long tariffGroupId) {
        this.tariffGroupId = tariffGroupId;
    }
}
