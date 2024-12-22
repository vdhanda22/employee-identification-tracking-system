package com.karzansoft.fastvmi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.karzansoft.fastvmi.Models.DocumentItem;
import com.karzansoft.fastvmi.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Yasir on 2/15/2016.
 */
public class DocumentGridAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private View.OnClickListener mClickListener;
    private List<DocumentItem> items;


    public DocumentGridAdapter(Context context, View.OnClickListener mClickListener,List<DocumentItem> items)
    {
        mContext = context;
        this.items=items;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mClickListener=mClickListener;
    }

    @Override
    public int getCount() {
         return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final  ViewHolder holder;
        View rowView = convertView;
        if(rowView==null)
        {
            rowView = mLayoutInflater.inflate(R.layout.grid_item, parent,false);
            holder=new ViewHolder(rowView);
            rowView.setTag(holder);
        }else
        {
            holder=(ViewHolder)rowView.getTag();
        }

        DocumentItem item=(DocumentItem)getItem(position);

        item.setItemIndex(position);
        holder.img.setOnClickListener(mClickListener);
        holder.img.setTag(item);

        if (item.getImagePath().startsWith("_action"))
        {
            holder.img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.img.setImageResource(R.drawable.camera_icon);
        }
        else
        {
            holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.img.setImageResource(R.drawable.empty_photo);
            ImageLoader.getInstance().displayImage(item.getImagePath(), holder.img);
        }

        return rowView;
    }


    static class ViewHolder {


        ImageView img;

        /** holder constructor */
        public ViewHolder(View view) {


            img=(ImageView)view.findViewById(R.id.image);


        }

    }
}
