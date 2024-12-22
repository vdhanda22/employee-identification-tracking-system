package com.karzansoft.fastvmi.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.karzansoft.fastvmi.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Yasir on 1/27/16.
 */
public class GalleryPagerAdapter extends PagerAdapter {


    Context mContext;
    LayoutInflater mLayoutInflater;
    List<String> mImagesPath;

    public GalleryPagerAdapter(Context context,List<String> images)
    {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImagesPath=images;
    }

    @Override
    public int getCount() {
        return mImagesPath.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.gallery_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
       // imageView.setImageResource(mResources[position]);
        ImageLoader.getInstance().displayImage(mImagesPath.get(position),imageView);
        container.addView(itemView);

        return itemView;
    }
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
