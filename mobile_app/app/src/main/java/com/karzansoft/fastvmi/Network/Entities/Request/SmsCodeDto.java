package com.karzansoft.fastvmi.Network.Entities.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SmsCodeDto {
    @SerializedName("Guid")
    @Expose
    private Object guid;
    @SerializedName("PhoneNumber")
    @Expose
    private Object phoneNumber;
    @SerializedName("IsCodeSent")
    @Expose
    private Boolean isCodeSent;
    @SerializedName("Code")
    @Expose
    private Object code;
    @SerializedName("CodeLength")
    @Expose
    private Integer codeLength;
    @SerializedName("NoOfTries")
    @Expose
    private Object noOfTries;
    @SerializedName("Status")
    @Expose
    private Object status;

    public void setGuid(Object guid) {
        this.guid = guid;
    }

    public void setPhoneNumber(Object phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getIsCodeSent() {
        return isCodeSent;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public Integer getCodeLength() {
        return codeLength;
    }

    public void setNoOfTries(Object noOfTries) {
        this.noOfTries = noOfTries;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }
}
