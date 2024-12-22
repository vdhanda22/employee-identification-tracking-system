package com.karzansoft.fastvmi.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karzansoft.fastvmi.Adapters.VehicleSearchAdapter;
import com.karzansoft.fastvmi.DataBase.DatabaseManager;
import com.karzansoft.fastvmi.Dialogs.DialogHelper;
import com.karzansoft.fastvmi.Fragments.RepairExteriorFragment;
import com.karzansoft.fastvmi.Fragments.RepairInteriorFragment;
import com.karzansoft.fastvmi.Models.MarkDetail;
import com.karzansoft.fastvmi.Models.MarkImage;
import com.karzansoft.fastvmi.Models.Vehicle;
import com.karzansoft.fastvmi.Network.APIError;
import com.karzansoft.fastvmi.Network.Entities.Request.VehicleMarksRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.VehicleMarksUpdateRequest;
import com.karzansoft.fastvmi.Network.ErrorUtils;
import com.karzansoft.fastvmi.Network.WebResponse;
import com.karzansoft.fastvmi.Network.WebServiceFactory;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Services.ImageSyncService;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Utils.Util;
import com.karzansoft.fastvmi.extended.CustomViewPager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yasir on 3/13/2017.
 */
public class InspectVehicleActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,View.OnClickListener{

    AutoCompleteTextView vehiclePlateNumber;
    CustomViewPager viewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    TabLayout tabLayout;
    int currentPage;
    ImageView smallDent,bigDent,smallScratch,largeScratch,crack,delete,imagesDetail;
    View smallDentSep,smallScratchSep,largeScratchSep;
    Vehicle selectedVehicle;
    ProgressDialog progressDialog;
    Handler mHandler;
    ArrayList<MarkImage> imagesToSync;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(selectedVehicle!=null)
        {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String vjson=gson.toJson(selectedVehicle,Vehicle.class);
            outState.putString("SELECTED_VEHICLE", vjson);

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspect_vehicle_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        vehiclePlateNumber=(AutoCompleteTextView)findViewById(R.id.vehicle_plate_number);
         viewPager=(CustomViewPager)findViewById(R.id.pager);
        viewPager.setPagingEnabled(false);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        smallDent=(ImageView)findViewById(R.id.img_small_dent);
        bigDent=(ImageView)findViewById(R.id.img_large_dent);
        smallScratch=(ImageView)findViewById(R.id.img_scratch_thin);
        largeScratch=(ImageView)findViewById(R.id.img_scratch);
        crack=(ImageView)findViewById(R.id.img_broken);
        imagesDetail=(ImageView)findViewById(R.id.img_info);
        delete=(ImageView)findViewById(R.id.img_del);
        smallDentSep=findViewById(R.id.small_dent_sep);
        smallScratchSep=findViewById(R.id.small_scratch_sep);
        largeScratchSep=findViewById(R.id.large_scratch_sep);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(AppUtils.getLocalizeString(this,"InspectVehicle","Inspect Vehicle"));
        TextInputLayout vehicleContainer = (TextInputLayout) findViewById(R.id.vehicle_plate_number_container);
        vehicleContainer.setHint(AppUtils.getLocalizeString(this,"SearchVehiclePlate","Search Vehicle"));
        vehiclePlateNumber.setAdapter(new VehicleSearchAdapter(this, true));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.addOnPageChangeListener(pagerListener);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);
        currentPage=0;

        smallDent.setOnClickListener(this);
        bigDent.setOnClickListener(this);
        smallScratch.setOnClickListener(this);
        largeScratch.setOnClickListener(this);
        crack.setOnClickListener(this);
        imagesDetail.setOnClickListener(this);
        delete.setOnClickListener(this);

        vehiclePlateNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedVehicle=  ((VehicleSearchAdapter)parent.getAdapter()).getVehicleAt(position);
                loadVehicle();
                if (vehiclePlateNumber.hasFocus())
                    AppUtils.hideKeyboardFrom(InspectVehicleActivity.this, vehiclePlateNumber);
            }
        });
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(AppUtils.getLocalizeString(this,"Loading","Loading..."));
        progressDialog.setCancelable(false);

        if(savedInstanceState!=null)
        {
            String vjson=savedInstanceState.getString("SELECTED_VEHICLE","");
            Gson gson = new GsonBuilder().create();
            if(vjson.length()>0)
            selectedVehicle=gson.fromJson(vjson,Vehicle.class);

           }

        mHandler=new Handler(Looper.getMainLooper());

    }




    ViewPager.OnPageChangeListener pagerListener= new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {

            currentPage=arg0;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        currentPage=tab.getPosition();
        toggleMenu();
        resetSelection();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void toggleMenu()
    {
        if(currentPage==0)
        {
            smallDent.setVisibility(View.VISIBLE);
            smallDentSep.setVisibility(View.VISIBLE);
            smallScratch.setVisibility(View.VISIBLE);
            smallScratchSep.setVisibility(View.VISIBLE);
            largeScratch.setVisibility(View.VISIBLE);
            largeScratchSep.setVisibility(View.VISIBLE);
        }else {
            smallDent.setVisibility(View.GONE);
            smallDentSep.setVisibility(View.GONE);
            smallScratch.setVisibility(View.GONE);
            smallScratchSep.setVisibility(View.GONE);
            largeScratch.setVisibility(View.GONE);
            largeScratchSep.setVisibility(View.GONE);
        }
    }

    public void resetSelection()
    {
        smallDent.setSelected(false);
        bigDent.setSelected(false);
        smallScratch.setSelected(false);
        largeScratch.setSelected(false);
        crack.setSelected(false);
    }

    @Override
    public void onClick(View v) {
        if(selectedVehicle==null)
            return;

        switch (v.getId())
        {
            case R.id.img_small_dent:
               ((RepairExteriorFragment)mSectionsPagerAdapter.instantiateItem(viewPager, 0)).addMark(Constant.SYMBOL_MARKER_SMALL_DENT);
                resetSelection();
                smallDent.setSelected(true);

                break;
            case R.id.img_large_dent:
                if(currentPage==0)
                ((RepairExteriorFragment)mSectionsPagerAdapter.instantiateItem(viewPager, 0)).addMark(Constant.SYMBOL_MARKER_LARGE_DENT);
                else if(currentPage==1)
                    ((RepairInteriorFragment)mSectionsPagerAdapter.instantiateItem(viewPager, 1)).addMark(Constant.SYMBOL_MARKER_LARGE_DENT);

                resetSelection();
                bigDent.setSelected(true);

                break;
            case R.id.img_scratch:
                ((RepairExteriorFragment)mSectionsPagerAdapter.instantiateItem(viewPager, 0)).addMark(Constant.SYMBOL_MARKER_SCRATCH);
                resetSelection();
                largeScratch.setSelected(true);

                break;
            case R.id.img_scratch_thin:
                ((RepairExteriorFragment)mSectionsPagerAdapter.instantiateItem(viewPager, 0)).addMark(Constant.SYMBOL_MARKER_SCRATCH_THIN);
                resetSelection();
                smallScratch.setSelected(true);
                break;
            case R.id.img_broken:
                if(currentPage==0)
                ((RepairExteriorFragment)mSectionsPagerAdapter.instantiateItem(viewPager, 0)).addMark(Constant.SYMBOL_MARKER_CRACK);
                else if(currentPage==1)
                ((RepairInteriorFragment)mSectionsPagerAdapter.instantiateItem(viewPager, 1)).addMark(Constant.SYMBOL_MARKER_CRACK);

                resetSelection();
                crack.setSelected(true);
                break;
            case R.id.img_info:
                if(currentPage==0)
                ((RepairExteriorFragment)mSectionsPagerAdapter.instantiateItem(viewPager, 0)).showSelectedMarkInfo();
                else if(currentPage==1)
                    ((RepairInteriorFragment)mSectionsPagerAdapter.instantiateItem(viewPager, 1)).showSelectedMarkInfo();

                break;
            case R.id.img_del:
                if(currentPage==0)
                ((RepairExteriorFragment)mSectionsPagerAdapter.instantiateItem(viewPager, 0)).removeSelectedMarker();
                else if(currentPage==1)
                    ((RepairInteriorFragment)mSectionsPagerAdapter.instantiateItem(viewPager, 1)).removeSelectedMarker();
                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_all, menu);
        menu.findItem(R.id.action_save).setTitle(AppUtils.getLocalizeString(this,"Save","Save"));

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                if(selectedVehicle==null)
                {
                    Toast.makeText(this,AppUtils.getLocalizeString(this,"PleaseSelectVehicleToSaveTtsMarks","Please select a vehicle to save it's marks"),Toast.LENGTH_LONG).show();
                    return false;
                }
                updateVehicleMarks();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateVehicleMarks()
    {

        final ArrayList<MarkDetail> markDetails=   ((RepairExteriorFragment)mSectionsPagerAdapter.instantiateItem(viewPager, 0)).getUpdatedMarks();
        if(markDetails!=null)
            markDetails.addAll(((RepairInteriorFragment)mSectionsPagerAdapter.instantiateItem(viewPager, 1)).getUpdatedMarks());

        VehicleMarksUpdateRequest vehicleMarksUpdateRequest=new VehicleMarksUpdateRequest(selectedVehicle.getId(),markDetails);
        Call<WebResponse<Object>> webResponseCall=WebServiceFactory.getInstance().updateVehicleMarks(vehicleMarksUpdateRequest,AppUtils.getAuthToken(this));
        progressDialog.setMessage(AppUtils.getLocalizeString(this,"UpdatingMarks","updating marks..."));
        progressDialog.show();

        webResponseCall.enqueue(new Callback<WebResponse<Object>>() {
            @Override
            public void onResponse(Call<WebResponse<Object>> call, Response<WebResponse<Object>> response) {
                progressDialog.dismiss();
                if(response.isSuccessful() && response.body()!=null && response.body().isSuccess())
                {
                    syncImages(markDetails);
                    DialogHelper.showAlert(InspectVehicleActivity.this, "", AppUtils.getLocalizeString(InspectVehicleActivity.this,"MarksUpdatedSuccessfully","Marks updated successfully"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           finish();
                            //loadVehicle();
                        }
                    });
                }else
                {
                    Toast.makeText(InspectVehicleActivity.this,AppUtils.getLocalizeString(InspectVehicleActivity.this,"ErrorInUpdatingVehicleMarks","Error in updating vehicle marks"),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<WebResponse<Object>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(InspectVehicleActivity.this,AppUtils.getLocalizeString(InspectVehicleActivity.this,"ErrorInUpdatingVehicleMarks","Error in updating vehicle marks"),Toast.LENGTH_LONG).show();
            }
        });


    }

    private void syncImages(ArrayList<MarkDetail> markDetails)
    {
        imagesToSync=new ArrayList<>();

        if (markDetails != null && markDetails.size() > 0)
        {
            for (int i = 0; i < markDetails.size(); i++) {
                MarkDetail det = markDetails.get(i);
                if (det.getImages() != null && det.getImages().size() > 0)
                    imagesToSync.addAll(det.getImages());
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseManager.getInstance(InspectVehicleActivity.this.getApplicationContext()).addMarkImage(imagesToSync);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent serviceIntent=new Intent(InspectVehicleActivity.this, ImageSyncService.class);
                        ContextCompat.startForegroundService(InspectVehicleActivity.this,serviceIntent);
                    }
                });

            }
        }).start();
    }

    private void loadVehicle()
    {
        if(selectedVehicle==null)
            return;
        resetSelection();
        VehicleMarksRequest vehicleMarksRequest=new VehicleMarksRequest(selectedVehicle.getId());

        Call<WebResponse<Vehicle>> responseCall= WebServiceFactory.getInstance().getVehicleMarks(vehicleMarksRequest,AppUtils.getAuthToken(this));
        progressDialog.setMessage(AppUtils.getLocalizeString(this,"LoadingVehicle","loading vehicle..."));
        progressDialog.show();

        responseCall.enqueue(new Callback<WebResponse<Vehicle>>() {
            @Override
            public void onResponse(Call<WebResponse<Vehicle>> call, Response<WebResponse<Vehicle>> response) {
                progressDialog.dismiss();
                if(response.isSuccessful() && response.body()!=null )
                {

                    if(response.body().isSuccess())
                    {
                        Vehicle vehicle=response.body().getResult();
                        ((RepairExteriorFragment)mSectionsPagerAdapter.instantiateItem(viewPager, 0)).reloadVehicleMarks(vehicle);
                        ((RepairInteriorFragment)mSectionsPagerAdapter.instantiateItem(viewPager, 1)).reloadVehicleMarks(vehicle);
                    }
                    else {
                        AppUtils.showToastMessage("" + response.body().getError().getMessage(), InspectVehicleActivity.this);
                    }
                }else
                {
                    String message = AppUtils.getLocalizeString(InspectVehicleActivity.this,"VehicleSavingError","Error in saving Vehicle!");
                    APIError error= ErrorUtils.parseError(response);
                    if(error!=null && error.message()!=null)
                        message=error.message();
                    AppUtils.showToastMessage(message, InspectVehicleActivity.this);
                }
            }

            @Override
            public void onFailure(Call<WebResponse<Vehicle>> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(InspectVehicleActivity.this, AppUtils.getLocalizeString(InspectVehicleActivity.this,"ErrorInLoadingMarks","Error in loading marks"),Toast.LENGTH_LONG).show();
            }
        });
    }


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment=null;

            switch (position) {
                case 0:
                    fragment = RepairExteriorFragment.newInstance();
                    Util.LogE("Pos",""+position);
                    break;

                case 1:
                    fragment = RepairInteriorFragment.newInstance();
                    Util.LogE("Pos",""+position);
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            String fragmentname;
            //		Log.v("getfragmentname", ""+pos);
            switch (position) {
                case 0:
                    fragmentname = AppUtils.getLocalizeString(InspectVehicleActivity.this,"Exterior","Exterior");
                    break;

                case 1:
                    fragmentname = AppUtils.getLocalizeString(InspectVehicleActivity.this,"Interior","Interior");
                    break;


                default: fragmentname =getString(R.string.app_name);
                    break;
            }
            return fragmentname;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


    }
}
