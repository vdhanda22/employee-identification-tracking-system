package com.karzansoft.fastvmi.Fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.karzansoft.fastvmi.Adapters.VehicleSearchAdapter;
import com.karzansoft.fastvmi.Models.InspectionDto;
import com.karzansoft.fastvmi.Network.Entities.Request.GetQuestionsTemplateRequest;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;

/**
 * Created by Dastgeer on 4/05/2020.
 */
public class InspectionFragment extends InspectionBaseFragment implements AdapterView.OnItemClickListener {

    public static InspectionFragment newInstance(int current_mode) {
        InspectionFragment f = new InspectionFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.CURRENT_MODE, current_mode);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVehiclenumber.setOnItemClickListener(this);
        mVehiclenumber.setAdapter(new VehicleSearchAdapter(getActivity(), true));
        getActivity().setTitle("Vehicle Inspection");
    }

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
            return;
        mLastClickTime = SystemClock.elapsedRealtime();

        switch (v.getId()) {
            case R.id.vs_action_next:
                fillInspectionData();
                super.onClick(v);
                break;
            default:
                super.onClick(v);

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mSelectedVehicle = ((VehicleSearchAdapter) parent.getAdapter()).getVehicleAt(position);

        if (mSelectedVehicle == null)
            return;

        mVehiclenumber.setText("" + mSelectedVehicle.getPlateNo()+ " ("+mSelectedVehicle.getFleetCode() +")");
        mVehicleFleet.setText("" + mSelectedVehicle.getFleetCode());
        mMake.setText("" + mSelectedVehicle.getMake());
        mModel.setText("" + mSelectedVehicle.getModel());
        mKm.setText("" + mSelectedVehicle.getkMs());
        GetQuestionsTemplateRequest request = new GetQuestionsTemplateRequest(mSelectedVehicle.getId());
        GetTemplateByVehicle(request);
        isOperationPermitted = true;
        mNext.setEnabled(isOperationPermitted);
        if (mVehiclenumber.hasFocus())
            AppUtils.hideKeyboardFrom(getActivity(), mVehiclenumber);
    }

    private void fillInspectionData() {
        inspectionDto = new InspectionDto();
        inspectionDto.setVehicleId(mSelectedVehicle.getId());
        inspectionDto.setkMs(Double.parseDouble(mKm.getText().toString()));
        if (geoLocation != null) {
            inspectionDto.setGeoLocationLatitude(geoLocation.getLatitude());
            inspectionDto.setGeoLocationLongitude(geoLocation.getLogitude());
        }
    }


}
