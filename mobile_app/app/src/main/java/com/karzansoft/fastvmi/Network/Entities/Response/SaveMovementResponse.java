package com.karzansoft.fastvmi.Network.Entities.Response;

import java.util.ArrayList;

/**
 * Created by Yasir on 6/13/2016.
 */
public class SaveMovementResponse {
    boolean saved;
    boolean log;
    ArrayList<Long> movementIds;

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public boolean isLog() {
        return log;
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    public ArrayList<Long> getMovementIds() {
        return movementIds;
    }

    public void setMovementIds(ArrayList<Long> movementIds) {
        this.movementIds = movementIds;
    }
}
