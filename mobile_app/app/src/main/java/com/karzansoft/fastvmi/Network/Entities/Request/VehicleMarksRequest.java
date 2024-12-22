package com.karzansoft.fastvmi.Network.Entities.Request;

/**
 * Created by Yasir on 3/13/2017.
 */
public class VehicleMarksRequest {

        int vehicleId;
            /*"fleetNo": 0,
            "plateNo": "string"*/
    public VehicleMarksRequest(int id)
    {
        this.vehicleId=id;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }
}
