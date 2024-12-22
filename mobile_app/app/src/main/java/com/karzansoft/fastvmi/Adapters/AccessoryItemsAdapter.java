package com.karzansoft.fastvmi.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.karzansoft.fastvmi.Models.AccessoryItem;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.Constant;

import java.util.ArrayList;

/**
 * Created by Yasir on 4/6/2016.
 */
public class AccessoryItemsAdapter extends BaseAdapter {

    Context context;
    int layoutResourceId;
    int currentMode;
    ArrayList<AccessoryItem> data;
    ArrayList<String> checkedIds;


    public AccessoryItemsAdapter(Context context, int layoutResourceId, ArrayList<AccessoryItem> data,int mode) {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.currentMode=mode;
    }



    public void setCheckedIds(ArrayList<String> ids)
    {
        this.checkedIds=ids;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public AccessoryItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final  AccessoriesHolder holder ;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new AccessoriesHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.header);
            holder.checkBox1=(CheckBox)row.findViewById(R.id.checkbox1);
            holder.checkBox2=(CheckBox)row.findViewById(R.id.checkbox2);

            row.setTag(holder);
        }
        else
        {
            holder = (AccessoriesHolder)row.getTag();


        }

        final AccessoryItem item=getItem(position);

        if(this.currentMode== Constant.ACCESSORIES_OUT)
        {
            holder.checkBox1.setVisibility(View.INVISIBLE);
        }else {

            if(checkedIds!=null && checkedIds.contains(""+item.getId()))
            {
                holder.checkBox1.setChecked(true);
                holder.checkBox1.setVisibility(View.VISIBLE);
            }
            else
                holder.checkBox1.setVisibility(View.INVISIBLE);

        }
        holder.checkBox1.setClickable(false);
        holder.checkBox2.setClickable(false);
        holder.checkBox2.setChecked(item.isStatus());
        holder.txtTitle.setText(item.getName());



        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox2.isChecked()) {
                    holder.checkBox2.setChecked(false);
                    item.setStatus(false);
                } else {
                    holder.checkBox2.setChecked(true);
                    item.setStatus(true);
                }


            }
        });


        return row;
    }

    static class  AccessoriesHolder
    {
        TextView txtTitle;
        CheckBox checkBox1,checkBox2;
    }
}
