package com.karzansoft.fastvmi.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karzansoft.fastvmi.Adapters.ContactSearchAdapter;
import com.karzansoft.fastvmi.Adapters.DriverSearchAdapter;
import com.karzansoft.fastvmi.Adapters.VehicleSearchAdapter;
import com.karzansoft.fastvmi.DataBase.DBConstants;
import com.karzansoft.fastvmi.DataBase.DatabaseManager;
import com.karzansoft.fastvmi.Dialogs.AddVehicleDialog;
import com.karzansoft.fastvmi.MCCApplication;
import com.karzansoft.fastvmi.Models.Contact;
import com.karzansoft.fastvmi.Models.CustomerDocument;
import com.karzansoft.fastvmi.Models.GeoLocation;
import com.karzansoft.fastvmi.Models.MovementInfo;
import com.karzansoft.fastvmi.Models.OperationInfo;
import com.karzansoft.fastvmi.Models.TariffGroup;
import com.karzansoft.fastvmi.Models.Vehicle;
import com.karzansoft.fastvmi.Models.VehicleLocation;
import com.karzansoft.fastvmi.Models.VehicleModel;
import com.karzansoft.fastvmi.Models.VehicleStatus;
import com.karzansoft.fastvmi.Network.APIError;
import com.karzansoft.fastvmi.Network.Entities.Request.AddVehicleRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.CustomerSearchRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.ValidateOperationRequest;
import com.karzansoft.fastvmi.Network.Entities.Response.SaveVehicleReponse;
import com.karzansoft.fastvmi.Network.Entities.Response.ValidateOperationResponse;
import com.karzansoft.fastvmi.Network.ErrorUtils;
import com.karzansoft.fastvmi.Network.WebResponse;
import com.karzansoft.fastvmi.Network.WebResponseList;
import com.karzansoft.fastvmi.Network.WebServiceFactory;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Utils.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*import edu.sfsu.cs.orange.ocr.CaptureActivity;
import edu.sfsu.cs.orange.ocr.MRZParser;*/
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yasir on 4/25/2016
 * Changed by Rameel Hassan on 10/22/2020.
 */
@SuppressWarnings("ResourceType")
public class VehicleSelectFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public final int PERMISSION_REQUEST_CODE = 100;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute
    LinearLayout mItemsContainer, customerIdContainer;
    AutoCompleteTextView mVehiclenumber, mCustomerId, mCustomerName,/*mCustomerRefNum,mVehicleFleet,*//*mLocation,*/
            mDriverName, mEmail;
    TextInputLayout /*mLocationInput,*/mCustomeridInput, mCustomerNameInput,/*mCustomerRefNumInput,*//*mGeoLocationInput,*/
            mDriverContainer,mAgreementNoContainer,
            vehicleNumberContainer,/*fleetContainer,*/
            emailContainer, makeContainer, modelContainer, yearContainer, kmContainer,
            fuelContainer,/*nrmTypeContainer,*//*vehicleStatusContainer,*//*docTypeContainer,*/
            kmOutContainer, fuelOutContainer;
    EditText mMake, mModel, mYear, mKm, mFuel,mAgreementNo,/*nrm_type,*//*mVehicleStatus,*//*scan_doc_type,*/
            mKmOut, mFuelOut;
    //    TextView mAttachDocs;
    Button mNext;
    ImageView qrCodeReader/*,personalInfo*/;
    Handler mHandler;
    String operationTitle = "";
    Vehicle mSelectedVehicle;
    boolean isOperationPermitted;
    ValidateOperationResponse movementOperation;
    LocationManager locationManager;
    int currentOperation;
    VehicleLocation selectedLocation;
    Contact selectedDriver;
    OperationInfo operationInfo;
    MovementInfo movementInfo;
    GeoLocation geoLocation;
    Gson gson;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler=new Handler();
        movementInfo=new MovementInfo();
        operationInfo=new OperationInfo();
        geoLocation=new GeoLocation();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        if(savedInstanceState!=null)
        {
            String opres=savedInstanceState.getString("OperationResponse", "");
            if(opres.length()>0)
                movementOperation=gson.fromJson(opres,ValidateOperationResponse.class);
            String driver=savedInstanceState.getString("Contact", "");
            if(driver.length()>0)
                selectedDriver=gson.fromJson(driver,Contact.class);
            String loc=savedInstanceState.getString("Location", "");
            if(loc.length()>0)
                selectedLocation=gson.fromJson(loc,VehicleLocation.class);
            isOperationPermitted=savedInstanceState.getBoolean("IsOperationPermitted",false);
            operationTitle=savedInstanceState.getString("Title", "");
            currentOperation=savedInstanceState.getInt("CurrentOperation", 0);

            Util.LogE("Restore",opres);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        try {

            if(movementOperation!=null)
                outState.putString("OperationResponse",gson.toJson(movementOperation,ValidateOperationResponse.class));
            if (selectedDriver!=null)
                outState.putString("Contact",gson.toJson(selectedDriver,Contact.class));
            if(selectedLocation!=null)
                outState.putString("Location",gson.toJson(selectedLocation,VehicleLocation.class));
            outState.putBoolean("IsOperationPermitted",isOperationPermitted);
            outState.putString("Title", operationTitle);
            outState.putInt("CurrentOperation", currentOperation);
            Util.LogE("Saved", gson.toJson(movementOperation,ValidateOperationResponse.class));
        }catch (Exception ex) {ex.printStackTrace();}

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_vehicle_select, container, false);
        rootView.findViewById(R.id.appbar).setVisibility(View.GONE);

        vehicleNumberContainer = (TextInputLayout) rootView.findViewById(R.id.vehiclenumber_container);
