package com.karzansoft.fastvmi.Network.Entities.Request;

/**
 * Created by Yasir on 8/24/2016.
 */
public class ContactSearchRequest {

    String keyword;
    Integer contactCategory;
    Integer contactType;
    Boolean activeOnly;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getContactCategory() {
        return contactCategory;
    }

    public void setContactCategory(Integer contactCategory) {
        this.contactCategory = contactCategory;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public Boolean getActiveOnly() {
        return activeOnly;
    }

    public void setActiveOnly(Boolean activeOnly) {
        this.activeOnly = activeOnly;
    }
}
