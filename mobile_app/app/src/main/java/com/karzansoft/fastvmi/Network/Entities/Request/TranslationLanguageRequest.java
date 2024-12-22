package com.karzansoft.fastvmi.Network.Entities.Request;

/**
 * Created by Yasir on 11/29/2018.
 */

public class TranslationLanguageRequest {
    String cultureName = "en";
    String appName="vmirental";

    public TranslationLanguageRequest(String cultureName) {
        this.cultureName = cultureName;
    }

    public String getCultureName() {
        return cultureName;
    }

    public void setCultureName(String cultureName) {
        this.cultureName = cultureName;
    }
}
