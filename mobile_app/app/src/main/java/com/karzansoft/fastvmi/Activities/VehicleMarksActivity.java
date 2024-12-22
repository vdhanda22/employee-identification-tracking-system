package com.karzansoft.fastvmi.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karzansoft.fastvmi.MCCApplication;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Interfaces.SymbolStateListener;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Utils.Util;
import com.karzansoft.fastvmi.extended.MarkerView;
import com.karzansoft.fastvmi.extended.SymbolMarker;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VehicleMarksActivity extends AppCompatActivity implements  View.OnClickListener,SymbolStateListener{

    boolean IsCheckout = false;
    Button mNext = null;
    static final int REQUEST_TAKE_PHOTO = 111;
    static final int REQUEST_CROP_PHOTO = 112;

    private ImageView selectedImageView;

    private ImageView leftArrowImageView;

    private ImageView rightArrowImageView;

    private LinearLayout mGallery;

    private int selectedImagePosition = 0;

    private List<Drawable> drawables;

    private GalleryImageAdapter galImageAdapter;
    CaptureSignatureView mCapture = null;
    private View mTopView;
    RelativeLayout mGalleryLayout,topDrawingControl,centerRightDrawingControl;
    private ImageView mTakePic;
    private String mCurrentPhotoPath;
    private Uri outputUri;
    private HorizontalScrollView scrollView;
    private List<String> imagesPath;
    int current_mode;
    MarkerView markerView;
    ImageView imgDent,imgLdent,imgScratch,imgScratchThin,imgMark,imgErase;
   /* View line_sep;*/




    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("path", mCurrentPhotoPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentPhotoPath=savedInstanceState.getString("path");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_vehicle_marks);

        IsCheckout = getIntent().getBooleanExtra("IsCheckout", true);
        current_mode=getIntent().getIntExtra(Constant.CURRENT_MODE, 1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
         mCurrentPhotoPath="";
        //getSupportActionBar().setTitle("Vehicle Marks " + (IsCheckout ? " for Checkout" : " for Check-in"));
       /* if(IsCheckout)
            setTitle("Check Out");
        else
            setTitle("Check In");*/

        Log.e("Oncreate","Oncreate");

        if(current_mode==Constant.RA_CHECKOUT)
        {
            setTitle("Check Out - Exterior");
        }else if(current_mode==Constant.RA_CHECKIN)
        {
            setTitle("Check In - Exterior");
        }
        else if(current_mode==Constant.REPLACEMENT_CHECKIN)
        {
            setTitle("Replacement(In)- Exterior");
        }
        else if(current_mode==Constant.REPLACEMENT_CHECKOUT)
        {
            setTitle("Replacement(Out)- Exterior");
        }
        else if(current_mode==Constant.NRM_CHECKOUT)
        {
            setTitle("NRM Out - Exterior");

        }else if(current_mode==Constant.NRM_CHECKIN)
        {
            setTitle("NRM In - Exterior");
        }
        else if(current_mode==Constant.GARAGE_CHECKOUT)
        {
            setTitle("Garage Check Out - Exterior");

        }else if(current_mode==Constant.GARAGE_CHECKIN)
        {
            setTitle("Garage Check In - Exterior");

        }

      /* topDrawingControl=(RelativeLayout)findViewById(R.id.top_controls);
      */  centerRightDrawingControl=(RelativeLayout)findViewById(R.id.footer);

        imgDent=(ImageView)findViewById(R.id.img_small_dent);
        imgLdent=(ImageView)findViewById(R.id.img_large_dent);
        imgScratch=(ImageView)findViewById(R.id.img_scratch);
        imgScratchThin=(ImageView)findViewById(R.id.img_scratch_thin);
        /*imgMark=(ImageView)findViewById(R.id.img_mark);
        imgErase=(ImageView)findViewById(R.id.img_erase);*/
       // line_sep=(View)findViewById(R.id.line_sep);



        mTopView = (View)findViewById(R.id.vm_topView);
        mNext = (Button) findViewById(R.id.vm_action_next);
        mGalleryLayout = (RelativeLayout) findViewById(R.id.gallery_relative_layout);
     /*   mCapture = (CaptureSignatureView) findViewById(R.id.vm_capture);
*/        mTakePic=(ImageView)findViewById(R.id.camera_img);
        mGallery=(LinearLayout)findViewById(R.id.mygallery);
        scrollView=(HorizontalScrollView)findViewById(R.id.scrollView);
        leftArrowImageView = (ImageView) findViewById(R.id.left_arrow_imageview);
        rightArrowImageView = (ImageView) findViewById(R.id.right_arrow_imageview);
        markerView=(MarkerView)findViewById(R.id.marker_view);

        markerView.setVisibility(View.VISIBLE);
        mCapture.setVisibility(View.GONE);
        mTakePic.setOnClickListener(this);
        leftArrowImageView.setOnClickListener(this);
        rightArrowImageView.setOnClickListener(this);
        mGallery.setOnClickListener(this);
        markerView.setSymbolStateListener(this);



        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;

                if(current_mode==Constant.RA_CHECKIN||current_mode==Constant.RA_CHECKOUT || current_mode==Constant.REPLACEMENT_CHECKIN||current_mode==Constant.REPLACEMENT_CHECKOUT
                        || current_mode==Constant.NRM_CHECKOUT||current_mode==Constant.NRM_CHECKIN|| current_mode==Constant.GARAGE_CHECKOUT||current_mode==Constant.GARAGE_CHECKIN
                        )
                 intent = new Intent(VehicleMarksActivity.this, VehicleMarksInteriorActivity.class);
                else
                    intent = new Intent(VehicleMarksActivity.this, NotesActivity.class);
                intent.putExtra("IsCheckout", IsCheckout);
                intent.putExtra(Constant.CURRENT_MODE,current_mode);
                startActivityForResult(intent, 1);
                //finish();
            }
        });

        //getDrawablesList();
        //setupUI();

        mCapture.setCanvasColor(Color.TRANSPARENT);
        mCapture.setPaintColor(Color.RED);




        ViewTreeObserver observer = mCapture.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //in here, place the code that requires you to know the dimensions.
                //this will be called as the layout is finished, prior to displaying.

              //  Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.car_interior);
                int width = 781;
                int height = 1121;

               /* int width = 335;
                int height = 612;*/

                mCapture.getLayoutParams().width =  mCapture.getHeight() * width / height;

                //Toast.makeText(this,findViewById(R.id.parentcapture).getHeight() + "",Toast.LENGTH_LONG).show();
            }
        });

  if(imagesPath==null)
  {
      imagesPath=new ArrayList<String>();
      String uri1="assets://natureimage2.jpg";
      String uri2="assets://natureimage3.jpg";
      String uri3="assets://natureimage7.jpg";

      imagesPath.add(uri1);
      imagesPath.add(uri2);
     // imagesPath.add(uri3);
      setDefaultImagesClickeListner();
  }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.camera_img:

            dispatchTakePictureIntent();
                break;

            case R.id.left_arrow_imageview:

                scrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                break;

            case R.id.right_arrow_imageview:
                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                break;
            case R.id.mygallery:

                break;
            case R.id.img_small_dent:
                markerView.addSymbolOntouch(Constant.SYMBOL_MARKER_SMALL_DENT);
                imgDent.setSelected(true);
                imgLdent.setSelected(false);
                imgScratch.setSelected(false);
                imgScratchThin.setSelected(false);
                getSnackbar(Constant.SYMBOL_MARKER_SMALL_DENT).show();

                break;
            case R.id.img_large_dent:
                markerView.addSymbolOntouch(Constant.SYMBOL_MARKER_LARGE_DENT);
                imgDent.setSelected(false);
                imgLdent.setSelected(true);
                imgScratch.setSelected(false);
                imgScratchThin.setSelected(false);
                getSnackbar(Constant.SYMBOL_MARKER_LARGE_DENT).show();

                break;
            case R.id.img_scratch:
                markerView.addSymbolOntouch(Constant.SYMBOL_MARKER_SCRATCH);
                imgDent.setSelected(false);
                imgLdent.setSelected(false);
                imgScratch.setSelected(true);
                imgScratchThin.setSelected(false);
                getSnackbar(Constant.SYMBOL_MARKER_SCRATCH).show();
                break;
            case R.id.img_scratch_thin:
                markerView.addSymbolOntouch(Constant.SYMBOL_MARKER_SCRATCH_THIN);
                imgDent.setSelected(false);
                imgLdent.setSelected(false);
                imgScratch.setSelected(false);
                imgScratchThin.setSelected(true);
                getSnackbar(Constant.SYMBOL_MARKER_SCRATCH_THIN).show();
                break;

