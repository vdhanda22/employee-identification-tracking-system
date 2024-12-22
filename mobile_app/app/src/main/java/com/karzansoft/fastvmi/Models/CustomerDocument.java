package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 8/22/2016.
 */
public class CustomerDocument {

    int identityDocumentType;
    String documentNo;
    // IssuedBy


    public int getIdentityDocumentType() {
        return identityDocumentType;
    }

    public void setIdentityDocumentType(int identityDocumentType) {
        this.identityDocumentType = identityDocumentType;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }
}

