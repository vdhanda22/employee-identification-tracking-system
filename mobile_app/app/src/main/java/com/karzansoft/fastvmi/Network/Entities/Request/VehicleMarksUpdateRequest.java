package com.karzansoft.fastvmi.Network.Entities.Request;

import com.karzansoft.fastvmi.Models.MarkDetail;

import java.util.ArrayList;

/**
 * Created by Yasir on 3/14/2017.
 */
public class VehicleMarksUpdateRequest
{
    int vehicleId;
    ArrayList<MarkDetail> marks;


    public VehicleMarksUpdateRequest(int vehicleId, ArrayList<MarkDetail> marks) {
        this.vehicleId = vehicleId;
        this.marks = marks;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public ArrayList<MarkDetail> getMarks() {
        return marks;
    }

    public void setMarks(ArrayList<MarkDetail> marks) {
        this.marks = marks;
    }
}
