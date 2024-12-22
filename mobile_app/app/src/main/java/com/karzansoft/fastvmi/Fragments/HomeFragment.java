package com.karzansoft.fastvmi.Fragments;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.karzansoft.fastvmi.MCCApplication;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;

/**
 * Created by Yasir on 4/5/2016.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    Handler mHandler;
    TextView /*txtAddVehicle,*/txtAgreementCheckOut, txtAgreementCheckIn, txtStaffCheckOut, txtStaffCheckIn,
    /*txtReport,txtHowTo,*/txtContactUs, txtVersion, txtWorkshopOut, txtWorkshopIn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.content_home, container, false);
        txtVersion = (TextView) rootView.findViewById(R.id.txt_version);
//        txtAddVehicle=(TextView)rootView.findViewById(R.id.txt_add_vehicle);
        txtAgreementCheckOut = (TextView) rootView.findViewById(R.id.txt_agreement_checkout);
        txtAgreementCheckIn = (TextView) rootView.findViewById(R.id.txt_agreement_checkin);
        txtStaffCheckOut = (TextView) rootView.findViewById(R.id.txt_staff_checkout);
        txtStaffCheckIn = (TextView) rootView.findViewById(R.id.txt_staff_checkin);
//        txtReport=(TextView)rootView.findViewById(R.id.txt_report);
//        txtHowTo=(TextView)rootView.findViewById(R.id.txt_howto);
        txtContactUs = (TextView) rootView.findViewById(R.id.txt_email);
        txtWorkshopOut = rootView.findViewById(R.id.txt_workshop_out);
        txtWorkshopIn = rootView.findViewById(R.id.txt_workshop_in);

        isGooglePlayServicesAvailable();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateText();

        mHandler = new Handler(Looper.getMainLooper());

//        view.findViewById(R.id.add_vehicle).setOnClickListener(this);
        view.findViewById(R.id.checkout_vehicle).setOnClickListener(this);
        view.findViewById(R.id.checkin_vehicle).setOnClickListener(this);
        view.findViewById(R.id.checkout_vehicle_staff).setOnClickListener(this);
        view.findViewById(R.id.checkin_vehicle_staff).setOnClickListener(this);
//        view.findViewById(R.id.txt_reports).setOnClickListener(this);
//        view.findViewById(R.id.txt_howtous).setOnClickListener(this);
        view.findViewById(R.id.checkin_vehicle_workshop).setOnClickListener(this);
        view.findViewById(R.id.checkout_vehicle_workshop).setOnClickListener(this);
        //companyCode.setText("Company code: "+ AppUtils.getTenantName(getActivity()));
    }

    private void updateText() {
        getActivity().setTitle(AppUtils.getLocalizeString(getActivity(), "VMI", "VMI"));
//        txtAddVehicle.setText(getlocalizeString("AddVehicle","Add Vehicle"));
        txtAgreementCheckOut.setText(getlocalizeString("AgreementCheckOut", "Agreement Check-Out"));
        txtAgreementCheckIn.setText(getlocalizeString("AgreementCheckIn", "Agreement Check-In"));
        txtStaffCheckOut.setText(getlocalizeString("StaffCheckOut", "Staff Check-Out"));
        txtStaffCheckIn.setText(getlocalizeString("StaffCheckIn", "Staff Check-In"));
        txtWorkshopOut.setText(getlocalizeString("WorkshopCheckOut", "Workshop Check-Out"));
        txtWorkshopIn.setText(getlocalizeString("WorkshopCheckIn", "Workshop Check-In"));
//        txtReport.setText(getlocalizeString("Reports","Reports "));
//        txtHowTo.setText(getlocalizeString("HowToUse","How to use"));
        txtContactUs.setText(getlocalizeString("ContactUsMessage", "Contact us for queries and suggestions"));

        try {
            String versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            txtVersion.setText(AppUtils.getLocalizeString(getActivity(), "Version", "Version") + " " + versionName);
        } catch (PackageManager.NameNotFoundException ex) {
            txtVersion.setText("");
        }
    }


    private void setTextSpaneAble(TextView tv)
    {
        Spannable wordtoSpan = new SpannableString(tv.getText());

        wordtoSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.colorPrimary)), tv.getText().length() - 3, tv.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setText(wordtoSpan);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
/*            case R.id.add_vehicle:
                isMovement(false);
                AddVehicleFragment fragment= AddVehicleFragment.newInstance();
                addFragment(fragment, "" + fragment.getClass().getName());*//*
                break;
                */
            case R.id.checkout_vehicle:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaseFragment fragment = AgreementFragment.newInstance(Constant.RA_CHECKOUT);
                        addFragment(fragment, "" + fragment.getClass().getName());
                    }
                }, 350);
                ((MCCApplication)getActivity().getApplication()).removeAllSymbols();// clear previous data before starting new operation
                ((MCCApplication)getActivity().getApplication()).clearDocuments();
                break;
            case R.id.checkin_vehicle:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaseFragment fragment = AgreementFragment.newInstance(Constant.RA_CHECKIN);
                        addFragment(fragment, "" + fragment.getClass().getName());
                    }
                }, 350);
                ((MCCApplication)getActivity().getApplication()).removeAllSymbols();// clear previous data before starting new operation
                ((MCCApplication)getActivity().getApplication()).clearDocuments();
                break;
            case R.id.checkout_vehicle_staff:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NRMFragment fragment = NRMFragment.newInstance(Constant.NRM_CHECKOUT);
                        addFragment(fragment, "" + fragment.getClass().getName());
                    }
                }, 350);
                ((MCCApplication)getActivity().getApplication()).removeAllSymbols();
                ((MCCApplication)getActivity().getApplication()).clearDocuments();
                break;
            case R.id.checkin_vehicle_staff:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NRMFragment fragment = NRMFragment.newInstance(Constant.NRM_CHECKIN);
                        addFragment(fragment, "" + fragment.getClass().getName());
                    }
                }, 350);
                ((MCCApplication)getActivity().getApplication()).removeAllSymbols();
                ((MCCApplication)getActivity().getApplication()).clearDocuments();
                break;
            case R.id.checkout_vehicle_workshop:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GarageFragment fragment = GarageFragment.newInstance(Constant.GARAGE_CHECKOUT);
                        addFragment(fragment, "" + fragment.getClass().getName());
                    }
                }, 350);
                ((MCCApplication)getActivity().getApplication()).removeAllSymbols();
                ((MCCApplication)getActivity().getApplication()).clearDocuments();
                break;

            case R.id.checkin_vehicle_workshop:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GarageFragment fragment = GarageFragment.newInstance(Constant.GARAGE_CHECKIN);
                        addFragment(fragment, "" + fragment.getClass().getName());
                    }
                }, 350);
                ((MCCApplication)getActivity().getApplication()).removeAllSymbols();
                ((MCCApplication)getActivity().getApplication()).clearDocuments();
                break;

     /*       case R.id.txt_reports:

                DialogHelper.showReportsDialog(getActivity());

                break;

            case R.id.txt_howtous:

                try{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.speedautosystems.com/how-to-use-vmi/"));
                    startActivity(browserIntent);
                }catch (Exception ex){}
                break;*/
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getActivity());
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result,
                        1111).show();
            }

            return false;
        }

        return true;
    }
}
