package com.karzansoft.fastvmi.Network.Entities.Request;

import com.karzansoft.fastvmi.Models.MovementInfo;

import java.util.ArrayList;

/**
 * Created by Yasir on 4/28/2016.
 */
public class SaveMovementRequest {

    ArrayList<MovementInfo> movements;

    public ArrayList<MovementInfo> getMovements() {
        return movements;
    }

    public void setMovements(ArrayList<MovementInfo> movements) {
        this.movements = movements;
    }
}
