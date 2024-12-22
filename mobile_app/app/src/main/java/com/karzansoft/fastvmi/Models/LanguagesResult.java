package com.karzansoft.fastvmi.Models;

import java.util.ArrayList;

/**
 * Created by Yasir on 11/28/2016.
 */
public class LanguagesResult {
    String defaultLanguageName="en";
    ArrayList<LanguageItem> items;

    public String getDefaultLanguageName() {
        return defaultLanguageName;
    }

    public void setDefaultLanguageName(String defaultLanguageName) {
        this.defaultLanguageName = defaultLanguageName;
    }

    public ArrayList<LanguageItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<LanguageItem> items) {
        this.items = items;
    }
}
