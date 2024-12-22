package com.karzansoft.fastvmi.Fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.karzansoft.fastvmi.Adapters.VehicleSearchAdapter;
import com.karzansoft.fastvmi.DataBase.DatabaseManager;
import com.karzansoft.fastvmi.Dialogs.AlertDialogFragment;
import com.karzansoft.fastvmi.Models.AppSettings;
import com.karzansoft.fastvmi.Models.MovementInfo;
import com.karzansoft.fastvmi.Models.OperationInfo;
import com.karzansoft.fastvmi.Models.ServiceLog;
import com.karzansoft.fastvmi.Models.Vehicle;
import com.karzansoft.fastvmi.Models.VehicleLocation;
import com.karzansoft.fastvmi.Models.VehicleService;
import com.karzansoft.fastvmi.Network.APIError;
import com.karzansoft.fastvmi.Network.Entities.Request.ValidateOperationRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.VehicleServiceRequest;
import com.karzansoft.fastvmi.Network.Entities.Response.ValidateOperationResponse;
import com.karzansoft.fastvmi.Network.ErrorUtils;
import com.karzansoft.fastvmi.Network.WebResponse;
import com.karzansoft.fastvmi.Network.WebResponseList;
import com.karzansoft.fastvmi.Network.WebServiceFactory;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Enums.SearchVehicleType;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yasir on 4/25/2016.
 */
public class GarageFragment extends VehicleSelectFragment implements AdapterView.OnItemClickListener {

    TextInputLayout /*vehiclestatusContainer,workshopContainer,poNumberContainer,*/workshopTypeContainer,/*workshopServiceContainer,*/
            notesContainer;
    /*
        AutoCompleteTextView workshopName,poNumber;
    */
//    Button addPon;
//    Contact selectedWorkshop;
    int workshopType;
    //    VoucherSearchAdapter voucherSearchAdapter;
//    Voucher selectedVoucher;
    Calendar calendar;
    //    VehicleService dueService;
//    ServiceLog serviceLog;
//    List<VehicleService> vehicleServices;
    AppSettings appSettings;
    public static GarageFragment newInstance(int current_mode) {
        GarageFragment f = new GarageFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.CURRENT_MODE, current_mode);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
//        serviceLog = new ServiceLog();
//        vehicleServices = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        vehiclestatusContainer = rootView.findViewById(R.id.vehiclestatusContainer);
//        workshopName =  rootView.findViewById(R.id.workshopname);
//        poNumber = rootView.findViewById(R.id.ponumber);
//        workshopContainer =  rootView.findViewById(R.id.workshopnameContainer);
//        poNumberContainer =  rootView.findViewById(R.id.ponumberContainer);
        workshopTypeContainer = rootView.findViewById(R.id.workshop_type_container);
//        workshopServiceContainer = rootView.findViewById(R.id.workshop_service_container);
        notesContainer = rootView.findViewById(R.id.notesContainer);
//        addPon = rootView.findViewById(R.id.add_pon);
        appSettings = AppUtils.getSettings(getActivity());
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mCustomeridInput.setHint(getlocalizeString("StaffId","Staff ID"));
//        mCustomerNameInput.setHint(getlocalizeString("StaffName","Staff Name"));
//        mLocationInput.setHint(getlocalizeString("LocationIn","Branch In"));
//        mCustomerRefNumInput.setHint(getlocalizeString("ReferenceNumber","Reference Number"));
//        workshopContainer.setHint(getlocalizeString("Workshop","Workshop"));
//        poNumberContainer.setHint(getlocalizeString("PurchaseOrderNo","Purchase Order No"));
        workshopTypeContainer.setHint(getlocalizeString("Type", "Workshop Type"));
        notesContainer.setHint(getlocalizeString("Notes", "Notes"));
//        workshopServiceContainer.setHint(getlocalizeString("Service","Service"));
        //mVehicleFleet.setOnItemClickListener(this);
        mVehiclenumber.setOnItemClickListener(this);


//        voucherSearchAdapter = new VoucherSearchAdapter(getActivity(),poNumber);


//        workshopContainer.setVisibility(View.GONE);

