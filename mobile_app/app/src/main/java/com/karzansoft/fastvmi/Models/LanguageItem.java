package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 11/28/2016.
 */
public class LanguageItem {

    String name="";
    String displayName="";
    boolean isDefault;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