//        fleetContainer = (TextInputLayout) rootView.findViewById(R.id.vehicle_fleet_number_container);
        emailContainer = (TextInputLayout) rootView.findViewById(R.id.customeremailContainer);
        makeContainer = (TextInputLayout) rootView.findViewById(R.id.make_container);
        modelContainer = (TextInputLayout) rootView.findViewById(R.id.model_container);
        yearContainer = (TextInputLayout) rootView.findViewById(R.id.year_container);
        mAgreementNoContainer = (TextInputLayout) rootView.findViewById(R.id.AgreementNoContainer);
        kmContainer = (TextInputLayout) rootView.findViewById(R.id.km_container);
        fuelContainer = (TextInputLayout) rootView.findViewById(R.id.fuel_container);
//        nrmTypeContainer = (TextInputLayout) rootView.findViewById(R.id.nrm_type_container);
//        vehicleStatusContainer = (TextInputLayout) rootView.findViewById(R.id.vehiclestatusContainer);
//        docTypeContainer = (TextInputLayout) rootView.findViewById(R.id.documentTypeContainer);
        kmOutContainer = (TextInputLayout) rootView.findViewById(R.id.km_out_container);
        fuelOutContainer = (TextInputLayout) rootView.findViewById(R.id.fuel_out_container);

        current_mode = this.getArguments().getInt(Constant.CURRENT_MODE, 1);
        mItemsContainer = (LinearLayout) rootView.findViewById(R.id.itemsContainer);
        mVehiclenumber = (AutoCompleteTextView) rootView.findViewById(R.id.vehiclenumber);
//        mVehicleFleet = (AutoCompleteTextView) rootView.findViewById(R.id.vehicle_fleet_number);
        mCustomerId = (AutoCompleteTextView) rootView.findViewById(R.id.customerid);
        mCustomerName = (AutoCompleteTextView) rootView.findViewById(R.id.customername);
//        mCustomerRefNum = (AutoCompleteTextView) rootView.findViewById(R.id.customerreferencenumber);
//        mLocation = (AutoCompleteTextView) rootView.findViewById(R.id.location);
        mDriverName = (AutoCompleteTextView) rootView.findViewById(R.id.driverrname);
//        mLocationInput = (TextInputLayout) rootView.findViewById(R.id.locationContainer);
        mCustomeridInput = (TextInputLayout) rootView.findViewById(R.id.customeridContainer);
        mCustomerNameInput = (TextInputLayout) rootView.findViewById(R.id.customernameContainer);
//        mCustomerRefNumInput = (TextInputLayout) rootView.findViewById(R.id.customerreferencenumberContainer);
        mDriverContainer = (TextInputLayout) rootView.findViewById(R.id.drivernameContainer);
        mMake = (EditText) rootView.findViewById(R.id.make);
        mModel = (EditText) rootView.findViewById(R.id.model);
        mYear = (EditText) rootView.findViewById(R.id.year);
        mAgreementNo = (EditText) rootView.findViewById(R.id.agreementNo);
        mNext = (Button) rootView.findViewById(R.id.vs_action_next);
        qrCodeReader = (ImageView) rootView.findViewById(R.id.qrcode);
//        personalInfo=(ImageView)rootView.findViewById(R.id.personal_info);
        mFuel = (EditText) rootView.findViewById(R.id.fuel);
        mKm = (EditText) rootView.findViewById(R.id.kilometer);
        mFuelOut = (EditText) rootView.findViewById(R.id.fuel_out);
        mKmOut = (EditText) rootView.findViewById(R.id.kilometer_out);
//        nrm_type = (EditText) rootView.findViewById(R.id.nrm_type);
//        mVehicleStatus = (EditText) rootView.findViewById(R.id.vehiclestatus);
//        mGeoLocationInput = (TextInputLayout) rootView.findViewById(R.id.geolocationContainer);
//        mAttachDocs=(TextView)rootView.findViewById(R.id.document);
//        scan_doc_type=(AutoCompleteTextView)rootView.findViewById(R.id.documenttype);
        mEmail = (AutoCompleteTextView) rootView.findViewById(R.id.customeremail);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        customerIdContainer = (LinearLayout) rootView.findViewById(R.id.customeridLayout);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadViews(current_mode);
        setTitle();
        loadLanguageStrings();
        if(operationTitle.length()>0)
        {
            getActionBar().setTitle(operationTitle);
            // mNext.setText(operationTitle);
        }
        mNext.setEnabled(isOperationPermitted);

//        mVehicleFleet.setOnClickListener(this);
        mVehiclenumber.setOnClickListener(this);
        mFuel.setOnClickListener(this);
//        nrm_type.setOnClickListener(this);
        qrCodeReader.setOnClickListener(this);
//        personalInfo.setOnClickListener(this);
//        mVehicleStatus.setOnClickListener(this);
        mNext.setOnClickListener(this);
//        mAttachDocs.setOnClickListener(this);
//        scan_doc_type.setOnClickListener(this);
//        mLocation.setOnClickListener(this);
        // mLocation.setAdapter(new LocationSearchAdapter(getActivity()));
        mDriverName.setAdapter(new ContactSearchAdapter(getActivity(), mDriverName));

