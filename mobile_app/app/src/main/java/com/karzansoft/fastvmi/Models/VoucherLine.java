package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 19/12/2018.
 */

public class VoucherLine {
    Long expenseTypeId;
    String description;
    float debit;
    float credit;
    long vehicleId;
    float tax;
    Vehicle vehicle;
    Float totalAmount;
    Boolean isNew;

    public Long getExpenseTypeId() {
        return expenseTypeId;
    }

    public void setExpenseTypeId(Long expenseTypeId) {
        this.expenseTypeId = expenseTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getDebit() {
        return debit;
    }

    public void setDebit(float debit) {
        this.debit = debit;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Float getTotalAmount() {
        if (totalAmount ==null)
            return 0.0f;
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Boolean getNew() {
        if(isNew ==null)
            return false;
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }
}
