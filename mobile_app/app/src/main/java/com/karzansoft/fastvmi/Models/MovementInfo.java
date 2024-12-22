package com.karzansoft.fastvmi.Models;

import java.util.ArrayList;

/**
 * Created by Yasir on 4/17/2016.
 */
public class MovementInfo {
    int id;
    int movementTypeId;
    Integer movementSubType;
    String refNo="";
    String parentRefNo="";
    String contactId="";
    int vehicleId;
    String agreementNo = "";

    OperationInfo outDetail;
    OperationInfo inDetail;
    ArrayList<AccessoryItem> outCheckList;
    ArrayList<MarkDetail>  outMarks;
    ArrayList<AccessoryItem> inCheckList;
    ArrayList<MarkDetail> inMarks;
    boolean isComplete;

    String outDetail_VehicleStatusId="";
    int outDetail_LocationId;
    Integer outDetail_DepartmentId;
    GeoLocation outDetail_GeoLocation;
    Integer outDetail_DriverId ;
    String inDetail_VehicleStatusId ="";
    int inDetail_LocationId ;
    Integer inDetail_DepartmentId;
    GeoLocation inDetail_GeoLocation;
    Integer inDetail_DriverId;
    ArrayList<DocumentImage> documents;
    boolean isCRS;
    Contact localDriver;
    Vehicle vehicle;
    Contact outDetail_Driver;
    Contact inDetail_Driver;
    ServiceLog serviceLog;

    public String getOutDetail_VehicleStatusId() {
        return outDetail_VehicleStatusId;
    }

    public void setOutDetail_VehicleStatusId(String outDetail_VehicleStatusId) {
        this.outDetail_VehicleStatusId = outDetail_VehicleStatusId;
    }

    public int getOutDetail_LocationId() {
        return outDetail_LocationId;
    }

    public void setOutDetail_LocationId(int outDetail_LocationId) {
        this.outDetail_LocationId = outDetail_LocationId;
    }

    public int getOutDetail_DepartmentId() {
        return outDetail_DepartmentId;
    }

    public void setOutDetail_DepartmentId(int outDetail_DepartmentId) {
        this.outDetail_DepartmentId = outDetail_DepartmentId;
    }

    public GeoLocation getOutDetail_GeoLocation() {
        return outDetail_GeoLocation;
    }

    public void setOutDetail_GeoLocation(GeoLocation outDetail_GeoLocation) {
        this.outDetail_GeoLocation = outDetail_GeoLocation;
    }

    public int getOutDetail_DriverId() {
        if(outDetail_DriverId==null)
            return 0;
        return outDetail_DriverId;
    }

    public void setOutDetail_DriverId(int outDetail_DriverId) {
        this.outDetail_DriverId = outDetail_DriverId;
    }

    public String getInDetail_VehicleStatusId() {
        return inDetail_VehicleStatusId;
    }

    public void setInDetail_VehicleStatusId(String inDetail_VehicleStatusId) {
        this.inDetail_VehicleStatusId = inDetail_VehicleStatusId;
    }

    public int getInDetail_LocationId() {
        return inDetail_LocationId;
    }

    public void setInDetail_LocationId(int inDetail_LocationId) {
        this.inDetail_LocationId = inDetail_LocationId;
    }

    public int getInDetail_DepartmentId() {
        return inDetail_DepartmentId;
    }

    public void setInDetail_DepartmentId(int inDetail_DepartmentId) {
        this.inDetail_DepartmentId = inDetail_DepartmentId;
    }

    public GeoLocation getInDetail_GeoLocation() {
        return inDetail_GeoLocation;
    }

    public void setInDetail_GeoLocation(GeoLocation inDetail_GeoLocation) {
        this.inDetail_GeoLocation = inDetail_GeoLocation;
    }

    public int getInDetail_DriverId() {
        if (inDetail_DriverId ==null)
            return 0;
        return inDetail_DriverId;
    }

    public void setInDetail_DriverId(int inDetail_DriverId) {
        this.inDetail_DriverId = inDetail_DriverId;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    Contact contact;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
/* public int getMovementId() {
        return movementId;
    }

    public void setMovementId(int movementId) {
        this.movementId = movementId;
    }*/

    public int getMovementTypeId() {
        return movementTypeId;
    }

    public void setMovementTypeId(int movementTypeId) {
        this.movementTypeId = movementTypeId;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getParentRefNo() {
        return parentRefNo;
    }

    public void setParentRefNo(String parentRefNo) {
        this.parentRefNo = parentRefNo;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public OperationInfo getOutDetail() {
        return outDetail;
    }

    public void setOutDetail(OperationInfo outDetail) {
        this.outDetail = outDetail;
    }

    public OperationInfo getInDetail() {
        return inDetail;
    }

    public void setInDetail(OperationInfo inDetail) {
        this.inDetail = inDetail;
    }

    public ArrayList<AccessoryItem> getOutCheckList() {
        return outCheckList;
    }

    public void setOutCheckList(ArrayList<AccessoryItem> outCheckList) {
        this.outCheckList = outCheckList;
    }

    public ArrayList<AccessoryItem> getInCheckList() {
        return inCheckList;
    }

    public void setInCheckList(ArrayList<AccessoryItem> inCheckList) {
        this.inCheckList = inCheckList;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public ArrayList<MarkDetail> getOutMarks() {
        return outMarks;
    }

    public void setOutMarks(ArrayList<MarkDetail> outMarks) {
        this.outMarks = outMarks;
    }

    public ArrayList<MarkDetail> getInMarks() {
        return inMarks;
    }

    public void setInMarks(ArrayList<MarkDetail> inMarks) {
        this.inMarks = inMarks;
    }

    public ArrayList<DocumentImage> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<DocumentImage> documents) {
        this.documents = documents;
    }

    public boolean isCRS() {
        return isCRS;
    }

    public void setCRS(boolean CRS) {
        isCRS = CRS;
    }

    public Contact getLocalDriver() {
        return localDriver;
    }

    public void setLocalDriver(Contact localDriver) {
        this.localDriver = localDriver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Integer getMovementSubType() {
        return movementSubType;
    }

    public void setMovementSubType(Integer movementSubType) {
        this.movementSubType = movementSubType;
    }

    public Contact getOutDetail_Driver() {
        return outDetail_Driver;
    }

    public void setOutDetail_Driver(Contact outDetail_Driver) {
        this.outDetail_Driver = outDetail_Driver;
    }

    public Contact getInDetail_Driver() {
        return inDetail_Driver;
    }

    public void setInDetail_Driver(Contact inDetail_Driver) {
        this.inDetail_Driver = inDetail_Driver;
    }

    public String getAgreementNo() {
        return agreementNo;
    }

    public void setAgreementNo(String agreementNo) {
        this.agreementNo = agreementNo;
    }

    public ServiceLog getServiceLog() {
        return serviceLog;
    }

    public void setServiceLog(ServiceLog serviceLog) {
        this.serviceLog = serviceLog;
    }
}
