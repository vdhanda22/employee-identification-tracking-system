package com.karzansoft.fastvmi.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.karzansoft.fastvmi.Models.LanguageItem;
import com.karzansoft.fastvmi.Models.LanguagesResult;
import com.karzansoft.fastvmi.Models.LocalizeText;
import com.karzansoft.fastvmi.Network.Entities.Request.TranslationLanguageRequest;
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
 * Created by Yasir on 5/19/2016.
 */
public class SettingsFragment extends BaseFragment implements View.OnClickListener{

    CheckBox nId,passId;
    TextView language,selectedLanguage, scanningDocs,txtVersion,txtLegal;
    int selectedIndex;
    boolean isLanguageChanged;
    public static SettingsFragment newInstance()
    {
        SettingsFragment fragment=new SettingsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.setting_layout, container, false);
      //  nId=(CheckBox)rootView.findViewById(R.id.checkbox_nid);
     //   croId=(CheckBox)rootView.findViewById(R.id.checkbox_croid);
        language=(TextView)rootView.findViewById(R.id.language);
        selectedLanguage=(TextView)rootView.findViewById(R.id.selected_language);
        scanningDocs=(TextView)rootView.findViewById(R.id.scanning_docs);
        //passId=(CheckBox)rootView.findViewById(R.id.checkbox_passid);
        txtVersion=(TextView)rootView.findViewById(R.id.version);
        txtLegal=(TextView)rootView.findViewById(R.id.legal);

        return  rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateText();
        //view.findViewById(R.id.container_nid).setOnClickListener(this);
       // view.findViewById(R.id.container_croid).setOnClickListener(this);
        view.findViewById(R.id.layout_language).setOnClickListener(this);
       // view.findViewById(R.id.container_passid).setOnClickListener(this);
        txtLegal.setOnClickListener(this);
//        nId.setChecked(false);
  //      passId.setChecked(false);
      //  croId.setChecked(false);
    //    nId.setChecked(AppUtils.getDocType(getActivity(), Constant.PREF_KEY_DOC_NID, false));
      //  passId.setChecked(AppUtils.getDocType(getActivity(), Constant.PREF_KEY_DOC_PASS, true));


    }

    private void updateText()
    {
        getActionBar().setTitle(AppUtils.getLocalizeString(getActivity(),"Settings","Settings"));
        language.setText(AppUtils.getLocalizeString(getActivity(),"Language","Language"));
        scanningDocs.setText(AppUtils.getLocalizeString(getActivity(),"About","About"));
        txtLegal.setText(AppUtils.getLocalizeString(getActivity(),"LegalAndPrivacy","Legal & Privacy"));
        if(AppUtils.getCurrentLanguage(getActivity())!=null)
            selectedLanguage.setText(""+AppUtils.getCurrentLanguage(getActivity()).getDisplayName());
        try {
            String versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            txtVersion.setText(AppUtils.getLocalizeString(getActivity(),"Version","Version")+" "+versionName);
        }catch (PackageManager.NameNotFoundException ex)
        {
            txtVersion.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.legal:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.speedautosystems.com/speed-application-privacy-policy/"));
                startActivity(browserIntent);
                break;
           /* case R.id.container_nid:
                if (nId.isChecked())
                    nId.setChecked(false);
                else
                    nId.setChecked(true);
                AppUtils.saveDocType(getActivity(), Constant.PREF_KEY_DOC_NID, nId.isChecked());
                break;

            case R.id.container_passid:
                if(passId.isChecked())
                    passId.setChecked(false);
                else
                    passId.setChecked(true);
                AppUtils.saveDocType(getActivity(), Constant.PREF_KEY_DOC_PASS,passId.isChecked());
                break;*/
            case R.id.layout_language:
                LanguagesResult languagesResult=AppUtils.getLanguagesList(getActivity());
                if(languagesResult==null || languagesResult.getItems()==null || languagesResult.getItems().size()<1)
                    return;

                ArrayList<LanguageItem> languageItems=languagesResult.getItems();

                String[] items=new String[languageItems.size()];
                LanguageItem languageItem=AppUtils.getCurrentLanguage(getActivity());
                if(languageItem==null)
                    languageItem=new LanguageItem();

                for (int i=0;i<items.length;i++)
                {
                    items[i]=languageItems.get(i).getDisplayName();
                    if(languageItem.getDisplayName().equalsIgnoreCase(languageItems.get(i).getDisplayName()))
                        selectedIndex=i;
                }
                isLanguageChanged=false;
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setTitle(AppUtils.getLocalizeString(getActivity(),"LanguagesList","Languages List"));


                builder.setSingleChoiceItems(items, selectedIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which!=selectedIndex)
                        {
                            selectedIndex=which;
                            isLanguageChanged=true;
                        }
                    }
                });

                builder.setPositiveButton(AppUtils.getLocalizeString(getActivity(),"Ok","Ok"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(isLanguageChanged)
                            changesLanguage();

                    }
                });
                builder.setNegativeButton(AppUtils.getLocalizeString(getActivity(),"Cancel","Cancel"),null);
                builder.show();
            break;
        }
    }


    private void changesLanguage()
    {
        final LanguageItem languageItem=AppUtils.getLanguagesList(getActivity()).getItems().get(selectedIndex);
        if(languageItem==null)
            return;
        showProgress();
        Call<WebResponse<WebResponseList<LocalizeText>>> webResponseCall= WebServiceFactory.getInstance().getLanguageText(new TranslationLanguageRequest(languageItem.getName()),AppUtils.getAuthToken(getActivity()));
        webResponseCall.enqueue(new Callback<WebResponse<WebResponseList<LocalizeText>>>() {
            @Override
            public void onResponse(Call<WebResponse<WebResponseList<LocalizeText>>> call, Response<WebResponse<WebResponseList<LocalizeText>>> response) {

                hideProgress();
                if(response.body()!=null && response.body().isSuccess() && response.body().getResult()!=null)
                {
                    if(response.body().getResult().getItems()!=null)
                    {
                        AppUtils.saveLanguageStrings(getActivity(),AppUtils.convertToMap(response.body().getResult().getItems()));
                        AppUtils.setCurrentLanguage(getActivity(),languageItem);
                        loadLocalizeStrings();
                        Intent intent=getActivity().getIntent();
                        getActivity().finish();
                        getActivity().startActivity(intent);

                    }
                }


            }

            @Override
            public void onFailure(Call<WebResponse<WebResponseList<LocalizeText>>> call, Throwable t) {
                Toast.makeText(getActivity(),AppUtils.getLocalizeString(getActivity(),"ConnectionTimeOut","Connection time out!"), Toast.LENGTH_LONG).show();
                hideProgress();
            }
        });
    }
}
