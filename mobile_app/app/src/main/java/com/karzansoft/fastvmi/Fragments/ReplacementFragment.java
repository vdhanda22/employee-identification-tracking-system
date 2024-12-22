package com.karzansoft.fastvmi.Fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.karzansoft.fastvmi.Adapters.VehicleSearchAdapter;
import com.karzansoft.fastvmi.Dialogs.AlertDialogFragment;
import com.karzansoft.fastvmi.Models.MovementInfo;
import com.karzansoft.fastvmi.Network.Entities.Request.ValidateOperationRequest;
import com.karzansoft.fastvmi.Network.Entities.Response.ValidateOperationResponse;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;

/**
 * Created by Yasir on 4/25/2016.
 */
public class ReplacementFragment extends VehicleSelectFragment implements AdapterView.OnItemClickListener{

    /*TextInputLayout vehiclestatusContainer;*/

    public static ReplacementFragment newInstance(int current_mode) {
        ReplacementFragment f = new ReplacementFragment();
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

//        mCustomerRefNumInput.setHint(getlocalizeString("RentalAgreementNo","Rental Agreement No"));
//        mVehicleFleet.setOnItemClickListener(this);
        mVehiclenumber.setOnItemClickListener(this);
        mVehiclenumber.setAdapter(new VehicleSearchAdapter(getActivity(), true));
//        mVehicleFleet.setAdapter(new VehicleSearchAdapter(getActivity(), false));
        if (currentOperation > 0)
            resetFields();
//        mVehicleFleet.setOnFocusChangeListener(onFocusChangeListener);
        mVehiclenumber.setOnFocusChangeListener(onFocusChangeListener);
        mDriverName.setOnFocusChangeListener(onFocusChangeListener);

    }

