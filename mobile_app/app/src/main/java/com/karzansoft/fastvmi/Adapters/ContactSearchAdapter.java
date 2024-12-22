package com.karzansoft.fastvmi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.karzansoft.fastvmi.Enums.ContactType;
import com.karzansoft.fastvmi.Models.Contact;
import com.karzansoft.fastvmi.Network.Entities.Request.ContactSearchRequest;
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
 * Created by Yasir on 8/24/2016.
 */
public class ContactSearchAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<Contact> resultList = new ArrayList<Contact>();
    private ContactType contactType;
    private View adapterView;


    public ContactSearchAdapter(Context context, View view) {
        this.mContext = context;
        this.contactType = contactType;
        this.adapterView = view;

    }

    public Contact getContactAt(int position)
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
                if(adapterView!=null && !adapterView.hasFocus())
                    return filterResults;

                if (constraint != null && constraint.length()>0) {
                    List<Contact> contacts;
                    if(constraint.equals("suggest"))
                        constraint="";
                  contacts=searchContact(constraint.toString(),contactType);

                    // Assign the data to the FilterResults
                    if(contacts!=null){
                        filterResults.values = contacts;
                        filterResults.count = contacts.size();
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

    private ArrayList<Contact> searchContact(String keyword, ContactType type)
    {

        final ContactSearchRequest request=new ContactSearchRequest();
        if(type==ContactType.CUSTOMER)
        {
            request.setContactType(2);
            request.setActiveOnly(true);
        }
        else if(type==ContactType.DRIVER)
        {
            request.setContactCategory(1);
        }
        else if(type==ContactType.ADDITIONAL_DRIVER)
        {
            request.setContactType(2);
            request.setContactCategory(1);
        }
        else if(type==ContactType.WORKSHOP_CONTACT)
        {
            request.setContactType(3);
        }

        request.setKeyword(keyword);
        ArrayList<Contact> contacts=new ArrayList<>();

        Call<WebResponse<WebResponseList<Contact>>> searchResponse= WebServiceFactory.getInstance().searchContact(request, AppUtils.getAuthToken(mContext));
        try{
            Response<WebResponse<WebResponseList<Contact>>> responseResult=searchResponse.execute();

            WebResponseList<Contact> result=responseResult.body().getResult();
            if(result!=null)
                contacts=result.getItems();
        }catch (Exception ex){ex.printStackTrace();}

        return contacts;
    }
}
