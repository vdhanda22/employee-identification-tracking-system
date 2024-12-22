package com.karzansoft.fastvmi.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.karzansoft.fastvmi.Adapters.ModelSearchAdapter;
import com.karzansoft.fastvmi.Models.AppSettings;
import com.karzansoft.fastvmi.Models.Vehicle;
import com.karzansoft.fastvmi.Models.VehicleModel;
import com.karzansoft.fastvmi.Network.APIError;
import com.karzansoft.fastvmi.Network.Entities.Request.AddVehicleRequest;
import com.karzansoft.fastvmi.Network.Entities.Response.GetTariffGroupResponse;
import com.karzansoft.fastvmi.Network.Entities.Response.SaveVehicleReponse;
import com.karzansoft.fastvmi.Network.ErrorUtils;
import com.karzansoft.fastvmi.Network.WebResponse;
import com.karzansoft.fastvmi.Network.WebResponseList;
import com.karzansoft.fastvmi.Network.WebServiceFactory;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yasir on 3/2/2017.
 */
public class AddVehicleFragment extends BaseFragment implements View.OnClickListener {

    public EditText edtPlateNo, edtYear;
    public AutoCompleteTextView edtModal;
    public VehicleModel selectedModel;
    public ArrayList<GetTariffGroupResponse> TariffGroup;
    long TariffGroupId;
    TextInputLayout plateNUmberContainer, modelContainer, yearContainer;
    Spinner VehicleType;
    Button btn_save;
    AppSettings appSettings;

    public static AddVehicleFragment newInstance() {
        AddVehicleFragment f = new AddVehicleFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_vehicle_fragment, container, false);
        edtPlateNo = (EditText) rootView.findViewById(R.id.plateNumber);
        edtModal = (AutoCompleteTextView) rootView.findViewById(R.id.model);
        edtModal.setAdapter(new ModelSearchAdapter(getActivity()));
        edtYear = (EditText) rootView.findViewById(R.id.year);
        btn_save = (Button) rootView.findViewById(R.id.save);
        plateNUmberContainer = (TextInputLayout) rootView.findViewById(R.id.plateNumberContainer);
        modelContainer = (TextInputLayout) rootView.findViewById(R.id.modelContainer);
        yearContainer = (TextInputLayout) rootView.findViewById(R.id.yearContainer);
        VehicleType = (Spinner) rootView.findViewById(R.id.vehicletype);
        appSettings = AppUtils.getSettings(getActivity());
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(AppUtils.getLocalizeString(getActivity(), "AddVehicle", "Add Vehicle"));
        plateNUmberContainer.setHint(AppUtils.getLocalizeString(getActivity(), "PlateNumber", "Plate Number"));
        modelContainer.setHint(AppUtils.getLocalizeString(getActivity(), "SearchMakeModel", "Search Make/Model"));
        yearContainer.setHint(AppUtils.getLocalizeString(getActivity(), "Year", "Year"));
        btn_save.setText(AppUtils.getLocalizeString(getActivity(), "Save", "Save"));

        btn_save.setOnClickListener(this);

