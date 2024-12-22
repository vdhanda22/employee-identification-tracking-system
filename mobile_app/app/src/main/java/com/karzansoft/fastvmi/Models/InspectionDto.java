package com.karzansoft.fastvmi.Models;

public class InspectionDto {
    long id, inspectionNo, vehicleId, signatureId;
    double kMs;
    String notes, geoLocationLongitude, geoLocationLatitude, safetyChecks;
    DocumentImage signature;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInspectionNo() {
        return inspectionNo;
    }

    public void setInspectionNo(long inspectionNo) {
        this.inspectionNo = inspectionNo;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public long getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(long signatureId) {
        this.signatureId = signatureId;
    }

    public double getkMs() {
        return kMs;
    }

    public void setkMs(double kMs) {
        this.kMs = kMs;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getGeoLocationLatitude() {
        return geoLocationLatitude;
    }

    public void setGeoLocationLatitude(String geoLocationLatitude) {
        this.geoLocationLatitude = geoLocationLatitude;
    }

    public String getGeoLocationLongitude() {
        return geoLocationLongitude;
    }

    public void setGeoLocationLongitude(String geoLocationLongitude) {
        this.geoLocationLongitude = geoLocationLongitude;
    }

    public DocumentImage getSignature() {
        return signature;
    }

    public void setSignature(DocumentImage signature) {
        this.signature = signature;
    }

    public String getSafetyChecks() {
        return safetyChecks;
    }

    public void setSafetyChecks(String safetyChecks) {
        this.safetyChecks = safetyChecks;
    }
}