/*
            case R.id.img_mark:
                 if(mCapture!=null)
                mCapture.setIsEraser(false);
                imgMark.setSelected(true);
                imgErase.setSelected(false);
                break;
            case R.id.img_erase:
                if(mCapture!=null)
                    mCapture.setIsEraser(true);
                imgMark.setSelected(false);
                imgErase.setSelected(true);
                break;*/
            case R.id.img_info:
                SymbolMarker selected=markerView.getSelectedMarker();
                if(selected!=null)
                {
                    Intent intent=new Intent(this,SymbolDetailActivity.class);
                    intent.putExtra("ID",selected.getId());
                    intent.putExtra("IS_EDITABLE",selected.isEditable());
                    startActivity(intent);
                }else{

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(this);
                    builder.setTitle("Symbol detail");
                    builder.setMessage("Please select at least one symbol to view it's detail ");
                    builder.setPositiveButton("Ok", null);

                    builder.show();
                }
                break;


        }
    }

    public void resetSelection()
    {
        imgDent.setSelected(false);
        imgLdent.setSelected(false);
        imgScratch.setSelected(false);
        imgScratchThin.setSelected(false);
    }


    View.OnClickListener imagesListerner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int tag=Integer.parseInt(v.getTag().toString());

            if(imagesPath.size()>0)
            {
                Intent intent = new Intent(VehicleMarksActivity.this, GalleryViewActivity.class);
                intent.putStringArrayListExtra("Images", (ArrayList<String>) imagesPath);
                intent.putExtra("Index",tag);
                startActivity(intent);
            }

        }
    };



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = Util.createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCurrentPhotoPath=photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }




   private void setDefaultImagesClickeListner()
   {
       try {

         int size= mGallery.getChildCount();
           for (int i=0;i<size;i++)
           {
               mGallery.getChildAt(i).setOnClickListener(imagesListerner);
           }


       }catch (Exception ex)
       {
           ex.printStackTrace();
       }
   }



    private void addImageToGallry()
    {
       try {

        ImageView imageView = new ImageView(this);
           LinearLayout.LayoutParams vp =
                   new LinearLayout.LayoutParams(Util.convertDpToPixel(48, this),Util.convertDpToPixel(48, this));
           vp.setMargins(Util.convertDpToPixel(1, this), Util.convertDpToPixel(1, this), Util.convertDpToPixel(1, this), Util.convertDpToPixel(1, this));
        imageView.setLayoutParams(vp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
           imageView.setImageBitmap(Util.getScaledBitmap(Util.convertDpToPixel(48, this), Util.convertDpToPixel(48, this), mCurrentPhotoPath));
           imageView.setTag(imagesPath.size());
           imageView.setOnClickListener(imagesListerner);
        mGallery.addView(imageView);
           imagesPath.add("file://"+mCurrentPhotoPath);

           if(imagesPath.size()>1)
           {
               FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
               params.gravity= Gravity.LEFT;
               mGallery.setLayoutParams(params);

           }
       }catch (Exception ex)
       {
           ex.printStackTrace();
       }
    }




    @Override
    protected void onStart()
    {
        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                //String result=data.getStringExtra("result");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",IsCheckout);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }else if(requestCode==REQUEST_TAKE_PHOTO)
        {
            if(resultCode == Activity.RESULT_OK){

               // outputUri=Uri.parse("file://"+mCurrentPhotoPath);
                addImageToGallry();

                //Crop.of(outputUri, outputUri).withMaxSize(480,800).asSquare().start(VehicleMarksActivity.this);

               //  Crop.of(outputUri, outputUri).asSquare().start(VehicleMarksActivity.this);
            }
        }
       else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            addImageToGallry();

        }

    }//onActivityResult
    private void setupUI() {

        //selectedImageView = (ImageView) findViewById(R.id.selected_imageview);
        leftArrowImageView = (ImageView) findViewById(R.id.left_arrow_imageview);
        rightArrowImageView = (ImageView) findViewById(R.id.right_arrow_imageview);

        /*leftArrowImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (selectedImagePosition > 0) {
                    --selectedImagePosition;

                }

                *//*gallery.setSe
                gallery.setSelection(selectedImagePosition, false);*//*
            }
        });

        rightArrowImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (selectedImagePosition < drawables.size() - 1) {
                    ++selectedImagePosition;

                }

                *//*gallery.setSelection(selectedImagePosition, false);*//*

            }
        });

            gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Toast.makeText(getApplicationContext(),view.getId() + "",Toast.LENGTH_LONG).show();
                selectedImagePosition = pos;

                if (selectedImagePosition > 0 && selectedImagePosition < drawables.size() - 1) {

                    leftArrowImageView.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_left_enabled));
                    rightArrowImageView.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_right_enabled));

                } else if (selectedImagePosition == 0) {

                    leftArrowImageView.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_left_disabled));

                } else if (selectedImagePosition == drawables.size() - 1) {

                    rightArrowImageView.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_right_disabled));
                }

                changeBorderForSelectedImage(selectedImagePosition);
                setSelectedImage(selectedImagePosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        galImageAdapter = new GalleryImageAdapter(this, drawables);

        gallery.setAdapter(galImageAdapter);

        if (drawables.size() > 0) {

            gallery.setSelection(selectedImagePosition, false); removed by sajjad

        }

        if (drawables.size() == 1) {

            rightArrowImageView.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_right_disabled));
        }


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // set gallery to left side
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gallery.getLayoutParams();
        mlp.setMargins(-(metrics.widthPixels / 2 + (150 / 2)), mlp.topMargin,
                mlp.rightMargin, mlp.bottomMargin);
        gallery.setSelection(1);
        */

    }

  /*  private void changeBorderForSelectedImage(int selectedItemPos) {

        int count = gallery.getChildCount();

        for (int i = 0; i < count; i++) {

            ImageView imageView = (ImageView) gallery.getChildAt(i);
            imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_border));
            imageView.setPadding(3, 3, 3, 3);

        }

        ImageView imageView = (ImageView) gallery.getSelectedView();
        imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_image_border));
        imageView.setPadding(3, 3, 3, 3);
    }*/

    private void getDrawablesList() {

        drawables = new ArrayList<Drawable>();
      /*  drawables.add(getResources().getDrawable(R.mipmap.natureimage1));
        drawables.add(getResources().getDrawable(R.mipmap.natureimage2));
        drawables.add(getResources().getDrawable(R.mipmap.natureimage3));
        drawables.add(getResources().getDrawable(R.mipmap.natureimage4));
        drawables.add(getResources().getDrawable(R.mipmap.natureimage5));
        drawables.add(getResources().getDrawable(R.mipmap.natureimage6));
        drawables.add(getResources().getDrawable(R.mipmap.natureimage7));
        drawables.add(getResources().getDrawable(R.mipmap.natureimage8));
        drawables.add(getResources().getDrawable(R.mipmap.natureimage9));
        drawables.add(getResources().getDrawable(R.mipmap.natureimage10));
        drawables.add(getResources().getDrawable(R.mipmap.natureimage11));
        drawables.add(getResources().getDrawable(R.mipmap.natureimage12));
        drawables.add(getResources().getDrawable(R.mipmap.natureimage13));
        drawables.add(getResources().getDrawable(R.mipmap.natureimage14));
        drawables.add(getResources().getDrawable(R.mipmap.natureimage15));
*/
    }

    private void setSelectedImage(int selectedImagePosition) {

        BitmapDrawable bd = (BitmapDrawable) drawables.get(selectedImagePosition);
        Bitmap b = Bitmap.createScaledBitmap(bd.getBitmap(), (int) (bd.getIntrinsicHeight() * 0.9), (int) (bd.getIntrinsicWidth() * 0.7), false);

        //selectedImageView.setImageBitmap(b);
        //selectedImageView.setScaleType(ScaleType.FIT_XY);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra("picture", byteArray);
        startActivity(intent);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_mark) {
           /* if(mCapture!=null)
                mCapture.setIsEraser(false);*/
            Intent i=new Intent(VehicleMarksActivity.this,MarkerActivity.class);
            startActivity(i);
            return true;
        }
        else if(id == R.id.action_erase)
        {
          /*  mCapture.setIsEraser(true);*/
            return true;
        } else if(id==android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Snackbar getSnackbar(int markerSymbol)
    {
        String text="";
        if (markerSymbol == Constant.SYMBOL_MARKER_SMALL_DENT) {
            text = "Small Dent Selected";
        } else if (markerSymbol == Constant.SYMBOL_MARKER_LARGE_DENT) {
            text = "Large Dent Selected";
        }
        else if (markerSymbol == Constant.SYMBOL_MARKER_SCRATCH) {
            text = "Large Scratch Selected";
        }
        else if (markerSymbol == Constant.SYMBOL_MARKER_SCRATCH_THIN) {
            text = "Small Scratch Selected";
        }
        Snackbar snackbar=Snackbar.make(markerView, text, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
       // FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)sbView .getLayoutParams();
       // params.gravity = Gravity.TOP;
       // sbView .setLayoutParams(params);
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        return snackbar;
    }

    @Override
    public void onSymbolAdded(SymbolMarker symbolMarker) {

        ((MCCApplication)getApplication()).addSymbol(symbolMarker.markerType, symbolMarker.getId());
    }

    @Override
    public void onRemoved(SymbolMarker symbolMarker) {

        ((MCCApplication)getApplication()).removeSymbol(symbolMarker.getId());

    }

    @Override
    public void onSymbolSelected(SymbolMarker symbolMarker) {

        resetSelection();
    }

   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }*/
}
