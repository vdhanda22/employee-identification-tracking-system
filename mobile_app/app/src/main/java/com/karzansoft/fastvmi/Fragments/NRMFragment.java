package com.karzansoft.fastvmi.Fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.karzansoft.fastvmi.Adapters.VehicleSearchAdapter;
import com.karzansoft.fastvmi.DataBase.DatabaseManager;
import com.karzansoft.fastvmi.Dialogs.AlertDialogFragment;
import com.karzansoft.fastvmi.Models.MovementInfo;
import com.karzansoft.fastvmi.Models.VehicleLocation;
import com.karzansoft.fastvmi.Network.Entities.Request.ValidateOperationRequest;
import com.karzansoft.fastvmi.Network.Entities.Response.ValidateOperationResponse;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;

import java.util.ArrayList;

/**
 * Created by Yasir on 4/25/2016.
 */
public class NRMFragment extends VehicleSelectFragment implements AdapterView.OnItemClickListener{

    /*TextInputLayout vehiclestatusContainer;*/

    public static NRMFragment newInstance(int current_mode) {
        NRMFragment f = new NRMFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.CURRENT_MODE, current_mode);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=super.onCreateView(inflater, container, savedInstanceState);
//        vehiclestatusContainer=(TextInputLayout)rootView.findViewById(R.id.vehiclestatusContainer);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        mCustomeridInput.setHint(getlocalizeString("StaffId","Staff ID"));
//        mCustomerNameInput.setHint(getlocalizeString("StaffName","Staff Name"));


        if (current_mode == Constant.NRM_CHECKOUT) {
//            mLocationInput.setHint(getlocalizeString("DestinationLocation","Destination Location"));
        } else {
//            mLocationInput.setHint(getlocalizeString("Location","Location"));
            mDriverContainer.setHint(getlocalizeString("DriverName", "Driver Name"));
        }

//        mCustomerRefNumInput.setHint(getlocalizeString("RentalAgreementNo","Rental Agreement No"));

//        mVehicleFleet.setOnItemClickListener(this);
        mVehiclenumber.setOnItemClickListener(this);
        mVehiclenumber.setAdapter(new VehicleSearchAdapter(getActivity(), true));
//        mVehicleFleet.setAdapter(new VehicleSearchAdapter(getActivity(), false));
        if(currentOperation>0)
            resetFields();
//        mVehicleFleet.setOnFocusChangeListener(onFocusChangeListener);
        mVehiclenumber.setOnFocusChangeListener(onFocusChangeListener);
        mDriverName.setOnFocusChangeListener(onFocusChangeListener);
        //validateDriver();

        ArrayList<VehicleLocation> locations= DatabaseManager.getInstance(getActivity().getApplicationContext()).getVehiclLocations();

