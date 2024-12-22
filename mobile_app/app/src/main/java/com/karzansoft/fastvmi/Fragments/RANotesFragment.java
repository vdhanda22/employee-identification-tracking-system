package com.karzansoft.fastvmi.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karzansoft.fastvmi.Activities.CaptureSignatureView;
import com.karzansoft.fastvmi.DataBase.DatabaseManager;
import com.karzansoft.fastvmi.Dialogs.AlertDialogFragment;
import com.karzansoft.fastvmi.Models.AppSettings;
import com.karzansoft.fastvmi.Models.Document;
import com.karzansoft.fastvmi.Models.DocumentImage;
import com.karzansoft.fastvmi.Models.MarkDetail;
import com.karzansoft.fastvmi.Models.MarkImage;
import com.karzansoft.fastvmi.Models.MovementInfo;
import com.karzansoft.fastvmi.Models.OperationInfo;
import com.karzansoft.fastvmi.Network.Entities.Request.SaveMovementRequest;
import com.karzansoft.fastvmi.Network.Entities.Response.SaveMovementResponse;
import com.karzansoft.fastvmi.Network.WebResponse;
import com.karzansoft.fastvmi.Network.WebServiceFactory;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Services.ImageSyncService;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Utils.Util;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yasir on 4/6/2016.
 */
public class RANotesFragment extends BaseFragment implements View.OnClickListener {

    CaptureSignatureView capt,capt2;
    TextView customerSign,staffSign;
    Button finishButton,clearButton,clearButton2;
    ScrollView mScroller;
    MovementInfo currentMovement;
    EditText notes;
    OperationInfo info;
    Handler mHandler;
    ArrayList<MarkImage> imagesToSync;
    String requestInfo="";
    TextView termsAndConditions;
    CheckBox acceptTerms;
    LinearLayout termsContainer;
    TextInputLayout notesContainer;

