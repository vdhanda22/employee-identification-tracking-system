package com.karzansoft.fastvmi.Models;

import java.util.ArrayList;

/**
 * Created by Yasir on 3/13/2016.
 */
public class SymbolDetails {


    private int symbolId;
    private ArrayList<SymbolImageDetail> images;
    private String info="";

    public SymbolDetails(int symbolId, ArrayList<SymbolImageDetail> imagesDetail) {
        this.symbolId = symbolId;
        this.images=imagesDetail;

    }

    public SymbolDetails(int symbolId,String info, ArrayList<SymbolImageDetail> imagesDetail) {
        this.symbolId = symbolId;
        this.images=imagesDetail;
        this.info=info;

    }

    public SymbolDetails(int symbolId) {
        this.symbolId = symbolId;
        this.images=new ArrayList<SymbolImageDetail>();
        this.images.add(new SymbolImageDetail("",""));

    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(int symbolId) {
        this.symbolId = symbolId;
    }

    public ArrayList<SymbolImageDetail> getImages() {
        return images;
    }

    public void setImages(ArrayList<SymbolImageDetail> images) {
        this.images = images;
    }
}
