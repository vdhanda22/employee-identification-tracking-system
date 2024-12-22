package com.karzansoft.fastvmi.Models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;

import com.karzansoft.fastvmi.Utils.Constant;

/**
 * Created by Yasir on 2/28/2016.
 */
public class Marker {
    public int x ,ex;
    public int y,ey;
    public int markerType;
    Bitmap bitmap,delbtm,dragbtm;
    public Rect rect;
    float anchor=0.5f;
    Paint mPaint;


    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    private boolean isSelected;

    public boolean isDeleteActive() {
        return isDeleteActive;
    }

    public void setIsDeleteActive(boolean isDeleteActive) {
        this.isDeleteActive = isDeleteActive;
    }

    private boolean isDeleteActive;

    public Marker(int x, int y, int mtype, Bitmap marketBitmap,Bitmap delbt,Bitmap drgbt) {
       int w=(int)(marketBitmap.getWidth()*anchor);
        int h=(int)(marketBitmap.getHeight()*anchor);
        this.x = x-w;
        this.y = y-h;

        this.markerType = mtype;
        this.bitmap = marketBitmap;
        this.delbtm=delbt;
        this.dragbtm=drgbt;
        rect=new Rect(this.x,this.y,this.x+this.bitmap.getWidth(),this.y+this.bitmap.getHeight());

        mPaint = new Paint(Paint.DITHER_FLAG);
        if (this.markerType == Constant.MARKER_SYMBOL_TICK)
            mPaint.setColorFilter(new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP));
        else if ((this.markerType == Constant.MARKER_SYMBOL_CROSS))
            mPaint.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP));

    }

    public void updatePosition(int deltaX,int deltaY)
    {
        this.x+=deltaX;
        this.y+=deltaY;
        this.rect.set(this.x,this.y,this.x+this.bitmap.getWidth(),this.y+this.bitmap.getHeight());
    }
    public  Rect getExpectedUpdatedRect(int deltaX,int deltaY)
    {
       if(this.bitmap!=null)
       {
           Rect rect=new Rect(this.x+deltaX,this.y+deltaY,this.x+deltaX+this.bitmap.getWidth(),this.y+deltaY+this.bitmap.getHeight());
           return rect;
       }else

        return null;
    }
    public void draw(Canvas canvas, Paint mBitmapPaint,Paint paint) {
        if (bitmap != null)
        {
            canvas.drawBitmap(bitmap, x, y, mPaint);
            if(this.isSelected) {
                canvas.drawRect(rect,paint);
                if(!this.isDeleteActive)
                {
                    int dx=rect.centerX()-(int)(dragbtm.getWidth()*anchor);
                    int dy=rect.centerY()-(int)(dragbtm.getHeight()*anchor);
                    canvas.drawBitmap(dragbtm,dx,dy,mBitmapPaint);
                }
            }
            if(this.isDeleteActive)
            {
                int delX=x-(int)(delbtm.getWidth()*anchor);
                int delY=y-(int)(delbtm.getHeight()*anchor);
                canvas.drawBitmap(delbtm,delX,delY,mBitmapPaint);
            }
        }
    }

}