    public static RANotesFragment newInstance(int current_mode,MovementInfo movementInfo) {
        RANotesFragment f = new RANotesFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.CURRENT_MODE, current_mode);
        if(movementInfo!=null){
            Gson gson = new GsonBuilder().create();
            args.putString("CurrentMovementInfo",gson.toJson(movementInfo,MovementInfo.class));
        }
        f.setArguments(args);
        return f;
    }

      @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         View rootView = inflater.inflate(R.layout.activity_notes_ra, container, false);
          rootView.findViewById(R.id.appbar).setVisibility(View.GONE);
          current_mode = this.getArguments().getInt(Constant.CURRENT_MODE, 1);
          capt = (CaptureSignatureView) rootView.findViewById(R.id.capture);
          capt2 = (CaptureSignatureView) rootView.findViewById(R.id.capture2);
          finishButton = (Button) rootView.findViewById(R.id.ns_action_finish);
          clearButton = (Button) rootView.findViewById(R.id.ns_action_clearpaint);
          clearButton2 = (Button) rootView.findViewById(R.id.ns_action_clearpaint2);
          customerSign=(TextView)rootView.findViewById(R.id.cust_sign);
          staffSign=(TextView)rootView.findViewById(R.id.staff_sign);
          mScroller=(ScrollView)rootView.findViewById(R.id.scoller);
          notes=(EditText)rootView.findViewById(R.id.notes);
          notesContainer = (TextInputLayout) rootView.findViewById(R.id.notes_parent);
          termsContainer=(LinearLayout)rootView.findViewById(R.id.termsContainer);
          termsAndConditions=(TextView)rootView.findViewById(R.id.text_terms);
          acceptTerms=(CheckBox)rootView.findViewById(R.id.checkboxTerm);
          String jsonm=this.getArguments().getString("CurrentMovementInfo","");
          requestInfo=jsonm;
          Gson gson = new GsonBuilder().create();
          Util.LogE("Json", "" + jsonm);
          if (jsonm.length() > 0)
              currentMovement = gson.fromJson(jsonm, MovementInfo.class);

        return  rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle();

        AppUtils.setText(finishButton,getlocalizeString("Finish","Finish"));
        AppUtils.setText(clearButton,getlocalizeString("Clear","Clear"));
        AppUtils.setText(clearButton2,getlocalizeString("Clear","Clear"));
        AppUtils.setText(customerSign,getlocalizeString("CustomerSign","Customer Signatures"));
        AppUtils.setText(staffSign,getlocalizeString("UserSignatures","User Signatures"));
        AppUtils.setTextHint(notesContainer,getlocalizeString("Notes","Notes"));

        if(current_mode==Constant.REPLACEMENT_CHECKOUT)
        {
            getActionBar().setTitle(getlocalizeString("Replacement","Replacement"));
        }
        finishButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        clearButton2.setOnClickListener(this);
        termsAndConditions.setOnClickListener(this);
        mScroller.requestDisallowInterceptTouchEvent(true);
        mHandler=new Handler(Looper.getMainLooper());
        imagesToSync=new ArrayList<>();
        mLastClickTime = SystemClock.elapsedRealtime();

        AppSettings appSettings=AppUtils.getSettings(getActivity());
        if(appSettings.getTermsAndConditions()!=null && appSettings.getTermsAndConditions().length()>1)
            termsContainer.setVisibility(View.VISIBLE);
        else
            termsContainer.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.ns_action_finish:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500)
                    return ;
                mLastClickTime = SystemClock.elapsedRealtime();

                if(termsContainer.getVisibility()==View.VISIBLE && !acceptTerms.isChecked())
                {
                    AppUtils.showMessage("Please Accept Terms & Conditions first",finishButton, Snackbar.LENGTH_SHORT);
                    return;
                }

                if (current_mode == Constant.RA_CHECKIN || current_mode == Constant.REPLACEMENT_CHECKIN || current_mode == Constant.NRM_CHECKIN)
                    info = currentMovement.getInDetail();
                else
                    info = currentMovement.getOutDetail();
                showProgress();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        info.setNotes("" + notes.getText().toString());
                        info.setCustomerSignature(capt.getBase64String());
                        info.setStaffSignature(capt2.getBase64String());
                        mHandler.post(new Runnable() {
                           @Override
                           public void run() {
                                         saveMovement();
                           }
                       });

                    }
                }).start();




                break;
            case R.id.ns_action_clearpaint:
                capt.ClearCanvas();
                break;
            case R.id.ns_action_clearpaint2:
                capt2.ClearCanvas();
                break;
            case R.id.text_terms:
                String terms=getString(R.string.terms_conditions);
                AlertDialogFragment alertTerm=AlertDialogFragment.newInstance("Terms & Conditions",terms,"Ok", false);
                alertTerm.show(getFragmentManager(), "termAlert");
                break;
        }
    }

    private void saveMovement()
    {

        SaveMovementRequest request=new SaveMovementRequest();
        ArrayList<MovementInfo> movementInfos=new ArrayList<>();
        if(current_mode == Constant.REPLACEMENT_CHECKOUT && AppUtils.getMovementInfo(getActivity())!=null)
        {
            MovementInfo repMovement=AppUtils.getMovementInfo(getActivity());
            repMovement.setLocalDriver(null);
            movementInfos.add(repMovement);
            currentMovement.setContact(repMovement.getContact());
            currentMovement.setRefNo(repMovement.getRefNo());

            ArrayList<MarkDetail> markDetails=repMovement.getInMarks();
            if (markDetails != null && markDetails.size() > 0) {
                for (int i = 0; i < markDetails.size(); i++) {
                    MarkDetail det = markDetails.get(i);
                    if (det.getImages() != null && det.getImages().size() > 0)
                        imagesToSync.addAll(det.getImages());
                }
            }
        }

        ArrayList<MarkDetail> markDetails;
        if(current_mode==Constant.RA_CHECKIN )
            markDetails=currentMovement.getInMarks();
        else
            markDetails=currentMovement.getOutMarks();

        if (markDetails != null && markDetails.size() > 0) {
            for (int i = 0; i < markDetails.size(); i++) {
                MarkDetail det = markDetails.get(i);
                if (det.getImages() != null && det.getImages().size() > 0)
                    imagesToSync.addAll(det.getImages());
            }
        }

        if(current_mode==Constant.RA_CHECKOUT || current_mode==Constant.REPLACEMENT_CHECKOUT)
        {
            ArrayList<Document> documentsImages=AppUtils.getDocumentsImages(getActivity());
            ArrayList<DocumentImage> docServerImgs=new ArrayList<>();
            if(documentsImages.size()>0)
            {
                for (int i=0;i<documentsImages.size();i++)
                {
                    Document doc=documentsImages.get(i);

                    for (int j=0;j<doc.getImages().size();j++)
                    {
                        String UID= UUID.randomUUID().toString().replace('-','_');
                        MarkImage img=new MarkImage();
                        img.setImagePath(doc.getImages().get(j).replace("file://", ""));// localpath
                        img.setGuid(UID);
                        imagesToSync.add(img);
                        DocumentImage dimg=new DocumentImage();
                        dimg.setDocumentType(i+2);
                        dimg.setGuid(UID);
                        dimg.setName(UID+".jpg");
                        docServerImgs.add(dimg);
                    }

                }
            }

            if(docServerImgs.size()>0)
                currentMovement.setDocuments(docServerImgs);

        }

        movementInfos.add(currentMovement);
        request.setMovements(movementInfos);
        Call<WebResponse<SaveMovementResponse>> responseBodyCall= WebServiceFactory.getInstance().saveMovement(request, AppUtils.getAuthToken(getActivity()));

        showProgress();
       responseBodyCall.enqueue(new Callback<WebResponse<SaveMovementResponse>>() {
           @Override
           public void onResponse(Call<WebResponse<SaveMovementResponse>> call, Response<WebResponse<SaveMovementResponse>> response) {
               hideProgress();
               if (response.isSuccessful() && response.body() != null) {
                   if (response.body().isSuccess()) {
//                       popAllFragment();
                      showProgress();
                       syncImages();

                     /* if(response.body().getResult().getMovementIds()!=null)
                      {
                          ArrayList<Long> movIds=response.body().getResult().getMovementIds();
                          for (int i=0;i<movIds.size();i++)
                          {
                           emailCheckCard(movIds.get(i).longValue());
                          }
                      }*/

                   } else {
                       AppUtils.showAlertWithEmailOption("Movement Saving Error",response.body().getError().getMessage(),requestInfo,RANotesFragment.this);

                       showAlert("" + response.body().getError().getMessage());
                       //AppUtils.showMessage("" + response.body().getError().getMessage(), finishButton, Snackbar.LENGTH_LONG);
                   }
               }else {
                   if (response.code() == 401)
                       reLogin();
                   else
                       AppUtils.showMessage(getlocalizeString("InternalServerError","Internal server error!"), finishButton, Snackbar.LENGTH_LONG);
               }

           }

           @Override
           public void onFailure(Call<WebResponse<SaveMovementResponse>> call, Throwable t) {
               hideProgress();
               AppUtils.showMessage(getlocalizeString("ConnectionTimeOut","Connection time out!"), finishButton, Snackbar.LENGTH_SHORT);
           }
       });
    }


    private void syncImages()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseManager.getInstance(getActivity().getApplicationContext()).addMarkImage(imagesToSync);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent serviceIntent=new Intent(getActivity(), ImageSyncService.class);
                        ContextCompat.startForegroundService(getActivity(),serviceIntent);
                        hideProgress();
                        showOperationStatusMsg(true);
                        popAllFragment();
                    }
                });

            }
        }).start();
    }
    private void showAlert(String msg)
    {
        AlertDialogFragment alert=AlertDialogFragment.newInstance("Movement Saving Error", msg, "Ok", false);
        alert.show(getFragmentManager(), "detailAlert");
    }
}
