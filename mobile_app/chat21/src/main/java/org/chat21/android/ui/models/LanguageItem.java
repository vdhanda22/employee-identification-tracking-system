package org.chat21.android.ui.models;

/**
 * Created by Yasir on 9/27/2018.
 */

public class LanguageItem {

     String name = "English";
     String nativeLanguage = "English";
     String code = "en";


    public LanguageItem(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
