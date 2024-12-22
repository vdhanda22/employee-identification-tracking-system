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
import com.karzansoft.fastvmi.Models.Contact;
import com.karzansoft.fastvmi.Network.Entities.Request.CustomerSearchRequest;
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
 * Created by Yasir on 6/12/2016.
 */
public class CustomerSearchAdapter extends BaseAdapter implements Filterable {


    private Context mContext;
    private List<Contact> resultList = new ArrayList<Contact>();
    private boolean isEmailSearch;

    public CustomerSearchAdapter(Context context,boolean emailSearch)
    {
        this.mContext=context;
        this.isEmailSearch=emailSearch;
    }

    public Contact getCustomerAt(int position)
    {
        return resultList.get(position);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int position) {
        if(isEmailSearch)
        return resultList.get(position).getEmail();
        else
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
                if (constraint != null && constraint.length()>0) {
                    List<Contact> customers;
                    if(isEmailSearch)
                        customers = searchCustomers("", constraint.toString().toLowerCase());
                    else
                        customers = searchCustomers(constraint.toString().toUpperCase(), "");

                    // Assign the data to the FilterResults
                    if(customers!=null){
                        filterResults.values = customers;
                        filterResults.count = customers.size();
                    }
                    else {
                        filterResults.values = new ArrayList<Contact>();
                        filterResults.count = 0;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<Contact>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private ArrayList<Contact> searchCustomers( String name,String email)
    {
        final CustomerSearchRequest request=new CustomerSearchRequest();
        if(name.length()>0)
            request.setKeyword(name);
        if(email.length()>0)
            request.setKeyword(email);
        ArrayList<Contact> customers=new ArrayList<>();

        Call<WebResponse<WebResponseList<Contact>>> searchResponse= WebServiceFactory.getInstance().searchCustomer(request, AppUtils.getAuthToken(mContext));
        try{
            Response<WebResponse<WebResponseList<Contact>>> responseResult=searchResponse.execute();
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

            WebResponseList<Contact> result=responseResult.body().getResult();
            if(result!=null)
                customers=result.getItems();
        }catch (Exception ex){ex.printStackTrace();}

        return customers;
    }
}
