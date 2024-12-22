package com.karzansoft.fastvmi.Enums;

/**
 * Created by Yasir on 9/5/2016.
 */
public enum SearchVehicleType {
    NONE(1),AGREEMENT_OPEN(2),NRM_OPEN(3),AGREEMENT_CHECK_IN(4),WORKSHOP_OPEN(5),WORKSHOP_CLOSE(6),CUSTODY_OPEN(7),CUSTODY_CLOSE(8);

    private final int id;
    SearchVehicleType(int id) { this.id = id; }
    public int getValue() { return id; }
}
