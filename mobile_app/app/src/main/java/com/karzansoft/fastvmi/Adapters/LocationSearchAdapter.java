package com.karzansoft.fastvmi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.karzansoft.fastvmi.DataBase.DatabaseManager;
import com.karzansoft.fastvmi.Models.VehicleLocation;
import com.karzansoft.fastvmi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yasir on 4/21/2016.
 */
public class LocationSearchAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<VehicleLocation> resultList = new ArrayList<VehicleLocation>();

    public LocationSearchAdapter(Context context)
    {
        this.mContext=context;

    }

    public VehicleLocation getLocationAt(int position)
    {
        return resultList.get(position);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int position) {

            return resultList.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.vehicle_search_item, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.text1)).setText(getItem(position));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null && constraint.length()>1) {
                    List<VehicleLocation> vehicles;

                        vehicles = searchVehicle(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = vehicles;
                    filterResults.count = vehicles.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<VehicleLocation>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private ArrayList<VehicleLocation> searchVehicle( String kewWord)
    {

        ArrayList<VehicleLocation> vehicles=new ArrayList<>();
        vehicles= DatabaseManager.getInstance(mContext.getApplicationContext()).searchLocation(kewWord);

        return vehicles;
    }
}
