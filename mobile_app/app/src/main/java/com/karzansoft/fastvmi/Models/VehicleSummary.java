package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 10/22/2018.
 */

public class VehicleSummary {
    int hired;
    int onNrm;
    int workShop;
    int forSale;
    int totalLoss;
    int pendingCustomerDelivery;
    int pendingWorkshopDelivery;
    int notReady;
    int available;
    int readyToGo;
    int idle;
    int total;


    public int getHired() {
        return hired;
    }

    public void setHired(int hired) {
        this.hired = hired;
    }

    public int getOnNrm() {
        return onNrm;
    }

    public void setOnNrm(int onNrm) {
        this.onNrm = onNrm;
    }

    public int getWorkShop() {
        return workShop;
    }

    public void setWorkShop(int workShop) {
        this.workShop = workShop;
    }

    public int getForSale() {
        return forSale;
    }

    public void setForSale(int forSale) {
        this.forSale = forSale;
    }

    public int getTotalLoss() {
        return totalLoss;
    }

    public void setTotalLoss(int totalLoss) {
        this.totalLoss = totalLoss;
    }

    public int getPendingCustomerDelivery() {
        return pendingCustomerDelivery;
    }

    public void setPendingCustomerDelivery(int pendingCustomerDelivery) {
        this.pendingCustomerDelivery = pendingCustomerDelivery;
    }

    public int getPendingWorkshopDelivery() {
        return pendingWorkshopDelivery;
    }

    public void setPendingWorkshopDelivery(int pendingWorkshopDelivery) {
        this.pendingWorkshopDelivery = pendingWorkshopDelivery;
    }

    public int getNotReady() {
        return notReady;
    }

    public void setNotReady(int notReady) {
        this.notReady = notReady;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getReadyToGo() {
        return readyToGo;
    }

    public void setReadyToGo(int readyToGo) {
        this.readyToGo = readyToGo;
    }

    public int getIdle() {
        return idle;
    }

    public void setIdle(int idle) {
        this.idle = idle;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
