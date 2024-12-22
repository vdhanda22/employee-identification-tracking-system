package com.karzansoft.fastvmi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.karzansoft.fastvmi.Models.Vehicle;
import com.karzansoft.fastvmi.Models.VehicleModel;
import com.karzansoft.fastvmi.Network.Entities.Request.ModelSearchRequest;
import com.karzansoft.fastvmi.Network.WebResponse;
import com.karzansoft.fastvmi.Network.WebResponseList;
import com.karzansoft.fastvmi.Network.WebServiceFactory;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Yasir on 7/21/2016.
 */
public class ModelSearchAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<VehicleModel> resultList = new ArrayList<VehicleModel>();
    public ModelSearchAdapter(Context context)
    {
        this.mContext=context;

    }

    public VehicleModel getModelAt(int position)
    {
        return resultList.get(position);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int position) {

            return resultList.get(position).getName()+" "+resultList.get(position).getMake();
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
                if (constraint != null && constraint.length()>0) {
                    List<VehicleModel> vehiclesModels;
                    if(constraint.equals("suggest"))
                        constraint="";

                    vehiclesModels=searchVehicleModel(constraint.toString());

                    // Assign the data to the FilterResults
                    if(vehiclesModels!=null){
                        filterResults.values = vehiclesModels;
                        filterResults.count = vehiclesModels.size();
                    }
                    else {
                        filterResults.values = new ArrayList<Vehicle>();
                        filterResults.count = 0;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<VehicleModel>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private ArrayList<VehicleModel> searchVehicleModel( String name)
    {

        final ModelSearchRequest request=new ModelSearchRequest();

            request.setName(name);
        ArrayList<VehicleModel> vehiclesModels=new ArrayList<>();

        Call<WebResponse<WebResponseList<VehicleModel>>> searchResponse= WebServiceFactory.getInstance().searchVehicleModels(request, AppUtils.getAuthToken(mContext));
        try{
            Response<WebResponse<WebResponseList<VehicleModel>>> responseResult=searchResponse.execute();


            WebResponseList<VehicleModel> result=responseResult.body().getResult();
            if(result!=null)
                vehiclesModels=result.getItems();
        }catch (Exception ex){ex.printStackTrace();}

        return vehiclesModels;
    }

}
