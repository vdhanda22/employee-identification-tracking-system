package com.karzansoft.fastvmi.Network.Entities.Request;

import com.karzansoft.fastvmi.Models.MovementInfo;

/**
 * Created by Yasir on 11/20/2018.
 */

public class WorkshopMovementRequest {

    MovementInfo workshopMovement;

    public WorkshopMovementRequest(MovementInfo workshopMovement) {
        this.workshopMovement = workshopMovement;
    }

    public MovementInfo getWorkshopMovement() {
        return workshopMovement;
    }

    public void setWorkshopMovement(MovementInfo workshopMovement) {
        this.workshopMovement = workshopMovement;
    }
}
