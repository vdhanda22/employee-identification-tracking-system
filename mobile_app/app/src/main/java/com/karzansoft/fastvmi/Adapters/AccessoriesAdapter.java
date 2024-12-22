package com.karzansoft.fastvmi.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.Constant;

/**
 * Created by Yasir on 3/9/2016.
 */
public class AccessoriesAdapter extends ArrayAdapter<String> {

    Context context;
    int layoutResourceId;
    int currentMode;
    String data[] = null;

    public AccessoriesAdapter(Context context, int layoutResourceId, String[] data,int mode) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.currentMode=mode;
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

        if(this.currentMode== Constant.ACCESSORIES_OUT)
        {
            holder.checkBox1.setVisibility(View.INVISIBLE);
        }else {
            holder.checkBox1.setVisibility(View.VISIBLE);
            if(position==0||position==3||position==5||position==6)
                holder.checkBox1.setChecked(true);

        }
        holder.checkBox1.setClickable(false);

        holder.txtTitle.setText(data[position]);

        holder.checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkBox2.isChecked())
                    holder.checkBox2.setChecked(false);
                else
                    holder.checkBox2.setChecked(true);

            }
        });


        return row;
    }

    @Override
    public String getItem(int position) {
        return data[position];
    }

    @Override
    public int getCount() {
        return this.data.length;
    }

    static class  AccessoriesHolder
    {
        TextView txtTitle;
        CheckBox checkBox1,checkBox2;
    }
}
