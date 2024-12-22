package com.karzansoft.fastvmi.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.Constant;

import java.util.Arrays;
import java.util.List;


@SuppressWarnings("ResourceType")
public class VehicleSelectActivity extends AppCompatActivity {

    private static final String[] DUMMY_VEHICLES_FLEET = new String[]{
            "12336", "12115"
    };
    private static final String[] DUMMY_VEHICLES = new String[]{
            "12345-H", "12145-J"
    };

    AutoCompleteTextView mVehiclenumber = null/*,mCustomerId,mCustomerName,mCustomerRefNum,mVehicleFleet*/;
    //    TextInputLayout mLocationInput,mCustomeridInput,mCustomerNameInput,mCustomerRefNumInput;
    LinearLayout /*mCutomerinfoLayout,*/mItemsContainer;
    EditText mMake = null;
    EditText mModel = null;
    EditText mYear = null;
    EditText spinner/*,nrm_type*/;
    Button mNext = null;
    ImageView qrCodeReader/*,personalInfo*/;
    ProgressDialog mProgress;
    Handler mHandler;
    private boolean IsCheckout = true;
    int current_mode, selected_vehicle_index = 1;
    TextView attachDocs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_select);
        IsCheckout = getIntent().getBooleanExtra("IsCheckout", true);
        current_mode=getIntent().getIntExtra(Constant.CURRENT_MODE,1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Processing... ");
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mHandler = new Handler();
        mItemsContainer = (LinearLayout) findViewById(R.id.itemsContainer);
        mVehiclenumber = (AutoCompleteTextView) findViewById(R.id.vehiclenumber);
//        mVehicleFleet = (AutoCompleteTextView) findViewById(R.id.vehicle_fleet_number);
//        mCustomerId = (AutoCompleteTextView) findViewById(R.id.customerid);
//        mCustomerName = (AutoCompleteTextView) findViewById(R.id.customername);
//        mCustomerRefNum = (AutoCompleteTextView) findViewById(R.id.customerreferencenumber);
//        mLocationInput = (TextInputLayout) findViewById(R.id.locationContainer);
//        mCustomeridInput = (TextInputLayout) findViewById(R.id.customeridContainer);
//        mCustomerNameInput = (TextInputLayout) findViewById(R.id.customernameContainer);
//        mCustomerRefNumInput = (TextInputLayout) findViewById(R.id.customerreferencenumberContainer);
//        mCutomerinfoLayout=(LinearLayout)findViewById(R.id.customeridLayout);
        mMake = (EditText) findViewById(R.id.make);
        mModel = (EditText) findViewById(R.id.model);
        mYear = (EditText) findViewById(R.id.year);
        mNext = (Button) findViewById(R.id.vs_action_next);
        qrCodeReader = (ImageView) findViewById(R.id.qrcode);
//        personalInfo=(ImageView)findViewById(R.id.personal_info);
//        attachDocs=(TextView)findViewById(R.id.document);
        spinner = (EditText) findViewById(R.id.fuel);
//        nrm_type = (EditText) findViewById(R.id.nrm_type);


        if (current_mode == Constant.RA_CHECKOUT) {
            setTitle("Check Out");
        } else if (current_mode == Constant.RA_CHECKIN) {
            setTitle("Check In");
        }
        else if(current_mode==Constant.REPLACEMENT_CHECKIN)
        {
            setTitle("Replacement (In)");
        }
        else if(current_mode==Constant.REPLACEMENT_CHECKOUT)
        {
            setTitle("Replacement (Out)");
            /*mCutomerinfoLayout.setVisibility(View.GONE);
            mCustomerName.setVisibility(View.GONE);
            mCustomerRefNum.setVisibility(View.GONE);
            attachDocs.setVisibility(View.INVISIBLE);*/

        }

        else if(current_mode==Constant.NRM_CHECKOUT) {
            setTitle("NRM Out");
          /*  mCustomerRefNum.setVisibility(View.GONE);
            attachDocs.setVisibility(View.INVISIBLE);*/
//            mCustomeridInput.setHint("Staff ID");
//            mCustomerNameInput.setHint("Staff Name");

//            mLocationInput.setHint("Destination Location");

        }else if(current_mode==Constant.NRM_CHECKIN) {
            setTitle("NRM In");
           /* mCustomerRefNum.setVisibility(View.GONE);
            attachDocs.setVisibility(View.INVISIBLE);*/
//            mCustomeridInput.setHint("Staff ID");
//            mCustomerNameInput.setHint("Staff Name");
        }

       /* else if(current_mode==Constant.GARAGE_CHECKOUT)
        {
            setTitle("Garage Check Out");
            mCustomerRefNumInput.setHint("Workshop");
            attachDocs.setVisibility(View.INVISIBLE);
            mCustomeridInput.setHint("Staff ID");
            mCustomerNameInput.setHint("Staff Name");


        }else if(current_mode==Constant.GARAGE_CHECKIN)
        {
            setTitle("Garage Check In");
            mCustomerRefNumInput.setHint("Workshop");
            attachDocs.setVisibility(View.INVISIBLE);
            mCustomeridInput.setHint("Staff ID");
            mCustomerNameInput.setHint("Staff Name");
        }*/



     /*   // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.fuel_levels, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/


        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 String[] items=getResources().getStringArray(R.array.fuel_levels);
                PopupMenu popup = new PopupMenu(VehicleSelectActivity.this, spinner);
                //popup.getMenuInflater().inflate(R.menu.menu_mark_view, popup.getMenu());
                for (int i=0;i<items.length;i++)
                    popup.getMenu().add(items[i]);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        spinner.setText("" + item.getTitle());
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
/*

        nrm_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] items=getResources().getStringArray(R.array.nrm_type);
                PopupMenu popup = new PopupMenu(VehicleSelectActivity.this, nrm_type);
                //popup.getMenuInflater().inflate(R.menu.menu_mark_view, popup.getMenu());
                for (int i=0;i<items.length;i++)
                    popup.getMenu().add(items[i]);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        nrm_type.setText("" + item.getTitle());
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
*/


        attachDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VehicleSelectActivity.this, DocumentGridActivity.class);
                startActivity(i);
            }
        });


        qrCodeReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scanNow(null);
                    }
                }, 100);

            }
        });

