package com.karzansoft.fastvmi.Network.Entities.Response;

import com.karzansoft.fastvmi.Models.AccessoryItem;
import com.karzansoft.fastvmi.Models.AppSettings;
import com.karzansoft.fastvmi.Models.Contact;
import com.karzansoft.fastvmi.Models.LanguageItem;
import com.karzansoft.fastvmi.Models.VehicleLocation;
import com.karzansoft.fastvmi.Models.VehicleStatus;

import java.util.ArrayList;

/**
 * Created by Yasir on 4/21/2016.
 */
public class SyncResponse {

    ArrayList<VehicleLocation> locations;
    ArrayList<AccessoryItem> checklistItems;
    ArrayList<Contact> staffMembers;
    ArrayList<VehicleStatus> vehicleStatus;
    AppSettings settings;
    ArrayList<LanguageItem> languages;

    public ArrayList<VehicleLocation> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<VehicleLocation> locations) {
        this.locations = locations;
    }

    public ArrayList<AccessoryItem> getChecklistItems() {
        return checklistItems;
    }

    public void setChecklistItems(ArrayList<AccessoryItem> checklistItems) {
        this.checklistItems = checklistItems;
    }

    public ArrayList<Contact> getStaffMembers() {
        return staffMembers;
    }

    public void setStaffMembers(ArrayList<Contact> staffMembers) {
        this.staffMembers = staffMembers;
    }

    public ArrayList<VehicleStatus> getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(ArrayList<VehicleStatus> vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public AppSettings getSettings() {
        return settings;
    }

    public void setSettings(AppSettings settings) {
        this.settings = settings;
    }

    public ArrayList<LanguageItem> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<LanguageItem> languages) {
        this.languages = languages;
    }
}
