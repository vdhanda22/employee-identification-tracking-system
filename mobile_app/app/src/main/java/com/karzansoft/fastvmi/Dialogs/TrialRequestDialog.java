////package com.karzansoft.mcc.Dialogs;
////
////import android.annotation.SuppressLint;
////import android.app.Dialog;
////import android.content.Context;
////import android.content.DialogInterface;
////import android.os.Bundle;
////import android.support.annotation.Nullable;
////import android.support.design.widget.Snackbar;
////import android.support.design.widget.TextInputLayout;
////import android.support.v7.app.AlertDialog;
////import android.support.v7.app.AppCompatDialogFragment;
////import android.text.Html;
////import android.text.Spanned;
////import android.text.method.LinkMovementMethod;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.WindowManager;
////import android.view.inputmethod.InputMethodManager;
////import android.widget.Button;
////import android.widget.CheckBox;
////import android.widget.TextView;
////
////import com.google.gson.Gson;
////import com.karzansoft.mcc.Models.Country;
////import com.karzansoft.mcc.Models.CountryResult;
////import com.karzansoft.mcc.Network.Entities.Request.RegistrationRequest;
////import com.karzansoft.mcc.R;
////import com.karzansoft.mcc.Utils.AppUtils;
////import com.karzansoft.mcc.Utils.Util;
////
////
////import java.util.HashMap;
////import java.util.List;
////
/////**
//// * Created by Yasir on 7/21/2016.
//// */
////public class TrialRequestDialog extends AppCompatDialogFragment implements View.OnClickListener {
////    View.OnClickListener clickListener;
////    TextInputLayout nameContainer, companyContainer, emailContainer, phoneContainer, fleetSizeContainer, messageContainer,
////            countryContainer, codeCountainer, passContainer, rePassContainer;
////    Button btn_cancel, btn_save;
////    TextView txtView_privacyAgreementText;
////    CheckBox checkBox_privacyAgreement;
////    HashMap<String, String> languageTexts;
////    private String emailBody = "";
////    CountryResult countryResult;
////    Country selectedCountry;
////
////    public static TrialRequestDialog newInstance() {
////        TrialRequestDialog frag = new TrialRequestDialog();
////        return frag;
////    }
////
////
////    public String getLocalizeString(String key, String defValue) {
////        if (languageTexts != null && languageTexts.get(key) != null) {
////            return languageTexts.get(key);
////
////        }
////        return defValue;
////    }
////
////    @Override
////    public void onCreate(@Nullable Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        Gson gson = new Gson();
////        countryResult = gson.fromJson(Util.loadJSONFromAsset(getActivity()), CountryResult.class);
////        Util.LogE("Country loded", "++++++++++");
////    }
////
////    @Override
////    public Dialog onCreateDialog(Bundle savedInstanceState) {
////        LayoutInflater inflater = getActivity().getLayoutInflater();
////        //languageTexts= AppUtils.getLanguageStrings(getActivity());
////        View dialogView = inflater.inflate(R.layout.request_trial, null);
////
////        AlertDialog.Builder dialog;
////
////        dialog = new AlertDialog.Builder(getActivity())
////
////                .setTitle(getLocalizeString("Signup", "Sign up"))
////                .setView(dialogView);
////
////        nameContainer = (TextInputLayout) dialogView.findViewById(R.id.nameContainer);
////        companyContainer = (TextInputLayout) dialogView.findViewById(R.id.companyNameContainer);
////        emailContainer = (TextInputLayout) dialogView.findViewById(R.id.emailContainer);
////        phoneContainer = (TextInputLayout) dialogView.findViewById(R.id.phoneContainer);
////        fleetSizeContainer = (TextInputLayout) dialogView.findViewById(R.id.fleetSizeContainer);
////        messageContainer = (TextInputLayout) dialogView.findViewById(R.id.messageContainer);
////        countryContainer = (TextInputLayout) dialogView.findViewById(R.id.countryContainer);
////        codeCountainer = (TextInputLayout) dialogView.findViewById(R.id.codeContainer);
////        passContainer = (TextInputLayout) dialogView.findViewById(R.id.passContainer);
////        rePassContainer = (TextInputLayout) dialogView.findViewById(R.id.rePassContainer);
////        countryContainer.getEditText().setOnClickListener(this);
////        btn_save = (Button) dialogView.findViewById(R.id.send);
////
////        checkBox_privacyAgreement = dialogView.findViewById(R.id.privacyAgreementCheckBox);
////        checkBox_privacyAgreement.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                if (checkBox_privacyAgreement.isChecked()) {
////                    txtView_privacyAgreementText.setTextColor(getResources().getColor(R.color.secondaryTextColor));
////                } else {
////                    txtView_privacyAgreementText.setTextColor(getResources().getColor(R.color.colorPrimary));
////                }
////            }
////        });
////
////        txtView_privacyAgreementText = dialogView.findViewById(R.id.privacyAgreementText);
////        String privacyString = getString(R.string.privacy_accept_statement);
////        Spanned privacy = Html.fromHtml(privacyString);
////        txtView_privacyAgreementText.setText(privacy);
////        txtView_privacyAgreementText.setMovementMethod(LinkMovementMethod.getInstance());
////
////        btn_save.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if (!validateField(nameContainer) || !validateField(companyContainer) || !validateField(emailContainer) || !validateField(countryContainer)
////                        || !validateField(phoneContainer) || !validateField(fleetSizeContainer) || !validateField(passContainer) ||
////                        !validateField(rePassContainer) || !validatePrivacyCheckBox(checkBox_privacyAgreement))
////                    return;
////                if (!validateEmail(emailContainer))
////                    return;
////                if (!passContainer.getEditText().getText().toString().equals(rePassContainer.getEditText().getText().toString())) {
////                    AppUtils.showMessage("New password mismatch ", btn_save, Snackbar.LENGTH_LONG);
////                    return;
////                }
////
////                clearFocus(fleetSizeContainer.getEditText());
////
////                if (clickListener != null)
////                    clickListener.onClick(v);
////            }
////        });
////
////        return dialog.create();
////    }
////
////    public RegistrationRequest getRestrationObject() {
////        String name = nameContainer.getEditText().getText().toString();
////        String company = companyContainer.getEditText().getText().toString();
////        String email = emailContainer.getEditText().getText().toString();
////        String phone = codeCountainer.getEditText().getText().toString() + phoneContainer.getEditText().getText().toString();
////        String fleet = fleetSizeContainer.getEditText().getText().toString();
////        String country = countryContainer.getEditText().getText().toString();
////        String pass = passContainer.getEditText().getText().toString();
////        RegistrationRequest registrationRequest = new RegistrationRequest(company.replace(" ", ""), company, name,
////                email, Integer.parseInt(fleet), phone, country, pass);
////        return registrationRequest;
////    }
////
////    @Override
////    public void onClick(View v) {
////        switch (v.getId()) {
////            case R.id.country:
////                if (countryResult == null || countryResult.getCountryList() == null)
////                    return;
////                final List<Country> countries = countryResult.getCountryList();
////                String[] items = new String[countries.size()];
////
////
////                for (int i = 0; i < items.length; i++) {
////                    items[i] = countries.get(i).getCountry_name();
////                }
////
////                AlertDialog.Builder builder =
////                        new AlertDialog.Builder(getActivity());
////                builder.setTitle("Select Country");
////                builder.setItems(items, new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        selectedCountry = countries.get(which);
////                        countryContainer.getEditText().setText("" + selectedCountry.getCountry_name());
////                        codeCountainer.getEditText().setText("+" + selectedCountry.getCountry_code());
////                    }
////                });
////                builder.show();
////                break;
////        }
////    }
////
////    public void setButtonsClickListener(View.OnClickListener clickListener) {
////        this.clickListener = clickListener;
////    }
////
////
////    private boolean validateField(TextInputLayout nameContainer) {
////        nameContainer.getEditText().requestFocus();
////        if (nameContainer.getEditText().getText().toString().trim().isEmpty()) {
////            String msg = nameContainer.getHint().toString();
////            nameContainer.setError("Please enter " + msg.substring(0, msg.length() - 1));
////            requestFocus(nameContainer.getEditText());
////            return false;
////        } else {
////            nameContainer.setErrorEnabled(false);
////        }
////        return true;
////    }
////
////    private boolean validateEmail(TextInputLayout nameContainer) {
////        nameContainer.getEditText().requestFocus();
////        if (!Util.isValidEmail(nameContainer.getEditText().getText().toString())) {
////            nameContainer.setError("Please enter a valid Email Address");
////            requestFocus(nameContainer.getEditText());
////            return false;
////        } else {
////            nameContainer.setErrorEnabled(false);
////        }
////        return true;
////    }
////
////    private boolean validatePrivacyCheckBox(CheckBox checkBox) {
////        if (checkBox.isChecked() == false) {
////            txtView_privacyAgreementText.setTextColor(getResources().getColor(R.color.colorPrimary));
////        }
////        return checkBox.isChecked();
////    }
////
////    private void requestFocus(View view) {
////        if (view.requestFocus()) {
////            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
////        }
////    }
////
////    public String getEmailText() {
////        emailBody = "Name : " + nameContainer.getEditText().getText() + "\n";
////        emailBody += "Company Name : " + companyContainer.getEditText().getText() + "\n";
////        emailBody += "Email Address : " + emailContainer.getEditText().getText() + "\n";
////        emailBody += "Phone Number : " + phoneContainer.getEditText().getText() + "\n";
////        emailBody += "Fleet Size : " + fleetSizeContainer.getEditText().getText() + "\n";
////        if (messageContainer.getEditText().getText().toString().length() > 0)
////            emailBody += "Message : " + messageContainer.getEditText().getText() + "\n";
////
////        return emailBody;
////    }
////
////
////    private void sendEmail() {
////        try {
////            emailBody = "Name : " + nameContainer.getEditText().getText() + "\n";
////            emailBody += "Company Name : " + companyContainer.getEditText().getText() + "\n";
////            emailBody += "Email Address : " + emailContainer.getEditText().getText() + "\n";
////            emailBody += "Phone Number : " + phoneContainer.getEditText().getText() + "\n";
////            emailBody += "Fleet Size : " + fleetSizeContainer.getEditText().getText() + "\n";
////            if (messageContainer.getEditText().getText().toString().length() > 0)
////                emailBody += "Message : " + messageContainer.getEditText().getText() + "\n";
////
////            /*String uriText =
////                    "mailto:info@speedautosystems.com" +
////                            "?subject=" + Uri.encode("Free Trial Request") +
////                            "&body=" + Uri.encode(body);
////
////            Uri uri = Uri.parse(uriText);
////            Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
////            sendIntent.setData(uri);
////            ActivityInfo activityInfo = sendIntent.resolveActivityInfo(getActivity().getPackageManager(), sendIntent.getFlags());
////            if (activityInfo.exported)
////                getActivity().startActivity(Intent.createChooser(sendIntent, "Send email"));
////*/
////
////
////        } catch (Exception ex) {
////            ex.printStackTrace();
////        }
////
////    }
////
////    private void clearFocus(View view) {
////        if (view != null) {
////            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
////            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
////        }
////    }
////}
//package com.karzansoft.mcc.Dialogs;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.design.widget.Snackbar;
//import android.support.design.widget.TextInputLayout;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatDialogFragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//
//import com.google.gson.Gson;
//import com.karzansoft.mcc.Models.Country;
//import com.karzansoft.mcc.Models.CountryResult;
////import com.karzansoft.mcc.Network.Entities.Request.RegistrationRequest;
//import com.karzansoft.mcc.R;
//import com.karzansoft.mcc.Utils.AppUtils;
//import com.karzansoft.mcc.Utils.Util;
//
//
//import java.util.HashMap;
//import java.util.List;
//
///**
// * Created by Yasir on 7/21/2016.
// */
//public class TrialRequestDialog extends AppCompatDialogFragment implements View.OnClickListener{
//    View.OnClickListener clickListener;
//    TextInputLayout nameContainer,companyContainer,emailContainer,phoneContainer,fleetSizeContainer,messageContainer,countryContainer,codeCountainer,passContainer,rePassContainer;
//    Button btn_cancel,btn_save;
//    HashMap<String,String> languageTexts;
//    private String emailBody="";
//    CountryResult countryResult;
//    Country selectedCountry;
//    public static TrialRequestDialog newInstance() {
//        TrialRequestDialog frag = new TrialRequestDialog();
//        return frag;
//    }
//
//
//    public String getLocalizeString(String key,String defValue)
//    {
//        if(languageTexts!=null && languageTexts.get(key)!=null)
//        {
//            return languageTexts.get(key);
//
//        }
//        return defValue;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Gson gson = new Gson();
//        countryResult = gson.fromJson(Util.loadJSONFromAsset(getActivity()), CountryResult.class);
//        Util.LogE("Country loded","++++++++++");
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        //languageTexts= AppUtils.getLanguageStrings(getActivity());
//        View dialogView= inflater.inflate(R.layout.request_trial, null);
//
//        AlertDialog.Builder dialog;
//
//
//        dialog= new AlertDialog.Builder(getActivity())
//
//                .setTitle(getLocalizeString("Signup","Sign up"))
//                .setView(dialogView)
//        ;
//        nameContainer=(TextInputLayout) dialogView.findViewById(R.id.nameContainer);
//        companyContainer=(TextInputLayout) dialogView.findViewById(R.id.companyNameContainer);
//        emailContainer=(TextInputLayout) dialogView.findViewById(R.id.emailContainer);
//        phoneContainer=(TextInputLayout) dialogView.findViewById(R.id.phoneContainer);
//        fleetSizeContainer=(TextInputLayout) dialogView.findViewById(R.id.fleetSizeContainer);
//        messageContainer=(TextInputLayout) dialogView.findViewById(R.id.messageContainer);
//        countryContainer=(TextInputLayout)dialogView.findViewById(R.id.countryContainer);
//        codeCountainer=(TextInputLayout)dialogView.findViewById(R.id.codeContainer);
//        passContainer=(TextInputLayout)dialogView.findViewById(R.id.passContainer);
//        rePassContainer=(TextInputLayout)dialogView.findViewById(R.id.rePassContainer);
//        countryContainer.getEditText().setOnClickListener(this);
//        btn_save=(Button)dialogView.findViewById(R.id.send);
//
//        btn_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!validateField(nameContainer)|| !validateField(companyContainer) ||!validateField(emailContainer)||!validateField(countryContainer) ||!validateField(phoneContainer)||!validateField(fleetSizeContainer)||!validateField(passContainer)||!validateField(rePassContainer))
//                    return;
//                if( !validateEmail(emailContainer))
//                    return ;
//                if(!passContainer.getEditText().getText().toString().equals(rePassContainer.getEditText().getText().toString()))
//                {
//                    AppUtils.showMessage("New password mismatch ",btn_save, Snackbar.LENGTH_LONG);
//                    return;
//                }
//
//                clearFocus(fleetSizeContainer.getEditText());
//
//                if(clickListener!=null)
//                    clickListener.onClick(v);
//
//
//            }
//        });
//
//        return dialog.create();
//    }
//
//    public RegistrationRequest getRestrationObject()
//    {
//        String name=nameContainer.getEditText().getText().toString();
//        String company=companyContainer.getEditText().getText().toString();
//        String email=emailContainer.getEditText().getText().toString();
//        String phone=codeCountainer.getEditText().getText().toString()+"-"+phoneContainer.getEditText().getText().toString();
//        String fleet=fleetSizeContainer.getEditText().getText().toString();
//        String country=countryContainer.getEditText().getText().toString();
//        String pass=passContainer.getEditText().getText().toString();
//        RegistrationRequest registrationRequest=new RegistrationRequest(company.replace(" ",""),company,name,email,Integer.parseInt(fleet),phone,country,pass);
//        return registrationRequest;
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId())
//        {
//            case R.id.country:
//                if(countryResult==null || countryResult.getCountryList()==null)
//                    return;
//                final List<Country> countries=countryResult.getCountryList();
//                String[] items=new String[countries.size()];
//
//
//                for (int i=0;i<items.length;i++)
//                {
//                    items[i]=countries.get(i).getCountry_name();
//                }
//
//                AlertDialog.Builder builder =
//                        new AlertDialog.Builder(getActivity());
//                builder.setTitle("Select Country");
//                builder.setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        selectedCountry=countries.get(which);
//                        countryContainer.getEditText().setText(""+selectedCountry.getCountry_name());
//                        codeCountainer.getEditText().setText("+"+selectedCountry.getCountry_code());
//                    }
//                });
//                builder.show();
//                break;
//        }
//    }
//
//    public void setButtonsClickListener(View.OnClickListener clickListener)
//    {
//        this.clickListener=clickListener;
//    }
//
//    private boolean validateField(TextInputLayout nameContainer) {
//        nameContainer.getEditText().requestFocus();
//        if (nameContainer.getEditText().getText().toString().trim().isEmpty()) {
//            String msg=nameContainer.getHint().toString();
//            nameContainer.setError("Please enter "+msg.substring(0,msg.length()-1));
//            requestFocus(nameContainer.getEditText());
//            return false;
//        } else {
//            nameContainer.setErrorEnabled(false);
//        }
//        return true;
//    }
//
//    private boolean validateEmail(TextInputLayout nameContainer) {
//        nameContainer.getEditText().requestFocus();
//        if (!Util.isValidEmail(nameContainer.getEditText().getText().toString())) {
//            nameContainer.setError("Please enter a valid Email Address");
//            requestFocus(nameContainer.getEditText());
//            return false;
//        } else {
//            nameContainer.setErrorEnabled(false);
//        }
//        return true;
//    }
//
//
//    private void requestFocus(View view) {
//        if (view.requestFocus()) {
//            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }
//
//    public String getEmailText()
//    {
//        emailBody = "Name : " + nameContainer.getEditText().getText() + "\n";
//        emailBody += "Company Name : " + companyContainer.getEditText().getText() + "\n";
//        emailBody += "Email Address : " + emailContainer.getEditText().getText() + "\n";
//        emailBody += "Phone Number : " + phoneContainer.getEditText().getText() + "\n";
//        emailBody += "Fleet Size : " + fleetSizeContainer.getEditText().getText() + "\n";
//        if(messageContainer.getEditText().getText().toString().length()>0)
//            emailBody += "Message : " + messageContainer.getEditText().getText() + "\n";
//
//        return emailBody;
//    }
//
//
//
//    private void sendEmail()
//    {
//        try {
//            emailBody = "Name : " + nameContainer.getEditText().getText() + "\n";
//            emailBody += "Company Name : " + companyContainer.getEditText().getText() + "\n";
//            emailBody += "Email Address : " + emailContainer.getEditText().getText() + "\n";
//            emailBody += "Phone Number : " + phoneContainer.getEditText().getText() + "\n";
//            emailBody += "Fleet Size : " + fleetSizeContainer.getEditText().getText() + "\n";
//            if(messageContainer.getEditText().getText().toString().length()>0)
//                emailBody += "Message : " + messageContainer.getEditText().getText() + "\n";
//
//            /*String uriText =
//                    "mailto:info@speedautosystems.com" +
//                            "?subject=" + Uri.encode("Free Trial Request") +
//                            "&body=" + Uri.encode(body);
//
//            Uri uri = Uri.parse(uriText);
//            Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
//            sendIntent.setData(uri);
//            ActivityInfo activityInfo = sendIntent.resolveActivityInfo(getActivity().getPackageManager(), sendIntent.getFlags());
//            if (activityInfo.exported)
//                getActivity().startActivity(Intent.createChooser(sendIntent, "Send email"));
//*/
//
//
//        }catch (Exception ex){ex.printStackTrace();}
//
//    }
//
//    private void clearFocus(View view)
//    {
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//}
