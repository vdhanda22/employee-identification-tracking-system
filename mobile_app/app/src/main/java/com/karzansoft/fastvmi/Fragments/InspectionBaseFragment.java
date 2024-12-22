package com.karzansoft.fastvmi.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karzansoft.fastvmi.Models.GeoLocation;
import com.karzansoft.fastvmi.Models.InspectionDto;
import com.karzansoft.fastvmi.Models.Vehicle;
import com.karzansoft.fastvmi.Network.APIError;
import com.karzansoft.fastvmi.Network.Entities.Request.GetQuestionsTemplateRequest;
import com.karzansoft.fastvmi.Network.Entities.Response.GetQuestionTemplateResponse;
import com.karzansoft.fastvmi.Network.ErrorUtils;
import com.karzansoft.fastvmi.Network.WebResponse;
import com.karzansoft.fastvmi.Network.WebServiceFactory;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yasir on 4/25/2016.
 */

@SuppressWarnings("ResourceType")
public class InspectionBaseFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute
    public final int PERMISSION_REQUEST_CODE = 100;
    public GetQuestionTemplateResponse QuestionsTemplateResponse;
    LinearLayout mItemsContainer;
    AutoCompleteTextView mVehiclenumber, mCustomerId, mCustomerName;
    TextInputLayout vehicleNumberContainer, fleetContainer, makeContainer, modelContainer, kmContainer;
    EditText mMake, mModel, mKm, scan_doc_type, mVehicleFleet;
    Button mNext;
    ImageView qrCodeReader;
    Handler mHandler;
    String operationTitle = "";
    Vehicle mSelectedVehicle;
    InspectionDto inspectionDto;
    boolean isOperationPermitted;
    LocationManager locationManager;
    int currentOperation;
    GeoLocation geoLocation;
    Gson gson;
    SwipeRefreshLayout swipeRefreshLayout;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String lat = Double.toString(location.getLatitude());
            String lon = Double.toString(location.getLongitude());
            geoLocation.setLatitude(lat);
            geoLocation.setLogitude(lon);
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        geoLocation = new GeoLocation();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        if (savedInstanceState != null) {
            String opres = savedInstanceState.getString("OperationResponse", "");
            if (opres.length() > 0)
                isOperationPermitted = savedInstanceState.getBoolean("IsOperationPermitted", false);
            operationTitle = savedInstanceState.getString("Title", "");
            currentOperation = savedInstanceState.getInt("CurrentOperation", 0);

            Util.LogE("Restore", opres);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        try {
            outState.putBoolean("IsOperationPermitted", isOperationPermitted);
            outState.putString("Title", operationTitle);
            outState.putInt("CurrentOperation", currentOperation);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.activity_inspection_vehicle_select, container, false);
        rootView.findViewById(R.id.appbar).setVisibility(View.GONE);

        vehicleNumberContainer = (TextInputLayout) rootView.findViewById(R.id.vehiclenumber_container);
        fleetContainer = (TextInputLayout) rootView.findViewById(R.id.vehicle_fleet_number_container);
        makeContainer = (TextInputLayout) rootView.findViewById(R.id.make_container);
        modelContainer = (TextInputLayout) rootView.findViewById(R.id.model_container);
        kmContainer = (TextInputLayout) rootView.findViewById(R.id.km_container);
        mItemsContainer = (LinearLayout) rootView.findViewById(R.id.itemsContainer);
        mVehiclenumber = (AutoCompleteTextView) rootView.findViewById(R.id.vehiclenumber);
        mVehicleFleet = (EditText) rootView.findViewById(R.id.vehicle_fleet_number);
        mMake = (EditText) rootView.findViewById(R.id.make);
        mModel = (EditText) rootView.findViewById(R.id.model);
        mNext = (Button) rootView.findViewById(R.id.vs_action_next);
        qrCodeReader = (ImageView) rootView.findViewById(R.id.qrcode);
        mKm = (EditText) rootView.findViewById(R.id.kilometer);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);

        return rootView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadViews(current_mode);
        getTitle(Constant.VehicleInspection);
        loadLanguageStrings();
        mNext.setEnabled(isOperationPermitted);
        mVehiclenumber.setOnClickListener(this);
        qrCodeReader.setOnClickListener(this);
        mNext.setOnClickListener(this);
        if (RequestPermission())
            AppUtils.checkLocationNetwork(getActivity());

        if (!AppUtils.isNetworkAvailable(getActivity()))
            AppUtils.showMessage(getlocalizeString("NetworkError", "Can't connect to network!"), mNext, Snackbar.LENGTH_SHORT);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.dividerColor);

    }

    public void loadLanguageStrings() {

        AppUtils.setTextHint(vehicleNumberContainer, getlocalizeString("SearchVehiclePlate", "Search Vehicle by Plate No"));
        AppUtils.setTextHint(fleetContainer, getlocalizeString("VehicleFleet", "Fleet No"));
        AppUtils.setTextHint(makeContainer, getlocalizeString("Make", "Make"));
        AppUtils.setTextHint(modelContainer, getlocalizeString("Model", "Model"));
        AppUtils.setTextHint(kmContainer, getlocalizeString("KMOut", "Mileage"));
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
        int loadMode = current_mode;
    }

    /*Web Services Call*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vs_action_next:
                Util.hideKeyboard(getActivity());
                int cMode = currentOperation;
                QuestionsFragment fragment = QuestionsFragment.newInstance(QuestionsTemplateResponse, inspectionDto);
                addFragment(fragment, fragment.getClass().getName());
                break;
            case R.id.qrcode:
                scanQrCOdeNow();
                break;

        }
    }

    /* End Web Services Call */

    protected void GetTemplateByVehicle(final GetQuestionsTemplateRequest request) {
        if (getActivity() == null)
            return;
        if (!AppUtils.isNetworkAvailable(getActivity())) {
            AppUtils.showMessage(getlocalizeString("NetworkError", "Can't connect to network!"), mNext, Snackbar.LENGTH_SHORT);
            return;
        }
        showProgress();
        Call<WebResponse<GetQuestionTemplateResponse>> responseCall = WebServiceFactory.getInstance().GetTemplateByVehicle(request, AppUtils.getAuthToken(getActivity()));
        responseCall.enqueue(new Callback<WebResponse<GetQuestionTemplateResponse>>() {
            @Override
            public void onResponse(Call<WebResponse<GetQuestionTemplateResponse>> call, Response<WebResponse<GetQuestionTemplateResponse>> response) {
                hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {

                        QuestionsTemplateResponse = response.body().getResult();
                    } else {
                        AppUtils.showMessage("" + response.body().getError().getMessage(), mNext, Snackbar.LENGTH_SHORT);
                    }
                } else {
                    if (response.code() == 401)
                        reLogin();
                    else {
                        String message = getlocalizeString("InternalServerError", "Internal server error!");
                        APIError error = ErrorUtils.parseError(response);
                        if (error != null && error.message() != null)
                            message = error.message();

                        AppUtils.showMessage(message, mNext, Snackbar.LENGTH_SHORT);
                    }
                }
            }

            @Override
            public void onFailure(Call<WebResponse<GetQuestionTemplateResponse>> call, Throwable t) {
                hideProgress();
                AppUtils.showMessage(getlocalizeString("ConnectionTimeOut", "Connection time out!"), mNext, Snackbar.LENGTH_SHORT);
            }
        });

    }

    public void scanQrCOdeNow() {
        FragmentIntentIntegrator integrator = new FragmentIntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    private void loadViews(int current_mode) {
        try {
            int id = getResources().getIdentifier("vehicle_select_" + current_mode, "array", getActivity().getApplicationContext().getPackageName());
            int[] views_visibilities = getResources().getIntArray(id);
            if (mItemsContainer.getChildCount() == views_visibilities.length) {
                for (int i = 0; i < mItemsContainer.getChildCount(); i++)
                    mItemsContainer.getChildAt(i).setVisibility(views_visibilities[i]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void setEditable(EditText field, boolean isEditable) {
        field.setFocusableInTouchMode(isEditable);
        field.setFocusable(isEditable);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        hideProgress();
        if (requestCode == 111) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle resultBundle = data.getExtras();
                mCustomerId.setText(resultBundle.getString("Number"));
                mCustomerName.setText(resultBundle.getString("Name"));
                int id = resultBundle.getInt("DOC_ID", 1);
                scan_doc_type.setText(getResources().getStringArray(R.array.doc_type)[id - 1]);
                // searchCustomer(id+1,resultBundle.getString("Number"));
            }
        } else {
            try {
                IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

                if (scanningResult != null && scanningResult.getContents() != null && scanningResult.getFormatName() != null) {
                    String scanContent = scanningResult.getContents();
                    Vehicle vehicle = AppUtils.parseQRCode(scanContent);
                    if (vehicle != null) {
                        mVehiclenumber.setText("" + vehicle.getPlateNo()+ " ("+vehicle.getFleetCode() +")");
                        mVehiclenumber.showDropDown();
                        mMake.setText("" + vehicle.getMake());
                        mModel.setText("" + vehicle.getModel());
                        mVehicleFleet.setText("" + vehicle.getFleetCode());
                        mSelectedVehicle = vehicle;
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
        int permissionReadStoarge = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStoarge = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionReadStoarge != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStoarge != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
        if (requestCode == PERMISSION_REQUEST_CODE) {
            Map<String, Integer> perms = new HashMap<>();
            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    AppUtils.checkLocationNetwork(getActivity());
                }
            }


        }
    }

    public void requestLocationUpdate() {
        try {
            boolean gps_enabled = false, network_enabled = false;
            try {
                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }
            try {
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
            }
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
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(false);
        //syncMasters();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
    }

}
