package com.karzansoft.fastvmi.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.karzansoft.fastvmi.Models.AppSettings;
import com.karzansoft.fastvmi.Models.Vehicle;
import com.karzansoft.fastvmi.Models.VoucherLine;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yasir on 10/23/2018.
 */

public class VoucherLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int FOOTER = 1;
    private List<VoucherLine> voucherLineList;
    private String labelDescription = "Description";
    private String labelVehicle = "Vehicle";
    private String labelAmount = "Amount";
    private String labelAmountTax = "Amount + Tax";
    private String labelTotalAmount = "Total";
    private String labelAddRow = "Add Row";
    private String labelColanSpace = ": ";
    private String labelTax = "Tax";
    private String labelInclusive = "inclusive";
    private Context context;
    private boolean isFooterAdded = false;
    View.OnClickListener clickListener;
    AppSettings appSettings;

    public VoucherLineAdapter(Context context) {
        this.context = context;
        this.appSettings = AppUtils.getSettings(context);
        this.voucherLineList = new ArrayList<>();
        labelAddRow = AppUtils.getLocalizeString(context,"AddRow",labelAddRow);
        labelDescription = AppUtils.getLocalizeString(context,"Description",labelDescription)+labelColanSpace;
        labelVehicle = AppUtils.getLocalizeString(context,"Vehicle",labelVehicle)+labelColanSpace;
        labelTotalAmount = AppUtils.getLocalizeString(context,"Total",labelTotalAmount)+ " "+labelColanSpace;
        labelAmount = AppUtils.getLocalizeString(context,"Amount","Amount")+labelColanSpace;
        labelAmountTax = AppUtils.getLocalizeString(context,"Amount","Amount")+" + "+ AppUtils.getLocalizeString(context,"Tax","Tax")+labelColanSpace;
        labelTax = AppUtils.getLocalizeString(context,"Tax","Tax");
        labelInclusive = AppUtils.getLocalizeString(context,"Inclusive","inclusive");

    }

    public void setOnclickListener(View.OnClickListener listener)
    {
        this.clickListener=listener;
    }


    public List<VoucherLine> getItems() {
        return this.voucherLineList;
    }

    public void setItems(List<VoucherLine> list)
    {
        this.voucherLineList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case ITEM:
                View itemLayoutView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_voucher_line, parent,false);

                viewHolder = new VoucherLineViewHolder(itemLayoutView);
                break;
            case FOOTER:
                View loadingLayoutView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.voucher_line_footer, parent,false);

                viewHolder = new FooterViewHolder(loadingLayoutView);
                break;
        }

         return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, int position) {



        switch (getItemViewType(position)) {
            case ITEM:
                VoucherLineViewHolder holder = (VoucherLineViewHolder) vHolder;
                VoucherLine agreement = this.voucherLineList.get(position);
               bindItem(holder,agreement,position);
                break;
            case FOOTER:
//                Do nothing
                FooterViewHolder holder1 = (FooterViewHolder) vHolder;
                VoucherLine footer = this.voucherLineList.get(position);
                /*
                 if (isTaxInclusive)
            tax_percentage.setText(getlocalizeString("Tax","Tax") +": "+ agreement.getTaxPercent() + "%" + " "+getlocalizeString("Inclusive","inclusive"));
        else
            tax_percentage.setText(getlocalizeString("Tax","Tax") + ": "+agreement.getTaxPercent() + "%");

                 */
                if (appSettings.isTaxInclusive())
                {
                    String label = labelTax + " ("+appSettings.getTaxPercent()+"%"+labelInclusive+") : ";
                    holder1.tax.setText(label + getFormatedValue(footer.getTax()));
                    holder1.tax.setVisibility(View.VISIBLE);
                    holder1.total.setText(labelTotalAmount + "" + getFormatedValue(footer.getDebit()) );
                    setTextSpaneAbleBlack(holder1.tax,label.length()-1);
                }else {
                    holder1.tax.setVisibility(View.GONE);
                    holder1.total.setText(labelTotalAmount + "" + getFormatedValue(footer.getDebit()) + " + " + getFormatedValue(footer.getTax()) + " = " + getFormatedValue(footer.getTotalAmount()));
                }

                setTextSpaneAbleBlack(holder1.total,labelTotalAmount.length()-1);
                if(this.clickListener!=null)
                {
                    holder1.addRow.setOnClickListener(this.clickListener);
                }

                break;
        }

    }

    private String getFormatedValue(float input)
    {
       return String.format("%.2f", input);
    }


    @Override
    public int getItemViewType(int position) {
        return (position == voucherLineList.size() - 1 && isFooterAdded) ? FOOTER : ITEM;
    }

    @Override
    public int getItemCount() {
        return  voucherLineList == null ? 0 : voucherLineList.size();
    }

    // Helpers


    public void addBeforeFooter(VoucherLine agreement) {
        voucherLineList.add(voucherLineList.size()-1,agreement);
        notifyItemInserted(voucherLineList.size() - 2);
    }

    public void add(VoucherLine agreement) {
        voucherLineList.add(agreement);
        notifyItemInserted(voucherLineList.size() - 1);
    }


    public void addAll(List<VoucherLine> aList) {
        for (VoucherLine ag : aList) {
            add(ag);
        }

    }

    public void remove(VoucherLine agreement) {
        int position = voucherLineList.indexOf(agreement);
        if (position > -1) {
            voucherLineList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isFooterAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isFooterAdded = true;
        add(new VoucherLine());
    }

    public void removeLoadingFooter() {
        isFooterAdded = false;

        int position = voucherLineList.size() - 1;
        VoucherLine item = getItem(position);

        if (item != null) {
            voucherLineList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public VoucherLine getItem(int position) {
        return voucherLineList.get(position);
    }

    private void bindItem(VoucherLineViewHolder holder, VoucherLine voucherLine, int pos)
    {
        holder.description.setText(labelDescription +"" + voucherLine.getDescription());
        Vehicle vehicle = voucherLine.getVehicle();
        if (vehicle!=null){
        holder.vehicle.setText(labelVehicle+ ""+vehicle.getPlateNo() + " ("+vehicle.getMake()+" "+vehicle.getModel()+")");
        }else {
            holder.vehicle.setText(labelVehicle);
        }

        if (appSettings.isTaxInclusive())
        {
            holder.amount.setText(labelAmount + getFormatedValue(voucherLine.getDebit()) + " (" + appSettings.getTaxPercent() + "% tax inclusive)" );
            setTextSpaneAble(holder.amount,labelAmount.length()-1);
        }else {
            holder.amount.setText(labelAmountTax + getFormatedValue(voucherLine.getDebit()) + " + " + getFormatedValue(voucherLine.getTax()) + " = " + getFormatedValue(roundValue(voucherLine.getDebit() + voucherLine.getTax())));
            setTextSpaneAble(holder.amount,labelAmountTax.length()-1);
        }
        setTextSpaneAble(holder.description,labelDescription.length()-1);
        setTextSpaneAble(holder.vehicle,labelVehicle.length()-1);


        holder.more.setTag(pos);

        if(this.clickListener !=null)
        {
            holder.more.setOnClickListener(this.clickListener);
        }
    }

    private float roundValue(float amount)
    {
        float roundAmount =0;

        roundAmount=amount*100;
        roundAmount=(float) Math.round(roundAmount ) /(float) 100;

        return roundAmount;
    }

    private void setTextSpaneAble(TextView tv, int chr_count) {
        Spannable wordtoSpan = new SpannableString(tv.getText());

        //wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, chr_count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new StyleSpan(Typeface.BOLD),0,chr_count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(wordtoSpan);
    }
    private void setTextSpaneAbleBlack(TextView tv, int chr_count) {
        Spannable wordtoSpan = new SpannableString(tv.getText());

        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, chr_count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new StyleSpan(Typeface.BOLD),0,chr_count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(wordtoSpan);
    }
    private void setTextSpaneAble(TextView tv,int chr_count,int lastchrCount)
    {
        Spannable wordtoSpan = new SpannableString(tv.getText());
        wordtoSpan.setSpan(new StyleSpan(Typeface.BOLD),0,chr_count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.colorAccent)), tv.getText().length() - lastchrCount, tv.getText().length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setText(wordtoSpan);
    }

    // View Holders
    protected class VoucherLineViewHolder extends RecyclerView.ViewHolder {


        TextView description, vehicle, amount;
        View more;
        View row;

        public VoucherLineViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            description = itemLayoutView.findViewById(R.id.description);
            vehicle = itemLayoutView.findViewById(R.id.vehicle);
            amount = itemLayoutView.findViewById(R.id.amount);
            more = itemLayoutView.findViewById(R.id.more_menu);
            row = itemLayoutView.findViewById(R.id.row);
        }
    }

    protected class FooterViewHolder extends RecyclerView.ViewHolder {

        TextView addRow,total,tax;
        public FooterViewHolder(View itemView) {
            super(itemView);
            tax = itemView.findViewById(R.id.tax);
            addRow = itemView.findViewById(R.id.add_row);
            total = itemView.findViewById(R.id.total);
        }
    }
}