/*        personalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      *//*  Intent intent = new Intent(VehicleSelectActivity.this, CaptureActivity.class);
                        VehicleSelectActivity.this.startActivityForResult(intent, 111);*//*
                    }
                }, 100);

            }
        });*/
/*
        mVehicleFleet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                String selected = (String) adapter.getItemAtPosition(pos);
                selected_vehicle_index = pos;

                if (current_mode == Constant.RA_CHECKOUT) {

                    if (selected_vehicle_index == 0) {
                        setTitle("Check In");
                        mNext.setText("Check In");
                    } else {
                        setTitle("Check Out");
                        mNext.setText("Check Out");
                    }

                } else if (current_mode == Constant.NRM_CHECKOUT) {
                    if (selected_vehicle_index == 0) {
                        setTitle("NRM In");
                        mNext.setText("NRM In");
                    } else {
                        setTitle("NRM Out");
                        mNext.setText("NRM Out");
                    }
                }
                mMake.setText("Toyota");
                mModel.setText("Corolla");
                mYear.setText("2015");
            }

        });*/

        mVehiclenumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                String selected = (String) adapter.getItemAtPosition(pos);
                selected_vehicle_index = pos;

                if (current_mode == Constant.RA_CHECKOUT) {

                    if (selected_vehicle_index == 0) {
                        setTitle("Check In");
                        mNext.setText("Check In");
                    } else {
                        setTitle("Check Out");
                        mNext.setText("Check Out");
                    }

                } else if (current_mode == Constant.NRM_CHECKOUT) {
                    if (selected_vehicle_index == 0) {
                        setTitle("NRM In");
                        mNext.setText("NRM In");
                    } else {
                        setTitle("NRM Out");
                        mNext.setText("NRM Out");
                    }
                }
                mMake.setText("Toyota");
                mModel.setText("Corolla");
                mYear.setText("2015");
            }

        });



        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int cMode = current_mode;

                if (current_mode == Constant.RA_CHECKOUT) {


                    if (selected_vehicle_index == 0) {
                        cMode = Constant.RA_CHECKIN;
                    } else {
                        cMode = Constant.RA_CHECKOUT;
                    }

                } else if (current_mode == Constant.NRM_CHECKOUT) {
                    if (selected_vehicle_index == 0) {
                        cMode = Constant.NRM_CHECKIN;
                    } else {
                        cMode = Constant.NRM_CHECKOUT;
                    }
                }

                Intent intent = new Intent(VehicleSelectActivity.this, VehicleAccessoriesActivity.class);
                intent.putExtra("IsCheckout", IsCheckout);
                intent.putExtra(Constant.CURRENT_MODE, cMode);
                startActivityForResult(intent, 1);

            }
        });



        addVehiclesToAutoComplete(Arrays.asList(DUMMY_VEHICLES));
        loadViews(current_mode);


    }

    public void scanNow(View view){

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(true );

        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }





    private void loadViews(int current_mode)
    {

        try {
            int id = getResources().getIdentifier("vehicle_select_" + current_mode, "array", getApplicationContext().getPackageName());
            int[] views_visibilities = getResources().getIntArray(id);
            Log.e("Size", "" + mItemsContainer.getChildCount() + " " + views_visibilities.length);
            if (mItemsContainer.getChildCount() == views_visibilities.length) {
                for (int i = 0; i < mItemsContainer.getChildCount(); i++)
                    mItemsContainer.getChildAt(i).setVisibility(views_visibilities[i]);
            }

        }catch (Exception ex){ex.printStackTrace();}
    }





    private void addVehiclesToAutoComplete(List<String> vehicleCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(VehicleSelectActivity.this,
                        android.R.layout.simple_dropdown_item_1line, vehicleCollection);

        mVehiclenumber.setAdapter(adapter);

      /*  ArrayAdapter<String> adapterFleet =
                new ArrayAdapter<>(VehicleSelectActivity.this,
                        android.R.layout.simple_dropdown_item_1line, Arrays.asList(DUMMY_VEHICLES_FLEET));

        mVehicleFleet.setAdapter(adapterFleet);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

       if(mProgress!=null && mProgress.isShowing())
           mProgress.cancel();

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                //String result=data.getStringExtra("result");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",IsCheckout);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

       else if (requestCode == 111) {
            if(resultCode == Activity.RESULT_OK) {
                Bundle resultBundle = data.getExtras();
//               mCustomerId.setText(resultBundle.getString("Number"));
//                mCustomerName.setText(resultBundle.getString("Name"));
            }

        }

        else{

            try {
                IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

                if (scanningResult != null && scanningResult.getContents() != null && scanningResult.getFormatName() != null) {
                    //we have a result
                    String scanContent = scanningResult.getContents();
                    String scanFormat = scanningResult.getFormatName();

                    String[] parts = scanContent.split(",");
                    if (parts.length == 4) {
                        mVehiclenumber.setText("" + parts[0]);
                        mMake.setText("" + parts[1]);
                        mModel.setText("" + parts[2]);
                        mYear.setText("" + parts[3]);
                    }

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }//onActivityResult

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id==android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}