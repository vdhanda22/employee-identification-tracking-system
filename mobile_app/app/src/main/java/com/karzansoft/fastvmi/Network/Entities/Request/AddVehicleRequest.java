package com.karzansoft.fastvmi.Network.Entities.Request;

import com.karzansoft.fastvmi.Models.Vehicle;

/**
 * Created by Yasir on 7/21/2016.
 */
public class AddVehicleRequest {

    Vehicle vehicle;

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