        edtModal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedModel = ((ModelSearchAdapter) parent.getAdapter()).getModelAt(position);
                if (edtModal.hasFocus())
                    AppUtils.hideKeyboardFrom(getActivity(), edtModal);
            }
        });
        edtModal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
              /*  if(s.length()>0)
                    modelContainer.setHint("Make Model");
                else
                    modelContainer.setHint("Search Make/Model");*/
            }
        });
        if (appSettings.issCSEnabled()) {
            GetTariffGroup();
            VehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    GetTariffGroupResponse TariffGroup = (GetTariffGroupResponse) parent.getSelectedItem();
                    TariffGroupId = TariffGroup.getId();
                    //Toast.makeText(getActivity(), "TariffGroup ID: "+TariffGroup.getId()+",   Name : "+TariffGroup.getTitle(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            VehicleType.setVisibility(View.GONE);
        }

    }

    protected void GetTariffGroup() {
        if (getActivity() == null)
            return;
        if (!AppUtils.isNetworkAvailable(getActivity())) {
            AppUtils.showMessage(getlocalizeString("NetworkError", "Can't connect to network!"), btn_save, Snackbar.LENGTH_SHORT);
            return;
        }
        showProgress();
        TariffGroup = new ArrayList<>();
        Call<WebResponse<WebResponseList<GetTariffGroupResponse>>> responseCall = WebServiceFactory.getInstance().GetTariffGroups(new Object(), AppUtils.getAuthToken(getActivity()));
        responseCall.enqueue(new Callback<WebResponse<WebResponseList<GetTariffGroupResponse>>>() {
            @Override
            public void onResponse(Call<WebResponse<WebResponseList<GetTariffGroupResponse>>> call, Response<WebResponse<WebResponseList<GetTariffGroupResponse>>> response) {
                hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        WebResponseList<GetTariffGroupResponse> result = response.body().getResult();
                        if (result != null) {
                            TariffGroup = result.getItems();
                            GetTariffGroupResponse zero = new GetTariffGroupResponse();
                            zero.setTitle("Select Vehicle Type");
                            TariffGroup.add(0, zero);
                            ArrayAdapter<GetTariffGroupResponse> adapter = new ArrayAdapter<GetTariffGroupResponse>
                                    (getActivity(), android.R.layout.simple_spinner_dropdown_item, TariffGroup) {
                                @Override
                                public boolean isEnabled(int position) {
                                    if (position == 0) {
                                        // Disable the first item from Spinner
                                        // First item will be use for hint
                                        return false;
                                    } else {
                                        return true;
                                    }
                                }

                                @Override
                                public View getDropDownView(int position, View convertView,
                                                            ViewGroup parent) {
                                    View view = super.getDropDownView(position, convertView, parent);
                                    TextView tv = (TextView) view;
                                    if (position == 0) {
                                        // Set the hint text color gray
                                        tv.setTextColor(Color.GRAY);
                                    } else {
                                        tv.setTextColor(Color.BLACK);
                                    }
                                    return view;
                                }
                            };
                            VehicleType.setAdapter(adapter);
                        }
                    } else {
                        AppUtils.showMessage("" + response.body().getError().getMessage(), btn_save, Snackbar.LENGTH_SHORT);
                    }
                } else {
                    if (response.code() == 401)
                        reLogin();
                    else {
                        String message = getlocalizeString("InternalServerError", "Internal server error!");
                        APIError error = ErrorUtils.parseError(response);
                        if (error != null && error.message() != null)
                            message = error.message();

                        AppUtils.showMessage(message, btn_save, Snackbar.LENGTH_SHORT);
                    }
                }
            }

            @Override
            public void onFailure(Call<WebResponse<WebResponseList<GetTariffGroupResponse>>> call, Throwable t) {
                hideProgress();
                AppUtils.showMessage(getlocalizeString("ConnectionTimeOut", "Connection time out!"), btn_save, Snackbar.LENGTH_SHORT);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                addVehicle();
                break;
        }
    }

    private void addVehicle() {
        if (edtPlateNo.getText().length() < 1 || edtModal.getText().length() < 1 || edtYear.getText().length() < 1 || ((Long.valueOf(TariffGroupId) == null || TariffGroupId == 0) && appSettings.issCSEnabled())) {
            String msg = "";
            if (edtPlateNo.getText().length() < 1)
                msg = AppUtils.getLocalizeString(getActivity(), "PlateNoValidateMsg", "Please Enter Plate No");
            else if (edtModal.getText().length() < 1)
                msg = AppUtils.getLocalizeString(getActivity(), "ModalValidateMsg", "Please Enter Make Model");
            else if ((Long.valueOf(TariffGroupId) == null || TariffGroupId == 0) && appSettings.issCSEnabled())
                msg = AppUtils.getLocalizeString(getActivity(), "SelectVehicleType", "Select Vehicle Type");
            else
                msg = AppUtils.getLocalizeString(getActivity(), "YearValidateMsg", "Please Enter Make Year");
            AppUtils.showToastMessage(msg, getActivity());
        } else {
            final VehicleModel model = selectedModel;
            if (model == null || !edtModal.getText().toString().equalsIgnoreCase("" + model.getName() + " " + model.getMake())) {
                AppUtils.showToastMessage(AppUtils.getLocalizeString(getActivity(), "MakeModalValidateMsg", "Please select a valid Make Model"), getActivity());
                return;
            }
            Vehicle vehicle = new Vehicle();
            vehicle.setFleetCode("0");
            vehicle.setPlateNo(edtPlateNo.getText().toString());
            vehicle.setModelYear(edtYear.getText().toString());
            vehicle.setModelId("" + selectedModel.getId());
            if (appSettings.issCSEnabled())
                vehicle.setTariffGroupId(TariffGroupId);
            final AddVehicleRequest request = new AddVehicleRequest();
            request.setVehicle(vehicle);
            showProgress();
            Call<WebResponse<SaveVehicleReponse>> saveRequest = WebServiceFactory.getInstance().saveVehicle(request, AppUtils.getAuthToken(getActivity()));
            saveRequest.enqueue(new Callback<WebResponse<SaveVehicleReponse>>() {
                @Override
                public void onResponse(Call<WebResponse<SaveVehicleReponse>> call, Response<WebResponse<SaveVehicleReponse>> response) {
                    hideProgress();
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().isSuccess()) {
                            popAllFragment();
                            AppUtils.showToastMessage(AppUtils.getLocalizeString(getActivity(), "VehicleAddedSuccessfully", "Vehicle Added Successfully"), getActivity());

                        } else {
                            AppUtils.showToastMessage("" + response.body().getError().getMessage(), getActivity());
                        }
                    } else {
                        String message = AppUtils.getLocalizeString(getActivity(), "VehicleSavingError", "Error in saving Vehicle!");
                        APIError error = ErrorUtils.parseError(response);
                        if (error != null && error.message() != null)
                            message = error.message();
                        AppUtils.showToastMessage(message, getActivity());
                    }
                }

                @Override
                public void onFailure(Call<WebResponse<SaveVehicleReponse>> call, Throwable t) {
                    hideProgress();
                    t.printStackTrace();
                    String message = AppUtils.getLocalizeString(getActivity(), "VehicleSavingError", "Error in saving Vehicle!");
                    AppUtils.showToastMessage(message, getActivity());

                }
            });
        }
    }
}