//        mGeoLocationInput.getEditText().setEnabled(false);
//        personalInfo.setVisibility(View.INVISIBLE);
//        mVehicleFleet.addTextChangedListener(new GenericTextWatcher(mVehicleFleet));
        mVehiclenumber.addTextChangedListener(new GenericTextWatcher(mVehiclenumber));

        ArrayList<VehicleStatus> items = DatabaseManager.getInstance(getActivity().getApplicationContext()).getVehiclStatuses();
    /*    if(items.size()>0 && savedInstanceState==null)
            mVehicleStatus.setText(items.get(0).getName());

        if(mGeoLocationInput.getEditText().getText().toString().length()==0)
            mGeoLocationInput.getEditText().setText("loading...");
        */
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

      /*  mLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedLocation=((LocationSearchAdapter)parent.getAdapter()).getLocationAt(position);
                if (mLocation.hasFocus())
                    AppUtils.hideKeyboardFrom(getActivity(), mLocation);
            }
        });*/
       // validateDriver();
        mDriverName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDriver = ((ContactSearchAdapter) parent.getAdapter()).getContactAt(position);
                selectedDriver.setFirstName(selectedDriver.getName());
                if (mDriverName.hasFocus())
                    AppUtils.hideKeyboardFrom(getActivity(), mDriverName);
            }
        });

        Contact staff=DatabaseManager.getInstance(getActivity().getApplicationContext()).getDriverByID(AppUtils.getLoginStaffId(getActivity()));
        if(staff!=null)
        {
            selectedDriver=staff;
            if(staff.getName()!=null)
            mDriverName.setText(""+staff.getName());
        }

        if(RequestPermission())
            AppUtils.checkLocationNetwork(getActivity());

        if(!AppUtils.isNetworkAvailable(getActivity()))
            AppUtils.showMessage(getlocalizeString("NetworkError","Can't connect to network!"),mNext, Snackbar.LENGTH_SHORT);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.dividerColor);

    }


    public void loadLanguageStrings()
    {

        AppUtils.setTextHint(vehicleNumberContainer,getlocalizeString("SearchVehiclePlate","Search Vehicle by Plate No"));
//        AppUtils.setTextHint(fleetContainer,getlocalizeString("SearchVehicleFleet","Search Vehicle by Fleet No"));
        AppUtils.setTextHint(makeContainer, getlocalizeString("Make", "Make"));
        AppUtils.setTextHint(modelContainer, getlocalizeString("Model", "Model"));
        AppUtils.setTextHint(emailContainer, getlocalizeString("CustomerEmail", "Customer Email"));
//        AppUtils.setTextHint(docTypeContainer,getlocalizeString("Type","Type"));
        AppUtils.setTextHint(mCustomeridInput, getlocalizeString("DocumentNo", "Document No"));
        AppUtils.setTextHint(mCustomerNameInput, getlocalizeString("CustomerName", "Customer Name"));
//        AppUtils.setTextHint(mCustomerRefNumInput,getlocalizeString("RentalAgreementNo","Rental Agreement No"));
        AppUtils.setTextHint(mDriverContainer, getlocalizeString("Driver", "Driver/Staff Name"));
        AppUtils.setTextHint(kmContainer, getlocalizeString("KM", "Mileage"));
        AppUtils.setTextHint(fuelContainer, getlocalizeString("FuelLevel", "Fuel Level"));
//        AppUtils.setTextHint(mLocationInput,getlocalizeString("LocationIn","Branch In"));
//        AppUtils.setTextHint( mGeoLocationInput,getlocalizeString("GeoLocation","Geo Location"));
//        AppUtils.setTextHint(nrmTypeContainer,getlocalizeString("NRMType","NRM Type"));
//        AppUtils.setTextHint(vehicleStatusContainer,getlocalizeString("VehicleStatus","Vehicle Status"));
        AppUtils.setTextHint(kmOutContainer, getlocalizeString("KMOut", "Mileage Out"));
        AppUtils.setTextHint(fuelOutContainer, getlocalizeString("FuelLevelOut", "Fuel Level Out"));
//        AppUtils.setText(mAttachDocs,getlocalizeString("Documents","Documents"));
        AppUtils.setText(mNext, getlocalizeString("Next", "Next"));


    }

    @Override
    public void onResume() {
        super.onResume();
        requestLocationUpdate();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        int loadMode=current_mode;
       /* if(currentOperation>0)
            loadMode=currentOperation;*/
      /*  if(loadMode==Constant.RA_CHECKOUT||loadMode==Constant.REPLACEMENT_CHECKOUT || loadMode==Constant.NRM_CHECKOUT)
        inflater.inflate(R.menu.add_vehicle, menu);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_add:
                final AddVehicleDialog dialog=AddVehicleDialog.newInstance();
                dialog.setButtonsClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId())
                        {
                            case R.id.save:
                                if (dialog.edtPlateNo.getText().length()<1 || dialog.edtModal.getText().length()<1||dialog.edtYear.getText().length()<1)
                                {
                                    String msg="";
                                    if(dialog.edtPlateNo.getText().length()<1)
                                        msg="Please Enter Plate No";
                                    else if (dialog.edtModal.getText().length()<1)
                                        msg="Please Enter Make Model";
                                    else msg="Please Enter Make Year";
                                    AppUtils.showToastMessage(msg,getActivity());
                                }else {
                                    final VehicleModel model=dialog.selectedModel;
                                    if(model==null || !dialog.edtModal.getText().toString().equalsIgnoreCase(""+model.getName()+" "+model.getMake()))
                                    {
                                        AppUtils.showToastMessage("Please select a valid Make Model",getActivity());
                                        return;
                                    }
                                    Vehicle vehicle=new Vehicle();
                                    vehicle.setFleetCode("0");
                                    vehicle.setPlateNo(dialog.edtPlateNo.getText().toString());
                                    vehicle.setModelYear(dialog.edtYear.getText().toString());
                                    vehicle.setModelId(""+dialog.selectedModel.getId());
                                    final AddVehicleRequest request=new AddVehicleRequest();
                                    request.setVehicle(vehicle);
                                    showProgress();
                                    Call<WebResponse<SaveVehicleReponse>> saveRequest=WebServiceFactory.getInstance().saveVehicle(request,AppUtils.getAuthToken(getActivity()));
                                    saveRequest.enqueue(new Callback<WebResponse<SaveVehicleReponse>>() {
                                        @Override
                                        public void onResponse(Call<WebResponse<SaveVehicleReponse>> call, Response<WebResponse<SaveVehicleReponse>> response) {
                                           hideProgress();
                                            if(response.isSuccessful() && response.body()!=null)
                                            {
                                                if (response.body().isSuccess()) {
                                                    String fleetcode = "";
                                                    dialog.dismiss();
                                                    if (response.body().getResult() != null && response.body().getResult().getFleetCode() > 0)
                                                        fleetcode = "" + response.body().getResult().getFleetCode();
                                                    if (fleetcode.length() > 0) {
                                                        mVehiclenumber.setText("" + dialog.edtPlateNo.getText().toString());
                                                        mMake.setText("" + model.getMake());
                                                        mModel.setText("" + model.getName());
                                                        mYear.setText("" + dialog.edtYear.getText().toString());
//                                                        mVehicleFleet.setText("" + fleetcode);
                                                       final ValidateOperationRequest request = AppUtils.getOperationRequest(fleetcode, current_mode);

                                                        mHandler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                 validateOperation(request, current_mode);
                                                            }
                                                        }, 400);

                                                    }

                                                } else {
                                                    AppUtils.showToastMessage("" + response.body().getError().getMessage(), getActivity());
                                                }
                                            }else {
                                                String message = "Error in saving Vehicle!";
                                                APIError error= ErrorUtils.parseError(response);
                                                if(error!=null && error.message()!=null)
                                                    message=error.message();
                                                AppUtils.showToastMessage(message, getActivity());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<WebResponse<SaveVehicleReponse>> call, Throwable t) {
                                            hideProgress();
                                            String message = "Network error in saving Vehicle!";
                                            AppUtils.showToastMessage(message, getActivity());

                                        }
                                    });


                                }
                                break;
                            case R.id.cancel:
                                dialog.dismiss();
                                break;
                        }

                    }
                });
                dialog.show(getFragmentManager(),"AddVehicle");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    View.OnFocusChangeListener onFocusChangeListener=new View.OnFocusChangeListener() {// for vehicleNo and fleet no
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            final AutoCompleteTextView tv=(AutoCompleteTextView)v;
            if (hasFocus && tv.getText().length()<1)
            {
                if (tv.getId() == R.id.driverrname)
                    showDriverSuggestion(tv, 400);
                else
                    showVehicleSuggestion(tv, 400);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fuel:
                if (mKm.isFocusable())
                    AppUtils.showOptionsList(mFuel, getResources().getStringArray(R.array.fuel_levels), getActivity());
                break;
/*            case R.id.nrm_type:
                if (nrm_type.isClickable())
                    AppUtils.showOptionsList(nrm_type, getResources().getStringArray(R.array.nrm_type),getActivity());
                break;
                */
            case R.id.qrcode:
                scanQrCOdeNow();
                break;
/*            case R.id.vehiclestatus:
                AppUtils.showVehicleStatusList(mVehicleStatus, DatabaseManager.getInstance(getActivity().getApplicationContext()).getVehiclStatuses(), getActivity());
                break;
                */
     /*       case R.id.location:
                AppUtils.showVehicleLocationList(mLocation, DatabaseManager.getInstance(getActivity().getApplicationContext()).getVehiclLocations(), getActivity());
                break;*/
     /*          case R.id.personal_info:

             if (AppUtils.getDocType(getActivity(), Constant.PREF_KEY_DOC_PASS, true) && AppUtils.getDocType(getActivity(), Constant.PREF_KEY_DOC_NID, false)) {

                    final CharSequence[] items = {"Passport", "National Id"};
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(getActivity());
                    builder.setTitle("Select Document Type");

                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final int docT;
                            if (which == 0)
                                docT = MRZParser.SCAN_DOC_PASSPORT_ID;
                            else
                                docT = MRZParser.SCAN_DOC_NATIONAL_ID;
                            showProgress();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getActivity(), CaptureActivity.class);
                                    intent.putExtra("DOC_ID", docT);
                                    startActivityForResult(intent, 111);
                                }
                            }, 100);
                        }
                    });
                    builder.setPositiveButton("Cancel", null);
                    builder.show();
                } else {
                    final int docT;
                    if (AppUtils.getDocType(getActivity(), Constant.PREF_KEY_DOC_NID, false))
                        docT = MRZParser.SCAN_DOC_NATIONAL_ID;
                    else
                        docT = MRZParser.SCAN_DOC_PASSPORT_ID;

                    showProgress();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getActivity(), CaptureActivity.class);
                            intent.putExtra("DOC_ID", docT);
                            startActivityForResult(intent, 111);
                        }
                    }, 100);
                }

                break;

      */
            case R.id.vs_action_next:
                Util.hideKeyboard(getActivity());
                AppUtils.saveDocumetImages(getActivity(),((MCCApplication)getActivity().getApplication()).getSerializedDoc());
                int cMode = currentOperation;
                VehicleAccessoriesFragment fragment = VehicleAccessoriesFragment.newInstance(cMode, movementOperation,movementInfo);
                addFragment(fragment, fragment.getClass().getName());
                break;
/*
            case R.id.document:
                Intent i = new Intent(getActivity(), DocumentGridActivity.class);
                if(currentOperation==Constant.RA_CHECKIN || currentOperation==Constant.NRM_CHECKIN || currentOperation==Constant.REPLACEMENT_CHECKIN)
                {
                    if(movementOperation.getLastMovement()!=null)
                        i.putExtra("DOCUMENTS",AppUtils.convertToSerializeDocs(movementOperation.getLastMovement().getDocuments()));
                    else
                        i.putExtra("DOCUMENTS",AppUtils.convertToSerializeDocs(null));
                    i.putExtra("IS_READ_ONLY",true);

                }
                startActivity(i);
                break;
*/

/*            case R.id.documenttype:
                AppUtils.showOptionsList(scan_doc_type, getResources().getStringArray(R.array.doc_type), getActivity());
                break;*/
        }
    }

      /*Web Services Call*/

    protected void validateOperation(final ValidateOperationRequest request, final int OperationType)
    {
     if(getActivity()==null)
         return;
        if(!AppUtils.isNetworkAvailable(getActivity()))
        {
            AppUtils.showMessage(getlocalizeString("NetworkError","Can't connect to network!"),mNext,Snackbar.LENGTH_SHORT);
            return;
        }
        showProgress();
        Call<WebResponse<ValidateOperationResponse>> responseCall= WebServiceFactory.getInstance().validateOperation(request, AppUtils.getAuthToken(getActivity()));
        responseCall.enqueue(new Callback<WebResponse<ValidateOperationResponse>>() {
            @Override
            public void onResponse(Call<WebResponse<ValidateOperationResponse>> call, Response<WebResponse<ValidateOperationResponse>> response) {
                hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        movementOperation = response.body().getResult();
                        validateResponse(movementOperation, OperationType);
                        //validateDriver();

                    } else {
//                        AppUtils.showAlertWithEmailOption("Validation Error",response.body().getError().getMessage(),gson.toJson(request,ValidateOperationRequest.class),VehicleSelectFragment.this);
                        AppUtils.showMessage("" + response.body().getError().getMessage(), mNext, Snackbar.LENGTH_SHORT);
                    }
                }else
                {
                    if (response.code() == 401)
                        reLogin();
                    else
                    {
                        String message = getlocalizeString("InternalServerError","Internal server error!");
                        APIError error= ErrorUtils.parseError(response);
                        if(error!=null && error.message()!=null)
                            message=error.message();

                       // AppUtils.showAlertWithEmailOption("Validation Error",message,gson.toJson(request,ValidateOperationRequest.class),VehicleSelectFragment.this);

                        AppUtils.showMessage(message, mNext, Snackbar.LENGTH_SHORT);
                    }
                }
            }

            @Override
            public void onFailure(Call<WebResponse<ValidateOperationResponse>> call, Throwable t) {
                hideProgress();
                AppUtils.showMessage(getlocalizeString("ConnectionTimeOut","Connection time out!"), mNext, Snackbar.LENGTH_SHORT);
            }
        });

    }

    /* End Web Services Call */

    protected void validateResponse(ValidateOperationResponse response,int type) {

    }

    protected void downloadVehicleCardImages(Vehicle vehicle)
    {

        if (vehicle ==null)
            return;
        TariffGroup tariffGroup = vehicle.getTariffGroup();

        if (tariffGroup!=null)
        {
            if (tariffGroup.getExteriorDiagramName()!=null && tariffGroup.getExteriorDiagramName().length()>0)
            {
                ImageLoader.getInstance().loadImage(AppUtils.getImageUrlFromName(tariffGroup.getExteriorDiagramName()), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {}
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {Util.LogE("Image Loded",imageUri);}
                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {}
                });
            }

            if (tariffGroup.getInteriorDiagramName()!=null && tariffGroup.getInteriorDiagramName().length()>0)
            {
                ImageLoader.getInstance().loadImage(AppUtils.getImageUrlFromName(tariffGroup.getInteriorDiagramName()), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {}
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {Util.LogE("Image Loded",imageUri);}
                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {}
                });
            }
        }

    }

    public void validateDriver()
    {
       /* if(movementOperation!=null && movementOperation.getDriver()!=null) // CRS Case
        {
            mDriverContainer.setVisibility(View.VISIBLE);
            return;
        }*/

        if (DatabaseManager.getInstance(getActivity().getApplicationContext()).getRecordCount(DBConstants.TABLE_STAFF_MEMBER) > 1)
            mDriverContainer.setVisibility(View.VISIBLE);
        else {
            mDriverContainer.setVisibility(View.GONE);
            ArrayList<Contact> drivers = DatabaseManager.getInstance(getActivity().getApplicationContext()).getDrivers("1");
            if (drivers.size() > 0)
                selectedDriver = drivers.get(0);
        }

    }

    public void scanQrCOdeNow(){
        FragmentIntentIntegrator integrator = new FragmentIntentIntegrator(this);
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
            int id = getResources().getIdentifier("vehicle_select_" + current_mode, "array", getActivity().getApplicationContext().getPackageName());
            int[] views_visibilities = getResources().getIntArray(id);
            if (mItemsContainer.getChildCount() == views_visibilities.length) {
                for (int i = 0; i < mItemsContainer.getChildCount(); i++)
                    mItemsContainer.getChildAt(i).setVisibility(views_visibilities[i]);
            }
        }catch (Exception ex){ex.printStackTrace();}
    }

    protected void updateTitlesAndFields() {
        //mNext.setText(operationTitle);
        mNext.setEnabled(isOperationPermitted);
        getActionBar().setTitle(operationTitle);
        mCustomerId.setText("");
        mCustomerName.setText("");
//        mCustomerRefNum.setText("");
        mEmail.setText("");
        mKm.setText("");
        mFuel.setText("");
//        mLocationInput.getEditText().setText("");
    }

    protected void lockFields() {
        setEditable(mCustomeridInput.getEditText(), false);
        setEditable(mCustomerNameInput.getEditText(), false);
//        setEditable(mCustomerRefNumInput.getEditText(), false);
        setEditable(mEmail, false);
        setEditable(mFuel, false);
        setEditable(mKm, false);
//        setEditable(nrm_type, false);
//        setEditable(scan_doc_type, false);
//        nrm_type.setClickable(false);
//        scan_doc_type.setClickable(false);
//        personalInfo.setClickable(false);
     /*   if(DatabaseManager.getInstance(getActivity().getApplicationContext()).getVehiclLocations().size()<2)
            mLocationInput.setVisibility(View.GONE);
        else
            mLocationInput.setVisibility(View.VISIBLE);*/
    }

    protected boolean isValidLocationSelected()
    {
        ArrayList<VehicleLocation> locations=DatabaseManager.getInstance(getActivity().getApplicationContext()).getVehiclLocations();
        if(locations.size()==1)
        {
            selectedLocation=locations.get(0);
            return true;
        }

       /* if( mLocation.getText().length()<1)
            return false;
        selectedLocation= AppUtils.searchLocation(locations,mLocation.getText().toString());*/
        if (selectedLocation == null)
            return false;
        return true;
    }
    protected boolean isValidDriverSelected()
    {
        if(movementOperation!=null && movementOperation.getDriver()!=null) // CRS Case
            return true;

        if (selectedDriver != null && mDriverName.getText().toString().equalsIgnoreCase(selectedDriver.getName() ))
            return true;
        return false;
    }
    protected void updateVehicleInfo()
    {
        Vehicle vehicle = movementOperation.getVehicle();
        if (vehicle != null) {
            mKm.setText("" + vehicle.getkMs());
            int index = (int) (vehicle.getFuelLevel() * 8);
            String[] fuelLevels = getResources().getStringArray(R.array.fuel_levels);
            if ((index > -1 && index < fuelLevels.length))
                mFuel.setText("" + fuelLevels[index]);


        }
    }

    protected void showOutKMsAndFuel()
    {
        Vehicle vehicle = movementOperation.getVehicle();
        if (vehicle != null) {
            mKmOut.setText("" + vehicle.getkMs());
            int index = (int) (vehicle.getFuelLevel() * 8);
            String[] fuelLevels = getResources().getStringArray(R.array.fuel_levels);
            if ((index > -1 && index < fuelLevels.length))
                mFuelOut.setText("" + fuelLevels[index]);
        }
    }

    protected void updateLocationinfo()
    {
        Vehicle vehicle = movementOperation.getVehicle();
        if (vehicle != null) {
        VehicleLocation loc = DatabaseManager.getInstance(getActivity().getApplicationContext()).getLocationByID("" + vehicle.getLocationId());
        if (loc != null)
        {   selectedLocation=loc;
//            mLocationInput.getEditText().setText("" + loc.getName());
        }
        }
    }

    protected void updateCustomerInf()
    {
        if (movementOperation.getContact() != null) {
            Contact contact = movementOperation.getContact();
            CustomerDocument customerDocument = null;
            if (contact.getIdentityDocuments() != null && contact.getIdentityDocuments().size() > 0)
                customerDocument = contact.getIdentityDocuments().get(0);

            if (customerDocument != null && customerDocument.getDocumentNo() != null)
                mCustomerId.setText("" + customerDocument.getDocumentNo());
            if (contact.getName() != null)
                mCustomerName.setText("" + contact.getName());
            if (contact.getEmail() != null)
                mEmail.setText(contact.getEmail());
//
//                if (customerDocument!=null && customerDocument.getIdentityDocumentType() > 1 && customerDocument.getIdentityDocumentType() < 4) {
//                    String dt = getResources().getStringArray(R.array.doc_type)[customerDocument.getIdentityDocumentType() - 2];
//                    scan_doc_type.setText("" + dt);
//                }

        }

    }

    protected void updateCustomerDocsVisibility()
    {
        if (movementOperation.getContact() != null) {
        Contact contact = movementOperation.getContact();
        CustomerDocument customerDocument=null;
        if(contact.getIdentityDocuments()!=null && contact.getIdentityDocuments().size()>0 )
            customerDocument=contact.getIdentityDocuments().get(0);

        if (customerDocument!=null  && customerDocument.getIdentityDocumentType() > 1 && customerDocument.getIdentityDocumentType() < 4)
            customerIdContainer.setVisibility(View.VISIBLE);
        else
            customerIdContainer.setVisibility(View.GONE);
        }
    }

    protected void updateCustomerInfo(Contact contact)
    {
        CustomerDocument customerDocument=null;
        if (contact.getIdentityDocuments() != null && contact.getIdentityDocuments().size() > 0)
            customerDocument = contact.getIdentityDocuments().get(0);

        if (customerDocument != null && customerDocument.getIdentityDocumentType() > 1 && customerDocument.getIdentityDocumentType() < 4) {
            String dt = getResources().getStringArray(R.array.doc_type)[customerDocument.getIdentityDocumentType() - 2];
//            scan_doc_type.setText(""+dt);
        }
        if (customerDocument != null && customerDocument.getDocumentNo() != null)
            mCustomerId.setText(customerDocument.getDocumentNo());
//        if(contact.getName()!=null)
//            mCustomerName.setText(contact.getName());
        if (contact.getEmail() != null)
            mEmail.setText(contact.getEmail());

        if (mCustomerName.hasFocus())
            AppUtils.hideKeyboardFrom(getActivity(), mCustomerName);
        else if (mEmail.hasFocus())
            AppUtils.hideKeyboardFrom(getActivity(), mEmail);

        if (mEmail.getText().toString().length() > 0)
            setEditable(mEmail, false);
        else
            setEditable(mEmail, true);

    }

    protected void updateReferenceNo() {/*
        if (movementOperation.getRefNo() != null)
            mCustomerRefNum.setText(""+ movementOperation.getRefNo());*/
    }

    protected void setEditable(EditText field,boolean isEditable)
    {
        field.setFocusableInTouchMode(isEditable);
        field.setFocusable(isEditable);
    }

    public void searchCustomer(int dType,String documentNo) {
        if (!AppUtils.isNetworkAvailable(getActivity()))
            return;
        showProgress();
        CustomerSearchRequest request = new CustomerSearchRequest();
//        request.setDocumentType(Integer.valueOf(dType));
//        request.setDocumentNo(documentNo);
        Call<WebResponse<WebResponseList<Contact>>> responseCall = WebServiceFactory.getInstance().searchCustomer(request, AppUtils.getAuthToken(getActivity()));
        responseCall.enqueue(new Callback<WebResponse<WebResponseList<Contact>>>() {
            @Override
            public void onResponse(Call<WebResponse<WebResponseList<Contact>>> call, Response<WebResponse<WebResponseList<Contact>>> response) {
                hideProgress();
                try {
                    if (response.isSuccessful() && response.body().getResult() != null) {
                        ArrayList<Contact> arrayList = response.body().getResult().getItems();
                        if (arrayList != null && arrayList.size() > 0)
                            updateCustomerInfo(arrayList.get(0));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<WebResponse<WebResponseList<Contact>>> call, Throwable t) {
                hideProgress();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        hideProgress();
        if (requestCode == 111) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle resultBundle = data.getExtras();
                mCustomerId.setText(resultBundle.getString("Number"));
                mCustomerName.setText(resultBundle.getString("Name"));
                int id=resultBundle.getInt("DOC_ID",1);
//                scan_doc_type.setText(getResources().getStringArray(R.array.doc_type)[id-1]);
                searchCustomer(id+1,resultBundle.getString("Number"));
            }
        } else {
            try {
                IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

                if (scanningResult != null && scanningResult.getContents() != null && scanningResult.getFormatName() != null) {
                    String scanContent = scanningResult.getContents();
                    Vehicle vehicle= AppUtils.parseQRCode(scanContent);
                    if (vehicle !=null) {
                        mVehiclenumber.setText("" + vehicle.getPlateNo() + " ("+vehicle.getFleetCode() +")");
                        mMake.setText("" + vehicle.getMake());
                        mModel.setText("" + vehicle.getModel());
                        mYear.setText("" + vehicle.getModelYear());
//                        mVehicleFleet.setText(""+vehicle.getFleetCode());
                        mSelectedVehicle=vehicle;

                        ValidateOperationRequest request=new ValidateOperationRequest();
                        request.setFleetCode(mSelectedVehicle.getFleetCode());
                        request.setPlateNo(mSelectedVehicle.getPlateNo());
                        request.setMake(mSelectedVehicle.getMake());
                        request.setModel(mSelectedVehicle.getModel());
                        request.setMovementTypeId(-1);
                        request.setOperationType(-1);
                        request.setCheckType(-1);
                        request.setMovementCategory(-1);
                        if(current_mode== Constant.RA_CHECKOUT)
                        {
                            request.setMovementTypeId(Constant.RA_CHECKOUT);
                            request.setCheckType(1);
                            validateOperation(request, Constant.RA_CHECKOUT);
                        }else if(current_mode== Constant.RA_CHECKIN)
                        {
                            request.setMovementTypeId(Constant.RA_CHECKOUT);
                            request.setCheckType(2);
                            validateOperation(request, Constant.RA_CHECKIN);
                        }

                        else if(current_mode== Constant.REPLACEMENT_CHECKIN)
                        {
                            request.setMovementTypeId(Constant.RA_CHECKOUT);
                            request.setOperationType(2);
                            request.setCheckType(2);
                            validateOperation(request, Constant.REPLACEMENT_CHECKIN);
                        }else if(current_mode== Constant.REPLACEMENT_CHECKOUT)
                        {
                            request.setMovementTypeId(Constant.RA_CHECKOUT);
                            request.setOperationType(2);
                            request.setCheckType(1);
                            validateOperation(request, Constant.REPLACEMENT_CHECKOUT);
                        }else if(current_mode== Constant.NRM_CHECKOUT)
                        {
                            request.setCheckType(1);
                            request.setMovementCategory(2);
                            validateOperation(request, Constant.NRM_CHECKOUT);
                        }
                        else if(current_mode== Constant.NRM_CHECKIN)
                        {
                            request.setCheckType(2);
                            request.setMovementCategory(2);
                            validateOperation(request, Constant.NRM_CHECKIN);
                        } else if(current_mode== Constant.GARAGE_CHECKOUT)
                        {
                            request.setMovementTypeId(3);
                            request.setCheckType(1);
                            request.setMovementCategory(2);
                            validateOperation(request, Constant.GARAGE_CHECKOUT);
                        }
                        else if(current_mode== Constant.GARAGE_CHECKIN)
                        {
                            request.setMovementTypeId(3);
                            request.setCheckType(2);
                            request.setMovementCategory(2);
                            validateOperation(request, Constant.GARAGE_CHECKIN);
                        }


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

// Geo Locations
    private boolean RequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        int permissionCamera = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE )
        {
            Map<String, Integer> perms = new HashMap<>();
            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if(perms.get(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED )
                {
                    AppUtils.checkLocationNetwork(getActivity());
                }
            }


        }
    }

    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String lat = Double.toString(location.getLatitude());
            String lon = Double.toString(location.getLongitude());
            geoLocation.setLatitude(lat);
            geoLocation.setLogitude(lon);
            /*if(geoLocation.getWebID()==null)
             getPlaceId(lat, lon);*/
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };


    private void requestLocationUpdate()
    {
        try {
            boolean gps_enabled = false, network_enabled = false;
            try {
                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {}
            try {
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {}
            Location location = null;
            if (network_enabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                if (locationManager != null)
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } else if (gps_enabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                if (locationManager != null)
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (location != null) {
                String lat = Double.toString(location.getLatitude());
                String lon = Double.toString(location.getLongitude());
                geoLocation.setLatitude(lat);
                geoLocation.setLogitude(lon);
               // getPlaceId(lat, lon);
                }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getPlaceId(final String lat,final String lon)
    {
        if(!AppUtils.isNetworkAvailable(getActivity()))
        {
            AppUtils.showMessage("Can't connect to network!",mNext,Snackbar.LENGTH_SHORT);
            return;
        }
        if (FirebaseAuth.getInstance().getCurrentUser() ==null)
            return;

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful())
                {
                    Util.LogE("User Token",""+task.getResult().getToken());
                    String auth = "Bearer "+task.getResult().getToken();

                    WebServiceFactory.getInstance().getPlaceIdByLoc(lat+","+lon,"50",auth).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful())
                            {
                                try {
                                    String json = response.body().string();
                                    JSONObject resObject = new JSONObject(json);
                                    JSONObject place=resObject.getJSONArray("results").getJSONObject(0);
                                    String currentPlaceId=place.getString("place_id");
//                                    mGeoLocationInput.getEditText().setText(place.getString("name") + " " + place.getString("vicinity"));
                                    geoLocation.setTitle(place.getString("name"));
                                    geoLocation.setWebID(currentPlaceId);
                                    geoLocation.setType(2);
                                    geoLocation.setDetail(place.getString("name") + " " + place.getString("vicinity"));
                                }catch (Exception ex) {}
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(false);
        syncMasters();
    }

    public void showVehicleSuggestion(final AutoCompleteTextView tv, final int afterMilis)
    {
        try {

            ((VehicleSearchAdapter) tv.getAdapter()).getFilter().filter("suggest", new Filter.FilterListener() {
                @Override
                public void onFilterComplete(int count) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           try{
                            tv.showDropDown();
                           }catch (Exception ex){}
                        }
                    }, afterMilis);

                }
            });
        }catch (Exception ex){ex.printStackTrace();}
    }

    public void showDriverSuggestion(final AutoCompleteTextView tv, final int afterMilis)
    {
        ((ContactSearchAdapter) tv.getAdapter()).getFilter().filter("suggest", new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tv.showDropDown();
                        } catch (Exception ex) {
                        }
                    }
                }, afterMilis);

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mHandler!=null)
            mHandler.removeCallbacksAndMessages(null);
    }

    class GenericTextWatcher implements TextWatcher
    {

        View view;
        public GenericTextWatcher(View view){this.view=view;}
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            try {

                String text = s.toString();
                TextInputLayout layout;
                switch (view.getId()) {
                    case R.id.vehicle_fleet_number:
                        layout = (TextInputLayout) view.getParent();
                        if (text.length() > 0)
                            layout.setHint(getlocalizeString("VehicleFleetNo", getString(R.string.vs_prompt_vehicle_fleet_number)));
                        else {
                            layout.setHint(getlocalizeString("SearchVehicleFleet", getString(R.string.vs_prompt_search_vehicle_fleet_number)));
                            showVehicleSuggestion((AutoCompleteTextView) view, 10);
                        }
                        break;
                    case R.id.vehiclenumber:
                        layout = (TextInputLayout) view.getParent();
                        if (text.length() > 0)
                            layout.setHint(getlocalizeString("VehiclePlateNo", getString(R.string.vs_prompt_vehicle_number)));
                        else {
                            layout.setHint(getlocalizeString("SearchVehiclePlate", getString(R.string.vs_prompt_search_vehicle_number)));
                            showVehicleSuggestion((AutoCompleteTextView) view, 10);
                        }
                        break;
                }
            }catch (Exception ex){}

        }
    }
}
