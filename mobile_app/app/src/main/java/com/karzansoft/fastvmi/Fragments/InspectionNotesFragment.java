package com.karzansoft.fastvmi.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karzansoft.fastvmi.Activities.CaptureSignatureView;
import com.karzansoft.fastvmi.DataBase.DatabaseManager;
import com.karzansoft.fastvmi.Dialogs.AlertDialogFragment;
import com.karzansoft.fastvmi.Models.AccessToken;
import com.karzansoft.fastvmi.Models.DocumentImage;
import com.karzansoft.fastvmi.Models.InspectionDto;
import com.karzansoft.fastvmi.Models.MarkImage;
import com.karzansoft.fastvmi.Network.APIError;
import com.karzansoft.fastvmi.Network.Entities.Request.InspectionRequest;
import com.karzansoft.fastvmi.Network.ErrorUtils;
import com.karzansoft.fastvmi.Network.WebResponse;
import com.karzansoft.fastvmi.Network.WebServiceFactory;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Services.ImageSyncService;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Utils.Util;
import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.StorageCredentialsSharedAccessSignature;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Yasir on 4/6/2016.
 */
public class InspectionNotesFragment extends BaseFragment implements View.OnClickListener {

    CaptureSignatureView capt;
    Button finishButton, clearButton;
    TextView staffSign;
    EditText notes;
    HashMap<String, String> languageTexts;
    Handler mHandler;
    ArrayList<MarkImage> imagesToSync;
    TextInputLayout notesContainer;
    InspectionDto inspectionNotes;