    @Override
    protected void validateResponse(ValidateOperationResponse response, int type) {
        super.validateResponse(response, type);
        String msg=response.getReason();
        if(msg==null || msg.length()<1)
            msg=getlocalizeString("VehicleNotAvailable","Selected vehicle is not available for this operation");

        switch (type) {
            case Constant.REPLACEMENT_CHECKIN:
                if (response.isPermitted()) {
                    isOperationPermitted = true;
                    operationTitle = getTitle(Constant.REPLACEMENT_CHECKIN);
                    currentOperation = Constant.REPLACEMENT_CHECKIN;

                    updateTitlesAndFields();
                    resetFields();
                    downloadVehicleCardImages(movementOperation.getVehicle());

                } else {
                    currentOperation = 0;
                    isOperationPermitted = false;
                    setTitle();
                    mNext.setText(getlocalizeString("Next","Next"));
                    mNext.setEnabled(isOperationPermitted);
                    AlertDialogFragment alert = AlertDialogFragment.newInstance(getlocalizeString("ValidationError","Validation Error"), msg, getlocalizeString("Ok","Ok"), false);
                    alert.show(getFragmentManager(), "detailAlert");

                }
                break;
            case Constant.REPLACEMENT_CHECKOUT:
                if (response.isPermitted()) {
                    isOperationPermitted = true;
                    operationTitle = getTitle(Constant.REPLACEMENT_CHECKOUT);
                    currentOperation = Constant.REPLACEMENT_CHECKOUT;
                    // CRS case
                    MovementInfo repMovement=AppUtils.getMovementInfo(getActivity());
                    if(repMovement.isCRS())
                        movementOperation.setDriver(repMovement.getLocalDriver());
                    //
                    updateTitlesAndFields();
                    resetFields();
                    downloadVehicleCardImages(movementOperation.getVehicle());

                } else {
                    currentOperation = 0;
                    isOperationPermitted = false;
                    setTitle();
                    mNext.setText(getlocalizeString("Next","Next"));
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

        if(currentOperation==Constant.REPLACEMENT_CHECKIN )
        {
            setEditable(mKm,true);
            //vehiclestatusContainer.setVisibility(View.VISIBLE);
        }else {
           // vehiclestatusContainer.setVisibility(View.GONE);
           // if(movementOperation.getLastMovement()==null)
                setEditable(mKm,true);
        }

        // CRS Case
        if(movementOperation.getDriver()!=null)
            setEditable(mDriverName,false);
        else
            setEditable(mDriverName,true);

    }

    @Override
    protected void updateTitlesAndFields() {
        super.updateTitlesAndFields();
        if(currentOperation==Constant.REPLACEMENT_CHECKOUT)
        {
            updateVehicleInfo();
            updateLocationinfo();
        }


        updateCustomerInf();
        updateReferenceNo();

        //CRS case
        if(movementOperation.getDriver()!=null)
            mDriverName.setText(""+movementOperation.getDriver().getName());
        else
            mDriverName.setText("");

    }
    private void fillData()
    {
        if(movementOperation.getContact()!=null)
        movementInfo.setContactId(""+movementOperation.getContact().getId());
        movementInfo.setMovementTypeId(1);// agreement
        movementInfo.setVehicleId(movementOperation.getVehicle().getId());
      //  movementInfo.setRefNo(movementOperation.getRefNo());
        if(currentOperation==Constant.REPLACEMENT_CHECKOUT)
        {
            movementInfo.setOutDetail(operationInfo);
            movementInfo.setInDetail(null);
            movementInfo.setIsComplete(false);
            operationInfo.setOperationType("2");// replacement
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
            operationInfo.setOperationType("2");// replacement
            movementInfo.setContact(movementOperation.getContact());
            movementInfo.setRefNo(movementOperation.getRefNo());
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
            {
                movementInfo.setInDetail_DriverId(movementOperation.getDriver().getId());
                movementInfo.setCRS(true);
                movementInfo.setLocalDriver(movementOperation.getDriver());
            }
            else if (selectedDriver != null)
                movementInfo.setInDetail_DriverId(selectedDriver.getId());
            if(geoLocation.getWebID()!=null)
            movementInfo.setInDetail_GeoLocation(geoLocation);
        }

        operationInfo.setKm(Integer.parseInt(mKm.getText().toString()));
        int index=AppUtils.getIndexOf( getResources().getStringArray(R.array.fuel_levels),mFuel.getText().toString());
        if(index>-1)
            operationInfo.setFuelLevel(((float)index/8));
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
        if(mDriverContainer.getVisibility()==View.VISIBLE &&!isValidDriverSelected())
        {
            AppUtils.showMessage(getlocalizeString("DriverValidateMsg","Driver is not selected Or invalid"),mNext, Snackbar.LENGTH_SHORT);
            return false;
        }

        if(mKm.getText().toString().length()<1 )
        {
            AppUtils.showMessage(getlocalizeString("OdoValidateMsg","Please enter ODO value"),mNext, Snackbar.LENGTH_SHORT);
            return false;
        }
        if(mFuel.getText().toString().length()<1 )
        {
            AppUtils.showMessage(getlocalizeString("FuelValidateMsg","Please select Fuel Level"),mNext, Snackbar.LENGTH_SHORT);
            return false;
        }
       /* if (vehiclestatusContainer.getEditText().getText().toString().length()<1 && currentOperation==Constant.REPLACEMENT_CHECKIN)
        {
            AppUtils.showMessage("Please select Vehicle Status",mNext, Snackbar.LENGTH_SHORT);
            return false;
        }*/

        if(!isValidLocationSelected())
        {
            AppUtils.showMessage(getlocalizeString("LocationValidateMsg","Location is not selected Or invalid"), mNext, Snackbar.LENGTH_SHORT);
            return false;
        }

        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mSelectedVehicle = ((VehicleSearchAdapter) parent.getAdapter()).getVehicleAt(position);

        if (mSelectedVehicle == null)
            return;

        mVehiclenumber.setText("" + mSelectedVehicle.getPlateNo()+ " ("+mSelectedVehicle.getFleetCode() +")");
//        mVehicleFleet.setText(""+mSelectedVehicle.getFleetCode());
        mMake.setText("" + mSelectedVehicle.getMake());
        mModel.setText("" + mSelectedVehicle.getModel());

//        if (mVehicleFleet.hasFocus())
//            AppUtils.hideKeyboardFrom(getActivity(), mVehicleFleet);
        if (mVehiclenumber.hasFocus())
            AppUtils.hideKeyboardFrom(getActivity(), mVehiclenumber);

        ValidateOperationRequest request = new ValidateOperationRequest();
        request.setFleetCode(mSelectedVehicle.getFleetCode());
        request.setPlateNo(mSelectedVehicle.getPlateNo());
        request.setMake(mSelectedVehicle.getMake());
        request.setModel(mSelectedVehicle.getModel());
        request.setMovementTypeId(Constant.RA_CHECKOUT);
        request.setOperationType(2);
        request.setMovementCategory(-1);

        if (current_mode == Constant.REPLACEMENT_CHECKIN)
        {
            request.setCheckType(2);
            validateOperation(request, Constant.REPLACEMENT_CHECKIN);
        }
        else
        {
            request.setCheckType(1);
            validateOperation(request, Constant.REPLACEMENT_CHECKOUT);
        }


    }
}
