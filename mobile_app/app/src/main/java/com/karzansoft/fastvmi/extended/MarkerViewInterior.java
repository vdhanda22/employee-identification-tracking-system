package com.karzansoft.fastvmi.extended;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.karzansoft.fastvmi.Models.AppSettings;
import com.karzansoft.fastvmi.Models.PreviousMarkerIno;
import com.karzansoft.fastvmi.Interfaces.SymbolStateListener;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Utils.Util;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Yasir on 3/30/2016.
 */
public class MarkerViewInterior extends View {

    private Bitmap mParentBitmap, mdelrect,mdel;
    private Paint mPaint,mBitmapPaint;
    private int mWidth,mHeight,mDesWidth,mDesHeight;
    private float imageAspectRation, widthScaleFactor,heightScaleFactor;
    private ArrayList<SymbolMarker> imageMarkers;
    private ArrayList<PreviousMarkerIno> checkoutMarksInfo;
    int lastX = 0;
    int lastY = 0;
    SymbolMarker selectedMarker;
    Rect boundingRect ,delBoundingRect;
    boolean isDeletionEnabled;
    private Context mContext;
    boolean isSymbolLoaded;
    boolean isAddonTouchEnable;
    int currentSymbolId;
    SymbolStateListener symbolStateListener;
    AppSettings appSettings;

    public SymbolStateListener getSymbolStateListener() {
        return symbolStateListener;
    }

    public void setSymbolStateListener(SymbolStateListener symbolStateListener) {
        this.symbolStateListener = symbolStateListener;
    }

    public SymbolMarker getSelectedMarker()
    {
        for (int i=0;i<imageMarkers.size();i++)
        {
            if(imageMarkers.get(i).isSelected())
                return  imageMarkers.get(i);
        }
        return null;
    }

    public  MarkerViewInterior(Context context) {
        super(context);
        this.mContext=context;
        intit();
    }

