package com.karzansoft.fastvmi.Network.Entities.Response;

import com.karzansoft.fastvmi.Models.AccessoryItem;

import java.util.ArrayList;

/**
 * Created by Yasir on 4/14/2016.
 */
public class AccessoriesResponse {
    ArrayList<AccessoryItem> items;

    public ArrayList<AccessoryItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<AccessoryItem> items) {
        this.items = items;
    }
}
