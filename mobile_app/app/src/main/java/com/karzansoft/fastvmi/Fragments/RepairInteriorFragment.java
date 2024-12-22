package com.karzansoft.fastvmi.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.karzansoft.fastvmi.Activities.InspectVehicleActivity;
import com.karzansoft.fastvmi.Activities.SymbolDetailActivity;
import com.karzansoft.fastvmi.Dialogs.AlertDialogFragment;
import com.karzansoft.fastvmi.Interfaces.SymbolStateListener;
import com.karzansoft.fastvmi.MCCApplication;
import com.karzansoft.fastvmi.Models.MarkDetail;
import com.karzansoft.fastvmi.Models.MarkImage;
import com.karzansoft.fastvmi.Models.SymbolDetails;
import com.karzansoft.fastvmi.Models.SymbolImageDetail;
import com.karzansoft.fastvmi.Models.TariffGroup;
import com.karzansoft.fastvmi.Models.Vehicle;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Utils.SymbolDeserializer;
import com.karzansoft.fastvmi.Utils.Util;
import com.karzansoft.fastvmi.extended.MarkerViewInterior;
import com.karzansoft.fastvmi.extended.SymbolMarker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Yasir on 3/13/2017.
 */
public class RepairInteriorFragment extends Fragment implements View.OnClickListener ,SymbolStateListener {
    HashMap<String,SymbolMarker> symbolsHashMap;
    ArrayList<String> fixedMarksIds;
    MarkerViewInterior markerView;
    Vehicle vehicle;

    public static RepairInteriorFragment newInstance() {
        RepairInteriorFragment f = new RepairInteriorFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SymbolMarker.class, new SymbolDeserializer());
        Gson gson = gsonBuilder.create();

        if (savedInstanceState != null) {

            String jsonString=savedInstanceState.getString("MARKS", "");
            String vjson=savedInstanceState.getString("VEHICLE","");
            try{
                Type typeOfHashMap = new TypeToken<HashMap<String, SymbolMarker>>() { }.getType();
                if (jsonString.length()>0)
                    symbolsHashMap= gson.fromJson(jsonString,typeOfHashMap);
                if(vjson.length()>0)
                    vehicle=gson.fromJson(vjson,Vehicle.class);
                Util.LogE("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&",""+vjson);
            }catch (Exception e){e.printStackTrace();
            }

            fixedMarksIds=savedInstanceState.getStringArrayList("fixedIDs");
        }

        if(symbolsHashMap==null)
        {
            symbolsHashMap=new HashMap<String,SymbolMarker>();
        }

        if(fixedMarksIds==null)
            fixedMarksIds=new ArrayList<>();


    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        try {
            Type typeOfHashMap = new TypeToken<HashMap<String, SymbolMarker>>() {
            }.getType();
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            if (symbolsHashMap != null) {
                String jsonString = gson.toJson(symbolsHashMap, typeOfHashMap);
                Util.LogE("Fragment", "" + jsonString);
                outState.putString("MARKS", jsonString);
            }
            if(vehicle!=null)
            {
                String vjson=gson.toJson(vehicle,Vehicle.class);
                outState.putString("VEHICLE", vjson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        outState.putStringArrayList("fixedIDs", fixedMarksIds);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.vehicle_interior_fragment, container, false);
        markerView=(MarkerViewInterior) rootView.findViewById(R.id.marker_view);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        markerView.setSymbolStateListener(this);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(symbolsHashMap.size()>0)
        {
            ArrayList<SymbolMarker> images=new ArrayList<SymbolMarker>(symbolsHashMap.values());
            markerView.addSymbols(images);
        }

        if(vehicle!=null)
            AppUtils.loadVehicleInteriorMarks(vehicle, markerView, getActivity(), fixedMarksIds);
    }

    public void reloadVehicleMarks(Vehicle vehicle)
    {
        this.vehicle=vehicle;
        markerView.removeAllMarks();
        fixedMarksIds.clear();
        symbolsHashMap.clear();
        AppUtils.loadVehicleInteriorMarks(vehicle, markerView, getActivity(), fixedMarksIds);
        markerView.invalidate();
        loadCustomImage(vehicle);
    }

    private void loadCustomImage(Vehicle vehicle)
    {
        if(vehicle==null)
            return;

        TariffGroup tariffGroup =vehicle.getTariffGroup();

        if (tariffGroup != null) {
            if (tariffGroup.getInteriorDiagramName() != null && tariffGroup.getInteriorDiagramName().length() > 0) {
                ImageLoader.getInstance().loadImage(AppUtils.getImageUrlFromName(tariffGroup.getInteriorDiagramName()), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                        try {
                            markerView.updateBackgroundBitmap(loadedImage);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                    }
                });
            } else {
                markerView.resetBackgroundBitmap();
            }
        } else {
            markerView.resetBackgroundBitmap();
        }
    }

    public void addMark(int mtype)
    {
        markerView.addSymbolOntouch(mtype);
    }

    public void showSelectedMarkInfo()
    {
        SymbolMarker selected=markerView.getSelectedMarker();

        if(selected!=null)
        {
            Intent intent=new Intent(getActivity(),SymbolDetailActivity.class);
            intent.putExtra("ID",selected.getId());
            intent.putExtra("IS_EDITABLE",selected.isEditable());
            intent.putExtra("ACCESS_TOKEN","");
            startActivity(intent);
        }else{

            AlertDialogFragment alert=AlertDialogFragment.newInstance("",AppUtils.getLocalizeString(getActivity(),"","Please select damage to add pictures or to view it's detail"),AppUtils.getLocalizeString(getActivity(),"Ok","Ok"),false);

            alert.show(getFragmentManager(), "detailAlert");

        }
    }

