package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 10/25/2018.
 */

public class TariffGroup {

    Integer id;
    int tenantId;
    String title;
    String exteriorDiagramName;
    String interiorDiagramName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExteriorDiagramName() {
        return exteriorDiagramName;
    }

    public void setExteriorDiagramName(String exteriorDiagramName) {
        this.exteriorDiagramName = exteriorDiagramName;
    }

    public String getInteriorDiagramName() {
        return interiorDiagramName;
    }

    public void setInteriorDiagramName(String interiorDiagramName) {
        this.interiorDiagramName = interiorDiagramName;
    }
}
