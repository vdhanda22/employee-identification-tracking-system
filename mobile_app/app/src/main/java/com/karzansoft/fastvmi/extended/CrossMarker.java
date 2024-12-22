package com.karzansoft.fastvmi.extended;

import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Yasir on 5/1/2016.
 */
public class CrossMarker extends SymbolMarker {

    int boundingRectRadious;

    public CrossMarker(int x, int y, int mtype, int color, float scaleFactor)
    {

        this.startX = x;
        this.startY = y;
        this.markerType = mtype;
        this.scaleFactor=scaleFactor;
        this.color=color;
        this.radious=(int)(11*this.scaleFactor);
        this.boundingRectRadious=(int)(this.radious*1.6);
        this.isEditable=true;

        //Log.e("scale f",radious+", "+boundingRectRadious+" : "+(11*this.scaleFactor)+", "+(this.radious*1.6));

         boundingRect=new Rect(this.startX-this.boundingRectRadious,this.startY-this.boundingRectRadious,this.startX+this.boundingRectRadious,this.startY+this.boundingRectRadious);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth((int) (3 * this.scaleFactor));
        mPaint.setPathEffect(new CornerPathEffect((int) (2 * this.scaleFactor)));
        mPaint.setColor(this.color);
    }



    @Override
    public void updatePosition(int deltaX, int deltaY) {
        this.startX += deltaX;
        this.startY += deltaY;
        boundingRect=new Rect(this.startX-this.boundingRectRadious,this.startY-this.boundingRectRadious,this.startX+this.boundingRectRadious,this.startY+this.boundingRectRadious);

    }

    @Override
    public Rect getExpectedUpdatedRect(int deltaX, int deltaY) {
        Rect rect;

        rect = new Rect(this.startX + deltaX-radious, this.startY + deltaY-radious, this.startX + deltaX + radious, this.startY + deltaY + radious);

        return rect;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
       // canvas.drawCircle(startX,startY,radious,mPaint);
        canvas.drawLine(startX-radious,startY-radious,startX+radious,startY+radious,mPaint);
        canvas.drawLine(startX-radious,startY+radious,startX+radious,startY-radious,mPaint);
        if (this.isSelected) {
            canvas.drawRect(boundingRect, paint);
        }
    }
}