    public void removeSelectedMarker()
    {
        SymbolMarker selectedMarker=markerView.getSelectedMarker();
        if(selectedMarker==null)
            return;
        AlertDialogFragment alert;

        if(selectedMarker.isEditable)
        {
            alert=AlertDialogFragment.newInstance(AppUtils.getLocalizeString(getActivity(),"RemoveMark","Remove Mark"),AppUtils.getLocalizeString(getActivity(),"RemoveMarkMsg","Are you sure you want to remove this mark?"),AppUtils.getLocalizeString(getActivity(),"","Remove"),true);
            alert.setButtonsClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == AlertDialog.BUTTON_POSITIVE)
                        markerView.removeSelectedMarker();
                }
            });
            alert.show(getFragmentManager(), "confirmationAlert");
            return;
        }

        if (AppUtils.getSettings(getActivity()).isCanRemoveDamage())
        {
            alert=AlertDialogFragment.newInstance(AppUtils.getLocalizeString(getActivity(),"RemoveMark","Remove Mark"),AppUtils.getLocalizeString(getActivity(),"RemoveMarkMsg","Are you sure you want to remove this mark?"),AppUtils.getLocalizeString(getActivity(),"Remove","Remove"),true);
            alert.setButtonsClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == AlertDialog.BUTTON_POSITIVE) {
                        fixedMarksIds.add(markerView.getSelectedMarker().getId());
                        markerView.removeSelectedMarker();
                    }
                }
            });
        }else
            alert=AlertDialogFragment.newInstance(AppUtils.getLocalizeString(getActivity(),"RemoveMark","Remove Mark"),AppUtils.getLocalizeString(getActivity(),"RemoveMarkError","This mark can not be removed"),AppUtils.getLocalizeString(getActivity(),"","Ok"),false);

        alert.show(getFragmentManager(), "confirmationAlert");

    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSymbolAdded(SymbolMarker symbolMarker)
    {
        if (symbolMarker.isEditable) {
            ((MCCApplication) getActivity().getApplication()).addSymbol(symbolMarker.markerType, symbolMarker.getId());
            symbolsHashMap.put(symbolMarker.getId(), symbolMarker);
        }
    }

    @Override
    public void onRemoved(SymbolMarker symbolMarker)
    {
        if (symbolMarker.isEditable) {
            ((MCCApplication) getActivity().getApplication()).removeSymbol(symbolMarker.getId());
            symbolsHashMap.remove(symbolMarker.getId());
        }
    }

    @Override
    public void onSymbolSelected(SymbolMarker symbolMarker) {

        if(getActivity() instanceof InspectVehicleActivity)
        {
            ((InspectVehicleActivity)getActivity()).resetSelection();
        }
    }

    public ArrayList<MarkDetail> getUpdatedMarks()
    {
        ArrayList<MarkDetail> marks=new ArrayList<>();
        if(symbolsHashMap.size()>0)
        {
            int vehicleId=vehicle.getId();
            ArrayList<SymbolMarker> images=new ArrayList<SymbolMarker>(symbolsHashMap.values());
            for (int i=0;i<images.size();i++)
            {
                SymbolMarker symbolMarker=images.get(i);
                MarkDetail mark=new MarkDetail();
                mark.setVehicleId(vehicleId);
                if (symbolMarker.markerType == Constant.SYMBOL_MARKER_LARGE_DENT)
                    mark.setMarkTypeId(5);
                else
                    mark.setMarkTypeId(7);

                SymbolDetails det=  ((MCCApplication) getActivity().getApplication()).getSymbolById(symbolMarker.getId());
                if(det!=null)
                    mark.setDescription(""+det.getInfo());
                // Images
                ArrayList<SymbolImageDetail> dentImages=det.getImages();
                if(dentImages!=null && dentImages.size()>0)
                {
                    ArrayList<MarkImage> markImages=new ArrayList<>();
                    for (int j=0;j<dentImages.size();j++)
                    {
                        SymbolImageDetail img=dentImages.get(j);
                        if(img!=null && img.getImgUrl()!=null && img.getImgUrl().length()>0)
                        {
                            MarkImage mimg=new MarkImage();
                            mimg.setImagePath(img.getImgUrl().replace("file://",""));// localpath
                            String UID= UUID.randomUUID().toString().replace('-','_');
                            mimg.setGuid(UID);
                            mimg.setWidth(512);
                            mimg.setHeight(512);
                            markImages.add(mimg);
                        }
                    }

                    mark.setImages(markImages);
                }

                JSONObject vector=new JSONObject();
                try {
                    switch (symbolMarker.markerType) {
                        case Constant.SYMBOL_MARKER_LARGE_DENT:
                        case Constant.SYMBOL_MARKER_CRACK:
                            vector.put("X", markerView.getScaledX(symbolMarker.startX));
                            vector.put("Y", markerView.getScaledY(symbolMarker.startY));
                            break;
                    }

                }catch (Exception ex){ex.printStackTrace();}
                mark.setVector(vector.toString());
                marks.add(mark);

            }

        }

        for (int i=0;i<fixedMarksIds.size();i++)// repaired mark ids
        {
            MarkDetail mark=new MarkDetail();
            mark.setId(Integer.parseInt(fixedMarksIds.get(i)));
            mark.setStatus(2);// repaired
            marks.add(mark);
        }

        return marks;
    }
}
