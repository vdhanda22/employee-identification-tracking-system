package com.karzansoft.fastvmi.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.karzansoft.fastvmi.Models.SymbolDetails;
import com.karzansoft.fastvmi.R;

import java.util.List;

/**
 * Created by Yasir on 3/13/2016.
 */
public class SymbolsPagerAdapter extends PagerAdapter {


    Context mContext;
    LayoutInflater mLayoutInflater;
    List<SymbolDetails> mItems;
    View.OnClickListener clickListener;

    public SymbolsPagerAdapter(Context context, List<SymbolDetails> items)
    {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mItems=items;
    }

    public void setOnclickListener(View.OnClickListener listener)
    {
        this.clickListener=listener;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.symbol_detail_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        ImageView imageCam = (ImageView) itemView.findViewById(R.id.camera_img);
        // imageView.setImageResource(mResources[position]);
        SymbolDetails item=mItems.get(position);
       /* if(item.getImgUrl()!=null && item.getImgUrl().length()>0)
        {
            imageView.setVisibility(View.VISIBLE);
            imageCam.setVisibility(View.INVISIBLE);
            ImageLoader.getInstance().displayImage(mItems.get(position).getImgUrl(), imageView);
        }else
        {
            imageView.setVisibility(View.INVISIBLE);
            imageCam.setVisibility(View.VISIBLE);
            if(clickListener!=null)
            imageCam.setOnClickListener(clickListener);
        }*/

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
