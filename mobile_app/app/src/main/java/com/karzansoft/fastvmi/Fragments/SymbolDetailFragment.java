package com.karzansoft.fastvmi.Fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.karzansoft.fastvmi.MCCApplication;
import com.karzansoft.fastvmi.Models.SymbolDetails;
import com.karzansoft.fastvmi.Models.SymbolImageDetail;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Utils.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.IOException;

/**
 * Created by Yasir on 3/13/2016.
 */
public class SymbolDetailFragment extends Fragment implements View.OnClickListener{
    public static final int REQUEST_TAKE_PHOTO = 111;
    ImageView imageView, imageCam;
    SymbolImageDetail detilItem;
    EditText detail;
    ProgressBar progress;
    private String mCurrentPhotoPath;
    int imageIndex;
    boolean isEditale;
    String imageAccessToken;

    public SymbolDetailFragment(){}

    public static SymbolDetailFragment newInstance(String id,int index,boolean isEditable,String imageAccessToken) {
        SymbolDetailFragment fragment = new SymbolDetailFragment();
        Bundle args = new Bundle();
        args.putString("ID", id);
        args.putInt("Index", index);
        args.putBoolean("IsEditable", isEditable);
        args.putString("ACCESS_TOKEN",imageAccessToken);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.symbol_detail_item, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        imageCam = (ImageView) rootView.findViewById(R.id.camera_img);
        detail=(EditText)rootView.findViewById(R.id.edtDetail);
       // placeHolder=rootView.findViewById(R.id.placeholder);
        progress=(ProgressBar)rootView.findViewById(R.id.progress);

        // imageView.setImageResource(mResources[position]);
        imageIndex=getArguments().getInt("Index");
        imageAccessToken=getArguments().getString("ACCESS_TOKEN");
        SymbolDetails symbolDetails=((MCCApplication)getActivity().getApplication()).getSymbolById(getArguments().getString("ID"));
        if (symbolDetails != null && symbolDetails.getImages() != null && symbolDetails.getImages().size() > imageIndex) {
            detilItem = symbolDetails.getImages().get(imageIndex);
        }
        isEditale=getArguments().getBoolean("IsEditable");
       /* if(isEditale)
            progress.setVisibility(View.INVISIBLE);*/
        //  setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!isEditale)
        {
            detail.setEnabled(false);
            detail.setFocusable(false);
            imageCam.setVisibility(View.INVISIBLE);
        }

        if (detilItem != null && detilItem.getImgUrl() != null && detilItem.getImgUrl().length() > 0) {
            imageView.setVisibility(View.VISIBLE);
            if (isEditale) {
                //image from gallery
                ImageLoader.getInstance().displayImage(detilItem.getImgUrl(), imageView, imageLoadingListener);
            } else {
                //image from server
                ImageLoader.getInstance().displayImage(Constant.BASE_URL + detilItem.getImgUrl().substring(1), imageView, imageLoadingListener);
            }
        } else {
            imageView.setVisibility(View.INVISIBLE);
            //  imageCam.setVisibility(View.VISIBLE);

        }
        //imageCam.setOnClickListener(this);
        detail.setText(detilItem.getDetail());
        detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                detilItem.setDetail(s.toString());
            }
        });

        /*placeHolder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (detail.hasFocus()) {
                    detail.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });*/
    }



    public void loadImage(String url)
    {
        if(url!=null && url.length()>0){
            detilItem.setImgUrl("file://"+url);
            imageView.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(Constant.BASE_URL + detilItem.getImgUrl().substring(1), imageView, imageLoadingListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_img:

                dispatchTakePictureIntent();
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_remove_item:

              /*  imageView.setVisibility(View.GONE);
                imageCam.setVisibility(View.VISIBLE);
                imageView.setImageDrawable(null);
                detilItem.setImgUrl("");
*/

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // getActivity().getMenuInflater().inflate(R.menu.menu_delete_gallery_item, menu);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode==REQUEST_TAKE_PHOTO)
            {
                detilItem.setImgUrl("file://" + mCurrentPhotoPath);
                imageView.setVisibility(View.VISIBLE);
               //imageCam.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(detilItem.getImgUrl(), imageView);
            }


        }*/
    }

    ImageLoadingListener imageLoadingListener= new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {
            progress.setVisibility(View.VISIBLE);

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            progress.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            progress.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

            progress.setVisibility(View.INVISIBLE);
        }
    };




}
