package com.karzansoft.fastvmi.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.karzansoft.fastvmi.BuildConfig;
import com.karzansoft.fastvmi.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Yasir on 1/26/16.
 */
public class Util {

    static boolean IS_LOG_ENABLE= BuildConfig.DEBUG;

    public static void LogE(String tag,String message)
    {
        if(IS_LOG_ENABLE)
        Log.e(tag,message);
    }
    public static void LogD(String tag,String message)
    {
        if(IS_LOG_ENABLE)
            Log.d(tag, message);
    }
    public static void LogI(String tag,String message)
    {
        if(IS_LOG_ENABLE)
            Log.i(tag, message);
    }
    public static void LogV(String tag,String message)
    {
        if(IS_LOG_ENABLE)
            Log.v(tag, message);
    }

    public static Bitmap getScaledBitmap(int w,int h,String mCurrentPhotoPath) {
        // Get the dimensions of the View
        int targetW = w;
        int targetH = h;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

      /*  // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
*/
        int inSampleSize = 1;
        if (photoH > targetH || photoW > targetW) {

            final int heightRatio = Math.round((float) photoH / (float) targetH);
            final int widthRatio = Math.round((float) photoW / (float) targetW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = inSampleSize;
        bmOptions.inPurgeable = true;


        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        bitmap=rotateImage(bitmap,getImageAngle(mCurrentPhotoPath));
        return bitmap;
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static Bitmap getScaledBitmapToFitH(int w,int h,String mCurrentPhotoPath) {
        // Get the dimensions of the View
        int targetW = w;
        int targetH = h;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

      /*  // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
*/
        int inSampleSize = 1;
        if (photoH > targetH || photoW > targetW) {

            final int heightRatio = Math.round((float) photoH / (float) targetH);
            final int widthRatio = Math.round((float) photoW / (float) targetW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = inSampleSize;
        bmOptions.inPurgeable = true;

        Bitmap temp=BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int resultH ,resultW;
        if(temp.getHeight()>temp.getWidth())
        {
            resultW=(int)(targetH*((float)temp.getWidth()/(float)temp.getHeight()));
            resultH=targetH;
        }else {
            resultH=(int)(targetW*((float)temp.getHeight()/(float)temp.getWidth()));
            resultW=targetW;
        }
        Log.e("Request",""+resultW + ""+resultH);
        Bitmap bitmap = Bitmap.createScaledBitmap(temp,resultW,resultH,true);
        temp.recycle();
        System.gc();
        bitmap=rotateImage(bitmap,getImageAngle(mCurrentPhotoPath));
        return bitmap;
    }

    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int)px;
    }

    public static void addPicToPhoneGallery(Activity activity,String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
    }

    public static File createImageFile() throws IOException {
        // Create an image file name
        File image=null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            storageDir.mkdirs();
            image=new File(storageDir,imageFileName+".jpg");
          /*  image = File.createTempFile(
                    imageFileName,  *//* prefix *//*
                    ".jpg",         *//* suffix *//*
                    storageDir      *//* directory *//*
            );*/

            // Save a file: path for use with ACTION_VIEW intents
            // mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        }

        return image;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    public  static int getImageAngle(String photoPath)
    {
        int orientation=90;
        try{

        ExifInterface ei = new ExifInterface(photoPath);
         orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                orientation=90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                orientation=180;
                break;
            // etc.
        }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return orientation;
    }

    public static int byteSizeOf(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }
    public static boolean copyAssets(Activity activity)
    {
        AssetManager assetManager = activity.getAssets();
        InputStream in = null;
        OutputStream out = null;
        String filename = "mrz.traineddata";
        File desFile=new File(activity.getFilesDir()+"/tesseract/tessdata/");
        desFile.mkdirs();
        File ouputFile=new File(desFile,filename);
        if(ouputFile.exists())
        {
            Log.e("File","output file found");
            return true;
        }

        try
        {

            in = assetManager.open(filename);
            out = new FileOutputStream(ouputFile);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;

            return  true;
        }
        catch(IOException e)
        {
            Log.e("tag", "Failed to copy asset file: " + filename, e);
            desFile.delete();
            return  false;
        }
    }
    public static void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }

    public static String getRealPathFromURI(Uri contentURI,Activity activity) {
        String result;
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static Uri saveScaledBitmap(Bitmap bmp,String realPath)
    {
        Uri uri=null;
        FileOutputStream out = null;
        try {
            File f=	Environment.getExternalStorageDirectory();

            File imgFile=new File(realPath);//f.getAbsolutePath()+"/","temp.jpg"
            out = new FileOutputStream(imgFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            uri=Uri.fromFile(imgFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return uri;
    }

    public static Bitmap getBitmapFromAsset(AssetManager mgr, String path) {
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = mgr.open(path);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (final IOException e) {
            bitmap = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
        return bitmap;
    }

    public static int getDistancebetweenTwoPoints(int x1,int y1,int x2,int y2)
    {

        double result=Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));

        return (int)result;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static Drawable getTintedDrawable(Context context, int drwId)
    {
        Drawable drawable = ContextCompat.getDrawable(context,drwId);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable,ContextCompat.getColor(context, R.color.colorPrimary));
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static Drawable getTintedDrawable(Context context, int drwId,int colorId)
    {
        Drawable drawable = ContextCompat.getDrawable(context,drwId);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable,ContextCompat.getColor(context, colorId));
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static String loadJSONFromAsset(Context context)
    {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try
        {
            InputStreamReader reader = new InputStreamReader(context.getAssets().open("jsondata.json"));
            br = new BufferedReader(reader);
            String temp;
            while ((temp = br.readLine()) != null)
            {
                sb.append(temp);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally {
            try
            {
                br.close(); // stop reading
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
