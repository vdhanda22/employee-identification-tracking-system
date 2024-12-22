package com.karzansoft.fastvmi.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yasir on 2/11/2016.
 */
public class Document {

    private String mTitle;
    private List<String> mImages;

    public Document()
    {
        mTitle="";
        mImages=new ArrayList<String>();
    }
    public Document(String documentTitle,List<String> images)
    {
        this.mTitle=documentTitle;
        this.mImages=images;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public List<String> getImages() {
        return mImages;
    }

    public void setImages(List<String> mImages) {
        this.mImages = mImages;
    }
}
