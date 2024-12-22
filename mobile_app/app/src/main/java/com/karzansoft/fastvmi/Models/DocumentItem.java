package com.karzansoft.fastvmi.Models;

/**
 * Created by Yasir on 2/15/2016.
 */
public class DocumentItem {
    private int sectionIndex;
    private String imagePath;
    private int itemIndex;

    public DocumentItem(int sectionId,String imagePath)
    {
        this.sectionIndex=sectionId;
        this.imagePath=imagePath;
    }

    public int getSectionIndex() {
        return sectionIndex;
    }

    public void setSectionIndex(int sectionIndex) {
        this.sectionIndex = sectionIndex;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }
}
