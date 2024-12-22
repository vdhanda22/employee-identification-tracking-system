package com.karzansoft.fastvmi.Network.Entities.Response;

public class GetTariffGroupResponse {
    String title;
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String Title) {
        this.title = Title;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return title;
    }
}
