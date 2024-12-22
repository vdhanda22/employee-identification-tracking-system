package com.karzansoft.fastvmi.extended;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Yasir on 3/2/2016.
 */
public class CircleMarker extends SymbolMarker {
    public boolean isLarge;
    public float anchor=1.4f;// used in calculating distance between marker edge to its bounding box

    public CircleMarker()
    {
        super();
    }
    public CircleMarker(int x, int y, int mtype, int color, float scaleFactor,boolean isLarge)
    {

        this.startX = x;
        this.startY = y;
        this.markerType = mtype;
        this.scaleFactor=scaleFactor;
        this.color=color;
        this.isLarge=isLarge;
        if(!this.isLarge)
        {
            this.radious=(int)(6.5*this.scaleFactor);
            this.anchor=2.2f;
        }
        else
            this.radious=(int)(14*this.scaleFactor);
        this.isEditable=true;



      //  symbolRect=new Rect(this.startX,this.startY,this.startX+radious,this.startY+radious);
        boundingRect=new Rect(this.startX-(int)(radious*anchor),this.startY-(int)(radious*anchor),this.startX+(int)(radious*anchor),this.startY+(int)(radious*anchor));

        mPaint = new Paint();
        if(this.isLarge)
            mPaint.setStyle(Paint.Style.FILL);
        else
            mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth((int)(3*this.scaleFactor));
        mPaint.setColor(this.color);
    }

    @Override
    public void updatePosition(int deltaX, int deltaY) {
        this.startX += deltaX;
        this.startY += deltaY;
        this.boundingRect.set(this.startX - (int) (radious * anchor), this.startY - (int) (radious * anchor), this.startX + (int) (radious * anchor), this.startY  + (int) (radious * anchor));

    }

    @Override
    public Rect getExpectedUpdatedRect(int deltaX, int deltaY) {
        Rect rect;

        rect = new Rect(this.startX + deltaX-radious, this.startY + deltaY-radious, this.startX + deltaX + radious, this.startY + deltaY + radious);

        return rect;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        canvas.drawCircle(startX,startY,radious,mPaint);
        if (this.isSelected) {
            canvas.drawRect(boundingRect, paint);
        }

    }
}
