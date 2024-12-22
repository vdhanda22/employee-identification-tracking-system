package com.karzansoft.fastvmi.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.karzansoft.fastvmi.Utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sajjad on 01/12/2016.
 */

public class CaptureSignatureView extends View {

    private Bitmap _Bitmap;
    private Canvas _Canvas;
    private Path _Path;
    private Paint _BitmapPaint;
    private Paint _paint;
    private float _mX;
    private float _mY;
    private float TouchTolerance = 4;
    private float LineThickness = 4;
    int _mWidth,_mHeight;
    float bitmapAspectRatio;
    private boolean isEpmty;


    private int canvasColor = Color.WHITE;
    private  int paintColor = Color.argb(255, 0, 0, 0);



    private boolean isEraser;
    Context context;
    public CaptureSignatureView(Context context, AttributeSet attr) {
        super(context, attr);

        this.context = context;
        LineThickness = Util.convertDpToPixel(3,context);

        _Path = new Path();
        _BitmapPaint = new Paint(Paint.DITHER_FLAG);
        _paint = new Paint();
        _paint.setAntiAlias(true);
        _paint.setDither(true);
        _paint.setColor(paintColor);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeJoin(Paint.Join.ROUND);
        _paint.setStrokeCap(Paint.Cap.ROUND);
        _paint.setStrokeWidth(LineThickness);
        this.isEpmty=true;

    }

    public boolean isEraser() {
        return isEraser;
    }

    public void setIsEraser(boolean isEraser) {
        this.isEraser = isEraser;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
       // Log.e("Size", w + " : " + h + "  " + oldw + " : " + oldh);
        _mWidth=w;
        _mHeight=h;

    }

    private void loadBitmap()
    {
        _Bitmap = Bitmap.createBitmap(_mWidth, (_mHeight > 0 ? _mHeight : ((View) this.getParent()).getHeight()), Bitmap.Config.ARGB_4444);
        _Canvas = new Canvas(_Bitmap);
        bitmapAspectRatio=_Bitmap.getWidth()/(float)_Bitmap.getHeight();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(_Bitmap==null)
            loadBitmap();
        canvas.drawColor(canvasColor);
        canvas.drawBitmap(_Bitmap, 0, 0, _BitmapPaint);
        canvas.drawPath(_Path, _paint);
    }

    private void TouchStart(float x, float y) {
        _Path.reset();
        _Path.moveTo(x, y);
        _mX = x;
        _mY = y;
    }

    private void TouchMove(float x, float y) {
        float dx = Math.abs(x - _mX);
        float dy = Math.abs(y - _mY);

        if (dx >= TouchTolerance || dy >= TouchTolerance) {
            _Path.quadTo(_mX, _mY, (x + _mX) / 2, (y + _mY) / 2);
            _mX = x;
            _mY = y;
            this.isEpmty=false;
        }
    }

    private void TouchUp() {
        if (!_Path.isEmpty()) {
            _Path.lineTo(_mX, _mY);
            _Canvas.drawPath(_Path, _paint);
        } else {
            _Canvas.drawPoint(_mX, _mY, _paint);
        }

        _Path.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(this.isEraser)
                {
                    _paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    LineThickness = Util.convertDpToPixel(25,context);
                    _paint.setStrokeWidth(LineThickness);
                }
                else
                {
                    _paint.setXfermode(null);
                    LineThickness = Util.convertDpToPixel(3,context);
                    _paint.setStrokeWidth(LineThickness);
                }
                TouchStart(x, y);
                invalidate();
                if(this.getParent()!=null && this.getParent().getParent()!=null &&this.getParent().getParent().getParent()!=null)
                    this.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                TouchMove(x, y);
                if(this.isEraser){
                    _Path.lineTo(x, y);
                    _Canvas.drawPath(_Path, _paint);
                    _Path.reset();
                    _Path.moveTo(x, y);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                TouchUp();
                invalidate();
                if(this.getParent()!=null && this.getParent().getParent()!=null &&this.getParent().getParent().getParent()!=null)
                    this.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        return true;
    }

    public void ClearCanvas() {
        _Canvas.drawColor(canvasColor);
        invalidate();
        isEpmty=true;
    }

    public String getBase64String()
    {
        int dh=(int)(320.0/bitmapAspectRatio);
        Bitmap temp=Bitmap.createScaledBitmap(_Bitmap,320,dh,true);
        return Util.encodeToBase64(temp, Bitmap.CompressFormat.PNG, 100);
    }



    public byte[] getBytes() {
        Bitmap b = getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public Bitmap getBitmap() {
        View v = (View) this.getParent();
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);

        return b;
    }

    public  void setCanvasColor(int __color)
    {
        canvasColor = __color;
    }

    public void setPaintColor(int __color) {
        paintColor = __color;
        _paint.setColor(paintColor);


    }

    public String saveBitmapOnDisk() {
        String path = "";

        try {
            File photoFile = Util.createImageFile();
            if (photoFile == null)
                return "";
            FileOutputStream fos = new FileOutputStream(photoFile);
            int dh = (int) (320.0 / bitmapAspectRatio);
            Bitmap temp = Bitmap.createScaledBitmap(_Bitmap, 320, dh, true);
            temp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            path = photoFile.getAbsolutePath();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }

    public boolean isEpmty() {
        return isEpmty;
    }

    public void setIsEpmty(boolean isEpmty) {
        this.isEpmty = isEpmty;
    }
}