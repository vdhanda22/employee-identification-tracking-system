package com.karzansoft.fastvmi.Models;

import java.util.List;

/**
 * Created by Yasir on 12/13/2018.
 */

public class Voucher {
    String voucherNo ="";
    long id;
    String date;
    String supplierBillNo;
    long contactId;
    int locationId;
    float amount;
    float tax;
    float taxPercent;
    String refNo;
    List<VoucherLine> voucherLines;


    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSupplierBillNo() {
        return supplierBillNo;
    }

    public void setSupplierBillNo(String supplierBillNo) {
        this.supplierBillNo = supplierBillNo;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(float taxPercent) {
        this.taxPercent = taxPercent;
    }

    public List<VoucherLine> getVoucherLines() {
        return voucherLines;
    }

    public void setVoucherLines(List<VoucherLine> voucherLines) {
        this.voucherLines = voucherLines;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }
}