        if (current_mode == Constant.GARAGE_CHECKOUT) {
            mVehiclenumber.setAdapter(new VehicleSearchAdapter(getActivity(), true, mVehiclenumber, SearchVehicleType.WORKSHOP_OPEN));
            ((VehicleSearchAdapter) mVehiclenumber.getAdapter()).inculdeOnlyIdle(true);
            workshopTypeContainer.getEditText().setOnClickListener(this);
         /*   workshopServiceContainer.getEditText().setOnClickListener(this);
            poNumber.setAdapter(voucherSearchAdapter);
            if (selectedWorkshop!=null)
            {
                voucherSearchAdapter.setContactId(selectedWorkshop.getId());
            }
            poNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedVoucher = voucherSearchAdapter.getVoucherAt(i);
                    if (poNumber.hasFocus())
                        AppUtils.hideKeyboardFrom(getActivity(), poNumber);
                }
            });

            addPon.setOnClickListener(this);*/
        }else {
//            addPon.setVisibility(View.GONE);
//            workshopTypeContainer.getEditText().setOnClickListener(this);
//            workshopServiceContainer.setHint(getlocalizeString("ServiceDone","Service Done"));
            mVehiclenumber.setAdapter(new VehicleSearchAdapter(getActivity(), true, mVehiclenumber, SearchVehicleType.WORKSHOP_CLOSE));
            //mVehicleFleet.setAdapter(new VehicleSearchAdapter(getActivity(), false));
            // validateCheckinLocation();
        }

        if(currentOperation>0)
            resetFields();
        //mVehicleFleet.setOnFocusChangeListener(onFocusChangeListener);
        mVehiclenumber.setOnFocusChangeListener(onFocusChangeListener);
        mDriverName.setOnFocusChangeListener(onFocusChangeListener);

  /*      workshopName.setAdapter(new ContactSearchAdapter(getActivity(), ContactType.WORKSHOP_CONTACT,workshopName));
        workshopName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedWorkshop=((ContactSearchAdapter)parent.getAdapter()).getContactAt(position);
                if (workshopName.hasFocus())
                    AppUtils.hideKeyboardFrom(getActivity(), workshopName);
                selectedVoucher = null;
                poNumber.setText("");
                voucherSearchAdapter.setContactId(selectedWorkshop.getId());
            }
        });*/

