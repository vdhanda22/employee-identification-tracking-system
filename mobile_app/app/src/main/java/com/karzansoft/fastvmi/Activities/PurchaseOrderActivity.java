package com.karzansoft.fastvmi.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karzansoft.fastvmi.Adapters.ContactSearchAdapter;
import com.karzansoft.fastvmi.Adapters.VoucherLineAdapter;
import com.karzansoft.fastvmi.DataBase.DatabaseManager;
import com.karzansoft.fastvmi.Dialogs.AddVoucherLineDialog;
import com.karzansoft.fastvmi.Dialogs.DatePicker;
import com.karzansoft.fastvmi.Enums.ContactType;
import com.karzansoft.fastvmi.Models.AppSettings;
import com.karzansoft.fastvmi.Models.Contact;
import com.karzansoft.fastvmi.Models.POSummary;
import com.karzansoft.fastvmi.Models.ServiceItem;
import com.karzansoft.fastvmi.Models.Vehicle;
import com.karzansoft.fastvmi.Models.VehicleLocation;
import com.karzansoft.fastvmi.Models.Voucher;
import com.karzansoft.fastvmi.Models.VoucherLine;
import com.karzansoft.fastvmi.Network.Entities.Request.SavePurchaseOrderRequest;
import com.karzansoft.fastvmi.Network.Entities.Response.LoginResponse;
import com.karzansoft.fastvmi.Network.WebResponse;
import com.karzansoft.fastvmi.Network.WebServiceFactory;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yasir on 19/12/2018.
 */

public class PurchaseOrderActivity extends AppCompatActivity implements View.OnClickListener{

    Calendar calendar;
    TextInputLayout dateInput,quotationInput,supplierInput,branchInput;
    Contact selectedSupplier;
    VehicleLocation selectedLocation;
    POSummary poSummary;
    ArrayList<VoucherLine> voucherLines = new ArrayList<>();

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Handler mHandler;
    VoucherLineAdapter voucherLineAdapter;
    AppSettings appSettings;
    ProgressDialog progressDialog;
    Gson gson;

    float tAmount,tTax;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(AppUtils.isRTLLanguageSelected(this))
            AppUtils.forceRTLIfSupported(this);
        super.onCreate(savedInstanceState);
        appSettings = AppUtils.getSettings(this);
        calendar = Calendar.getInstance();
        mHandler = new Handler(Looper.getMainLooper());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.purchase_order_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(AppUtils.isRTLLanguageSelected(this))
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(AppUtils.getLocalizeString(this,"CreateNewPurchaseOrder","Create New PO"));

        recyclerView = findViewById(R.id.recycler_view);
        dateInput = findViewById(R.id.dateContainer);
        quotationInput = findViewById(R.id.quotation_no_container);
        supplierInput = findViewById(R.id.supplierContainer);
        branchInput = findViewById(R.id.locationContainer);

         gson = new GsonBuilder().create();

        if(getIntent()!=null && getIntent().getExtras()!=null)
        {
            String json = getIntent().getStringExtra("PO");
            if (json!=null && json.length()>0)
            {
                poSummary = gson.fromJson(json,POSummary.class);
            }
        }