    public static InspectionNotesFragment newInstance(InspectionDto inspectionDto) {
        InspectionNotesFragment f = new InspectionNotesFragment();
        Bundle args = new Bundle();
        if (inspectionDto != null) {
            Gson gson = new Gson();
            args.putString("inspectionDto", gson.toJson(inspectionDto));
        }
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_notes, container, false);
        rootView.findViewById(R.id.appbar).setVisibility(View.GONE);
        current_mode = this.getArguments().getInt(Constant.CURRENT_MODE, 1);
        capt = (CaptureSignatureView) rootView.findViewById(R.id.capture);
        finishButton = (Button) rootView.findViewById(R.id.ns_action_finish);
        clearButton = (Button) rootView.findViewById(R.id.ns_action_clearpaint);
        notes = (EditText) rootView.findViewById(R.id.notes);
        notesContainer = (TextInputLayout) rootView.findViewById(R.id.notes_parent);
        staffSign = (TextView) rootView.findViewById(R.id.staff_sign);
        Gson gson = new GsonBuilder().create();
        inspectionNotes = gson.fromJson(getArguments().getString("inspectionDto"), InspectionDto.class);
        loadLanguageText();
        getActivity().setTitle(AppUtils.getLocalizeString(getActivity(), "Vehicle Inspection", "Vehicle Inspection"));

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle();
        AppUtils.setText(finishButton, getlocalizeString("Finish", "Finish"));
        AppUtils.setText(clearButton, getlocalizeString("Clear", "Clear"));
        AppUtils.setText(staffSign, getlocalizeString("StaffSign", "Your Signatures"));
        AppUtils.setTextHint(notesContainer, getlocalizeString("Notes", "Notes"));
        finishButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        mHandler = new Handler(Looper.getMainLooper());
        imagesToSync = new ArrayList<>();
        mLastClickTime = SystemClock.elapsedRealtime();
        if (current_mode == Constant.GARAGE_CHECKOUT || current_mode == Constant.GARAGE_CHECKIN) {
            notesContainer.setVisibility(View.GONE);
        }
        getActivity().setTitle(AppUtils.getLocalizeString(getActivity(), "Vehicle Inspection", "Vehicle Inspection"));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ns_action_finish:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                    return;
                if (ValidateSignature(capt))
                    saveSignautre();
                break;
            case R.id.ns_action_clearpaint:
                capt.ClearCanvas();
                break;
        }
    }

    private boolean ValidateSignature(CaptureSignatureView signatureView) {
        if (signatureView.isEpmty()) {
            Toast.makeText(getActivity(), "Please Enter Signature.", Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }


    private void saveSignautre() {

        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = capt.saveBitmapOnDisk();
                MarkImage markImage = new MarkImage();
                markImage.setSyncStatus(0);
                markImage.setImagePath(path.replace("file://", ""));// localpath
                String UID = UUID.randomUUID().toString().replace('-', '_');
                markImage.setGuid(UID);
                DocumentImage inspectionsignature = new DocumentImage();
                inspectionsignature.setGuid(UID);
                inspectionsignature.setName(UID + ".jpg");
                inspectionNotes.setSignature(inspectionsignature);
                inspectionNotes.setNotes(notes.getText().toString());
                final ArrayList<MarkImage> markImages = new ArrayList<MarkImage>(1);
                markImages.add(markImage);
                Util.LogE("Image Path", "" + path);
                AccessToken token = authenticateStorageAccess(AppUtils.getAuthToken(getActivity().getApplicationContext()));
                if (token != null && token.getToken().length() > 0) {
                    if (uploadImage(markImage, token)) {
                        Util.LogE("Signature", "Saved Successfully");
                    }
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        saveInspection(markImages);
                    }
                });

            }
        }).start();
    }


    public void saveInspection(final ArrayList<MarkImage> signatureImages) {
        showProgress();
        InspectionRequest request = new InspectionRequest();
        request.setInspection(inspectionNotes);
        Call<WebResponse<ResponseBody>> call = WebServiceFactory.getInstance().SaveInspection(request, AppUtils.getAuthToken(getActivity()));
        call.enqueue(new Callback<WebResponse<ResponseBody>>() {
            @Override
            public void onResponse(Call<WebResponse<ResponseBody>> call, Response<WebResponse<ResponseBody>> response) {

                if (getActivity() == null)
                    return;
                hideProgress();
                if (response.code() == 401) // if session is expired
                {
                    reLogin();
                    return;
                }

                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isSuccess()) {

                        try {

                            if (signatureImages != null) {
                                DatabaseManager.getInstance(getActivity().getApplicationContext()).addMarkImage(signatureImages);
                                Intent serviceIntent = new Intent(getActivity(), ImageSyncService.class);
                                ContextCompat.startForegroundService(getActivity(), serviceIntent);
                            }

                            showAlertAndFinish("", getLocalizeString("InspectionCreatedSuccessMsg", "Inspection Created successfully"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            showAlert(getLocalizeString("SavingException", "Saving Exception") + e.getMessage());
                        }
                    } else {
                        showAlert(getLocalizeString("SavingError", "Saving Error") + response.body().getError().getMessage());
                        //Utils.getSnackbar(parent,response.body().getError().getMessage(),"",null, Snackbar.LENGTH_LONG).show();
                    }
                } else {

                    APIError error = ErrorUtils.parseError(response);
                    String msg = null;
                    if (error != null && error.getMessage() != null)
                        msg = error.getMessage();
                    else if (response.raw() != null && response.raw().message() != null)
                        msg = response.raw().message();
                    else
                        msg = AppUtils.getLocalizeString(getActivity(), "TripStopingErrorMsg", "Error in stopping Trip");

                    showAlert("SavingError" + msg);
                    // Utils.getSnackbar(parent,msg,"",null, Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<WebResponse<ResponseBody>> call, Throwable t) {
                if (getActivity() == null)
                    return;
                hideProgress();
                showAlert("Connection time out");

            }
        });
    }


    private AccessToken authenticateStorageAccess(String authKey) {
        AccessToken token = null;
        try {
            Call<WebResponse<AccessToken>> request = WebServiceFactory.getInstance().getStorageAccessToken(authKey);
            token = request.execute().body().getResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return token;
    }

    private boolean uploadImage(MarkImage img, AccessToken token) {
        if (token == null || token.getBaseUrl() == null || getActivity() == null) {
            return false;
        }

        URI blobUri = URI.create(token.getBaseUrl() + "/" + img.getGuid() + ".jpg");
        StorageCredentials storageCredentials = new StorageCredentialsSharedAccessSignature(token.getToken());

        try {
            CloudBlockBlob blob = new CloudBlockBlob(blobUri, storageCredentials);
            if (img.getImagePath() != null && img.getImagePath().length() > 1)
                blob.uploadFromFile(img.getImagePath());

            Util.LogE("Success", "" + blobUri.getPath() + "" + token.getToken());
            img.setSyncStatus(1);
            /*DatabaseManager.getInstance(getActivity().getApplicationContext()).updateImageStatusByID(img.getId(),1);
            File file=new File(img.getImagePath());
            if(file!=null && file.exists())
                file.delete();*/
        } catch (StorageException ex) {
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public void showAlertAndFinish(String title, String msg) {
        try {
            AlertDialogFragment alert = AlertDialogFragment.newInstance(title, msg, "Ok", false);
            alert.setButtonsClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        popAllFragment();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            alert.show(getFragmentManager(), "alert");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void syncImages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseManager.getInstance(getActivity().getApplicationContext()).addMarkImage(imagesToSync);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent serviceIntent = new Intent(getActivity(), ImageSyncService.class);
                        ContextCompat.startForegroundService(getActivity(), serviceIntent);
                        hideProgress();
                        showOperationStatusMsg(true);
                        popAllFragment();
                    }
                });

            }
        }).start();
    }

    public void loadLanguageText() {
        languageTexts = AppUtils.getLanguageStrings(getActivity());
    }

    public String getLocalizeString(String key, String defValue) {
        if (languageTexts != null && languageTexts.get(key) != null) {
            return languageTexts.get(key);

        }
        return defValue;
    }

    private void showAlert(String msg) {
        AlertDialogFragment alert = AlertDialogFragment.newInstance("Inspection Saving Error", msg, "Ok", false);
        alert.show(getFragmentManager(), "detailAlert");
    }
}
