package com.karzansoft.fastvmi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.karzansoft.fastvmi.Models.Voucher;
import com.karzansoft.fastvmi.Network.Entities.Request.VoucherRequest;
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
public class VoucherSearchAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<Voucher> resultList = new ArrayList<Voucher>();
    private View adapterView;
    private long contactId;


    public VoucherSearchAdapter(Context context, View view)
    {
        this.mContext=context;

        this.adapterView=view;

    }

    public void setContactId(long id)
    {
        this.contactId = id;
    }

    public Voucher getVoucherAt(int position)
    {
        return resultList.get(position);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int position) {
            return resultList.get(position).getVoucherNo();
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

                if (constraint != null && constraint.length()>0 && contactId > 0) {
                    List<Voucher> vouchers;
                    if(constraint.equals("suggest"))
                        constraint="";
                    vouchers = searchVoucher(constraint.toString(),contactId);

                    // Assign the data to the FilterResults
                    if(vouchers!=null && adapterView!=null && adapterView.hasFocus()){
                        filterResults.values = vouchers;
                        filterResults.count = vouchers.size();
                    }
                    else {
                        filterResults.values = new ArrayList<Voucher>();
                        filterResults.count = 0;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<Voucher>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private ArrayList<Voucher> searchVoucher( String keyword,long id)
    {

        final VoucherRequest request=new VoucherRequest();
        request.setVoucherNo(""+keyword);
        request.setContactId(id);
        request.setType(12);

        ArrayList<Voucher> vouchers=new ArrayList<>();

        Call<WebResponse<WebResponseList<Voucher>>> searchResponse= WebServiceFactory.getInstance().searchVouchers(request, AppUtils.getAuthToken(mContext));
        try{
            Response<WebResponse<WebResponseList<Voucher>>> responseResult=searchResponse.execute();

            WebResponseList<Voucher> result=responseResult.body().getResult();
            if(result!=null)
                vouchers=result.getItems();
        }catch (Exception ex){ex.printStackTrace();}

        return vouchers;
    }
}
