package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 3/30/2016.
 */
public class SymbolImageDetail {
    private String detail="";
    private String imgUrl;

    public SymbolImageDetail(String detail, String imgUrl) {
        this.detail = detail;
        this.imgUrl = imgUrl;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
