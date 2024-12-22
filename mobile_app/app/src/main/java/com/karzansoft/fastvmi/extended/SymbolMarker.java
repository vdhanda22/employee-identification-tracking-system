package com.karzansoft.fastvmi.extended;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Yasir on 3/1/2016.
 */
public abstract class SymbolMarker {
    public int startX ,startY;
    public int markerType,radious;
    public Rect boundingRect,symbolRect;
    public float scaleFactor;
    public Paint mPaint;
    public boolean isSelected;
    public String id;
    public int color;
    public boolean isEditable;

    // seriallize able fields



    //serializeable fields end

    public SymbolMarker() {}
    public boolean isEditable() {
        return isEditable;
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public boolean isSelected() {
        return isSelected;
    }
    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public abstract  void updatePosition(int deltaX,int deltaY);
    public abstract Rect getExpectedUpdatedRect(int deltaX,int deltaY);
    public abstract  void draw(Canvas canvas,Paint paint) ;

}