        if(locations.size()<2)
        {
//            mLocationInput.setVisibility(View.GONE);
            if(locations.size()==1)
                selectedLocation=locations.get(0);
        }

    }

    @Override
    protected void validateResponse(ValidateOperationResponse response, int type) {
        super.validateResponse(response, type);

        String msg=response.getReason();
        if(msg==null || msg.length()<1)
            msg=getlocalizeString("VehicleNotAvailable","Selected vehicle is not available for this operation");
        switch (response.getPermittedCheckType()) {

            case Constant.CHECKIN:
                if (response.isPermitted()) {
                    isOperationPermitted = true;
                    operationTitle = getTitle(Constant.NRM_CHECKIN);
                    currentOperation = Constant.NRM_CHECKIN;
                    updateTitlesAndFields();
                    resetFields();
                    downloadVehicleCardImages(movementOperation.getVehicle());

                } else {
                    currentOperation = 0;
                    isOperationPermitted = false;
                    setTitle();
                    //mNext.setText("Next");
                    mNext.setEnabled(isOperationPermitted);
                    AlertDialogFragment alert = AlertDialogFragment.newInstance(getlocalizeString("ValidationError","Validation Error"), msg, getlocalizeString("Ok","Ok"), false);
                    alert.show(getFragmentManager(), "detailAlert");

                }
                break;
            case Constant.CHECKOUT:
                if (response.isPermitted()) {
                    isOperationPermitted = true;
                    operationTitle = getTitle(Constant.NRM_CHECKOUT);
                    currentOperation = Constant.NRM_CHECKOUT;
                    updateTitlesAndFields();
                    resetFields();
                    downloadVehicleCardImages(movementOperation.getVehicle());

                } else {
                    currentOperation = 0;
                    isOperationPermitted = false;
                    setTitle();
                   // mNext.setText("Next");
                    mNext.setEnabled(isOperationPermitted);
                    AlertDialogFragment alert = AlertDialogFragment.newInstance(getlocalizeString("ValidationError","Validation Error"), msg, getlocalizeString("Ok","Ok"), false);
                    alert.show(getFragmentManager(), "detailAlert");

                }
                break;
            default:
                AlertDialogFragment alert = AlertDialogFragment.newInstance(getlocalizeString("ValidationError","Validation Error"), msg, getlocalizeString("Ok","Ok"), false);
                alert.show(getFragmentManager(), "detailAlert");
                break;

        }
    }


    public void resetFields()
    {
        lockFields();

        if(currentOperation==Constant.NRM_CHECKIN )
        {
            setEditable(mKm,true);
           // vehiclestatusContainer.setVisibility(View.VISIBLE);
        }else {
            // vehiclestatusContainer.setVisibility(View.GONE);
//            nrm_type.setClickable(true);
         /*   if(movementOperation.getRefNo()==null)
            setEditable(mCustomerRefNumInput.getEditText(), true);*/
            // if(movementOperation.getLastMovement()==null)
            setEditable(mKm, true);
        }

        // CRS Case
        if(movementOperation.getDriver()!=null)
            setEditable(mDriverName,false);
        else
            setEditable(mDriverName,true);

    }
       /* if(mLocation.getText().toString().length()<1)
                {  Drawable background = mLocation.getBackground();
                    DrawableCompat.setTint(background, Color.RED);
                    mLocation.setBackground(background);

                }else {
                    mLocation.setBackground(mMake.getBackground().getConstantState().newDrawable());
                }*/

    @Override
    protected void updateTitlesAndFields() {
        super.updateTitlesAndFields();
        if(currentOperation==Constant.NRM_CHECKOUT)
        {
            updateVehicleInfo();
            updateLocationinfo();
        }


        if(currentOperation==Constant.NRM_CHECKIN)
        {
            updateReferenceNo();
            int nrm_index=movementOperation.getPermittedMovementTypeId()-2;
            if(nrm_index>-1 && nrm_index<4)
//            nrm_type.setText( getResources().getStringArray(R.array.nrm_type)[nrm_index]);
            showOutKMsAndFuel();
            updateDriverInfo();
        }
        //CRS case
        if(movementOperation.getDriver()!=null)
            mDriverName.setText(""+movementOperation.getDriver().getName());

    }

    private void updateDriverInfo()
    {
        if(movementOperation.getLastMovement()!=null)
        {
            MovementInfo lastMovement=movementOperation.getLastMovement();
            if(lastMovement.getContact()!=null)
            {
                selectedDriver=lastMovement.getContact();
                mDriverName.setText(""+selectedDriver.getName());
            }
        }
    }

    private void fillData()
    {
        movementInfo.setVehicleId(movementOperation.getVehicle().getId());
//        movementInfo.setRefNo("" + mCustomerRefNumInput.getEditText().getText().toString());
        if(selectedDriver!=null) {
            selectedDriver.setFirstName(selectedDriver.getName());
            movementInfo.setContact(selectedDriver);
        }
        if(currentOperation==Constant.NRM_CHECKOUT)
        {
            movementInfo.setOutDetail(operationInfo);
            movementInfo.setInDetail(null);
            movementInfo.setIsComplete(false);
            operationInfo.setOperationType("1");// opening
            if(selectedLocation!=null)
            movementInfo.setOutDetail_LocationId(Integer.parseInt(selectedLocation.getId()));

            if (movementOperation.getDriver() != null)
                movementInfo.setOutDetail_DriverId(movementOperation.getDriver().getId());
            else if (selectedDriver != null)
                movementInfo.setOutDetail_DriverId(selectedDriver.getId());

            if(geoLocation.getWebID()!=null)
            movementInfo.setOutDetail_GeoLocation(geoLocation);
            movementInfo.setId(0);
        }else
        {
            movementInfo.setInDetail(operationInfo);
            movementInfo.setOutDetail(null);
            movementInfo.setIsComplete(true);
            operationInfo.setOperationType("3");// closing
            movementInfo.setContact(selectedDriver);
            if(movementOperation.getLastMovement()!=null)
                movementInfo.setId(movementOperation.getLastMovement().getId());
            // VehicleStatus statusCode=AppUtils.searchStatus(DatabaseManager.getInstance(getActivity().getApplicationContext()).getVehiclStatuses(),mVehicleStatus.getText().toString());
            //  if(statusCode!=null)
            movementInfo.setInDetail_VehicleStatusId("IDL");
            /// outdetails
            if(movementOperation.getLastMovement()!=null){
                MovementInfo lastMovement=movementOperation.getLastMovement();
                movementInfo.setOutDetail_LocationId(lastMovement.getOutDetail_LocationId());
                movementInfo.setOutDetail_DriverId(lastMovement.getOutDetail_DriverId());
                movementInfo.setOutDetail_GeoLocation(lastMovement.getOutDetail_GeoLocation());
            }

            movementInfo.setInDetail_LocationId(Integer.parseInt(selectedLocation.getId()));

            if (movementOperation.getDriver() != null)
                movementInfo.setInDetail_DriverId(movementOperation.getDriver().getId());
            else if (selectedDriver != null)
                movementInfo.setInDetail_DriverId(selectedDriver.getId());

            if(geoLocation.getWebID()!=null)
            movementInfo.setInDetail_GeoLocation(geoLocation);

        }

        operationInfo.setKm(Integer.parseInt(mKm.getText().toString()));
        int index = AppUtils.getIndexOf(getResources().getStringArray(R.array.fuel_levels), mFuel.getText().toString());
        if (index > -1)
            operationInfo.setFuelLevel(((float) index / 8));

/*        int mIndex=AppUtils.getIndexOf( getResources().getStringArray(R.array.nrm_type),nrm_type.getText().toString());
        if(mIndex<0)
            mIndex=0;*/
        movementInfo.setMovementTypeId(2);// NRM
        operationInfo.setMobile(true);
       /* operationInfo.setLocationId(selectedLocation.getId());
        operationInfo.setDriverId(""+selectedDriver.getId());*/

    }


    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
            return ;
        mLastClickTime = SystemClock.elapsedRealtime();
        if(v.getId()==R.id.vs_action_next)
        {
            if(!validateFields())
                return;
            fillData();
            super.onClick(v);
        }else
        {
            super.onClick(v);
        }
    }

    private boolean validateFields()
    {
        if(mDriverContainer.getVisibility()==View.VISIBLE && !isValidDriverSelected())
        {
            AppUtils.showMessage(getlocalizeString("DriverValidateMsg","Driver is not selected Or invalid"),mNext, Snackbar.LENGTH_SHORT);
            return false;
        }

      /*  if(nrm_type.getText().toString().length()<1 && currentOperation==Constant.NRM_CHECKOUT)
        {
            AppUtils.showMessage("Please select NRM Type",mNext, Snackbar.LENGTH_SHORT);
            return false;
        }*/

        if (mKm.getText().toString().length() < 1 && currentOperation == Constant.NRM_CHECKIN) {
            AppUtils.showMessage(getlocalizeString("OdoValidateMsg", "Please enter ODO value"), mNext, Snackbar.LENGTH_SHORT);
            return false;
        }
        if (Float.valueOf(mKm.getText().toString()) < movementOperation.getVehicle().getkMs()) {
            AppUtils.showMessage("ODO must be greater than " + movementOperation.getVehicle().getkMs(), mNext, Snackbar.LENGTH_LONG);
            return false;
        }
        if (mFuel.getText().toString().length() < 1 && currentOperation == Constant.NRM_CHECKIN) {
            AppUtils.showMessage(getlocalizeString("FuelValidateMsg", "Please select Fuel Level"), mNext, Snackbar.LENGTH_SHORT);
            return false;
        }
       /* if (vehiclestatusContainer.getEditText().getText().toString().length()<1 && currentOperation==Constant.NRM_CHECKIN)
        {
            AppUtils.showMessage("Please select Vehicle Status", mNext, Snackbar.LENGTH_SHORT);
            return false;
        }*/

     /*   if(mLocationInput.getVisibility()==View.VISIBLE && !isValidLocationSelected())
        {
            AppUtils.showMessage(getlocalizeString("LocationValidateMsg","Location is not selected Or invalid"), mNext, Snackbar.LENGTH_SHORT);
            return false;
        }*/

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mSelectedVehicle=  ((VehicleSearchAdapter)parent.getAdapter()).getVehicleAt(position);

        if(mSelectedVehicle==null)
            return;

        mVehiclenumber.setText(""+mSelectedVehicle.getPlateNo()+ " ("+mSelectedVehicle.getFleetCode() +")");
//        mVehicleFleet.setText(""+mSelectedVehicle.getFleetCode());
        mMake.setText("" + mSelectedVehicle.getMake());
        mModel.setText("" + mSelectedVehicle.getModel());
/*
        if (mVehicleFleet.hasFocus())
            AppUtils.hideKeyboardFrom(getActivity(), mVehicleFleet);*/
        if (mVehiclenumber.hasFocus())
            AppUtils.hideKeyboardFrom(getActivity(), mVehiclenumber);

        ValidateOperationRequest request=new ValidateOperationRequest();
        request.setFleetCode(mSelectedVehicle.getFleetCode());
        request.setPlateNo(mSelectedVehicle.getPlateNo());
        request.setMake(mSelectedVehicle.getMake());
        request.setModel(mSelectedVehicle.getModel());
        request.setMovementTypeId(2);
        request.setOperationType(-1);
        request.setMovementCategory(2);
        request.setCheckType(-1);
       // validateOperation(request, currentOperation);
        if (current_mode == Constant.NRM_CHECKIN)
        {
            request.setCheckType(2);
            validateOperation(request, Constant.NRM_CHECKIN);
        }
        else
        {
            request.setCheckType(1);
            validateOperation(request, Constant.NRM_CHECKOUT);
        }
    }
}