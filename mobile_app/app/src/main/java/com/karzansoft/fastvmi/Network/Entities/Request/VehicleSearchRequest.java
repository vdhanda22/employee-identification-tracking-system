package com.karzansoft.fastvmi.Network.Entities.Request;

import java.util.List;

/**
 * Created by Yasir on 4/14/2016.
 */
public class VehicleSearchRequest {
    String fleetCode;
    String keyword;
    String plateNo;
    String statusId;
    String source;
    List<Integer> subStatuses;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getFleetCode() {
        return fleetCode;
    }

    public void setFleetCode(String fleetCode) {
        this.fleetCode = fleetCode;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Integer> getSubStatuses() {
        return subStatuses;
    }

    public void setSubStatuses(List<Integer> subStatuses) {
        this.subStatuses = subStatuses;
    }
}
