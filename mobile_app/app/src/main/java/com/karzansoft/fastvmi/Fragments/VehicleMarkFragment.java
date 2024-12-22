package com.karzansoft.fastvmi.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.karzansoft.fastvmi.Activities.SymbolDetailActivity;
import com.karzansoft.fastvmi.Dialogs.AlertDialogFragment;
import com.karzansoft.fastvmi.Interfaces.SymbolStateListener;
import com.karzansoft.fastvmi.MCCApplication;
import com.karzansoft.fastvmi.Models.AppSettings;
import com.karzansoft.fastvmi.Models.MarkDetail;
import com.karzansoft.fastvmi.Models.MarkImage;
import com.karzansoft.fastvmi.Models.MovementInfo;
import com.karzansoft.fastvmi.Models.SymbolDetails;
import com.karzansoft.fastvmi.Models.SymbolImageDetail;
import com.karzansoft.fastvmi.Models.TariffGroup;
import com.karzansoft.fastvmi.Network.Entities.Response.ValidateOperationResponse;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Utils.SymbolDeserializer;
import com.karzansoft.fastvmi.Utils.Util;
import com.karzansoft.fastvmi.extended.LineMarker;
import com.karzansoft.fastvmi.extended.MarkerView;
import com.karzansoft.fastvmi.extended.SymbolMarker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by Yasir on 4/6/2016.
 */
public class VehicleMarkFragment extends BaseFragment implements View.OnClickListener ,SymbolStateListener {
    Button mNext;
    MarkerView markerView;
    ImageView imgDent,imgLdent,imgScratch,imgScratchThin,imgInfo,imgDel,imgBrk,imgDelAll;
    HashMap<String,SymbolMarker> symbolsHashMap;
    ValidateOperationResponse currentOperation;
    MovementInfo currentMovement;
    ArrayList<String> fixedMarksIds;

    AppSettings appSettings;

