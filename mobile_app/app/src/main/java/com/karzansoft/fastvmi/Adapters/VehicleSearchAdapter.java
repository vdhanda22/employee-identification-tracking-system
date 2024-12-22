package com.karzansoft.fastvmi.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.karzansoft.fastvmi.Activities.FragmentMainActivity;
import com.karzansoft.fastvmi.Models.Vehicle;
import com.karzansoft.fastvmi.Network.Entities.Request.VehicleSearchRequest;
import com.karzansoft.fastvmi.Network.WebResponse;
import com.karzansoft.fastvmi.Network.WebResponseList;
import com.karzansoft.fastvmi.Network.WebServiceFactory;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Enums.SearchVehicleType;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Yasir on 4/17/2016.
 */
public class VehicleSearchAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<Vehicle> resultList = new ArrayList<Vehicle>();
    private boolean isReg;
    private boolean onlyIdle;
    private View adapterView;
    private SearchVehicleType searchForType;

    public VehicleSearchAdapter(Context context,boolean isReg)
    {
        this.mContext=context;
        this.isReg=isReg;
        searchForType=SearchVehicleType.NONE;

    }

    public VehicleSearchAdapter(Context context,boolean isReg,View adapterView)
    {
        this.mContext=context;
        this.isReg=isReg;
        this.adapterView=adapterView;
        searchForType=SearchVehicleType.NONE;

    }

    public VehicleSearchAdapter(Context context,boolean isReg,SearchVehicleType type)
    {
        this.mContext=context;
        this.isReg=isReg;
        //this.adapterView=adapterView;
        this.searchForType=type;

    }

    public VehicleSearchAdapter(Context context,boolean isReg,View adapterView,SearchVehicleType type)
    {
        this.mContext=context;
        this.isReg=isReg;
        this.adapterView=adapterView;
        this.searchForType=type;

    }

    public void inculdeOnlyIdle(boolean onlyIdle)
    {
        this.onlyIdle=onlyIdle;
    }


    public Vehicle getVehicleAt(int position)
    {
        return resultList.get(position);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int position) {
        if (isReg)
            return resultList.get(position).getPlateNo();
        else
            return resultList.get(position).getFleetCode();
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

                if(adapterView!=null && !adapterView.hasFocus())
                    return filterResults;

                if (constraint != null && constraint.length()>0) {
                    List<Vehicle> vehicles;
                    if(constraint.equals("suggest"))
                        constraint="";
                    if(isReg)
                        vehicles = searchVehicle("",constraint.toString().toLowerCase());
                    else
                        vehicles = searchVehicle(constraint.toString().toUpperCase(),"");

                    // Assign the data to the FilterResults
                    if(vehicles!=null){
                    filterResults.values = vehicles;
                    filterResults.count = vehicles.size();
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
                    resultList = (List<Vehicle>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private ArrayList<Vehicle> searchVehicle( String fleetCode,String RegNo) {

        final VehicleSearchRequest request = new VehicleSearchRequest();
        if (fleetCode.length() > 0)
            request.setKeyword(fleetCode);
//            request.setFleetCode(fleetCode);
        if (RegNo.length() > 0)
            request.setKeyword(RegNo);
//            request.setPlateNo(RegNo.toUpperCase());
        if (this.onlyIdle)
            request.setStatusId("IDL");

        if (searchForType == SearchVehicleType.WORKSHOP_OPEN) {
            List<Integer> subStatuses = new ArrayList<>();
            //request.setSource("openworkshopmovement");
           /* subStatuses.add(new Integer(1));
            subStatuses.add(new Integer(6));
            subStatuses.add(new Integer(8));
            request.setSubStatuses(subStatuses);*/
        }else if (searchForType == SearchVehicleType.WORKSHOP_CLOSE)
        {
           /* List<Integer> subStatuses = new ArrayList<>();
            subStatuses.add(new Integer(1));
            subStatuses.add(new Integer(7));
            request.setSubStatuses(subStatuses);*/
        }

        ArrayList<Vehicle> vehicles=new ArrayList<>();

        Call<WebResponse<WebResponseList<Vehicle>>> searchResponse;

        if(searchForType==SearchVehicleType.WORKSHOP_OPEN) // updated end point
        {
            searchResponse = WebServiceFactory.getInstance().searchVehicle(request, AppUtils.getAuthToken(mContext));
        }
      else {
            searchResponse= WebServiceFactory.getInstance().searchVehicle(request, AppUtils.getAuthToken(mContext));
        }

       try{
           Response<WebResponse<WebResponseList<Vehicle>>> responseResult=searchResponse.execute();
           if(responseResult.code()==401)
           {
               try {
                   ((Activity) mContext).runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           ((FragmentMainActivity) mContext).relogin();
                       }
                   });
               } catch (Exception ex) {ex.printStackTrace();
               }
               return null;
           }

           WebResponseList<Vehicle> result=responseResult.body().getResult();
           if(result!=null)
           vehicles=result.getItems();
       }catch (Exception ex){ex.printStackTrace();}

        return vehicles;
    }
}
