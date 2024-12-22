package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 11/4/2016.
 */
public class AppSettings {
    String currency = "";
    float taxPercent;
    boolean isTaxInclusive;
    boolean isTaxOptional;
    String termsAndConditions;
    boolean isLeftHandDriving;
    boolean canRemoveDamage;
    boolean isPoMandatoryForWorkshopMovement;
    boolean expectedReturnDate;
    boolean scsEnabled;
    boolean serviceEnabled;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(float taxPercent) {
        this.taxPercent = taxPercent;
    }

    public boolean isTaxInclusive() {
        return isTaxInclusive;
    }

    public void setTaxInclusive(boolean taxInclusive) {
        isTaxInclusive = taxInclusive;
    }

    public boolean isTaxOptional() {
        return isTaxOptional;
    }

    public void setTaxOptional(boolean taxOptional) {
        isTaxOptional = taxOptional;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public boolean isLeftHandDriving() {
        return isLeftHandDriving;
    }

    public void setLeftHandDriving(boolean leftHandDriving) {
        isLeftHandDriving = leftHandDriving;
    }

    public boolean isCanRemoveDamage() {
        return canRemoveDamage;
    }

    public void setCanRemoveDamage(boolean canRemoveDamage) {
        this.canRemoveDamage = canRemoveDamage;
    }

    public boolean isPoMandatoryForWorkshopMovement() {
        return isPoMandatoryForWorkshopMovement;
    }

    public void setPoMandatoryForWorkshopMovement(boolean poMandatoryForWorkshopMovement) {
        isPoMandatoryForWorkshopMovement = poMandatoryForWorkshopMovement;
    }

    public boolean isExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(boolean expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public boolean issCSEnabled() {
        return scsEnabled;
    }

    public void setsCSEnabled(boolean scsEnabled) {
        this.scsEnabled = scsEnabled;
    }

    public boolean isServiceEnabled() {
        return serviceEnabled;
    }

    public void setServiceEnabled(boolean serviceEnabled) {
        this.serviceEnabled = serviceEnabled;
    }
}
