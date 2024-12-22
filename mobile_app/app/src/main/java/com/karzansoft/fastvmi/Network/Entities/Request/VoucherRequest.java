package com.karzansoft.fastvmi.Network.Entities.Request;

/**
 * Created by Yasir on 12/13/2018.
 */

public class VoucherRequest {
     String voucherNo;
     int type;
     long contactId;

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }
}
