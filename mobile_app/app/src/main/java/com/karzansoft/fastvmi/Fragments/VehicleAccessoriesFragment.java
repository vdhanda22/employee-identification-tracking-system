package com.karzansoft.fastvmi.Fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karzansoft.fastvmi.Adapters.AccessoryItemsAdapter;
import com.karzansoft.fastvmi.DataBase.DatabaseManager;
import com.karzansoft.fastvmi.Models.AccessoryItem;
import com.karzansoft.fastvmi.Models.MovementInfo;
import com.karzansoft.fastvmi.Network.Entities.Response.ValidateOperationResponse;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Utils.Util;

import java.util.ArrayList;

/**
 * Created by Yasir on 4/6/2016.
 */
public class VehicleAccessoriesFragment extends BaseFragment implements View.OnClickListener {


    ListView mListView ;
    Button mNext ;
    ArrayList<AccessoryItem> items;
    AccessoryItemsAdapter adapter;
    ValidateOperationResponse currentOperation;
    MovementInfo currentMovement;
    boolean isPreviousAccessoriesLoaded;

    public static VehicleAccessoriesFragment newInstance(int current_mode,ValidateOperationResponse currentOpe,MovementInfo movementInfo) {
        VehicleAccessoriesFragment f = new VehicleAccessoriesFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.CURRENT_MODE, current_mode);
        if(currentOpe!=null){
        Gson gson = new GsonBuilder().create();
        args.putString("CurrentOperation",gson.toJson(currentOpe,ValidateOperationResponse.class));
        args.putString("CurrentMovementInfo",gson.toJson(movementInfo,MovementInfo.class));
        }
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items= DatabaseManager.getInstance(getActivity().getApplicationContext()).getAccessories();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_vehicle_accessories, container, false);
        rootView.findViewById(R.id.appbar).setVisibility(View.GONE);
        current_mode=this.getArguments().getInt(Constant.CURRENT_MODE,1);
        mNext = (Button) rootView.findViewById(R.id.va_action_next);
        mListView = (ListView) rootView.findViewById(R.id.listviewcheckboxes);
        String json=this.getArguments().getString("CurrentOperation","");
        String jsonm=this.getArguments().getString("CurrentMovementInfo","");
        Util.LogE("Json", "" + jsonm);
        Util.LogE("Json", "" + json);
        Gson gson = new GsonBuilder().create();
        if (json.length() > 0)
            currentOperation = gson.fromJson(json, ValidateOperationResponse.class);

        if (jsonm.length() > 0)
            currentMovement = gson.fromJson(jsonm, MovementInfo.class);


        return  rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle();
        AppUtils.setText(mNext,getlocalizeString("Next","Next"));
        mNext.setOnClickListener(this);
        mListView.setChoiceMode(mListView.CHOICE_MODE_MULTIPLE);
        mListView.setTextFilterEnabled(true);
        mNext.setOnClickListener(this);
        if(current_mode==Constant.RA_CHECKIN ||current_mode==Constant.REPLACEMENT_CHECKIN ||current_mode==Constant.NRM_CHECKIN ||current_mode==Constant.GARAGE_CHECKIN )
        {
            adapter=new AccessoryItemsAdapter(getActivity(),R.layout.list_item_accessories,items,Constant.ACCESSORIES_IN);
            if (currentOperation!=null )
            {
                MovementInfo lastmovement=currentOperation.getLastMovement();
                if(lastmovement!=null && lastmovement.getOutDetail()!=null && !lastmovement.isComplete())
                    adapter.setCheckedIds(AppUtils.getCheckedListIds(lastmovement.getOutCheckList()));
            }
        }else
        {
            if (currentOperation!=null && !isPreviousAccessoriesLoaded)
            {
                MovementInfo lastmovement=currentOperation.getLastMovement();
                if(lastmovement!=null && lastmovement.getInDetail()!=null && lastmovement.getInCheckList()!=null)
                {
                    ArrayList<String> ids=AppUtils.getCheckedListIds(lastmovement.getInCheckList());
                    for (int i=0;i<items.size();i++)
                    {
                        AccessoryItem item=items.get(i);
                        if(ids.contains(""+item.getId()))
                            item.setStatus(true);
                    }
                }
                isPreviousAccessoriesLoaded=true;
            }

            adapter=new AccessoryItemsAdapter(getActivity(),R.layout.list_item_accessories,items,Constant.ACCESSORIES_OUT);
        }


        mListView.setAdapter(adapter);
        mLastClickTime = SystemClock.elapsedRealtime();
    }

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 500)
            return ;
        mLastClickTime = SystemClock.elapsedRealtime();
        switch (v.getId())
        {
            case R.id.va_action_next:
                ArrayList<AccessoryItem> checkedItems=new ArrayList<>();
                for (int i=0;i<items.size();i++)
                {
                    if(items.get(i).isStatus())
                        checkedItems.add(items.get(i));
                }

                if(checkedItems.size()>0)
                {
                    if (current_mode == Constant.RA_CHECKIN || current_mode == Constant.REPLACEMENT_CHECKIN || current_mode == Constant.NRM_CHECKIN ||current_mode==Constant.GARAGE_CHECKIN)
                        currentMovement.setInCheckList(checkedItems);
                    else
                        currentMovement.setOutCheckList(checkedItems);


                }



                VehicleMarkFragment fragment=VehicleMarkFragment.newInstance(current_mode,currentOperation,currentMovement);
                addFragment(fragment,fragment.getClass().getName());

                break;
        }
    }
}
