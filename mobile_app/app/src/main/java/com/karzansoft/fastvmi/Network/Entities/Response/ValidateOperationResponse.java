package com.karzansoft.fastvmi.Network.Entities.Response;

import com.karzansoft.fastvmi.Models.Contact;
import com.karzansoft.fastvmi.Models.MovementInfo;
import com.karzansoft.fastvmi.Models.Vehicle;


/**
 * Created by Yasir on 4/17/2016.
 */
public class ValidateOperationResponse {
    MovementInfo lastMovement;
    Vehicle vehicle;
    String refNo ;
    Contact contact;
    String agreementNo;
    int permittedMovementTypeId ;
    int permittedCheckType ;
    boolean isPermitted;
    Contact driver;
    String reason="";

    public MovementInfo getLastMovement() {
        return lastMovement;
    }

    public void setLastMovement(MovementInfo lastMovement) {
        this.lastMovement = lastMovement;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public int getPermittedMovementTypeId() {
        return permittedMovementTypeId;
    }

    public void setPermittedMovementTypeId(int permittedMovementTypeId) {
        this.permittedMovementTypeId = permittedMovementTypeId;
    }

    public int getPermittedCheckType() {
        return permittedCheckType;
    }

    public void setPermittedCheckType(int permittedCheckType) {
        this.permittedCheckType = permittedCheckType;
    }

    public boolean isPermitted() {
        return isPermitted;
    }

    public void setIsPermitted(boolean isPermitted) {
        this.isPermitted = isPermitted;
    }

    public Contact getDriver() {
        return driver;
    }

    public void setDriver(Contact driver) {
        this.driver = driver;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAgreementNo() {
        return agreementNo;
    }

    public void setAgreementNo(String agreementNo) {
        this.agreementNo = agreementNo;
    }
}
