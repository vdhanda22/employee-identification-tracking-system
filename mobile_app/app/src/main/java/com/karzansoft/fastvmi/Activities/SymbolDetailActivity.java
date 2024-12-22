package com.karzansoft.fastvmi.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.karzansoft.fastvmi.Fragments.SymbolDetailFragment;
import com.karzansoft.fastvmi.MCCApplication;
import com.karzansoft.fastvmi.Models.SymbolDetails;
import com.karzansoft.fastvmi.Models.SymbolImageDetail;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Utils.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yasir on 3/13/2016.
 */
public class SymbolDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public final int PERMISSION_REQUEST_CODE = 111;
    private List<SymbolImageDetail> symbolsIds;
    private ViewPager mPager;
    private int currentIndex;
    private String symbolId;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private String mCurrentPhotoPath;
    private ImageView imageCam;
    private LinearLayout mGallery;
    private boolean isEditable;
    private Handler mUiHandler;
    private ProgressDialog mProgress;
    String imageAccessToken = "";
    EditText detail;
    View placeHolder;
    View.OnClickListener imagesListerner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int tag = Integer.parseInt(v.getTag().toString());
            currentIndex = tag;
            updateSelectedIndex(currentIndex);
            mPager.setCurrentItem(currentIndex);


        }
    };
    boolean isRestoringInstance;
    private SymbolDetails symbolDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppUtils.isRTLLanguageSelected(this))
            AppUtils.forceRTLIfSupported(this);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.gallery_view_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (AppUtils.isRTLLanguageSelected(this))
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
        setTitle(AppUtils.getLocalizeString(this, "ItemDetail", "Item Details"));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        MCCApplication app = (MCCApplication) getApplication();

        if (savedInstanceState != null) {
            symbolId = savedInstanceState.getString("ID");
            isEditable = savedInstanceState.getBoolean("IS_EDITABLE", true);
            imageAccessToken = savedInstanceState.getString("ACCESS_TOKEN", "");
            try {
                app.loadSerializeMap(savedInstanceState.getString(Constant.APP_STATE, ""));
                mCurrentPhotoPath = savedInstanceState.getString("path");
            } catch (Exception e) {
            }
            ;

            isRestoringInstance = true;
        } else {
            symbolId = getIntent().getStringExtra("ID");
            isEditable = getIntent().getBooleanExtra("IS_EDITABLE", true);
            imageAccessToken = getIntent().getStringExtra("ACCESS_TOKEN");
            isRestoringInstance = false;
        }


        if (symbolsIds == null) {

            if (app.getSymbolById(symbolId) != null) {
                symbolDetails = app.getSymbolById(symbolId);
                symbolsIds = symbolDetails.getImages();
            }


        }

        //symbolsIds.add(symbolId);


        imageCam = (ImageView) findViewById(R.id.camera_img);
        mGallery = (LinearLayout) findViewById(R.id.mygallery);
        imageCam.setOnClickListener(this);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mSectionsPagerAdapter);
        mPager.setOffscreenPageLimit(5);
        mPager.setCurrentItem(currentIndex);
        detail = (EditText) findViewById(R.id.edtDetail);
        detail.setText(symbolDetails.getInfo());
        placeHolder = findViewById(R.id.placeholder);

        detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                symbolDetails.setInfo(detail.getText().toString());
            }
        });
        placeHolder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (detail.hasFocus()) {
                    detail.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });


        if (!isEditable) {
            detail.setEnabled(false);
            detail.setFocusable(false);
            imageCam.setVisibility(View.INVISIBLE);
            isRestoringInstance = true;
        }

        findViewById(R.id.gallery_relative_layout).setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                currentIndex = position;
                updateSelectedIndex(currentIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mUiHandler = new Handler();
        mProgress = new ProgressDialog(this);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setMessage(AppUtils.getLocalizeString(this, "Processing", "Processing..."));

        loadImages();
        if (symbolsIds.size() > 0 && symbolsIds.get(0).getImgUrl().length() > 0)
            updateSelectedIndex(currentIndex);
        else {
            if (!isRestoringInstance) {
                mUiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (RequestPermission())
                            dispatchTakePictureIntent();
                    }
                }, 100);
            }
        }
    }

    @Override
    protected void onDestroy() {
        ImageLoader.getInstance().stop();
        mSectionsPagerAdapter = null;
        super.onDestroy();
    }

    private void loadImages() {

        for (int i = 0; i < symbolsIds.size(); i++) {
            if (isEditable) {
                //image from gallery
                addImageToGallry(symbolsIds.get(i).getImgUrl(), i, false);

            } else {
                //image from server
                addImageToGallry(Constant.BASE_URL + symbolsIds.get(i).getImgUrl().substring(1), i, false);

            }
        }
    }

    private void updateSelectedIndex(int index) {
        for (int i = 0; i < mGallery.getChildCount(); i++) {
            if (i == index && symbolsIds.size() == mGallery.getChildCount() && symbolsIds.get(i).getImgUrl().length() > 0)
                mGallery.getChildAt(i).setBackgroundColor(Color.WHITE);
            else
                mGallery.getChildAt(i).setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    private void updateTags() {
        for (int i = 0; i < mGallery.getChildCount(); i++) {
            mGallery.getChildAt(i).setTag(i);

        }
    }

    private void addImageToGallry(String path, int tag, boolean isSelected) {
        try {

            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams vp =
                    new LinearLayout.LayoutParams(Util.convertDpToPixel(50, this), Util.convertDpToPixel(50, this));
            vp.setMargins(Util.convertDpToPixel(1, this), Util.convertDpToPixel(1, this), Util.convertDpToPixel(1, this), Util.convertDpToPixel(1, this));

            int padding = Util.convertDpToPixel(2, this);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setCropToPadding(true);
            if (isSelected)
                imageView.setBackgroundColor(Color.WHITE);
            imageView.setLayoutParams(vp);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


            if (path.length() > 0) {
                if (!isEditable) {
                    imageView.setImageResource(R.drawable.empty_photo);
                    ImageLoader.getInstance().displayImage(path, imageView);

                } else
                    imageView.setImageBitmap(Util.getScaledBitmap(Util.convertDpToPixel(50, this), Util.convertDpToPixel(50, this), path));
            }


            imageView.setTag(tag);
            imageView.setOnClickListener(imagesListerner);
            mGallery.addView(imageView);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("ID", symbolId);
        outState.putBoolean("IS_EDITABLE", isEditable);
        outState.putString("path", mCurrentPhotoPath);
        outState.putString("ACCESS_TOKEN", imageAccessToken);
        MCCApplication app = (MCCApplication) getApplication();
        if (app != null)
            outState.putString(Constant.APP_STATE, app.getSerializedMap());


    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentPhotoPath = savedInstanceState.getString("path");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (isEditable) {
            getMenuInflater().inflate(R.menu.menu_delete_gallery_item, menu);
            MenuItem item = menu.findItem(R.id.action_remove_item);
            item.setTitle(AppUtils.getLocalizeString(this, "Delete", "Delete"));
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_remove_item:

                if (symbolsIds.size() > 0 && symbolsIds.get(0).getImgUrl().length() > 0) {
                    symbolsIds.remove(currentIndex);
                    mGallery.removeViewAt(currentIndex);

                    if (currentIndex >= symbolsIds.size())
                        currentIndex -= 1;
                    if (currentIndex < 0)
                        currentIndex = 0;
                    updateTags();
                    updateSelectedIndex(currentIndex);
                    mSectionsPagerAdapter.notifyDataSetChanged();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra("android.intent.extra.quickCapture", true);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = Util.createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(this,
                        getString(R.string.file_provider_authority),
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                startActivityForResult(takePictureIntent, SymbolDetailFragment.REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SymbolDetailFragment.REQUEST_TAKE_PHOTO) {
                beginCrop(mCurrentPhotoPath);

                /* File path= new File(Environment.getExternalStorageDirectory()+"/mCC");
                path.mkdirs();
                if(symbolsIds.size()==1 && symbolsIds.get(0).getImgUrl().length()<1)
                {
                    SymbolImageDetail dt=symbolsIds.remove(0);
                    symbolsIds.add(new SymbolImageDetail(dt.getDetail(), "file://" + mCurrentPhotoPath));
                    ImageView imageView=(ImageView)mGallery.getChildAt(0);
                    imageView.setImageBitmap(Util.getScaledBitmap(Util.convertDpToPixel(50, this), Util.convertDpToPixel(50, this), mCurrentPhotoPath));
                    imageView.setBackgroundColor(Color.GREEN);
                    mSectionsPagerAdapter.notifyDataSetChanged();

                    Util.saveScaledBitmap(Util.getScaledBitmapToFitH(800, 800, mCurrentPhotoPath),path.getPath()+"/"+System.currentTimeMillis()+"_tmp.jpg");

                }else
                {
                    symbolsIds.add(new SymbolImageDetail("", "file://" + mCurrentPhotoPath));
                    addImageToGallry(mCurrentPhotoPath, symbolsIds.size() - 1, false);
                    mSectionsPagerAdapter.notifyDataSetChanged();
                    updateSelectedIndex(currentIndex);
                    Util.saveScaledBitmap(Util.getScaledBitmapToFitH(800, 800, mCurrentPhotoPath), path.getPath() + "/" + System.currentTimeMillis() + "_tmp.jpg");

                }*/


            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, data);
            }


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_img:

                if (RequestPermission())
                    dispatchTakePictureIntent();
                break;
        }
    }

    private boolean RequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int locationExtStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (locationExtStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;

    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        dispatchTakePictureIntent();
                    } else
                        Toast.makeText(this, AppUtils.getLocalizeString(SymbolDetailActivity.this, "PermissionError", "Some permissions are not granted"), Toast.LENGTH_LONG)
                                .show();
                } else {
                    Toast.makeText(this, AppUtils.getLocalizeString(SymbolDetailActivity.this, "PermissionsEnableMsg", "Go to settings and enable permissions"), Toast.LENGTH_LONG)
                            .show();
                }

        }


    }


    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            String imageFilePath = uri.getPath();
           /* mDocuments.get(selectedIndex).getImages().add("file://"+uri.getPath());//"file://"
            loadSections();
            mAdapter.notifyDataSetChanged();
            simpleSectionedGridAdapter.notifyDataSetChanged();*/
            File path = new File(Environment.getExternalStorageDirectory() + "/mCC");
            path.mkdirs();
            if (symbolsIds.size() == 1 && symbolsIds.get(0).getImgUrl().length() < 1) {
                SymbolImageDetail dt = symbolsIds.remove(0);
                symbolsIds.add(new SymbolImageDetail(dt.getDetail(), "file://" + uri.getPath()));
                ImageView imageView = (ImageView) mGallery.getChildAt(0);
                imageView.setImageBitmap(Util.getScaledBitmap(Util.convertDpToPixel(50, this), Util.convertDpToPixel(50, this), imageFilePath));
                imageView.setBackgroundColor(Color.WHITE);
                mSectionsPagerAdapter.notifyDataSetChanged();

                //Util.saveScaledBitmap(BitmapFactory.decodeFile(imageFilePath), path.getPath()+"/"+System.currentTimeMillis()+"_tmp.jpg");

            } else {
                symbolsIds.add(new SymbolImageDetail("", "file://" + uri.getPath()));
                addImageToGallry(imageFilePath, symbolsIds.size() - 1, false);
                mSectionsPagerAdapter.notifyDataSetChanged();
                updateSelectedIndex(currentIndex);
                //Util.saveScaledBitmap(BitmapFactory.decodeFile(imageFilePath), path.getPath() + "/" + System.currentTimeMillis() + "_tmp.jpg");

            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void beginCrop(final String imagePath) {

        try {


            mProgress.show();

            Thread imageCompression = new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap srcbt = Util.getScaledBitmap(800, 800, imagePath);
                    final Uri src = Util.saveScaledBitmap(srcbt, imagePath);
                    srcbt.recycle();
                    System.gc();
                    if (src != null) {
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mProgress.dismiss();
                                Uri destination = Uri.fromFile(new File(imagePath));
                                Crop.of(src, destination).start(SymbolDetailActivity.this);
                            }
                        });

                    }
                }
            });

            imageCompression.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return SymbolDetailFragment.newInstance(symbolId, position, isEditable, imageAccessToken);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return symbolsIds.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


    }


}