    public static VehicleMarkFragment newInstance(int current_mode,ValidateOperationResponse currentOpe,MovementInfo movementInfo) {
        VehicleMarkFragment f = new VehicleMarkFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.CURRENT_MODE,current_mode);
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

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SymbolMarker.class, new SymbolDeserializer());
        Gson gson = gsonBuilder.create();

        if (savedInstanceState != null) {

            String jsonString=savedInstanceState.getString("MARKS", "");
            Util.LogE("SavedInstanceState:Marks",""+jsonString);
            try{
                Type typeOfHashMap = new TypeToken<HashMap<String, SymbolMarker>>() { }.getType();
                if (jsonString.length()>0)
                    symbolsHashMap= gson.fromJson(jsonString,typeOfHashMap);
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

        appSettings=AppUtils.getSettings(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_vehicle_marks, container, false);
        rootView.findViewById(R.id.appbar).setVisibility(View.GONE);
        current_mode=this.getArguments().getInt(Constant.CURRENT_MODE,1);
        imgDent=(ImageView)rootView.findViewById(R.id.img_small_dent);
        imgLdent=(ImageView)rootView.findViewById(R.id.img_large_dent);
        imgScratch=(ImageView)rootView.findViewById(R.id.img_scratch);
        imgScratchThin=(ImageView)rootView.findViewById(R.id.img_scratch_thin);
        imgInfo=(ImageView)rootView.findViewById(R.id.img_info);
        imgDel=(ImageView)rootView.findViewById(R.id.img_del);
        imgBrk=(ImageView)rootView.findViewById(R.id.img_broken);
        imgDelAll=(ImageView)rootView.findViewById(R.id.img_del_all);
        markerView=(MarkerView)rootView.findViewById(R.id.marker_view);
        mNext = (Button) rootView.findViewById(R.id.vm_action_next);
        String json=this.getArguments().getString("CurrentOperation", "");
        String jsonm=this.getArguments().getString("CurrentMovementInfo","");
        Gson gson = new GsonBuilder().create();
        Util.LogE("Json", "" + jsonm);
        if (json.length() > 0)
            currentOperation = gson.fromJson(json, ValidateOperationResponse.class);

        if (jsonm.length() > 0)
            currentMovement = gson.fromJson(jsonm, MovementInfo.class);


        return  rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle();
        AppUtils.setText(mNext,getlocalizeString("Next","Next"));
        imgDent.setOnClickListener(this);
        imgLdent.setOnClickListener(this);
        imgScratch.setOnClickListener(this);
        imgScratchThin.setOnClickListener(this);
        imgBrk.setOnClickListener(this);
        imgInfo.setOnClickListener(this);
        imgDel.setOnClickListener(this);

        mNext.setOnClickListener(this);
        markerView.setSymbolStateListener(this);
        mLastClickTime = SystemClock.elapsedRealtime();

        if(appSettings.isCanRemoveDamage())
        {
            view.findViewById(R.id.del_all_Sep).setVisibility(View.VISIBLE);
            imgDelAll.setVisibility(View.VISIBLE);
            imgDelAll.setOnClickListener(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        if(symbolsHashMap.size()>0)
        {
            ArrayList<SymbolMarker> images=new ArrayList<SymbolMarker>(symbolsHashMap.values());
            markerView.addSymbols(images);
        }

            AppUtils.loadCheckoutMrks(currentOperation, markerView, getActivity(), fixedMarksIds);


        loadCustomImage();

    }

    private void loadCustomImage()
    {
        if(currentOperation.getVehicle()==null)
            return;

        TariffGroup tariffGroup = currentOperation.getVehicle().getTariffGroup();

        if (tariffGroup!=null)
        {
            if (tariffGroup.getExteriorDiagramName()!=null && tariffGroup.getExteriorDiagramName().length()>0)
            {
                ImageLoader.getInstance().loadImage(AppUtils.getImageUrlFromName(tariffGroup.getExteriorDiagramName()), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {}
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Util.LogE("Image Loded",imageUri);
                        try {
                            markerView.updateBackgroundBitmap(loadedImage);
                        }catch (Exception ex){ex.printStackTrace();}
                    }
                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {}
                });
            }
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        outState.putStringArrayList("fixedIDs", fixedMarksIds);

    }


    private void fillData()
    {
        ArrayList<MarkDetail> marks=new ArrayList<>();
        if(symbolsHashMap.size()>0)
        {
            int vehicleId=currentMovement.getVehicleId();
            ArrayList<SymbolMarker> images=new ArrayList<SymbolMarker>(symbolsHashMap.values());

            for (int i=0;i<images.size();i++)
            {
                SymbolMarker symbolMarker=images.get(i);
                MarkDetail mark=new MarkDetail();
                mark.setVehicleId(vehicleId);
                mark.setMarkTypeId(symbolMarker.markerType);
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
                            String UID=UUID.randomUUID().toString().replace('-','_');
                            mimg.setGuid(UID);
                            mimg.setWidth(512);// dummy values
                            mimg.setHeight(512);
                            markImages.add(mimg);
                        }
                    }

                    mark.setImages(markImages);
                }


                JSONObject vector=new JSONObject();
                try {
                    switch (symbolMarker.markerType) {
                        case Constant.SYMBOL_MARKER_SMALL_DENT:
                        case Constant.SYMBOL_MARKER_LARGE_DENT:
                        case Constant.SYMBOL_MARKER_CRACK:
                            vector.put("X", markerView.getScaledX(symbolMarker.startX));
                            vector.put("Y", markerView.getScaledY(symbolMarker.startY));
                            break;
                        case Constant.SYMBOL_MARKER_SCRATCH:
                        case Constant.SYMBOL_MARKER_SCRATCH_THIN:
                            JSONObject point1=new JSONObject(),point2=new JSONObject();
                            point1.put("X", markerView.getScaledX(symbolMarker.startX));
                            point1.put("Y", markerView.getScaledY(symbolMarker.startY));
                            point2.put("X", markerView.getScaledX(((LineMarker) symbolMarker).endX));
                            point2.put("Y", markerView.getScaledY(((LineMarker) symbolMarker).endY));
                            vector.put("From",point1);
                            vector.put("To",point2);
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

        if (current_mode == Constant.RA_CHECKIN || current_mode == Constant.REPLACEMENT_CHECKIN || current_mode == Constant.NRM_CHECKIN ||current_mode==Constant.GARAGE_CHECKIN)
            currentMovement.setInMarks(marks);
        else
            currentMovement.setOutMarks(marks);
    }


    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.vm_action_next:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500)
                    return ;
                mLastClickTime = SystemClock.elapsedRealtime();
                fillData();
                VehicleMarkInteriorFragment fragment=VehicleMarkInteriorFragment.newInstance(current_mode,currentOperation,currentMovement);
                addFragment(fragment,fragment.getClass().getName());
                break;
            case R.id.img_small_dent:
                markerView.addSymbolOntouch(Constant.SYMBOL_MARKER_SMALL_DENT);
                imgDent.setSelected(true);
                imgLdent.setSelected(false);
                imgScratch.setSelected(false);
                imgScratchThin.setSelected(false);
                imgBrk.setSelected(false);
                getSnackbar(Constant.SYMBOL_MARKER_SMALL_DENT).show();

                break;
            case R.id.img_large_dent:
                markerView.addSymbolOntouch(Constant.SYMBOL_MARKER_LARGE_DENT);
                imgDent.setSelected(false);
                imgLdent.setSelected(true);
                imgScratch.setSelected(false);
                imgScratchThin.setSelected(false);
                imgBrk.setSelected(false);
                getSnackbar(Constant.SYMBOL_MARKER_LARGE_DENT).show();

                break;
            case R.id.img_scratch:
                markerView.addSymbolOntouch(Constant.SYMBOL_MARKER_SCRATCH);
                imgDent.setSelected(false);
                imgLdent.setSelected(false);
                imgScratch.setSelected(true);
                imgScratchThin.setSelected(false);
                imgBrk.setSelected(false);
                getSnackbar(Constant.SYMBOL_MARKER_SCRATCH).show();
                break;
            case R.id.img_scratch_thin:
                markerView.addSymbolOntouch(Constant.SYMBOL_MARKER_SCRATCH_THIN);
                imgDent.setSelected(false);
                imgLdent.setSelected(false);
                imgScratch.setSelected(false);
                imgScratchThin.setSelected(true);
                imgBrk.setSelected(false);
                getSnackbar(Constant.SYMBOL_MARKER_SCRATCH_THIN).show();
                break;
            case R.id.img_broken:
                 markerView.addSymbolOntouch(Constant.SYMBOL_MARKER_CRACK);
                imgDent.setSelected(false);
                imgLdent.setSelected(false);
                imgScratch.setSelected(false);
                imgScratchThin.setSelected(false);
                imgBrk.setSelected(true);
                getSnackbar(Constant.SYMBOL_MARKER_CRACK).show();
                break;
            case R.id.img_info:
                SymbolMarker selected=markerView.getSelectedMarker();

                if(selected!=null)
                {
                    Intent intent=new Intent(getActivity(),SymbolDetailActivity.class);
                    intent.putExtra("ID",selected.getId());
                    intent.putExtra("IS_EDITABLE",selected.isEditable());
                    intent.putExtra("ACCESS_TOKEN","");
                    startActivity(intent);
                }else{

                    AlertDialogFragment alert=AlertDialogFragment.newInstance(getlocalizeString("MarkDetail","Mark detail"),getlocalizeString("MarkDetailSelectioMsg","Please select at least one mark to view it's detail "),getlocalizeString("Ok","Ok"),false);

                    alert.show(getFragmentManager(), "detailAlert");

                }
                break;
            case R.id.img_del:
                SymbolMarker selectedMarker=markerView.getSelectedMarker();
                if(selectedMarker==null)
                    return;
                AlertDialogFragment alert;

                if(selectedMarker.isEditable)
                {
                    alert=AlertDialogFragment.newInstance(getlocalizeString("RemoveMark","Remove Mark"),getlocalizeString("RemoveMarkMsg","Are you sure you want to remove this mark?"),getlocalizeString("Remove","Remove"),true);
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



                if (appSettings.isCanRemoveDamage())
                {
                    alert=AlertDialogFragment.newInstance(getlocalizeString("RemoveMark","Remove Mark"),getlocalizeString("RemoveMarkMsg","Are you sure you want to remove this mark?"),getlocalizeString("Remove","Remove"),true);
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
                    alert=AlertDialogFragment.newInstance(getlocalizeString("RemoveMark","Remove Mark"),getlocalizeString("RemoveMarkError","This mark can not be removed"),getlocalizeString("Ok","Ok"),false);

                alert.show(getFragmentManager(), "confirmationAlert");

                break;

            case R.id.img_del_all:
                if(markerView.getMarksCount()<1)
                    return;
                AlertDialogFragment alertAll=AlertDialogFragment.newInstance(getlocalizeString("RemoveMark","Remove All Mark"),getlocalizeString("RemoveMarkMsg","Are you sure you want to remove all marks?"),getlocalizeString("Remove","Remove"),getlocalizeString("Cancel","Cancel"));
                alertAll.setButtonsClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == AlertDialog.BUTTON_POSITIVE) {

                            Vector<String> fixedIds=markerView.getAllFixedMarksIds();
                            Enumeration vEnum = fixedIds.elements();

                            while(vEnum.hasMoreElements())
                            {
                                String id =vEnum.nextElement().toString();
                                if(!fixedMarksIds.contains(id))
                                    fixedMarksIds.add(id);
                            }

                            symbolsHashMap.clear();
                            markerView.clearAlMarks();
                        }
                    }
                });
                alertAll.show(getFragmentManager(),"removeConfirmationAlert");
                break;

        }
    }

    private Snackbar getSnackbar(int markerSymbol)
    {
        String text="";
        if (markerSymbol == Constant.SYMBOL_MARKER_SMALL_DENT) {
            text = getlocalizeString("SmallDentMsg","Small Dent Selected");
        } else if (markerSymbol == Constant.SYMBOL_MARKER_LARGE_DENT) {
            text = getlocalizeString("LargeDentMsg","Large Dent Selected");
        }
        else if (markerSymbol == Constant.SYMBOL_MARKER_SCRATCH) {
            text = getlocalizeString("LargeScratch","Large Scratch Selected");
        }
        else if (markerSymbol == Constant.SYMBOL_MARKER_SCRATCH_THIN) {
            text = getlocalizeString("SmallScratch","Small Scratch Selected");
        }
        else if (markerSymbol == Constant.SYMBOL_MARKER_CRACK) {
            text = getlocalizeString("CrackSelected","Crack Selected");
        }
        Snackbar snackbar=Snackbar.make(markerView, text, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        return snackbar;
    }
    public void resetSelection()
    {
        imgDent.setSelected(false);
        imgLdent.setSelected(false);
        imgScratch.setSelected(false);
        imgScratchThin.setSelected(false);
        imgBrk.setSelected(false);
    }

    @Override
    public void onSymbolAdded(SymbolMarker symbolMarker) {
        if (symbolMarker.isEditable) {
            ((MCCApplication) getActivity().getApplication()).addSymbol(symbolMarker.markerType, symbolMarker.getId());
            symbolsHashMap.put(symbolMarker.getId(), symbolMarker);
        }
    }

    @Override
    public void onRemoved(SymbolMarker symbolMarker) {
        if (symbolMarker.isEditable) {
            ((MCCApplication) getActivity().getApplication()).removeSymbol(symbolMarker.getId());
            symbolsHashMap.remove(symbolMarker.getId());
        }



    }

    @Override
    public void onSymbolSelected(SymbolMarker symbolMarker) {
        resetSelection();
    }

    @Override
    public boolean onBackPressed() {

        if(symbolsHashMap.size()>0 || fixedMarksIds.size()>0){
            android.support.v7.app.AlertDialog.Builder builder =
                    new android.support.v7.app.AlertDialog.Builder(getActivity());
            // builder.setTitle("Warning!");
            builder.setMessage(getlocalizeString("YourMarksChangesWillBeDiscarded","Your marks's changes will be discarded. Are you sure you want to go back?"));
            builder.setPositiveButton(getlocalizeString("Yes","Yes"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    parentBackPress();
                }
            });
            builder.setNegativeButton(getlocalizeString("No","No"), null);

            builder.show();
            return true;
        }
        else
            return super.onBackPressed();

    }
}
