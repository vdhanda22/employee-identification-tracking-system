package com.karzansoft.fastvmi.Network.Entities.Request;

/**
 * Created by Yasir on 4/17/2016.
 */
public class ValidateOperationRequest {

    String fleetCode;
    String plateNo;
    String make;
    String model;
    int movementTypeId;
    int movementCategory;
    int operationType;
    int checkType;


    public String getFleetCode() {
        return fleetCode;
    }

    public void setFleetCode(String fleetCode) {
        this.fleetCode = fleetCode;
    }

    public int getMovementTypeId() {
        return movementTypeId;
    }

    public void setMovementTypeId(int movementTypeId) {
        this.movementTypeId = movementTypeId;
    }

    public int getMovementCategory() {
        return movementCategory;
    }

    public void setMovementCategory(int movementCategory) {
        this.movementCategory = movementCategory;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public int getCheckType() {
        return checkType;
    }

    public void setCheckType(int checkType) {
        this.checkType = checkType;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
