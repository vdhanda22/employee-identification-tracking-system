package com.karzansoft.fastvmi.extended;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Yasir on 3/2/2016.
 */
public class SquareMarker extends SymbolMarker {
    boolean isLarge;
    float anchor=0.4f;

    public SquareMarker(int x, int y, int mtype, int color, float scaleFactor,boolean isLarge)
    {
        if(!isLarge)
        {
            this.radious=(int)(12*scaleFactor);
            anchor=0.6f;
        }
        else
            this.radious=(int)(24*scaleFactor);

        this.startX = x-radious;
        this.startY = y-radious;
        this.markerType = mtype;
        this.scaleFactor=scaleFactor;

        symbolRect=new Rect(this.startX,this.startY,this.startX+radious,this.startY+radious);
        boundingRect=new Rect(this.startX-(int)(radious*anchor),this.startY-(int)(radious*anchor),this.startX+radious+(int)(radious*anchor),this.startY+radious+(int)(radious*anchor));

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth((int)(3*scaleFactor));
        mPaint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(symbolRect, mPaint);
        if (this.isSelected) {
            canvas.drawRect(boundingRect, paint);
        }
    }

    @Override
    public Rect getExpectedUpdatedRect(int deltaX, int deltaY) {
        Rect rect;

        rect = new Rect(this.startX + deltaX, this.startY + deltaY, this.startX + deltaX + radious, this.startY + deltaY + radious);

        return rect;
    }

    @Override
    public void updatePosition(int deltaX, int deltaY) {
        this.startX += deltaX;
        this.startY += deltaY;
        this.symbolRect.set(this.startX, this.startY, this.startX + radious, this.startY + radious);
        this.boundingRect.set(this.startX - (int) (radious * anchor), this.startY - (int) (radious * anchor), this.startX + radious + (int) (radious * anchor), this.startY + radious + (int) (radious * anchor));

    }
}
