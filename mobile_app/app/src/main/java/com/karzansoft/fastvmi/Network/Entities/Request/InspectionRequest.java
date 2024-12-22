package com.karzansoft.fastvmi.Network.Entities.Request;

import com.karzansoft.fastvmi.Models.InspectionDto;

public class InspectionRequest {
    InspectionDto inspection;

    public InspectionDto getInspection() {
        return inspection;
    }

    public void setInspection(InspectionDto inspection) {
        this.inspection = inspection;
    }

}
