package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 17/12/2018.
 */

public class ServiceItem {

    int tenantId;
    int id;
    String name;

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
