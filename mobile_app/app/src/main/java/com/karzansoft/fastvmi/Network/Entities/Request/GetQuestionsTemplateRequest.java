package com.karzansoft.fastvmi.Network.Entities.Request;

public class GetQuestionsTemplateRequest {
    long id;

    public GetQuestionsTemplateRequest(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
