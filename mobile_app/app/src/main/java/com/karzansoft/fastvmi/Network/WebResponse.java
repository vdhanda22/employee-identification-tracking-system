package com.karzansoft.fastvmi.Network;

import com.karzansoft.fastvmi.Network.Entities.ResponseError;

/**
 * Created by Yasir on 4/14/2016.
 */
public class WebResponse<T> {
    boolean success;
    T result;
    ResponseError error;
    boolean unAuthorizedRequest;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public ResponseError getError() {
        return error;
    }

    public void setError(ResponseError error) {
        this.error = error;
    }

    public boolean isUnAuthorizedRequest() {
        return unAuthorizedRequest;
    }

    public void setUnAuthorizedRequest(boolean unAuthorizedRequest) {
        this.unAuthorizedRequest = unAuthorizedRequest;
    }
}
