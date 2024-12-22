package com.karzansoft.fastvmi.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.karzansoft.fastvmi.Adapters.GalleryPagerAdapter;
import com.karzansoft.fastvmi.MCCApplication;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yasir on 1/27/16.
 */
public class GalleryViewActivity extends AppCompatActivity {

    private List<String> images;
    private ViewPager mPager;
    private GalleryPagerAdapter mAdpater;
    private int currentIndex;
    private int documentIndex;
    private boolean isReadOnly;
    private String title;
    private LinearLayout mGallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppUtils.isRTLLanguageSelected(this))
            AppUtils.forceRTLIfSupported(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_view_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(AppUtils.isRTLLanguageSelected(this))
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
        title="VMI Gallery";


        findViewById(R.id.camera_img).setVisibility(View.GONE);
        findViewById(R.id.info_parent).setVisibility(View.GONE);


       if(getIntent()!=null && getIntent().getExtras()!=null)
       {
           documentIndex=getIntent().getIntExtra("documentIndex",0);
           currentIndex=getIntent().getIntExtra("Index",0);

          if(getIntent().getBooleanExtra("isReadOnly",false))
          {
              images=getIntent().getStringArrayListExtra("Images");
              isReadOnly=true;
              title=getIntent().getStringExtra("Title");
          }
           else
          {
              images=((MCCApplication)getApplication()).getDocuments().get(documentIndex).getImages();
              isReadOnly=false;
              title=((MCCApplication)getApplication()).getDocuments().get(documentIndex).getTitle();

          }


       }
        else if(savedInstanceState!=null)
       {

           images=  savedInstanceState.getStringArrayList("Images");
           isReadOnly=savedInstanceState.getBoolean("isReadOnly", false);
           currentIndex=savedInstanceState.getInt("Index", 0);
           documentIndex=savedInstanceState.getInt("documentIndex", 0);

           if(isReadOnly)
           {
               images=savedInstanceState.getStringArrayList("Images");
               title=savedInstanceState.getString("Title");
           }
           else
           {
               images=((MCCApplication)getApplication()).getDocuments().get(documentIndex).getImages();
               title=((MCCApplication)getApplication()).getDocuments().get(documentIndex).getTitle();
           }
       }


        setTitle(title);
        findViewById(R.id.parentlayout).setBackgroundColor(Color.WHITE);
        mGallery=(LinearLayout)findViewById(R.id.mygallery);
        mPager=(ViewPager)findViewById(R.id.pager);
        mAdpater=new GalleryPagerAdapter(this,images);
        mPager.setAdapter(mAdpater);
        mPager.setCurrentItem(currentIndex);
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

        loadImages();
        if (images.size()>0 && images.get(0).length()>0)
            updateSelectedIndex(currentIndex);


    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putStringArrayList("Images", (ArrayList<String>) images);
        outState.putInt("Index", currentIndex);
        outState.putInt("documentIndex", documentIndex);
        outState.putBoolean("isReadOnly", isReadOnly);
        outState.putString("Title",""+title);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      if(!isReadOnly)
        getMenuInflater().inflate(R.menu.menu_delete_gallery_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_remove_item:
                if (this.images.size() > 0) {
                    this.images.remove(currentIndex);
                    mGallery.removeViewAt(currentIndex);
                    if (this.currentIndex >= this.images.size())
                        this.currentIndex -= 1;
                    if (currentIndex < 0)
                        currentIndex = 0;
                    updateTags();
                    updateSelectedIndex(currentIndex);
                    mAdpater.notifyDataSetChanged();
                    if (this.images.size() < 1)
                        finish();
                   /* else
                        mAdpater.notifyDataSetChanged();*/
                }
                return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }


    ////// Gallery view Methods

    private void loadImages()
    {
        for (int i=0;i<images.size();i++)
        {
            addImageToGallry(images.get(i).replace("file://",""),i,false);
        }
    }

    private void updateSelectedIndex(int index)
    {
        for (int i=0;i<mGallery.getChildCount();i++)
        {
            if(i==index && images.size()==mGallery.getChildCount()&& images.get(i).length()>0)
                mGallery.getChildAt(i).setBackgroundColor(Color.WHITE);
            else
                mGallery.getChildAt(i).setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    private void updateTags()
    {
        for (int i=0;i<mGallery.getChildCount();i++)
        {
            mGallery.getChildAt(i).setTag(i);

        }
    }

    private void addImageToGallry(String path,int tag,boolean isSelected)
    {
        try {

            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams vp =
                    new LinearLayout.LayoutParams(Util.convertDpToPixel(50, this),Util.convertDpToPixel(50, this));
            vp.setMargins(Util.convertDpToPixel(1, this), Util.convertDpToPixel(1, this), Util.convertDpToPixel(1, this), Util.convertDpToPixel(1, this));

            int padding =Util.convertDpToPixel(2, this);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setCropToPadding(true);
            if(isSelected)
                imageView.setBackgroundColor(Color.WHITE);
            imageView.setLayoutParams(vp);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


            if(path.length()>0)
            {
                if (isReadOnly)
                {
                    imageView.setImageResource(R.drawable.empty_photo);
                    ImageLoader.getInstance().displayImage(path, imageView);

                }
                else
                    imageView.setImageBitmap(Util.getScaledBitmap(Util.convertDpToPixel(50, this), Util.convertDpToPixel(50, this), path));
            }


            imageView.setTag(tag);
            imageView.setOnClickListener(imagesListerner);
            mGallery.addView(imageView);

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    View.OnClickListener imagesListerner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int tag=Integer.parseInt(v.getTag().toString());
            currentIndex=tag;
            updateSelectedIndex(currentIndex);
            mPager.setCurrentItem(currentIndex);


        }
    };
}
