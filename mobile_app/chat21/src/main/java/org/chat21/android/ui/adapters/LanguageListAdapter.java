package org.chat21.android.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import org.chat21.android.R;
import org.chat21.android.ui.models.LanguageItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yasir on 9/27/2018.
 */

public class LanguageListAdapter extends BaseAdapter implements Filterable{

    private final LayoutInflater mInflater;

    ArrayList<LanguageItem> languageItems;
    ArrayList<LanguageItem> filteredLanguageItems;
    Context context;
    String checkedCode = "";


    public LanguageListAdapter(Context context, ArrayList<LanguageItem> items)
    {
        this.context = context;
        this.languageItems = items;
        this.filteredLanguageItems = items;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void  setCheckCode(String code)
    {
        this.checkedCode = code;
    }

    @Override
    public int getCount() {
        return this.filteredLanguageItems.size();
    }

    @Override
    public LanguageItem getItem(int i) {
        return this.filteredLanguageItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        final  LanguageHolder holder ;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.row_language_list, viewGroup, false);

            holder = new LanguageHolder();
            holder.txtTitle = row.findViewById(R.id.name);
            holder.tick= row.findViewById(R.id.tick);

            row.setTag(holder);
        }
        else
        {
            holder = (LanguageHolder)row.getTag();
        }

        LanguageItem item = this.filteredLanguageItems.get(i);

        holder.txtTitle.setText(item.getName());
        if (item.getCode().equals(checkedCode))
        {
            holder.tick.setVisibility(View.VISIBLE);
        }
        else {
            holder.tick.setVisibility(View.INVISIBLE);
        }

        return row;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredLanguageItems = languageItems;
                } else {
                    ArrayList<LanguageItem> filteredList = new ArrayList<>();
                    for (LanguageItem row : languageItems) {
                        // search on the user fullname
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filteredLanguageItems = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredLanguageItems;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredLanguageItems = (ArrayList<LanguageItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class  LanguageHolder
    {
        TextView txtTitle;
        ImageView tick;
    }
}
