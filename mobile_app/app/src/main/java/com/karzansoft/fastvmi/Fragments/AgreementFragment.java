package com.karzansoft.fastvmi.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import com.karzansoft.fastvmi.Adapters.CustomerSearchAdapter;
import com.karzansoft.fastvmi.Adapters.VehicleSearchAdapter;
import com.karzansoft.fastvmi.DataBase.DatabaseManager;
import com.karzansoft.fastvmi.Dialogs.AlertDialogFragment;
import com.karzansoft.fastvmi.Dialogs.DatePicker;
import com.karzansoft.fastvmi.Dialogs.TimePicker;
import com.karzansoft.fastvmi.Models.Contact;
import com.karzansoft.fastvmi.Models.MovementInfo;
import com.karzansoft.fastvmi.Models.VehicleLocation;
import com.karzansoft.fastvmi.Network.Entities.Request.CustomerSearchRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.ValidateOperationRequest;
import com.karzansoft.fastvmi.Network.Entities.Response.ValidateOperationResponse;
import com.karzansoft.fastvmi.Network.WebResponse;
import com.karzansoft.fastvmi.Network.WebResponseList;
import com.karzansoft.fastvmi.Network.WebServiceFactory;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yasir on 4/25/2016.
 */
public class AgreementFragment extends VehicleSelectFragment implements AdapterView.OnItemClickListener {

//        TextInputLayout /*,vehiclestatusContainer,dateContainer,timeContainer*/;
//        View dateTimeContainer;
    Contact selectedCustomer;

    Calendar calendar;

    public static AgreementFragment newInstance(int current_mode) {
        AgreementFragment f = new AgreementFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.CURRENT_MODE, current_mode);
        f.setArguments(args);
        return f;
    }

    TextWatcher textChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (selectedCustomer != null) {
                if (!mCustomerName.getText().toString().equals(selectedCustomer.getName())) {
                    mEmail.setText("");
//                    setEditable(mEmail,true);
                    selectedCustomer = null;
                }
            }
        }
    };
    AdapterView.OnItemClickListener onCustomerSelection = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedCustomer = ((CustomerSearchAdapter) parent.getAdapter()).getCustomerAt(position);
            getCustomerByCode(selectedCustomer);

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
/*        vehiclestatusContainer=(TextInputLayout)rootView.findViewById(R.id.vehiclestatusContainer);
        dateTimeContainer = rootView.findViewById(R.id.dateTimeContainer);
        dateContainer = rootView.findViewById(R.id.dateContainer);
        timeContainer = rootView.findViewById(R.id.timeContainer);*/


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mCustomerRefNumInput.setVisibility(View.GONE);
        mCustomerNameInput.setHint(getlocalizeString("CustomerName", "Customer Name"));
        mAgreementNoContainer.setHint(getlocalizeString("AgreementNo","Agreement No"));

     /*   if(current_mode==Constant.RA_CHECKIN)
        {
            //mLocationInput.setHint("Destination Location");
//            mCustomerRefNumInput.setHint(getlocalizeString("RentalAgreementNo","Rental Agreement No"));

        }else{
            mLocationInput.setHint("Branch Out");
        }*/
//        mVehicleFleet.setOnItemClickListener(this);
        mVehiclenumber.setOnItemClickListener(this);
        mVehiclenumber.setAdapter(new VehicleSearchAdapter(getActivity(), true));
//        mVehicleFleet.setAdapter(new VehicleSearchAdapter(getActivity(), false));
        mCustomerName.setAdapter(new CustomerSearchAdapter(getActivity(), false));
        mCustomerName.addTextChangedListener(textChangeListener);
//        mEmail.setAdapter(new CustomerSearchAdapter(getActivity(),true));
        mCustomerName.setOnItemClickListener(onCustomerSelection);
//        mEmail.setOnItemClickListener(onCustomerSelection);
//        if(current_mode==Constant.CHECKOUT)
//        personalInfo.setVisibility(View.VISIBLE);
        if (currentOperation > 0)
            resetFields();
