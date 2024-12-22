package com.karzansoft.fastvmi.Models;

import java.util.ArrayList;

/**
 * Created by Yasir on 4/17/2016.
 */
public class MarkDetail {

    int vehicleId ;
    MarkType markType;
    String description ="";
    String vector ="";// json string
    int status=1 ;
    ArrayList<MarkImage> images;
    boolean isDeleted ;
    int deleterUserId ;
    String deletionTime ="";
    String lastModificationTime ="";
    int lastModifierUserId ;
    String creationTime="";
    int creatorUserId;
    int id;
    int markTypeId;

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public MarkType getMarkType() {
        return markType;
    }

    public void setMarkType(MarkType markType) {
        this.markType = markType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVector() {
        return vector;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<MarkImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<MarkImage> images) {
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(int creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public int getLastModifierUserId() {
        return lastModifierUserId;
    }

    public void setLastModifierUserId(int lastModifierUserId) {
        this.lastModifierUserId = lastModifierUserId;
    }

    public String getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(String lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

    public String getDeletionTime() {
        return deletionTime;
    }

    public void setDeletionTime(String deletionTime) {
        this.deletionTime = deletionTime;
    }

    public int getDeleterUserId() {
        return deleterUserId;
    }

    public void setDeleterUserId(int deleterUserId) {
        this.deleterUserId = deleterUserId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getMarkTypeId() {
        return markTypeId;
    }

    public void setMarkTypeId(int markTypeId) {
        this.markTypeId = markTypeId;
    }
}