        toggleService(workshopType);

    }

    @Override
    protected void validateResponse(ValidateOperationResponse response, int type) {
        super.validateResponse(response, type);
        try{
        String msg=response.getReason();
        if(msg==null || msg.length()<1)
            msg=getlocalizeString("VehicleNotAvailable","Selected vehicle is not available for this operation");

        switch (response.getPermittedCheckType()) {

            case Constant.CHECKIN:
                if (response.isPermitted()) {
                    isOperationPermitted = true;
                    operationTitle = getTitle(Constant.GARAGE_CHECKIN);
                    currentOperation = Constant.GARAGE_CHECKIN;
                    updateTitlesAndFields();
                    resetFields();
                    //validateCheckinLocation();
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
            case Constant.CHECKOUT:
                if (response.isPermitted()) {
                    isOperationPermitted = true;
                    operationTitle = getTitle(Constant.GARAGE_CHECKOUT);
                    currentOperation = Constant.GARAGE_CHECKOUT;
                    updateTitlesAndFields();
                    resetFields();

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
        }catch (Exception ex){}
    }

    public void validateCheckinLocation()
    {
        if(movementOperation==null)
            return;
        MovementInfo movementInfo=movementOperation.getLastMovement();
        if(movementInfo!=null && movementInfo.getInDetail_LocationId()>0)
        {
            VehicleLocation loc = DatabaseManager.getInstance(getActivity().getApplicationContext()).getLocationByID("" +movementInfo.getInDetail_LocationId());
            if (loc != null) {
                selectedLocation = loc;
//                mLocationInput.getEditText().setText("" + loc.getName());
//                mLocationInput.getEditText().setClickable(false);
            }
        }else
        {
//            mLocationInput.getEditText().setClickable(true);
        }
    }

    public void resetFields()
    {
        lockFields();

        View outKMAndFuel=mItemsContainer.findViewById(R.id.out_km_fuel_container);
        if(currentOperation== Constant.GARAGE_CHECKIN ) {
            setEditable(mKm, true);
            if (outKMAndFuel != null)
                outKMAndFuel.setVisibility(View.VISIBLE);
            // vehiclestatusContainer.setVisibility(View.VISIBLE);
//            mLocationInput.setVisibility(View.GONE);
//            setEditable(workshopName,false);
//            setEditable(poNumber,false);
        }else {
            // vehiclestatusContainer.setVisibility(View.GONE);
//            nrm_type.setClickable(true);
//            if(movementOperation.getRefNo()==null)
//            setEditable(mCustomerRefNumInput.getEditText(), true);
            // if(movementOperation.getLastMovement()==null)
            setEditable(mKm, true);
            if (outKMAndFuel != null)
                outKMAndFuel.setVisibility(View.GONE);
//            setEditable(workshopName,true);
//            setEditable(poNumber,true);
//            mLocationInput.setVisibility(View.GONE);
        }

        // CRS Case
       /* if(movementOperation.getDriver()!=null)
            setEditable(mDriverName,false);
        else
            setEditable(mDriverName,true);*/

    }


    @Override
    protected void updateVehicleInfo() {
        super.updateVehicleInfo();

        if (movementOperation==null)
            return;

        Vehicle vehicle = movementOperation.getVehicle();

        if (mSelectedVehicle!=null && vehicle!=null)
        {
            mSelectedVehicle.setId(vehicle.getId());
        }
    }

    @Override
    protected void updateTitlesAndFields() {
        super.updateTitlesAndFields();
        if(currentOperation== Constant.GARAGE_CHECKOUT)
        {
            updateVehicleInfo();
            updateLocationinfo();

            resetWorkshopType();
        }else
        {
            showOutKMsAndFuel();
        }


        if(currentOperation== Constant.GARAGE_CHECKIN) {
//            if (movementOperation.getRefNo() != null)
//                poNumber.setText(""+ movementOperation.getRefNo());
            if (movementOperation != null && movementOperation.getContact() != null) {
                mDriverName.setText(movementOperation.getContact().getName());
                selectedDriver = movementOperation.getContact();
            }
//            movementOperation.getLastMovement().setMovementSubType(1);
            if (movementOperation.getLastMovement() != null && movementOperation.getLastMovement().getMovementSubType() != null) {
                MovementInfo lastMovementInfo = movementOperation.getLastMovement();

                //workshopType = lastMovementInfo.getMovementSubType();
                if (lastMovementInfo.getMovementSubType() == 10)
                {
                    workshopType = 4;
                }else
                {
                    workshopType = lastMovementInfo.getMovementSubType();
                }
                int index = workshopType -1;
                if(index>-1 && index<4) {
                    workshopTypeContainer.getEditText().setText(getWorkshopTypes()[index]);

                    ServiceLog serviceLog = lastMovementInfo.getServiceLog();
//                    if(workshopType ==1 && serviceLog !=null && serviceLog.getService()!=null )
//                    {
//                        workshopServiceContainer.getEditText().setText(""+serviceLog.getService().getName());
//                    }

                    toggleService(workshopType);
                }



               /* selectedDriver = movementOperation.getLastMovement().getOutDetail_Driver();
                if (selectedDriver!=null)
                {
                    mDriverName.setText(""+selectedDriver.getName());
                    setEditable(mDriverName,false);
                }*/
                //  nrm_type.setText( getResources().getStringArray(R.array.nrm_type)[nrm_index]);
            }
        }
        //CRS case
      /*  if(movementOperation.getDriver()!=null)
            mDriverName.setText(""+movementOperation.getDriver().getName());
        else
            mDriverName.setText("");*/
    }

    private void resetWorkshopType() {
        workshopType = 0;
        workshopTypeContainer.getEditText().setText("");
//        workshopServiceContainer.getEditText().setText("");
//        vehicleServices.clear();
        toggleService(workshopType);
    }

    private void fillData() {
        movementInfo = new MovementInfo();
        operationInfo = new OperationInfo();

        movementInfo.setVehicleId(movementOperation.getVehicle().getId());
//
//        if (selectedVoucher!=null )
//        movementInfo.setRefNo("" + selectedVoucher.getVoucherNo());

//        if(selectedWorkshop!=null)
//        selectedWorkshop.setFirstName(selectedWorkshop.getName());
//        movementInfo.setContact(selectedWorkshop);
        if (movementInfo.getContact() != null)
            movementInfo.setContactId("" + movementInfo.getContact().getId());

        if (currentOperation == Constant.GARAGE_CHECKOUT) {
            movementInfo.setOutDetail(operationInfo);
            movementInfo.setInDetail(null);
            movementInfo.setIsComplete(false);
            operationInfo.setOperationType("1");// opening
            operationInfo.setDateTime("" + AppUtils.getSelectedDateAndTime(calendar));
            movementInfo.setVehicle(movementOperation.getVehicle());

            Vehicle vehicle = movementOperation.getVehicle();
            if (vehicle != null)
                movementInfo.setOutDetail_LocationId(Integer.parseInt( vehicle.getLocationId()));

            if (selectedLocation!=null)
            movementInfo.setInDetail_LocationId(Integer.parseInt(selectedLocation.getId()));

            if (movementOperation.getDriver() != null)
                movementInfo.setOutDetail_DriverId(movementOperation.getDriver().getId());
            else if (selectedDriver != null)
                movementInfo.setOutDetail_DriverId(selectedDriver.getId());

            if (geoLocation.getWebID() != null)
                movementInfo.setOutDetail_GeoLocation(geoLocation);
            movementInfo.setId(0);

            if (selectedDriver != null)
                movementInfo.setContact(selectedDriver);

//            if (serviceLog.getServiceId()>0)
//                movementInfo.setServiceLog(serviceLog);

        }else
        {
            movementInfo.setInDetail(operationInfo);
            movementInfo.setOutDetail(null);
            movementInfo.setIsComplete(true);
            operationInfo.setOperationType("3");// closing
            operationInfo.setDateTime(""+ AppUtils.getSelectedDateAndTime(calendar));
            movementInfo.setVehicle(movementOperation.getVehicle());
            movementInfo.setContact(movementOperation.getContact());
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

            if (selectedLocation!=null)
            movementInfo.setInDetail_LocationId(Integer.parseInt(selectedLocation.getId()));

            if (movementOperation.getDriver() != null)
                movementInfo.setInDetail_DriverId(movementOperation.getContact().getId());
            else if (selectedDriver != null)
                movementInfo.setInDetail_DriverId(selectedDriver.getId());

            if(geoLocation.getWebID()!=null)
            movementInfo.setInDetail_GeoLocation(geoLocation);

            if (selectedDriver!=null)
                movementInfo.setContact(selectedDriver);
        }


        if (notesContainer.getEditText().getText().toString().trim().length()>0)
        {
            operationInfo.setNotes(notesContainer.getEditText().getText().toString());
        }

        operationInfo.setKm(Integer.parseInt(mKm.getText().toString()));
        int index= AppUtils.getIndexOf( getResources().getStringArray(R.array.fuel_levels),mFuel.getText().toString());
        if(index>-1)
            operationInfo.setFuelLevel(((float) index / 8));

       /* int mIndex=AppUtils.getIndexOf( getResources().getStringArray(R.array.nrm_type),nrm_type.getText().toString());
        if(mIndex<0)
            mIndex=0;*/
        movementInfo.setMovementTypeId(3);// Garage
        //movementInfo.setMovementSubType(workshopType);
        if (workshopType == 4)
        {
            movementInfo.setMovementSubType(10);
        }
        else {
            movementInfo.setMovementSubType(workshopType);
        }
        operationInfo.setMobile(true);
       /* operationInfo.setLocationId(selectedLocation.getId());
        operationInfo.setDriverId(""+selectedDriver.getId());*/

    }


    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
            return ;
        mLastClickTime = SystemClock.elapsedRealtime();


        switch (v.getId())
        {
            case R.id.vs_action_next:
                if (!validateFields())
                    return;

                fillData();
                super.onClick(v);
                break;
            case R.id.workshop_type:
                if (mSelectedVehicle ==null)
                    return;
                AppUtils.showOptionsList(workshopTypeContainer.getEditText(),getWorkshopTypes(), getActivity(), new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        workshopTypeContainer.getEditText().setText("" + item.getTitle());
                        workshopType = 1 + item.getItemId();
                        toggleService(workshopType);
                        if (item.getItemId() == 0 && mSelectedVehicle != null && appSettings.isServiceEnabled()) {
                            loadServices();
                        } else {
//                            dueService = null;
//                            workshopServiceContainer.getEditText().setText("");
//                            serviceLog.setServiceId(0);
                        }
                        return true;
                    }
                });

                break;
/*

            case R.id.workshop_service:

                if (dueService==null)
                    return;

                AppUtils.showOptionsList(workshopServiceContainer.getEditText(), getWorkshopServices(), getActivity(), new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        workshopServiceContainer.getEditText().setText(""+item.getTitle());
                        VehicleService vs = vehicleServices.get(item.getItemId());
                        updateDueService(vs);
                        serviceLog.setServiceId(vs.getId());
                        return true;
                    }
                });
                break;

            case R.id.add_pon:

                if (dueService != null) {
                    Intent i = new Intent(getActivity(), PurchaseOrderActivity.class);
                    Gson gson = new GsonBuilder().create();
                    POSummary poSummary = new POSummary();
                    if (mSelectedVehicle!=null)
                    {
                        poSummary.setVehicleId(mSelectedVehicle.getId());
                        poSummary.setMakeModel(mSelectedVehicle.getMake()+" "+ mSelectedVehicle.getModel());
                        poSummary.setMake(mSelectedVehicle.getMake());
                        poSummary.setModel(mSelectedVehicle.getModel());
                        poSummary.setPlateNo(mSelectedVehicle.getPlateNo());
                    }

                    if (selectedWorkshop!=null)
                    {
                        poSummary.setSupplierName(selectedWorkshop.getName());
                        poSummary.setSupplierId(selectedWorkshop.getId());
                    }
                    poSummary.setServiceItems(dueService.getServiceItems());
                    i.putExtra("PO", gson.toJson(poSummary, POSummary.class));
                    startActivityForResult(i, 231);
                }
                break;
*/

            default:
                super.onClick(v);
        }

        }


    private void toggleService(int workshopType) {/*
            poNumberContainer.setVisibility(View.GONE);
            addPon.setVisibility(View.GONE);
            workshopServiceContainer.setVisibility(View.GONE);*/
          /*  if (workshopType == 1) // maintenance
            {
                workshopServiceContainer.setVisibility(View.VISIBLE);
                if (current_mode== Constant.GARAGE_CHECKOUT)
                addPon.setVisibility(View.VISIBLE);
            }else {
                workshopServiceContainer.setVisibility(View.INVISIBLE);
                addPon.setVisibility(View.GONE);
            }*/
    }

    private String[] getWorkshopTypes()
    {
        String[] names=new String[4];
        names[0] = "Maintenance";
        names[1] ="Repair";
        names[2] = "Accident Repair";
        names[3] = "Inspection";

        return names;
    }

    /*  private String[] getWorkshopServices()
      {
          String[] names=new String[vehicleServices.size()];

          for (int i=0 ;i< vehicleServices.size();i++)
          {
              names[i] = vehicleServices.get(i).getName();
          }


          return names;
      }
  */
    private boolean validateFields()
    {

        if(mKm.getText().toString().length()<1 )
        {
            AppUtils.showMessage(getlocalizeString("OdoValidateMsg","Please enter ODO value"),mNext, Snackbar.LENGTH_SHORT);
            return false;
        }
        if( currentOperation== Constant.GARAGE_CHECKIN &&(Float.valueOf(mKm.getText().toString())<movementOperation.getVehicle().getkMs()))
        {
            AppUtils.showMessage("ODO must be greater than "+movementOperation.getVehicle().getkMs(),mNext, Snackbar.LENGTH_LONG);
            return false;
        }
        if(mFuel.getText().toString().length()<1)
        {
            AppUtils.showMessage(getlocalizeString("FuelValidateMsg","Please select Fuel Level"),mNext, Snackbar.LENGTH_SHORT);
            return false;
        }

        if(mDriverContainer.getVisibility()==View.VISIBLE && !isValidDriverSelected())
        {
            AppUtils.showMessage(getlocalizeString("DriverValidateMsg","Driver is not selected Or invalid"),mNext, Snackbar.LENGTH_SHORT);
            return false;
        }

/*
        if(workshopName.getText().toString().length()<1 && currentOperation== Constant.GARAGE_CHECKOUT)
        {
            AppUtils.showMessage(getlocalizeString("PleaseSelectWorkshop","Please select workshop"),mNext, Snackbar.LENGTH_SHORT);
            return false;
        }
*/

        if(workshopTypeContainer.getEditText().getText().toString().length()<1 && currentOperation== Constant.GARAGE_CHECKOUT)
        {
            AppUtils.showMessage(getlocalizeString("PleaseSelectWorkshopType","Please select workshop Type"),mNext, Snackbar.LENGTH_SHORT);
            return false;
        }
/*
        if (currentOperation== Constant.GARAGE_CHECKOUT && workshopType == 1 && dueService ==null && appSettings.isServiceEnabled())
        {
            AppUtils.showMessage(getlocalizeString("PleaseSelectVehicleService","Please select vehicle service"),mNext, Snackbar.LENGTH_SHORT);
            return false;
        }*/

       /* if(AppUtils.getSettings(getActivity()).isPoMandatoryForWorkshopMovement() && (poNumber.getText().toString().length()<1 || selectedVoucher ==null) && currentOperation== Constant.GARAGE_CHECKOUT)
        {
            AppUtils.showMessage(getlocalizeString("PleaseSelectPONumber","Please select PO number"),mNext, Snackbar.LENGTH_SHORT);
            return false;
        }*/

       /* if (vehiclestatusContainer.getEditText().getText().toString().length()<1 && currentOperation==Constant.NRM_CHECKIN)
        {
            AppUtils.showMessage("Please select Vehicle Status", mNext, Snackbar.LENGTH_SHORT);
            return false;
        }*/

       /* if(mLocationInput.getVisibility() == View.VISIBLE && !isValidLocationSelected())
        {
            AppUtils.showMessage(getlocalizeString("LocationValidateMsg","Location is not selected Or invalid"), mNext, Snackbar.LENGTH_SHORT);
            return false;
        }*/

       /* if( currentOperation== Constant.GARAGE_CHECKIN && mLocationInput.getVisibility() == View.VISIBLE && !isCheckInAllowedFromOtherBranch())
        {
            AppUtils.showMessage(getlocalizeString("CheckInDeniedMsg","You are not allowed to check-in to this branch"),mNext, Snackbar.LENGTH_LONG);
            return false;
        }*/

        return true;
    }


  /*  public boolean isCheckInAllowedFromOtherBranch()
    {
        AppSettings appSettings= AppUtils.getSettings(getActivity());
        LoginResponse loginInfo= AppUtils.getLoginInfo(getActivity());

        if(!appSettings.isCanViewOtherBranchNrms()|| !appSettings.isActionsAllowedOnOtherBranchNrms())
        {
            if(loginInfo.getLocationId()!=null && loginInfo.getLocationId().intValue()!=Integer.parseInt(selectedLocation.getId()))
            {
                return false;
            }
        }

            return true;
    }*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mSelectedVehicle=  ((VehicleSearchAdapter)parent.getAdapter()).getVehicleAt(position);

        if(mSelectedVehicle==null)
            return;
        try {

            mVehiclenumber.setText("" + mSelectedVehicle.getPlateNo()+ " ("+mSelectedVehicle.getFleetCode() +")");
//            mVehicleFleet.setText("" + mSelectedVehicle.getFleetCode());
            mMake.setText("" + mSelectedVehicle.getMake());
            mModel.setText("" + mSelectedVehicle.getModel());
/*
            if (mVehicleFleet.hasFocus())
                AppUtils.hideKeyboardFrom(getActivity(), mVehicleFleet);*/
            if (mVehiclenumber.hasFocus())
                AppUtils.hideKeyboardFrom(getActivity(), mVehiclenumber);

            ValidateOperationRequest request = new ValidateOperationRequest();
            request.setFleetCode(mSelectedVehicle.getFleetCode());
            request.setPlateNo(mSelectedVehicle.getPlateNo());
            request.setMake(mSelectedVehicle.getMake());
            request.setModel(mSelectedVehicle.getModel());
            request.setMovementTypeId(3);
            request.setOperationType(-1);
            request.setMovementCategory(2);
            request.setCheckType(-1);
            //  validateOperation(request, currentOperation);

            if (current_mode == Constant.GARAGE_CHECKIN) {
                request.setCheckType(2);
                validateOperation(request, Constant.GARAGE_CHECKIN);
            } else {
                request.setCheckType(1);
                validateOperation(request, Constant.GARAGE_CHECKOUT);
            }
        }catch (Exception ex){}
    }


    private void loadServices()
    {
        if (mSelectedVehicle==null)
            return;
        long modelId =0;
        if (mSelectedVehicle.getModelId()!=null && mSelectedVehicle.getModelId().trim().length()>0)
            modelId = Long.parseLong(mSelectedVehicle.getModelId());

        VehicleServiceRequest vehicleServiceRequest = new VehicleServiceRequest();
        vehicleServiceRequest.setId(modelId);
        showProgress();
        WebServiceFactory.getInstance().getVehicleServices(vehicleServiceRequest, AppUtils.getAuthToken(getActivity())).enqueue(new Callback<WebResponse<WebResponseList<VehicleService>>>() {
            @Override
            public void onResponse(Call<WebResponse<WebResponseList<VehicleService>>> call, Response<WebResponse<WebResponseList<VehicleService>>> response) {

                if (response.isSuccessful() && response.body() !=null)
                {
                    if(response.body().isSuccess())
                    {
                        if (response.body().getResult()!=null && response.body().getResult().getItems() !=null)
                        {
//                            loadServiceAlerts(response.body().getResult().getItems());
                            return;
                        }
                    }else {// response.body().getError().getMessage()
                        AppUtils.showToastMessage(""+response.body().getError().getMessage(), getActivity());
                    }

                }else {
                    String message = "Error in getting services";
                    APIError error= ErrorUtils.parseError(response);
                    if(error!=null && error.message()!=null)
                        message=error.message();
                    AppUtils.showToastMessage(message, getActivity());

                }

                hideProgress();

            }

            @Override
            public void onFailure(Call<WebResponse<WebResponseList<VehicleService>>> call, Throwable t) {


                try {
                    hideProgress();
                    AppUtils.showToastMessage("Internal error in getting services", getActivity());
                }catch (Exception ex){ex.printStackTrace();}
            }
        });

    }

  /*  private void loadServiceAlerts(final List<VehicleService> vehicleServices)
    {
       VehicleServiceRequest vehicleServiceRequest= new VehicleServiceRequest();
       vehicleServiceRequest.setVehicleId((long)mSelectedVehicle.getId());

       WebServiceFactory.getInstance().getVehicleServicesAlert(vehicleServiceRequest, AppUtils.getAuthToken(getActivity())).enqueue(new Callback<WebResponse<WebResponseList<VehicleServiceAlert>>>() {
           @Override
           public void onResponse(Call<WebResponse<WebResponseList<VehicleServiceAlert>>> call, Response<WebResponse<WebResponseList<VehicleServiceAlert>>> response) {

               if (response.isSuccessful() && response.body() !=null)
               {
                   if(response.body().isSuccess())
                   {
                       if (response.body().getResult()!=null && response.body().getResult().getItems()!=null)
                       {

                           List<VehicleServiceAlert> serviceAlerts = response.body().getResult().getItems();

                           if (vehicleServices.size()>0 && serviceAlerts.size()>0)
                           {
                               VehicleServiceAlert vehicleServiceAlert = serviceAlerts.get(0);

                               if (vehicleServiceAlert.getDueService()!=null)
                               serviceLog.setServiceId(vehicleServiceAlert.getDueService().getId());
                               serviceLog.setDueDate(vehicleServiceAlert.getDueDate());
                               serviceLog.setDueKMs(vehicleServiceAlert.getDueKms());
                               serviceLog.setDueReason(vehicleServiceAlert.getDueReason());

                               dueService = vehicleServiceAlert.getDueService();

                               fillDueServices(vehicleServices);
                           }
                       }

                   }else {// response.body().getError().getMessage()
                       AppUtils.showToastMessage(""+response.body().getError().getMessage(), getActivity());
                   }

               }else {
                   String message = "Error in getting due services";
                   APIError error= ErrorUtils.parseError(response);
                   if(error!=null && error.message()!=null)
                       message=error.message();
                   AppUtils.showToastMessage(message, getActivity());

               }

               hideProgress();
           }

           @Override
           public void onFailure(Call<WebResponse<WebResponseList<VehicleServiceAlert>>> call, Throwable t) {

               try {
                   hideProgress();
                   AppUtils.showToastMessage("Internal error in getting due services", getActivity());
               }catch (Exception ex){ex.printStackTrace();}
           }
       });
    }

    private void fillDueServices(List<VehicleService> vServices)
    {
        if(getActivity() ==null || dueService ==null)
            return;

        try{
            this.vehicleServices.clear();
            for (VehicleService vehicleSer:vServices) {

                if (vehicleSer.getDueOnKMs() >= dueService.getDueOnKMs() && vehicleSer.getDueOnMonths() >= dueService.getDueOnMonths())
                {
                    this.vehicleServices.add(vehicleSer);
                }
            }

            if (this.vehicleServices.size()>0)
            {
                workshopServiceContainer.getEditText().setText(""+this.vehicleServices.get(0).getName());
                updateDueService(this.vehicleServices.get(0));
            }


        }catch (Exception ex){ex.printStackTrace();}

    }

    private void updateDueService(VehicleService dService)
    {
        serviceLog.setServiceId(dService.getId());
        this.dueService.setServiceItems(dService.getServiceItems());
    }
*/
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);231

        Util.LogE("Activity for result","with code: "+resultCode);

        if (resultCode == Activity.RESULT_OK && requestCode == 231 && data!=null)
        {
            String json = data.getStringExtra("VOUCHER");
            if (json!=null && json.length()>0)
            {
                selectedVoucher = gson.fromJson(json,Voucher.class);
                if(selectedVoucher!=null)
                {
                    poNumber.setText(""+selectedVoucher.getVoucherNo());
                }
            }
        }else
            {
                super.onActivityResult(requestCode, resultCode, data);
            }
    }*/
}