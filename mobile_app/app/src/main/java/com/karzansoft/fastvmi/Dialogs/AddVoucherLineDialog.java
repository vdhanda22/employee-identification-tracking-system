package com.karzansoft.fastvmi.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karzansoft.fastvmi.Adapters.VehicleSearchAdapter;
import com.karzansoft.fastvmi.Models.AppSettings;
import com.karzansoft.fastvmi.Models.Vehicle;
import com.karzansoft.fastvmi.Models.VoucherLine;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;

import java.util.HashMap;

/**
 * Created by Yasir on 7/21/2016.
 */
public class AddVoucherLineDialog extends AppCompatDialogFragment {
    View.OnClickListener clickListener;
    public TextInputLayout edtDescription,edtAmount,edtTax,edtVehicle;

    public Vehicle selectedVehicle;
    TextInputLayout vehicleContainer;
    Button btn_cancel,btn_save;
    HashMap<String,String> languageTexts;
    boolean isNew = true;
    AppSettings appSettings;

    public static AddVoucherLineDialog newInstance(/*boolean isNew,String descr ,St*/) {
        AddVoucherLineDialog frag = new AddVoucherLineDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    public static AddVoucherLineDialog newInstance(VoucherLine voucherLine) {
        AddVoucherLineDialog frag = new AddVoucherLineDialog();
        Bundle args = new Bundle();
        Gson gson = new GsonBuilder().create();
        args.putString("VL",gson.toJson(voucherLine,VoucherLine.class));


        frag.setArguments(args);
        return frag;
    }


 public String getLocalizeString(String key,String defValue)
 {
     if(languageTexts!=null && languageTexts.get(key)!=null)
     {
         return languageTexts.get(key);

     }
     return defValue;
 }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        appSettings = AppUtils.getSettings(getActivity());
        String json = this.getArguments().getString("VL","");
        VoucherLine voucherLine = null;
        if (json !=null && json.length() >0)
        {
            isNew = false;
            Gson gson = new GsonBuilder().create();
            voucherLine = gson.fromJson(json,VoucherLine.class);
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        languageTexts= AppUtils.getLanguageStrings(getActivity());
        View dialogView= inflater.inflate(R.layout.add_voucher_line, null);

        AlertDialog.Builder dialog;

        String title = getLocalizeString("AddRow","Add Row");

        if (!isNew)
        {
            title = getLocalizeString("EditRow","Edit Row");
        }



            dialog= new AlertDialog.Builder(getActivity())

                    .setTitle(title)
                    .setView(dialogView)
                /*    .setPositiveButton("Save", clickListener)
                    .setNegativeButton("Cancel",null
                    )*/;
        edtDescription = dialogView.findViewById(R.id.descriptionContainer);
        edtVehicle = dialogView.findViewById(R.id.vehicleContainer);

        ((AutoCompleteTextView)edtVehicle.getEditText()).setAdapter(new VehicleSearchAdapter(getActivity(),true,edtVehicle));
        edtAmount = dialogView.findViewById(R.id.amountContainer);
        edtTax = dialogView.findViewById(R.id.taxContainer);
        btn_cancel=(Button)dialogView.findViewById(R.id.cancel);
        btn_save=(Button)dialogView.findViewById(R.id.save);

        if (voucherLine!=null)
        {
            edtDescription.getEditText().setText(""+voucherLine.getDescription());
            edtAmount.getEditText().setText(""+getFormatedValue(voucherLine.getDebit()));
            edtTax.getEditText().setText(""+getFormatedValue(voucherLine.getTax()));
            if (voucherLine.getVehicle()!=null)
            {
                edtVehicle.getEditText().setText(""+voucherLine.getVehicle().getPlateNo());
                selectedVehicle = voucherLine.getVehicle();
            }

        }


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(!validateField(edtAmount))
                    return;

                clearFocus(edtDescription.getEditText());
                clearFocus(edtAmount.getEditText());
                clearFocus(edtTax.getEditText());

                dismiss();
                if (clickListener!=null)
                {
                    clickListener.onClick(view);
                }
            }
        });

        edtAmount.getEditText().addTextChangedListener(textWatcher);

        AppUtils.setTextHint(edtDescription,getLocalizeString("Description","Description"));
        AppUtils.setTextHint(edtVehicle,getLocalizeString("Vehicle","Vehicle"));
        AppUtils.setTextHint(edtAmount,getLocalizeString("Amount","Amount"));
        AppUtils.setTextHint(edtTax,getLocalizeString("Tax","Tax"));
        AppUtils.setText(btn_cancel,getLocalizeString("Cancel","Cancel"));
        AppUtils.setText(btn_save,getLocalizeString("Add","Add"));
        if (!isNew)
        {
            AppUtils.setText(btn_save,getLocalizeString("Update","Update"));
        }

        ((AutoCompleteTextView)edtVehicle.getEditText()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedVehicle = ((VehicleSearchAdapter) parent.getAdapter()).getVehicleAt(position);
                if ( edtVehicle.getEditText().hasFocus())
                    AppUtils.hideKeyboardFrom(getActivity(), edtVehicle.getEditText());
            }
        });


        return dialog.create();
    }

    public VoucherLine getVoucherLine()
    {
        VoucherLine voucherLine = new VoucherLine();
        voucherLine.setDescription(""+edtDescription.getEditText().getText().toString());
        if (selectedVehicle!=null && edtVehicle.getEditText().getText().toString().trim().length()>0){
        voucherLine.setVehicleId(selectedVehicle.getId());
        voucherLine.setVehicle(selectedVehicle);
        }

        String amount = edtAmount.getEditText().getText().toString().trim();
        if (amount.length()>0)
        {
            try {
                voucherLine.setDebit(Float.parseFloat(amount));
            }catch (NumberFormatException ex){ex.printStackTrace();}
        }

        String tax = edtTax.getEditText().getText().toString().trim();
        if (tax.length()>0)
        {
            try {
                voucherLine.setTax(Float.parseFloat(tax));
            }catch (NumberFormatException ex){ex.printStackTrace();}
        }

        voucherLine.setNew(isNew);

        return voucherLine;
    }


    private float getTaxAmount(float taxAbleAmount)
    {
        float totalTax = 0;

        if (appSettings.isTaxInclusive())
        {
            float chargesWithoutTax = (taxAbleAmount * 100) / (100 + appSettings.getTaxPercent());   //where taxableCharges are equal to chargesWithTax
            totalTax = taxAbleAmount - chargesWithoutTax;

        }else
            {
                totalTax=taxAbleAmount*appSettings.getTaxPercent()/100;
            }
        totalTax=totalTax*100;
        totalTax=(float) Math.round(totalTax ) /(float) 100;

        return totalTax;
    }



    public void setButtonsClickListener( View.OnClickListener clickListener)
    {
        this.clickListener=clickListener;
    }

    private boolean validateField(TextInputLayout nameContainer) {
        nameContainer.getEditText().requestFocus();
        if (nameContainer.getEditText().getText().toString().trim().isEmpty()) {
            String msg=nameContainer.getHint().toString();
            nameContainer.setError("Please enter "+msg.substring(0,msg.length()));
            requestFocus(nameContainer.getEditText());
            return false;
        } else {
            nameContainer.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void clearFocus(View view)
    {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String amount = edtAmount.getEditText().getText().toString().trim();
            if (amount.length()>0)
            {
                try {
                    float tax = getTaxAmount(Float.parseFloat(amount));
                    edtTax.getEditText().setText(""+getFormatedValue(tax));
                }catch (NumberFormatException ex){ex.printStackTrace();}
            }
        }
    };

    private String getFormatedValue(float input)
    {
        return String.format("%.2f", input);
    }
}
