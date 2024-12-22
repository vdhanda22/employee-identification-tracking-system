package com.karzansoft.fastvmi.Network.Entities.Request;


import com.karzansoft.fastvmi.Models.Voucher;

/**
 * Created by Yasir on 20/12/2018.
 */

public class SavePurchaseOrderRequest {
    Voucher purchaseOrder;

    public SavePurchaseOrderRequest(Voucher purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Voucher getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(Voucher purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
}
