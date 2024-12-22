////package com.karzansoft.mcc.Activities;
////
////import android.app.ProgressDialog;
////import android.content.Context;
////import android.content.DialogInterface;
////import android.content.Intent;
////import android.os.Bundle;
////import android.support.annotation.Nullable;
////import android.support.design.widget.Snackbar;
////import android.support.design.widget.TextInputLayout;
////import android.support.v7.app.AlertDialog;
////import android.support.v7.app.AppCompatActivity;
////import android.text.Html;
////import android.text.Spanned;
////import android.view.View;
////import android.view.WindowManager;
////import android.view.inputmethod.InputMethodManager;
////import android.widget.Button;
////import android.widget.CheckBox;
////import android.widget.EditText;
////
////import com.google.gson.Gson;
////import com.karzansoft.mcc.Dialogs.VerificationDialog;
////import com.karzansoft.mcc.Models.Country;
////import com.karzansoft.mcc.Models.CountryResult;
////import com.karzansoft.mcc.Network.Entities.Response.SMSResponse;
////import com.karzansoft.mcc.Network.WebServiceFactory;
////import com.karzansoft.mcc.R;
////import com.karzansoft.mcc.Utils.AppUtils;
////import com.karzansoft.mcc.Utils.Util;
////
////import org.json.JSONObject;
////
////import java.io.UnsupportedEncodingException;
////import java.util.HashMap;
////import java.util.List;
////import java.util.regex.Pattern;
////
////import okhttp3.ResponseBody;
////import retrofit2.Call;
////import retrofit2.Callback;
////import retrofit2.Response;
////
/////**
//// * Created by Yasir on 11/7/2018.
//// */
////
////public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
////
////    TextInputLayout nameContainer, companyContainer, emailContainer, phoneContainer, fleetSizeContainer,
////            messageContainer, countryContainer, codeCountainer, passContainer, rePassContainer;
////    Button btn_save;
////    HashMap<String, String> languageTexts;
////    private String emailBody = "";
////    CountryResult countryResult;
////    Country selectedCountry;
////    ProgressDialog progressDialog;
////    String randomNumber;
////    public static int NUMBER_OF_RETRIES;
////
////    @Override
////    protected void onCreate(@Nullable Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_signup);
////
////        nameContainer = findViewById(R.id.nameContainer);
////        companyContainer = findViewById(R.id.companyNameContainer);
////        emailContainer = findViewById(R.id.emailContainer);
////        phoneContainer = findViewById(R.id.phoneContainer);
////        fleetSizeContainer = findViewById(R.id.fleetSizeContainer);
////        messageContainer = findViewById(R.id.messageContainer);
////        countryContainer = findViewById(R.id.countryContainer);
////        codeCountainer = findViewById(R.id.codeContainer);
////        passContainer = findViewById(R.id.passContainer);
////        rePassContainer = findViewById(R.id.rePassContainer);
////        btn_save = findViewById(R.id.next);
////        btn_save.setOnClickListener(this);
////        countryContainer.getEditText().setOnClickListener(this);
////
////        Gson gson = new Gson();
////        countryResult = gson.fromJson(Util.loadJSONFromAsset(this), CountryResult.class);
////
////        progressDialog = new ProgressDialog(this);
////        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////        progressDialog.setMessage("Processing...");
////        progressDialog.setCancelable(false);
////        progressDialog.setMessage(AppUtils.getLocalizeString(this, "Processing", "Processing..."));
////
////        NUMBER_OF_RETRIES = 0;
////        randomNumber = AppUtils.generateRandomNumber();
////    }
////
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
////                        new AlertDialog.Builder(SignupActivity.this);
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
////
////            case R.id.next:
////
////                if (!validateField(nameContainer) || !validateField(companyContainer) || !validateField(emailContainer) || !validateEmail(emailContainer)
////                        || !validateField(fleetSizeContainer) || !validateField(countryContainer) || !validateField(codeCountainer)
////                        || !validateField(phoneContainer) || !validPhoneNumber(phoneContainer) || !validateField(passContainer)
////                        || !validateField(rePassContainer))
////                    return;
////
////                if (!passContainer.getEditText().getText().toString().equals(rePassContainer.getEditText().getText().toString())) {
////                    AppUtils.showMessage("Password mismatch ", btn_save, Snackbar.LENGTH_LONG);
////                    return;
////                }
////
////                clearFocus(phoneContainer.getEditText());
////                clearFocus(passContainer.getEditText());
////                clearFocus(rePassContainer.getEditText());
////
////                //showVerificationDialog();
////                confirmMobileNumber(codeCountainer.getEditText().getText().toString(), phoneContainer.getEditText().getText().toString());
////
////                break;
////        }
////    }
////
////    private String getFormatedNumber() {
////        String code = codeCountainer.getEditText().getText().toString();
////        String mobileNumber = phoneContainer.getEditText().getText().toString();
////
////        if (mobileNumber.substring(0, 1).equalsIgnoreCase("0")) {
////            mobileNumber = mobileNumber.substring(1);
////        }
////
////        if (!code.startsWith("+"))
////            code = "+" + code;
////
////        return code + mobileNumber;
////    }
////
////    private void confirmMobileNumber(String code, String mobileNumber) {
////        if (mobileNumber.substring(0, 1).equalsIgnoreCase("0")) {
////            mobileNumber = mobileNumber.substring(1);
////        }
////
////        if (!code.startsWith("+"))
////            code = "+" + code;
////
////        final String formattedNo = code + mobileNumber;
////
////        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignupActivity.this);
////        alertDialog.setTitle("Mobile Number Confirmation");
////        alertDialog.setCancelable(false);
////        alertDialog.setMessage("Is this number correct ?" + System.getProperty("line.separator") + formattedNo + System.getProperty("line.separator") + "An SMS will be sent to this number with account activation code.");
////
////        alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialogInterface, int i) {
////                //showVerificationDialog();
////                //sendActivationCode(formattedNo,randomNumber);
////
////            }
////        });
////
////        alertDialog.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialogInterface, int i) {
////
////            }
////        });
////
////        alertDialog.show();
////    }
////
////    private void sendActivationCode(String to, String actCode) {
////        if (!AppUtils.isNetworkAvailable(this)) {
////            AppUtils.showToastMessage(AppUtils.getLocalizeString(this, "NetworkError", "Can't connect to network!"), this);
////            return;
////        }
////
////
////        String auth = "";
////        try {
////
////            auth = "Basic " + AppUtils.getBase64String("ACb065ab1868cd02d2ad4308bc75e5cab6:3618940c5dcb7aa2b2b435992be71c7b");
////        } catch (UnsupportedEncodingException ex) {
////            ex.printStackTrace();
////
////            AppUtils.showToastMessage("" + ex.getMessage(), this);
////            return;
////
////        }
////
////        if (NUMBER_OF_RETRIES >= 3) {
////            AppUtils.showToastMessage("SMS limit exceeded", this);
////            return;
////        }
////       /* MESSAGE_SENT_COUNTS +=1;
////        showVerificationDialog();*/
////        progressDialog.show();
////        WebServiceFactory.getInstance().sendActivationCode(to, "+14438797269", "Your VMI account activation code is: " + actCode, auth).enqueue(new Callback<SMSResponse>() {
////            @Override
////            public void onResponse(Call<SMSResponse> call, Response<SMSResponse> response) {
////                try {
////                    progressDialog.dismiss();
////                    if (response.isSuccessful() && response.body() != null && (response.body().getError_code() == null || response.body().getError_code().length() < 1)) {
////                        NUMBER_OF_RETRIES += 1;
////                        showVerificationDialog();
////
////
////                    } else {
////                        AppUtils.showToastMessage("Error in sending activation code, please try again later", SignupActivity.this);
////                    }
////                } catch (Exception ex) {
////                    ex.printStackTrace();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<SMSResponse> call, Throwable t) {
////                try {
////                    progressDialog.dismiss();
////                } catch (Exception ex) {
////                    ex.printStackTrace();
////                }
////            }
////        });
////
////
////    }
////
////    private void showVerificationDialog() {
//////        String rNumber = randomNumber;
//////        Util.LogE("CODE", "" + rNumber);
//////        VerificationDialog verificationDialog = VerificationDialog.newInstance();
//////        // verificationDialog.setCancelable(false);
//////        verificationDialog.setButtonsClickListener(new View.OnClickListener() {
//////            @Override
//////            public void onClick(View view) {
//////
//////
//////                switch (view.getId()) {
//////                    case R.id.resendaccesscodeButton:
//////                        sendActivationCode(getFormatedNumber(), randomNumber);
//////
//////                        break;
//////                    case R.id.continueButton:
//////                        returnData();
//////                        finish();
//////                        break;
//////                }
//////            }
//////        });
//////        verificationDialog.show(getSupportFragmentManager(), "verificationDialog");
////    }
////
////    private void returnData() {
////        Intent data = new Intent();
////        data.putExtra("Tenant", companyContainer.getEditText().getText().toString());
////        data.putExtra("UserName", emailContainer.getEditText().getText().toString());
////        data.putExtra("Password", passContainer.getEditText().getText().toString());
////        setResult(RESULT_OK, data);
////    }
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
////    private boolean validPhoneNumber(TextInputLayout phoneContainer) {
////        phoneContainer.getEditText().requestFocus();
////
////        if (!isValidNumber(phoneContainer.getEditText())) {
////            phoneContainer.setError("Please enter a valid Phone Number");
////            requestFocus(phoneContainer.getEditText());
////            return false;
////        } else {
////            phoneContainer.setErrorEnabled(false);
////        }
////
////        return true;
////    }
////
////    private boolean isValidNumber(EditText editText) {
////        final String mobilenumber_entered = editText.getText().toString().trim();
////        //String invalidMobileNumber = mobilenumber_entered.replace("\\d", "");
////        String numericMobilenumer = mobilenumber_entered.replaceAll("[^\\d]", "");
////
////        if (numericMobilenumer.length() < 7 || mobilenumber_entered.length() < 7 || mobilenumber_entered.length() > 15) {
////            return false;
////        }
////
////        return true;
////    }
////
////
////    private void requestFocus(View view) {
////        if (view.requestFocus()) {
////            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
////        }
////    }
////
////    private void clearFocus(View view) {
////        if (view != null && view.hasFocus()) {
////            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
////        }
////    }
////}
//package com.karzansoft.mcc.Activities;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.design.widget.Snackbar;
//import android.support.design.widget.TextInputLayout;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.google.gson.Gson;
//import com.karzansoft.mcc.Dialogs.VerificationDialog;
//import com.karzansoft.mcc.Models.Country;
//import com.karzansoft.mcc.Models.CountryResult;
//import com.karzansoft.mcc.Network.Entities.Response.SMSResponse;
//import com.karzansoft.mcc.Network.WebServiceFactory;
//import com.karzansoft.mcc.R;
//import com.karzansoft.mcc.Utils.AppUtils;
//import com.karzansoft.mcc.Utils.Util;
//
//import org.json.JSONObject;
//
//import java.io.UnsupportedEncodingException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.regex.Pattern;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * Created by Yasir on 11/7/2018.
// */
//
//public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
//
//    TextInputLayout nameContainer,companyContainer,emailContainer,phoneContainer,fleetSizeContainer,messageContainer,countryContainer,codeCountainer,passContainer,rePassContainer;
//    Button btn_save;
//    HashMap<String,String> languageTexts;
//    private String emailBody="";
//    CountryResult countryResult;
//    Country selectedCountry;
//    ProgressDialog progressDialog;
//    String randomNumber;
//    public static int NUMBER_OF_RETRIES;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//
//        nameContainer = findViewById(R.id.nameContainer);
//        companyContainer = findViewById(R.id.companyNameContainer);
//        emailContainer = findViewById(R.id.emailContainer);
//        phoneContainer = findViewById(R.id.phoneContainer);
//        fleetSizeContainer = findViewById(R.id.fleetSizeContainer);
//        messageContainer = findViewById(R.id.messageContainer);
//        countryContainer = findViewById(R.id.countryContainer);
//        codeCountainer = findViewById(R.id.codeContainer);
//        passContainer = findViewById(R.id.passContainer);
//        rePassContainer = findViewById(R.id.rePassContainer);
//        btn_save = findViewById(R.id.next);
//        btn_save.setOnClickListener(this);
//        countryContainer.getEditText().setOnClickListener(this);
//
//        Gson gson = new Gson();
//        countryResult = gson.fromJson(Util.loadJSONFromAsset(this), CountryResult.class);
//
//        progressDialog=new ProgressDialog(this);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setMessage("Processing...");
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage(AppUtils.getLocalizeString(this,"Processing","Processing..."));
//
//        NUMBER_OF_RETRIES = 0;
//        randomNumber = AppUtils.generateRandomNumber();
//    }
//
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
//                        new AlertDialog.Builder(SignupActivity.this);
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
//
//            case R.id.next:
//
//                if(!validateField(nameContainer)|| !validateField(companyContainer) ||!validateField(emailContainer)||!validateEmail(emailContainer)||!validateField(fleetSizeContainer)||!validateField(countryContainer) ||!validateField(codeCountainer) ||!validateField(phoneContainer)|| !validPhoneNumber(phoneContainer)||!validateField(passContainer)||!validateField(rePassContainer))
//                    return;
//
//                if(!passContainer.getEditText().getText().toString().equals(rePassContainer.getEditText().getText().toString()))
//                {
//                    AppUtils.showMessage("Password mismatch ",btn_save, Snackbar.LENGTH_LONG);
//                    return;
//                }
//
//                clearFocus(phoneContainer.getEditText());
//                clearFocus(passContainer.getEditText());
//                clearFocus(rePassContainer.getEditText());
//
//                //showVerificationDialog();
//                confirmMobileNumber(codeCountainer.getEditText().getText().toString(),phoneContainer.getEditText().getText().toString());
//
//                break;
//        }
//    }
//
//    private String getFormatedNumber()
//    {
//        String code = codeCountainer.getEditText().getText().toString();
//        String mobileNumber = phoneContainer.getEditText().getText().toString();
//
//        if(mobileNumber.substring(0,1).equalsIgnoreCase("0"))
//        {
//            mobileNumber = mobileNumber.substring(1);
//        }
//
//        if (!code.startsWith("+"))
//            code = "+"+code;
//
//        return code + mobileNumber;
//    }
//
//    private void confirmMobileNumber(String code,String mobileNumber)
//    {
//        if(mobileNumber.substring(0,1).equalsIgnoreCase("0"))
//        {
//            mobileNumber = mobileNumber.substring(1);
//        }
//
//        if (!code.startsWith("+"))
//            code = "+"+code;
//
//        final String formattedNo = code + mobileNumber;
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignupActivity.this);
//        alertDialog.setTitle("Mobile Number Confirmation");
//        alertDialog.setCancelable(false);
//        alertDialog.setMessage("Is this number correct ?" + System.getProperty("line.separator") + formattedNo + System.getProperty("line.separator")+ "An SMS will be sent to this number with account activation code." );
//
//        alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //showVerificationDialog();
//                sendActivationCode(formattedNo,randomNumber);
//
//            }
//        });
//
//        alertDialog.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        alertDialog.show();
//    }
//
//    private void sendActivationCode(String to,String actCode)
//    {
//        if(!AppUtils.isNetworkAvailable(this))
//        {
//            AppUtils.showToastMessage(AppUtils.getLocalizeString(this,"NetworkError","Can't connect to network!"),this);
//            return;
//        }
//
//
//
//        String auth= "";
//        try {
//
//            auth = "Basic " + AppUtils.getBase64String("ACb065ab1868cd02d2ad4308bc75e5cab6:3618940c5dcb7aa2b2b435992be71c7b");
//        }catch (UnsupportedEncodingException ex){ex.printStackTrace();
//
//            AppUtils.showToastMessage(""+ex.getMessage(),this);
//            return;
//
//        }
//
//        if (NUMBER_OF_RETRIES>=3)
//        {
//            AppUtils.showToastMessage("SMS limit exceeded",this);
//            return;
//        }
//       /* NUMBER_OF_RETRIES +=1;
//        showVerificationDialog();*/
//        progressDialog.show();
//        WebServiceFactory.getInstance().sendActivationCode(to,"+14438797269","Your VMI account activation code is: "+actCode,auth).enqueue(new Callback<SMSResponse>() {
//            @Override
//            public void onResponse(Call<SMSResponse> call, Response<SMSResponse> response) {
//                try {
//                    progressDialog.dismiss();
//                    if(response.isSuccessful() && response.body() !=null && (response.body().getError_code() ==null || response.body().getError_code().length()<1))
//                    {
//                        NUMBER_OF_RETRIES +=1;
//                        showVerificationDialog();
//
//
//                    }else {
//                        AppUtils.showToastMessage("Error in sending activation code, please try again later",SignupActivity.this);
//                    }
//                }catch (Exception ex){ex.printStackTrace();}
//            }
//
//            @Override
//            public void onFailure(Call<SMSResponse> call, Throwable t) {
//                try {
//                    progressDialog.dismiss();
//                }catch (Exception ex){ex.printStackTrace();}
//            }
//        });
//
//
//    }
//
//    private void showVerificationDialog()
//    {
//        String rNumber = randomNumber;
//        Util.LogE("CODE",""+rNumber);
//        VerificationDialog verificationDialog = VerificationDialog.newInstance(rNumber);
//        // verificationDialog.setCancelable(false);
//        verificationDialog.setButtonsClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                switch (view.getId())
//                {
//                    case R.id.resendaccesscodeButton:
//                        sendActivationCode(getFormatedNumber(),randomNumber);
//
//                        break;
//                    case R.id.continueButton:
//                        returnData();
//                        finish();
//                        break;
//                }
//            }
//        });
//        verificationDialog.show(getSupportFragmentManager(),"verificationDialog");
//    }
//
//    private void returnData()
//    {
//        Intent data = new Intent();
//        data.putExtra("Tenant",companyContainer.getEditText().getText().toString());
//        data.putExtra("UserName",emailContainer.getEditText().getText().toString());
//        data.putExtra("Password",passContainer.getEditText().getText().toString());
//        setResult(RESULT_OK, data);
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
//    private boolean validPhoneNumber(TextInputLayout phoneContainer)
//    {
//        phoneContainer.getEditText().requestFocus();
//
//        if (!isValidNumber(phoneContainer.getEditText()))
//        {
//            phoneContainer.setError("Please enter a valid Phone Number");
//            requestFocus(phoneContainer.getEditText());
//            return false;
//        }else {
//            phoneContainer.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//    private boolean isValidNumber(EditText editText)
//    {
//        final String mobilenumber_entered = editText.getText().toString().trim() ;
//        //String invalidMobileNumber = mobilenumber_entered.replace("\\d", "");
//        String numericMobilenumer = mobilenumber_entered.replaceAll("[^\\d]", "");
//
//        if(numericMobilenumer.length()<7|| mobilenumber_entered.length()<7 || mobilenumber_entered.length()>15)
//        {
//            return false;
//        }
//
//        return true;
//    }
//
//
//    private void requestFocus(View view) {
//        if (view.requestFocus()) {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }
//
//    private void clearFocus(View view)
//    {
//        if (view != null && view.hasFocus()) {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//}
