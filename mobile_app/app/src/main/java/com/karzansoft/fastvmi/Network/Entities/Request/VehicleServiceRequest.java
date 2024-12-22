package com.karzansoft.fastvmi.Network.Entities.Request;

/**
 * Created by Yasir on 17/12/2018.
 */

public class VehicleServiceRequest {

    Long id;
    Long vehicleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
}