        ((AutoCompleteTextView) supplierInput.getEditText()).setAdapter(new ContactSearchAdapter(this, supplierInput.getEditText()));
        ((AutoCompleteTextView) supplierInput.getEditText()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSupplier = ((ContactSearchAdapter) parent.getAdapter()).getContactAt(position);
                if (supplierInput.getEditText().hasFocus())
                    AppUtils.hideKeyboardFrom(PurchaseOrderActivity.this, supplierInput.getEditText());

            }
        });

        voucherLineAdapter = new VoucherLineAdapter(this);
        voucherLineAdapter.setOnclickListener(this);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(voucherLineAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        setDate(dateInput.getEditText(),calendar);
        updateLocationInfo();
        updateSupplierInfo();
        loadVoucherLines();
        dateInput.getEditText().setOnClickListener(this);
        branchInput.getEditText().setOnClickListener(this);

        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppUtils.getLocalizeString(this,"Processing","Processing..."));


        progressDialog.setCancelable(false);

    }


    protected void updateLocationInfo()
    {

        LoginResponse loginInfo = AppUtils.getLoginInfo(this);
        if (loginInfo!=null && loginInfo.getLocationId() > 0 ){

            VehicleLocation loc = DatabaseManager.getInstance(getApplicationContext()).getLocationByID("" + loginInfo.getLocationId());
            if (loc != null)
            {   selectedLocation=loc;
                branchInput.getEditText().setText("" + loc.getName());
            }

        }
    }

    private void updateSupplierInfo()
    {
        if (poSummary!=null)
        {
            selectedSupplier = new Contact();
            selectedSupplier.setId((int) poSummary.getSupplierId());
            selectedSupplier.setName(""+poSummary.getSupplierName());
            supplierInput.getEditText().setText(""+poSummary.getSupplierName());
        }
    }

    private void loadVoucherLines()
    {
        if (poSummary==null || poSummary.getServiceItems() ==null)
            return;
        Vehicle vehicle = new Vehicle();
        vehicle.setId((int)poSummary.getVehicleId());
        vehicle.setPlateNo(poSummary.getPlateNo());
        vehicle.setMake(poSummary.getMake());
        vehicle.setModel(poSummary.getModel());
        for (ServiceItem item: poSummary.getServiceItems())
        {
            VoucherLine voucherLine = new VoucherLine();
            voucherLine.setVehicleId(poSummary.getVehicleId());
            voucherLine.setDescription(item.getName());

            voucherLine.setVehicle(vehicle);
            voucherLines.add(voucherLine);
        }

        if (voucherLines.size()>0)
            voucherLineAdapter.addAll(voucherLines);

        voucherLineAdapter.addLoadingFooter();

        updateTotalAmount();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.date:
                showDatePicker(dateInput.getEditText(),calendar);
                break;

            case R.id.location:
                AppUtils.showVehicleLocationList(branchInput.getEditText(), DatabaseManager.getInstance(getApplicationContext()).getVehiclLocations(), PurchaseOrderActivity.this);
                break;
            case R.id.add_row:

                final AddVoucherLineDialog addVoucherLineDialog = AddVoucherLineDialog.newInstance();
                addVoucherLineDialog.setButtonsClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            VoucherLine v = addVoucherLineDialog.getVoucherLine();
                            voucherLineAdapter.addBeforeFooter(v);
                            updateTotalAmount();

                        }catch (Exception ex){ex.printStackTrace();}
                    }
                });
                addVoucherLineDialog.show(getSupportFragmentManager(),"Add_Row");
                break;
            case R.id.more_menu:
                String[] options = new String[2];
                options[0] = AppUtils.getLocalizeString(this,"Edit","Edit");
                options[1] = AppUtils.getLocalizeString(this,"Delete","Delete");
                final int position = Integer.parseInt(view.getTag().toString());
                AppUtils.showOptionsList(view, options, PurchaseOrderActivity.this, new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Util.LogE("Item post",""+position);
                        try {
                            VoucherLine vitem = voucherLineAdapter.getItem(position);
                            switch (item.getItemId()) {
                                case 0:
                                    if (vitem != null) {
                                       showEditVoucherItemDialog(vitem,position);
                                    }
                                    break;
                                case 1:

                                        if (vitem != null) {
                                            voucherLineAdapter.remove(vitem);
                                            updateTotalAmount();
                                        }

                                    break;
                            }
                        }catch (Exception ex){ex.printStackTrace();}
                        return true;
                    }
                });
                break;
        }
    }

    private boolean validateFields()
    {


       if (selectedSupplier ==null || supplierInput.getEditText().getText().toString().trim().length() < 1 )
       {

           AppUtils.showToastMessage(AppUtils.getLocalizeString(this,"PleaseSelectAValidSupplier","Please select a valid Supplier"), this);
           return false;
       }

        if( selectedLocation ==null && !isValidLocationSelected())
        {
            AppUtils.showToastMessage(AppUtils.getLocalizeString(this,"LocationValidateMsg","Location is not selected Or invalid"), this);
            return false;
        }



        return true;
    }

    private void showEditVoucherItemDialog(VoucherLine voucherLine, final int position)
    {
        final AddVoucherLineDialog addVoucherLineDialog = AddVoucherLineDialog.newInstance(voucherLine);
        addVoucherLineDialog.setButtonsClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    VoucherLine v = addVoucherLineDialog.getVoucherLine();
                    if (!v.getNew())
                    {
                        VoucherLine dbItem = voucherLineAdapter.getItem(position);
                        if (v!=null && dbItem !=null)
                        {
                            dbItem.setDescription(v.getDescription());
                            dbItem.setVehicle(v.getVehicle());
                            dbItem.setDebit(v.getDebit());
                            dbItem.setTax(v.getTax());
                            dbItem.setVehicleId(dbItem.getVehicleId());

                            voucherLineAdapter.notifyItemChanged(position);
                            updateTotalAmount();

                        }
                    }

                }catch (Exception ex){ex.printStackTrace();}
            }
        });
        addVoucherLineDialog.show(getSupportFragmentManager(),"Edit_Row");
    }

    private void updateTotalAmount()
    {
        List<VoucherLine> items = voucherLineAdapter.getItems();

        if (items ==null)
            return;

        if ( items.size() <2 )
        {
            if (items.size() ==1)
            {
                VoucherLine voucherLine = voucherLineAdapter.getItem(0);

                if(voucherLine !=null)
                {
                    voucherLine.setTotalAmount(0f);
                    voucherLine.setTax(0f);
                    voucherLine.setDebit(0f);
                    tTax = 0;
                    tAmount = 0;
                    voucherLineAdapter.notifyDataSetChanged();
                }
            }
            return;
        }

        float amount = 0,tax=0;

        for (int i =0 ;i<= items.size() -2;i++) // ignoring footer item
        {
            amount += items.get(i).getDebit();
            tax += items.get(i).getTax();
        }

        float totalAmount = appSettings.isTaxInclusive()? amount : (amount+tax);

       VoucherLine voucherLine = voucherLineAdapter.getItem(items.size() -1);

       if(voucherLine !=null)
       {
           voucherLine.setDebit(roundValue(amount));
           voucherLine.setTax(roundValue(tax));
           voucherLine.setTotalAmount(roundValue(totalAmount));
           tTax = voucherLine.getTax();
           tAmount = voucherLine.getTotalAmount();
           voucherLineAdapter.notifyDataSetChanged();
           //voucherLineAdapter.notifyItemChanged(items.size()-1);
       }



    }

    private float roundValue(float amount)
    {
        float roundAmount =0;

        roundAmount=amount*100;
        roundAmount=(float) Math.round(roundAmount ) /(float) 100;

        return roundAmount;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

          getMenuInflater().inflate(R.menu.save_po, menu);
            MenuItem item= menu.findItem(R.id.action_save);
            item.setTitle(AppUtils.getLocalizeString(this,"Save","Save"));


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_save:

                if (!validateFields())
                    return true;

                savePurchaseOrder();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void savePurchaseOrder()
    {
        final Voucher voucher = new Voucher();

        voucher.setDate(AppUtils.getSelectedDateAndTime(calendar));
        voucher.setRefNo(quotationInput.getEditText().getText().toString());
        voucher.setTaxPercent(appSettings.getTaxPercent());
        voucher.setTax(roundValue(tTax));
        voucher.setAmount(roundValue(tAmount));
        if (selectedSupplier!=null)
        {
            voucher.setContactId(selectedSupplier.getId());
        }

        if (selectedLocation!=null)
        {
            try {
                voucher.setLocationId(Integer.parseInt(selectedLocation.getId()));
            }catch (NumberFormatException ex){ex.printStackTrace();}
        }

        if (voucherLineAdapter.getItemCount()>1)
        {
            List<VoucherLine> items = new ArrayList<>();

            for (int i=0; i<= voucherLineAdapter.getItems().size()-2;i++)
            {
                VoucherLine item =  voucherLineAdapter.getItems().get(i);
                VoucherLine itemToSave = new VoucherLine();
                itemToSave.setDescription(item.getDescription());
                itemToSave.setDebit(item.getDebit());
                itemToSave.setVehicleId(item.getVehicleId());
                itemToSave.setTax(item.getTax());

                items.add( itemToSave);
            }
            voucher.setVoucherLines(items);
        }


        SavePurchaseOrderRequest savePurchaseOrderRequest = new SavePurchaseOrderRequest(voucher);

        progressDialog.show();

        WebServiceFactory.getInstance().savePurchaseOrder(savePurchaseOrderRequest, AppUtils.getAuthToken(this)).enqueue(new Callback<WebResponse<Voucher>>() {
            @Override
            public void onResponse(Call<WebResponse<Voucher>> call, Response<WebResponse<Voucher>> response) {

                try {
                    progressDialog.dismiss();

                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().isSuccess()) {


                            Voucher savedVoucher = response.body().getResult();

                            sendBackVoucher(savedVoucher);


                        } else {
                            AppUtils.showToastMessage("" + response.body().getError().getMessage(), PurchaseOrderActivity.this);
                        }
                    } else {
                        AppUtils.showToastMessage(AppUtils.getLocalizeString(PurchaseOrderActivity.this, "InternalServerError", "Internal server error!"), PurchaseOrderActivity.this);
                    }

                }catch (Exception ex){ex.printStackTrace();}

            }

            @Override
            public void onFailure(Call<WebResponse<Voucher>> call, Throwable t) {

                try {
                    progressDialog.dismiss();
                    AppUtils.showToastMessage(AppUtils.getLocalizeString(PurchaseOrderActivity.this,"InternalServerError","Internal server error!"), PurchaseOrderActivity.this);
                }catch (Exception ex){}
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void sendBackVoucher(Voucher voucher)
    {
        if (voucher!=null)
        {
            String json = gson.toJson(voucher,Voucher.class);
            Intent data = new Intent();
            data.putExtra("VOUCHER",json);
            setResult(RESULT_OK,data);
        }else {
            setResult(RESULT_CANCELED);
        }

        finish();
    }


    protected boolean isValidLocationSelected()
    {
        ArrayList<VehicleLocation> locations= DatabaseManager.getInstance(getApplicationContext()).getVehiclLocations();
        if(locations.size()==1)
        {
            selectedLocation=locations.get(0);
            return true;
        }
        if( branchInput.getEditText().getText().length()<1)
            return false;
        selectedLocation= AppUtils.searchLocation(locations,branchInput.getEditText().getText().toString());
        if (selectedLocation == null)
            return false;
        return true;
    }

    private void  showDatePicker(final EditText editText, final Calendar calendar)
    {
        DatePicker datePicker=new DatePicker();
        Bundle args = new Bundle();
        args.putInt("day",  calendar.get(Calendar.DAY_OF_MONTH));
        args.putInt("month",  calendar.get(Calendar.MONTH));
        args.putInt("year",  calendar.get(Calendar.YEAR));
        datePicker.setArguments(args);
        datePicker.setCallBack(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.YEAR, year);

               setDate(editText,calendar);


            }
        });
        datePicker.show(getSupportFragmentManager(), "DatePicker");

    }

    private void setDate(final EditText editText, final Calendar calendar)
    {
        Date date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String selDate = formatter.format(date);
        editText.setText(""+selDate);
    }
}
