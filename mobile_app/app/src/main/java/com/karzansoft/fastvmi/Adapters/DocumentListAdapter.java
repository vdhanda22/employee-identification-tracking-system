package com.karzansoft.fastvmi.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.karzansoft.fastvmi.Models.Document;
import com.karzansoft.fastvmi.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Yasir on 2/11/2016.
 */
public class DocumentListAdapter extends BaseAdapter{

    private Context mContext;
    private List<Document> mDocuments;
    private LayoutInflater mLayoutInflater;
    private View.OnClickListener mClickListener;


    public DocumentListAdapter(Context context,List<Document> docs, View.OnClickListener mClickListener)
    {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDocuments=docs;
        this.mClickListener=mClickListener;
    }

    @Override
    public int getCount() {
        return mDocuments.size();
    }

    @Override
    public Object getItem(int position) {
        return mDocuments.get(position);
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
            rowView = mLayoutInflater.inflate(R.layout.layout_document_item, null);
            holder=new ViewHolder(rowView);
            rowView.setTag(holder);
        }else
        {
        holder=(ViewHolder)rowView.getTag();
        }

        final Document doc=(Document)getItem(position);

        holder.tvTitle.setText(doc.getTitle());
        holder.img1.setTag(position);
        holder.img2.setTag(position);
        holder.img3.setTag(position);
        holder.img1.setVisibility(View.GONE);
        holder.img2.setVisibility(View.GONE);
        holder.img3.setVisibility(View.VISIBLE);
        holder.img1.setOnClickListener(mClickListener);
        holder.img2.setOnClickListener(mClickListener);
        holder.img3.setOnClickListener(mClickListener);
        holder.img3.setImageResource(R.drawable.camera_button);
        holder.img3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        if(doc.getImages().size()>0)
        {
            holder.img1.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage(doc.getImages().get(0), holder.img1);
        }

        if(doc.getImages().size()>1)
        {
            holder.img2.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage(doc.getImages().get(1), holder.img2);
        }

        if(doc.getImages().size()>2)
        {
            //holder.img2.setVisibility(View.VISIBLE);
            holder.img3.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.img3.setVisibility(View.INVISIBLE);

            ImageLoader.getInstance().displayImage(doc.getImages().get(2), holder.img3, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.img3.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.img3.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.img3.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    holder.img3.setVisibility(View.VISIBLE);
                }
            });
        }

        return rowView;
    }



    static class ViewHolder {

        TextView tvTitle;
        ImageView img1,img2,img3;

        /** holder constructor */
        public ViewHolder(View view) {

            tvTitle = (TextView)view.findViewById(R.id.title);
            img1=(ImageView)view.findViewById(R.id.img1);
            img2=(ImageView)view.findViewById(R.id.img2);
            img3=(ImageView)view.findViewById(R.id.img3);

        }

    }
}