    public  MarkerViewInterior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        intit();
    }

    public int getScaledX(int x)
    {
        x-=boundingRect.left;
        return (int)(x/widthScaleFactor);
    }
    public int getScaledY(int y)
    {
        return (int)(y/heightScaleFactor);
    }

    private void intit()
    {
        appSettings= AppUtils.getSettings(mContext);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.GREEN);

        Bitmap bt;
        if(appSettings.isLeftHandDriving())
            bt= Util.getBitmapFromAsset(mContext.getAssets(), "car_interior_lefthand.jpg");
        else
            bt= Util.getBitmapFromAsset(mContext.getAssets(), "car_interior.jpg");

        mWidth=bt.getWidth();
        mHeight=bt.getHeight();
        imageAspectRation=mWidth /(float)mHeight;
        bt.recycle();

        imageMarkers=new ArrayList<SymbolMarker>();
        checkoutMarksInfo=new ArrayList<PreviousMarkerIno>();

        this.setOnTouchListener(touchListener);

    }

    private void loadSymbols()
    {
        isSymbolLoaded=true;

        if(checkoutMarksInfo==null)
            return;

        for (int i = 0; i < checkoutMarksInfo.size(); i++) {

            PreviousMarkerIno mrk = checkoutMarksInfo.get(i);

            if (mrk.getType() == Constant.SYMBOL_MARKER_LARGE_DENT) {
                CircleMarker dent = new CircleMarker(boundingRect.left + (int) (mrk.getX1() * widthScaleFactor), boundingRect.top + (int) (mrk.getY1() * heightScaleFactor), mrk.getType(), Color.BLUE, heightScaleFactor, true);
                dent.setIsEditable(false);
                dent.setId(mrk.getId());
                imageMarkers.add(dent);
            } else if (mrk.getType() == Constant.SYMBOL_MARKER_CRACK) {
                CrossMarker crack = new CrossMarker(boundingRect.left + (int) (mrk.getX1() * widthScaleFactor), boundingRect.top + (int) (mrk.getY1() * heightScaleFactor), mrk.getType(), Color.BLUE, heightScaleFactor);
                crack.setIsEditable(false);
                crack.setId(mrk.getId());
                imageMarkers.add(crack);
            }
        }

    }

    public void removeAllMarks()
    {
        checkoutMarksInfo.clear();
        imageMarkers.clear();
        isSymbolLoaded=false;
    }
    public void clearAlMarks()
    {
        imageMarkers.clear();
        invalidate();
    }

    public void addmark(int x,int y,int x2,int y2,int type,String id)
    {

        PreviousMarkerIno mrk=new PreviousMarkerIno(x,y,x2,y2,type,id);
        checkoutMarksInfo.add(mrk);

    }
    public void addSymbols(ArrayList<SymbolMarker> symbols)
    {

        if(imageMarkers!=null)
        {
            imageMarkers.addAll(symbols);
            invalidate();
        }
    }

    private void loadParentBitmap(int width,int height)
    {
        Bitmap bt;
        if(appSettings.isLeftHandDriving())
            bt= Util.getBitmapFromAsset(mContext.getAssets(), "car_interior_lefthand.jpg");
        else
            bt= Util.getBitmapFromAsset(mContext.getAssets(), "car_interior.jpg");

        mParentBitmap=Bitmap.createScaledBitmap(bt,width,height,true);
        int left=this.getWidth()/2-mParentBitmap.getWidth()/2;
        int top=this.getHeight()/2-mParentBitmap.getHeight()/2;
        boundingRect=new Rect(left,top,left+width,top+height);

    }

    public void updateBackgroundBitmap(Bitmap bitmap)
    {
        if (bitmap ==null || bitmap.isRecycled())
            return;

        mParentBitmap=Bitmap.createScaledBitmap(bitmap,mDesWidth,mDesHeight,true);
        if (boundingRect == null)
        {
            int left=this.getWidth()/2-mParentBitmap.getWidth()/2;
            int top=this.getHeight()/2-mParentBitmap.getHeight()/2;
            boundingRect=new Rect(left,top,left+mDesWidth,top+mDesHeight);
        }
        invalidate();

    }
    public void resetBackgroundBitmap()
    {
        Bitmap bt;
        if(appSettings.isLeftHandDriving())
            bt= Util.getBitmapFromAsset(mContext.getAssets(), "car_interior_lefthand.jpg");
        else
            bt= Util.getBitmapFromAsset(mContext.getAssets(), "car_interior.jpg");

        mParentBitmap=Bitmap.createScaledBitmap(bt,mDesWidth,mDesHeight,true);
        if (boundingRect == null)
        {
            int left=this.getWidth()/2-mParentBitmap.getWidth()/2;
            int top=this.getHeight()/2-mParentBitmap.getHeight()/2;
            boundingRect=new Rect(left,top,left+mDesWidth,top+mDesHeight);
        }
        invalidate();
    }

    public void clearSelection()
    {


        for (int i=0;i<imageMarkers.size();i++)
        {
            imageMarkers.get(i).setIsSelected(false);
        }
        invalidate();
    }


    public void addSymbolOntouch(int symbolType)
    {
        this.isAddonTouchEnable=true;
        this.currentSymbolId=symbolType;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if(mParentBitmap==null )
        {
            loadParentBitmap(mDesWidth,mDesHeight);
        }

        if(mParentBitmap!=null)
            canvas.drawBitmap(mParentBitmap,this.getWidth()/2-mParentBitmap.getWidth()/2,this.getHeight()/2-mParentBitmap.getHeight()/2,mBitmapPaint);

      /*  if(mdelrect==null)
        {
            int width=(int)(60*heightScaleFactor);
            int width2=(int)(20*heightScaleFactor);
            mdelrect=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_crop_din_black_48dp),width,width,true);
            mdel=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_delete_black_36dp),width2,width2,true);
            delBoundingRect=new Rect(0,0,mdelrect.getWidth(),mdelrect.getHeight());

        }

       if(mdelrect!=null)
           canvas.drawBitmap(mdelrect,0,0,mBitmapPaint);
        if(mdel!=null)
            canvas.drawBitmap(mdel,delBoundingRect.centerX()-mdel.getWidth()/2,delBoundingRect.centerY()-mdel.getHeight()/2,mBitmapPaint);
*/

        if(!isSymbolLoaded)
            loadSymbols();
        // canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        for (int i=0;i<imageMarkers.size();i++)
        {
            imageMarkers.get(i).draw(canvas,mPaint);
        }

        //canvas.drawRect(boundingRect,mPaint);



    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        int ww=(int)(h*imageAspectRation);

        widthScaleFactor=ww/(float)mWidth ;
        heightScaleFactor = h / (float) mHeight;
        mDesWidth=ww;
        mDesHeight=h;

        setMeasuredDimension(w, h);
       /* Log.e("Image res", mWidth + " : " + mHeight + " " + imageAspectRation);
        Log.e("View Res",ww+" : "+h);*/


    }

    private  SymbolMarker selectMarkerOntouch(int x,int y)
    {
        SymbolMarker marker=null;
        for (int i=imageMarkers.size()-1;i>-1;i--)
        {
            SymbolMarker current=imageMarkers.get(i);
            if (current.markerType == Constant.SYMBOL_MARKER_SCRATCH || current.markerType == Constant.SYMBOL_MARKER_SCRATCH_THIN) {
                LineMarker line=(LineMarker)current;
                if (line.boundingRect.contains(x, y) && !line.boundingRectEnd.contains(x, y)) {
                    line.selectedPoint = 1;
                    return line;
                } else if (line.boundingRectEnd.contains(x, y)&& !line.boundingRect.contains(x, y)) {
                    line.selectedPoint = 2;
                    return line;
                }else if(line.isPointonLine(x,y)) /// For complete line selection
                {
                    line.selectedPoint = 3;
                    return line;
                }

            } else {
                if (current.boundingRect.contains(x, y))
                    return current;
            }
        }
        return marker;
    }

    private OnTouchListener touchListener=new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() ) {

                case MotionEvent.ACTION_DOWN:

                    lastX=(int)event.getX();
                    lastY=(int)event.getY();

                    SymbolMarker symbolMarker=selectMarkerOntouch(lastX,lastY);
                    if(symbolMarker!=null)
                    {
                        clearSelection();
                        selectedMarker=symbolMarker;
                        symbolMarker.setIsSelected(true);
                        isAddonTouchEnable=false;
                        if(symbolStateListener!=null)
                            symbolStateListener.onSymbolSelected(selectedMarker);
                       /* if((Activity)mContext instanceof MarkerActivity)
                        {
                            ((MarkerActivity)mContext).resetSelection();
                        }else if((Activity)mContext instanceof VehicleMarksActivity)
                        {
                            ((VehicleMarksActivity)mContext).resetSelection();
                        }*/

                    }
                    else if(isAddonTouchEnable && boundingRect.contains(lastX,lastY)){
                        if(currentSymbolId==Constant.SYMBOL_MARKER_SMALL_DENT)
                        {
                            clearSelection();
                            CircleMarker dent=new CircleMarker(lastX,lastY,currentSymbolId, ContextCompat.getColor(mContext, R.color.colorPrimary),heightScaleFactor,false);
                            dent.setId(System.currentTimeMillis() + "");
                            dent.setIsSelected(true);
                            imageMarkers.add(dent);
                            if(symbolStateListener!=null)
                                symbolStateListener.onSymbolAdded(dent);
                        }
                        else   if(currentSymbolId==Constant.SYMBOL_MARKER_LARGE_DENT)
                        {
                            clearSelection();
                            CircleMarker dent=new CircleMarker(lastX,lastY,currentSymbolId,ContextCompat.getColor(mContext, R.color.colorPrimary),heightScaleFactor,true);
                            dent.setId(System.currentTimeMillis() + "");
                            dent.setIsSelected(true);
                            imageMarkers.add(dent);
                            if(symbolStateListener!=null)
                                symbolStateListener.onSymbolAdded(dent);

                        }
                       /* else  if(currentSymbolId==Constant.SYMBOL_MARKER_SCRATCH)
                        {
                            clearSelection();
                            LineMarker line=new LineMarker(lastX,lastY,lastX,lastY,currentSymbolId,Color.RED,heightScaleFactor);
                            line.setIsSelected(true);
                            line.selectedPoint=2;
                            selectedMarker=line;
                            line.setId(System.currentTimeMillis() + "");
                            imageMarkers.add(line);
                            if(symbolStateListener!=null)
                                symbolStateListener.onSymbolAdded(line);
                        }

                        else  if(currentSymbolId==Constant.SYMBOL_MARKER_SCRATCH_THIN)
                        {
                            clearSelection();
                            LineMarker line=new LineMarker(lastX,lastY,lastX,lastY,currentSymbolId,Color.RED,heightScaleFactor);
                            line.setIsSelected(true);
                            line.selectedPoint = 2;
                            selectedMarker=line;
                            line.setId(System.currentTimeMillis()+"");
                            imageMarkers.add(line);
                            if(symbolStateListener!=null)
                                symbolStateListener.onSymbolAdded(line);
                        }
*/
                        else if(currentSymbolId==Constant.SYMBOL_MARKER_CRACK)
                        {
                            clearSelection();
                            CrossMarker crack=new CrossMarker(lastX,lastY,currentSymbolId,ContextCompat.getColor(mContext, R.color.colorPrimary),heightScaleFactor);
                            crack.setId(System.currentTimeMillis()+"");
                            crack.setIsSelected(true);
                            imageMarkers.add(crack);
                            if(symbolStateListener!=null)
                                symbolStateListener.onSymbolAdded(crack);
                        }
                    }

                    break;

                case MotionEvent.ACTION_UP:
                    if(selectedMarker!=null)
                    {
                        //  selectedMarker.setIsSelected(false);

                        if(selectedMarker.markerType==Constant.SYMBOL_MARKER_SCRATCH || selectedMarker.markerType==Constant.SYMBOL_MARKER_SCRATCH_THIN)
                        {
                            LineMarker lineMarker=(LineMarker)selectedMarker;
                            if( Util.getDistancebetweenTwoPoints(lineMarker.startX,lineMarker.startY,lineMarker.endX,lineMarker.endY)< (int)(4*heightScaleFactor))
                                lineMarker.updatePosition((int)(10*heightScaleFactor),0);

                            if(!boundingRect.contains(lineMarker.startX,lineMarker.startY) || !boundingRect.contains(lineMarker.endX,lineMarker.endY))
                            {
                                imageMarkers.remove(selectedMarker);
                                if(symbolStateListener!=null)
                                    symbolStateListener.onRemoved(selectedMarker);
                            }

                            /*if(delBoundingRect.contains(lineMarker.boundingRect.centerX(),lineMarker.boundingRect.centerY())||delBoundingRect.contains(lineMarker.boundingRectEnd.centerX(),lineMarker.boundingRectEnd.centerY()))
                                imageMarkers.remove(selectedMarker);*/
                        }else {
                            if(!boundingRect.contains(selectedMarker.boundingRect.centerX(),selectedMarker.boundingRect.centerY()))
                            {
                                imageMarkers.remove(selectedMarker);
                                if(symbolStateListener!=null)
                                    symbolStateListener.onRemoved(selectedMarker);
                            }
                        }
                    }
                    selectedMarker=null;


                    break;

                case MotionEvent.ACTION_MOVE:
                    int currentX=(int)event.getX();
                    int currentY=(int)event.getY();
                    int deltaX=currentX-lastX;
                    int deltaY=currentY-lastY;

                    if(selectedMarker!=null && selectedMarker.isEditable)
                    {
                        //Rect expRect = selectedMarker.getExpectedUpdatedRect(deltaX, deltaY);
                        //if (expRect != null && boundingRect.contains(expRect))
                        {
                            selectedMarker.updatePosition(currentX - lastX, currentY - lastY);
                            lastX = currentX;
                            lastY = currentY;
                        }
                    }

                    break;
            }
            invalidate();
            return true;
        }
    };

    public Vector<String> getAllFixedMarksIds()
    {
        Vector<String> symbolMarkers=new Vector<String>();

        for (int i=0;i<imageMarkers.size();i++)
        {
            SymbolMarker symbolMarker=imageMarkers.get(i);
            if(!symbolMarker.isEditable())
                symbolMarkers.add(symbolMarker.getId());

        }

        return symbolMarkers;
    }

    public int getMarksCount()
    {
        return imageMarkers.size();
    }

    public void removeSelectedMarker()
    {
        SymbolMarker currentMarker=getSelectedMarker();
        if (currentMarker != null) {
            if (currentMarker.markerType == Constant.SYMBOL_MARKER_SCRATCH || currentMarker.markerType == Constant.SYMBOL_MARKER_SCRATCH_THIN) {
                LineMarker lineMarker = (LineMarker) currentMarker;

                imageMarkers.remove(currentMarker);
                if (symbolStateListener != null)
                    symbolStateListener.onRemoved(currentMarker);

            } else {
                imageMarkers.remove(currentMarker);
                if (symbolStateListener != null)
                    symbolStateListener.onRemoved(currentMarker);

            }
        }
        invalidate();
    }
}