//        mVehicleFleet.setOnFocusChangeListener(onFocusChangeListener);
        mVehiclenumber.setOnFocusChangeListener(onFocusChangeListener);
        mDriverName.setOnFocusChangeListener(onFocusChangeListener);

        if (current_mode == Constant.CHECKOUT) {
            operationTitle = getTitle(Constant.RA_CHECKOUT);
            /*AppSettings settings = AppUtils.getSettings(getActivity());
            if (settings!=null && settings.isExpectedReturnDate())
            {
                dateTimeContainer.setVisibility(View.VISIBLE);
                dateContainer.getEditText().setOnClickListener(this);
                timeContainer.getEditText().setOnClickListener(this);
            }*/
           // mNext.setText(operationTitle);
        }
        else
        {
            operationTitle=getTitle(Constant.RA_CHECKIN);
           // mNext.setText(operationTitle);

        }

        ArrayList<VehicleLocation> locations= DatabaseManager.getInstance(getActivity().getApplicationContext()).getVehiclLocations();

        if (locations.size() < 2) {
//            mLocationInput.setVisibility(View.GONE);
            if (locations.size() == 1)
                selectedLocation = locations.get(0);
        }

    }

    public void getCustomerByCode(Contact contactWithoutEmail) {
        if (!AppUtils.isNetworkAvailable(getActivity()))
            return;
        showProgress();
//        request.setDocumentType(Integer.valueOf(dType));
//        request.setDocumentNo(documentNo);
        Call<WebResponse<Contact>> responseCall = WebServiceFactory.getInstance().getCustomerDetailByCode(contactWithoutEmail, AppUtils.getAuthToken(getActivity()));
        responseCall.enqueue(new Callback<WebResponse<Contact>>() {
            @Override
            public void onResponse(Call<WebResponse<Contact>> call, Response<WebResponse<Contact>> response) {
                hideProgress();
                try {
                    if (response.isSuccessful() && response.body().getResult() != null) {
                        Contact contact = (Contact) response.body().getResult();
                        if (contact != null)
                            updateCustomerInfo(contact);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<WebResponse<Contact>> call, Throwable t) {
                hideProgress();
            }
        });
    }


    @Override
    protected void validateResponse(ValidateOperationResponse response, int type) {
        super.validateResponse(response, type);
        try {
            if (response.isPermitted()) {
                isOperationPermitted = true;
                if (response.getPermittedCheckType() == Constant.CHECKOUT) {
                    operationTitle = getTitle(Constant.RA_CHECKOUT);
                    currentOperation = Constant.RA_CHECKOUT;
                } else {
                    operationTitle = getTitle(Constant.RA_CHECKIN);
                    currentOperation = Constant.RA_CHECKIN;
                }
                updateTitlesAndFields();
                resetFields();
                downloadVehicleCardImages(movementOperation.getVehicle());

            } else {
                currentOperation = 0;
                isOperationPermitted = false;
                setTitle();
                mNext.setEnabled(isOperationPermitted);
                String msg = response.getReason();
                if (msg == null || msg.length() < 1)
                    msg = getlocalizeString("VehicleNotAvailable", "Selected vehicle is not available for this operation");
                AlertDialogFragment alert = AlertDialogFragment.newInstance(getlocalizeString("ValidationError", "Validation Error"), msg, getlocalizeString("Ok", "Ok"), false);
                alert.show(getFragmentManager(), "detailAlert");

            }
        }catch (Exception e){e.printStackTrace();}
    }


    public void resetFields()
    {
        lockFields();

        if(currentOperation==Constant.RA_CHECKIN )
        {
            setEditable(mKm, true);
            //vehiclestatusContainer.setVisibility(View.VISIBLE);
//            personalInfo.setVisibility(View.INVISIBLE);
        }else {
            //vehiclestatusContainer.setVisibility(View.GONE);
            setEditable(mKm, true);
            if (movementOperation.getContact() == null) {
                setEditable(mCustomeridInput.getEditText(), true);
                setEditable(mCustomerNameInput.getEditText(), true);
                setEditable(mEmail, false);
//                scan_doc_type.setClickable(true);
//                personalInfo.setVisibility(View.VISIBLE);
//                personalInfo.setClickable(true);
            }

//            if(movementOperation.getRefNo()==null)
//            setEditable(mCustomerRefNumInput.getEditText(), true);

            if (movementOperation.getLastMovement() == null)
                setEditable(mKm, true);
        }

        // CRS Case
        if(movementOperation.getDriver()!=null)
            setEditable(mDriverName,false);
        else
            setEditable(mDriverName,true);

      // updateCustomerDocsVisibility();


    }

    @Override
    protected void updateTitlesAndFields() {
        super.updateTitlesAndFields();

        if(currentOperation==Constant.RA_CHECKOUT)
        {
            updateVehicleInfo();
            updateLocationinfo();

            updateCustomerInf();
            updateReferenceNo();
        }

        if (currentOperation == Constant.RA_CHECKIN) {
            updateCustomerInf();
            updateReferenceNo();
            showOutKMsAndFuel();
        }


        if (movementOperation.getAgreementNo()!= null)
            mAgreementNo.setText(""+movementOperation.getAgreementNo());
        //CRS case
        if(movementOperation.getDriver()!=null)
            mDriverName.setText(""+movementOperation.getDriver().getName());
        else
            mDriverName.setText("");

    }

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
            return ;
        mLastClickTime = SystemClock.elapsedRealtime();


      /* if(v.getId()==R.id.vs_action_next)
       {

           if(!validateFields())
               return;
           fillData();
           super.onClick(v);
       }else
       {
           super.onClick(v);
       }
*/

        switch (v.getId())
        {
            case R.id.vs_action_next:
                if(!validateFields())
               return;
           fillData();
           super.onClick(v);
                break;

/*
            case R.id.date:
                showDatePicker(dateContainer.getEditText(),timeContainer.getEditText(),calendar);
                break;
            case R.id.time:
                showTimePicker(timeContainer.getEditText(),calendar);
                break;*/

                default:
                    super.onClick(v);

        }

    }

    @Override
    protected void updateVehicleInfo() {
        super.updateVehicleInfo();
    }

    private void fillData()
    {
        if (movementOperation.getAgreementNo()!=null){
            movementInfo.setAgreementNo(movementOperation.getAgreementNo());
        }
       if(movementOperation.getContact()!=null)
        movementInfo.setContactId(""+movementOperation.getContact().getId());
        movementInfo.setMovementTypeId(1);// agreement
        movementInfo.setVehicleId(movementOperation.getVehicle().getId());
//        movementInfo.setRefNo(mCustomerRefNum.getText().toString());

        if(currentOperation==Constant.RA_CHECKOUT) {
            movementInfo.setOutDetail(operationInfo);
/*            if (dateTimeContainer.getVisibility() == View.VISIBLE)
            {
                OperationInfo infoForDate = new OperationInfo();
                infoForDate.setDateTime(""+AppUtils.getSelectedDateAndTime(calendar));
                movementInfo.setInDetail(infoForDate);
            }else {
                movementInfo.setInDetail(null);
            }*/
            movementInfo.setInDetail(null);
            movementInfo.setIsComplete(false);
            if (movementOperation.getLastMovement() != null && !movementOperation.getLastMovement().isComplete())
                movementInfo.setId(movementOperation.getLastMovement().getId());
            else
                movementInfo.setId(0);
            operationInfo.setOperationType("1");// opening
            Contact contact = new Contact();

            /* ArrayList<CustomerDocument> documents=new ArrayList<>();
            if(scan_doc_type.getText().length()>1 &&  mCustomerId.getText().length()>0)
            {
                CustomerDocument customerDocument=new CustomerDocument();
                int index=AppUtils.getIndexOf( getResources().getStringArray(R.array.doc_type),scan_doc_type.getText().toString());
                if(index>-1)
                {
                    customerDocument.setIdentityDocumentType(index+2);
                    customerDocument.setDocumentNo("" + mCustomerId.getText().toString());
                    documents.add(customerDocument);
                    //contact.setDocumentType(index+2);
                }
            }
            contact.setIdentityDocuments(documents);*/

            //contact.setDocumentNo("" + mCustomerId.getText().toString());
//            contact.setName(null);
//            contact.setNationalId(null);
//            contact.setContactType(null);
//            contact.setContactSubType(null);
//            contact.setFirstName(mCustomerName.getText().toString());
//            contact.setEmail(mEmail.getText().toString());

            if (selectedCustomer != null) {
                contact = selectedCustomer;
                contact.setFirstName(selectedCustomer.getName());
                if (selectedCustomer.getEmail() == null) {
                    contact.setEmail(mEmail.getText().toString());
                } else if (selectedCustomer.getEmail().isEmpty()) {
                    contact.setEmail(mEmail.getText().toString());
                }
            }
            if (selectedLocation != null)
                movementInfo.setOutDetail_LocationId(Integer.parseInt(selectedLocation.getId()));

           /* if (movementOperation.getDriver() != null)
                movementInfo.setOutDetail_DriverId(movementOperation.getDriver().getId());
            else if (selectedDriver != null)
                movementInfo.setOutDetail_DriverId(selectedDriver.getId());*/

            if (geoLocation.getWebID() != null)
                movementInfo.setOutDetail_GeoLocation(geoLocation);
            movementInfo.setContact(contact);

         }else {
            movementInfo.setInDetail(operationInfo);
            movementInfo.setOutDetail(null);
            movementInfo.setIsComplete(true);

            //change contact name parameter to firstName


            movementInfo.setContact(movementOperation.getContact());
            if (movementOperation.getLastMovement() != null)
                movementInfo.setId(movementOperation.getLastMovement().getId());
            operationInfo.setOperationType("3");// closing
            // VehicleStatus statusCode=AppUtils.searchStatus(DatabaseManager.getInstance(getActivity().getApplicationContext()).getVehiclStatuses(),mVehicleStatus.getText().toString());
            //  if(statusCode!=null)
            movementInfo.setInDetail_VehicleStatusId("IDL");
            /// outdetails
            if (movementOperation.getLastMovement() != null) {
                MovementInfo lastMovement=movementOperation.getLastMovement();
            movementInfo.setOutDetail_LocationId(lastMovement.getOutDetail_LocationId());
            movementInfo.setOutDetail_DriverId(lastMovement.getOutDetail_DriverId());
                movementInfo.setOutDetail_GeoLocation(lastMovement.getOutDetail_GeoLocation());
            }
            if(selectedLocation!=null)
            movementInfo.setInDetail_LocationId(Integer.parseInt(selectedLocation.getId()));

           /* if (movementOperation.getDriver() != null)
                movementInfo.setInDetail_DriverId(movementOperation.getDriver().getId());
            else if (selectedDriver != null)
                movementInfo.setInDetail_DriverId(selectedDriver.getId());*/

            if(geoLocation.getWebID()!=null)
            movementInfo.setInDetail_GeoLocation(geoLocation);

        }
        if (mKm.getText().toString().length()>0)
            operationInfo.setKm(Long.parseLong(mKm.getText().toString()));
        else
            operationInfo.setKm(0);

        int index=AppUtils.getIndexOf( getResources().getStringArray(R.array.fuel_levels),mFuel.getText().toString());
        if(index>-1)
            operationInfo.setFuelLevel(((float) index / 8));
        operationInfo.setMobile(true);
      /*  operationInfo.setLocationId(selectedLocation.getId());
        operationInfo.setDriverId("" + selectedDriver.getId());*/
        //String uuid = UUID.randomUUID().toString();
    }

    private boolean validateFields()
    {
        if(currentOperation==Constant.RA_CHECKOUT) {
            if (mCustomerName.getText().toString().length() < 1) {
                AppUtils.showMessage(getlocalizeString("NameValidateMsg", "Please enter Customer name"), mNext, Snackbar.LENGTH_SHORT);
                return false;
            }
            if (selectedCustomer == null) {
                AppUtils.showMessage(getlocalizeString("NameValidateMsg", "Please select Customer name"), mNext, Snackbar.LENGTH_SHORT);
                return false;
            }

//            if(mEmail.getText().toString().length()<1 || !Util.isValidEmail(mEmail.getText().toString()))
//            {
//                AppUtils.showMessage("Please enter a valid Email ID",mNext, Snackbar.LENGTH_SHORT);
//                return false;
//            }


           /* if(mCustomerRefNum.getText().toString().length()<1)
            {
                AppUtils.showMessage("Please enter Agreement number",mNext, Snackbar.LENGTH_SHORT);
                return false;
            }*/
        }
       /* if(mDriverContainer.getVisibility()==View.VISIBLE && !isValidDriverSelected())
        {
            AppUtils.showMessage("Driver is not selected Or invalid",mNext, Snackbar.LENGTH_SHORT);
            return false;
        }*/

        if(mKm.getText().toString().trim().length()<1 )
        {
            AppUtils.showMessage(getlocalizeString("OdoValidateMsg","Please enter ODO value"),mNext, Snackbar.LENGTH_SHORT);
            return false;
        }

        if( currentOperation==Constant.RA_CHECKIN &&(Float.valueOf(mKm.getText().toString())<movementOperation.getVehicle().getkMs()))
        {
            AppUtils.showMessage("ODO must be greater than "+movementOperation.getVehicle().getkMs(),mNext, Snackbar.LENGTH_LONG);
            return false;
        }

        if(mFuel.getText().toString().length()<1 )
        {
            AppUtils.showMessage(getlocalizeString("FuelValidateMsg","Please select Fuel Level"),mNext, Snackbar.LENGTH_SHORT);
            return false;
        }
       /* if (vehiclestatusContainer.getEditText().getText().toString().length()<1 && currentOperation==Constant.RA_CHECKIN)
        {
            AppUtils.showMessage("Please select Vehicle Status",mNext, Snackbar.LENGTH_SHORT);
            return false;
        }*/

       /* if(mLocationInput.getVisibility()==View.VISIBLE && !isValidLocationSelected())
        {
            AppUtils.showMessage(getlocalizeString("LocationValidateMsg","Location is not selected Or invalid"),mNext, Snackbar.LENGTH_SHORT);
            return false;
        }*/

/*        if (dateTimeContainer.getVisibility() == View.VISIBLE && dateContainer.getEditText().getText().toString().length()<1)
        {
            AppUtils.showMessage(getlocalizeString("RdatetimeValidateMsg","Please select Return Date and Time"),mNext, Snackbar.LENGTH_SHORT);
            return false;

        }*/

        return true;
    }


    private void  showDatePicker(final EditText editText, final EditText editTextTime, final Calendar calendar)
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

                Date date = calendar.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String selDate = formatter.format(date);
                editText.setText(""+selDate);

                Date time = calendar.getTime();
                SimpleDateFormat formatterTime = new SimpleDateFormat("h:mm a");
                String seltime = formatterTime.format(time);
                editTextTime.setText(seltime);
            }
        });
        datePicker.show(getActivity().getSupportFragmentManager(), "DatePicker");

    }

    private void showTimePicker(final EditText editText,final Calendar calendar) {

        TimePicker time=new TimePicker();
        Bundle args = new Bundle();
        args.putInt("hours",  calendar.get(Calendar.HOUR_OF_DAY));
        args.putInt("minutes",  calendar.get(Calendar.MINUTE));
        time.setArguments(args);
        time.setCallBack(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                Date date = calendar.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
                String seltime = formatter.format(date);
                editText.setText(seltime);
            }

        });
        time.show(getActivity().getSupportFragmentManager(), "TimePicker");

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mSelectedVehicle=  ((VehicleSearchAdapter)parent.getAdapter()).getVehicleAt(position);

        if(mSelectedVehicle==null)
            return;

        mVehiclenumber.setText(""+mSelectedVehicle.getPlateNo()+ " ("+mSelectedVehicle.getFleetCode() +")");
//        mVehicleFleet.setText(""+mSelectedVehicle.getFleetCode());
        mMake.setText(""+mSelectedVehicle.getMake());
        mModel.setText(""+mSelectedVehicle.getModel());
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

      /*  if(current_mode==Constant.RA_CHECKOUT)
        {
            request.setMovementTypeId(Constant.RA_CHECKOUT);
            request.setOperationType(-1);
            request.setCheckType(-1);
            request.setMovementCategory(-1);
            validateOperation(request, Constant.RA_CHECKOUT);

        }*/

        request.setMovementTypeId(Constant.RA_CHECKOUT);
        request.setOperationType(-1);
        request.setCheckType(-1);
        request.setMovementCategory(1);

        if(current_mode== Constant.RA_CHECKOUT)
        {
            request.setCheckType(1);
            validateOperation(request, Constant.RA_CHECKOUT);
        }
        else
        {
            request.setCheckType(2);
            validateOperation(request, Constant.RA_CHECKIN);
        }
    }
}
