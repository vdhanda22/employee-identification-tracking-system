package com.karzansoft.fastvmi.extended;

import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Utils.Util;

/**
 * Created by Yasir on 3/2/2016.
 */
public class LineMarker extends SymbolMarker {


    public int endX,endY;
    public Rect boundingRectEnd;
    public int selectedPoint;
   // Paint mPaint;




    public LineMarker(){super();}


   public LineMarker(int sX, int sY, int eX, int eY, int symboltype,int color, float scaleFactor)
   {
       this.startX = sX;
       this.startY = sY;
       this.endX=eX;
       this.endY=eY;
       this.scaleFactor=scaleFactor;
       this.markerType = symboltype;
       this.radious=(int)(11*scaleFactor);
       this.isEditable=true;
       this.color=color;

   /*    boundingRect=new Rect(this.startX-radious,this.startY-radious,this.startX+radious,this.startY+radious);
       boundingRectEnd=new Rect(this.endX-radious,this.endY-radious,this.endX+radious,this.endY+radious);
*/
       boundingRect=new Rect(this.startX-radious,this.startY-radious,this.startX+radious,this.startY+radious);
       boundingRectEnd=new Rect(this.endX-radious,this.endY-radious,this.endX+radious,this.endY+radious);


       mPaint = new Paint();
       mPaint.setStyle(Paint.Style.STROKE);
       mPaint.setAntiAlias(true);
       mPaint.setDither(true);
       mPaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
       mPaint.setStrokeCap(Paint.Cap.ROUND);

       if (markerType == Constant.SYMBOL_MARKER_SCRATCH_THIN)
           mPaint.setStrokeWidth((int) (2.5 * this.scaleFactor));
       else
           mPaint.setStrokeWidth((int) (7 * this.scaleFactor));
       mPaint.setPathEffect(new CornerPathEffect((int) (2 * this.scaleFactor)));
       mPaint.setColor(this.color);
       this.updateBoundingRect();
   }



    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawLine(startX,startY,endX,endY,mPaint);
        if(isSelected)
        {
           // if(this.selectedPoint==1)
            {
                canvas.drawRect(boundingRect,paint);
            }//else
            {
                canvas.drawRect(boundingRectEnd,paint);
            }
        }
    }

    @Override
    public Rect getExpectedUpdatedRect(int deltaX, int deltaY) {


        Rect rect=new Rect(1,1,1,1);

          if(selectedPoint==1)
            {
                rect=new  Rect(this.startX - radious+deltaX, this.startY - radious+deltaY, this.startX + radious+deltaX, this.startY + radious+deltaY);
            }else if(selectedPoint==2)
            {
                rect=new  Rect(this.endX - radious+deltaX, this.endY - radious+deltaY, this.endX + radious+deltaX, this.endY + radious+deltaY);

            }

        return rect;
    }

    @Override
    public void updatePosition(int deltaX, int deltaY) {

        if(selectedPoint==1)
        {
            this.startX += deltaX;
            this.startY += deltaY;
            boundingRect.set(this.startX - radious, this.startY - radious, this.startX + radious, this.startY + radious);
        }else if(selectedPoint==2)
        {
            this.endX += deltaX;
            this.endY += deltaY;
            boundingRectEnd.set(this.endX-radious,this.endY-radious,this.endX+radious,this.endY+radious);
        }
        else if(selectedPoint==3)/// For complete line selection
        {
            this.startX += deltaX;
            this.startY += deltaY;
            boundingRect.set(this.startX - radious, this.startY - radious, this.startX + radious, this.startY + radious);
            this.endX += deltaX;
            this.endY += deltaY;
            boundingRectEnd.set(this.endX-radious,this.endY-radious,this.endX+radious,this.endY+radious);

        }

        updateBoundingRect();
    }


    private void updateBoundingRect()
    {
        int sleft=0,stop=0,sright=0,sbottom=0,eleft=0,etop=0,eright=0,ebottom=0;

        float pointAngle = (float) Math.toDegrees(Math.atan2(endY - startY, endX - startX));
        int angle=(int)pointAngle;

        if(angle>=-45 && angle <=45)
        {
            sleft=this.startX-radious-radious/2;
            sright=this.startX+radious-radious/2;
            eleft=this.endX-radious+radious/2;
            eright=this.endX+radious+radious/2;

            stop=this.startY - radious;
            sbottom=this.startY + radious;
            etop=this.endY - radious;
            ebottom=this.endY + radious;

        }else if(angle>=-120&&angle<-45)
        {

            sleft=this.startX-radious;
            sright=this.startX+radious;
            eleft=this.endX-radious;
            eright=this.endX+radious;
            stop=this.startY - radious+radious/2;
            sbottom=this.startY + radious+radious/2;
            etop=this.endY - radious-radious/2;
            ebottom=this.endY + radious-radious/2;

        }else if((angle>=-180&&angle<-120)||(angle>120 && angle<=180))
        {

            sleft=this.startX-radious+radious/2;
            sright=this.startX+radious+radious/2;
            eleft=this.endX-radious-radious/2;
            eright=this.endX+radious-radious/2;
            stop=this.startY - radious;
            sbottom=this.startY + radious;
            etop=this.endY - radious;
            ebottom=this.endY + radious;



        }else if(angle>45 && angle<=120)
        {
            sleft=this.startX-radious;
            sright=this.startX+radious;
            eleft=this.endX-radious;
            eright=this.endX+radious;
            stop=this.startY - radious-radious/2;
            sbottom=this.startY + radious-radious/2;
            etop=this.endY - radious+radious/2;
            ebottom=this.endY + radious+radious/2;
        }





        boundingRect.set(sleft, stop, sright, sbottom);
        boundingRectEnd.set(eleft,etop,eright,ebottom);

       // float angle = (float) Math.toDegrees(Math.atan2(endY - startY, endX - startX));
      //  Log.e("Angle_b ",""+angle);



    }

    public boolean isPointonLine(int x,int y)
    {

        Point point1=new Point(this.boundingRect.centerX(),this.boundingRect.centerY());
        Point point2=new Point(this.boundingRectEnd.centerX(),this.boundingRectEnd.centerY());

        int distLine= Util.getDistancebetweenTwoPoints(point1.x, point1.y, point2.x, point2.y);
        int distP1TOP=Util.getDistancebetweenTwoPoints(point1.x,point1.y,x,y);
        int distPTOP2=Util.getDistancebetweenTwoPoints(x,y,point2.x,point2.y);
       // Log.e("Distance from Line", "st " + distLine + " p Dis " + distP1TOP + " : " + distPTOP2 + " td " + (distP1TOP + distPTOP2) +" "+(distLine+radious) +" "+boundingRect.width()+" "+radious);
        if((distP1TOP+distPTOP2)<=(distLine+radious))
            return true;

        return false;
    }


}
