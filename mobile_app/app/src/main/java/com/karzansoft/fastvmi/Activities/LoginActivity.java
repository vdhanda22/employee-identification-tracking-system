//package com.karzansoft.mcc.Activities;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.support.design.widget.Snackbar;
//import android.support.design.widget.TextInputLayout;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.style.ForegroundColorSpan;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.karzansoft.mcc.DataBase.DatabaseManager;
//import com.karzansoft.mcc.Dialogs.AlertDialogFragment;
//import com.karzansoft.mcc.Dialogs.ForgotPasswordDialog;
//import com.karzansoft.mcc.Dialogs.TrialRequestDialog;
//import com.karzansoft.mcc.Dialogs.VerificationDialog;
//import com.karzansoft.mcc.Models.AccessoryItem;
//import com.karzansoft.mcc.Models.CodeVerificationFregDto;
//import com.karzansoft.mcc.Models.LanguageItem;
//import com.karzansoft.mcc.Models.LocalizeText;
//import com.karzansoft.mcc.Network.APIError;
//import com.karzansoft.mcc.Network.Entities.Request.LoginRequest;
//import com.karzansoft.mcc.Network.Entities.Request.RegistrationRequest;
//import com.karzansoft.mcc.Network.Entities.Request.SmsCodeDto;
//import com.karzansoft.mcc.Network.Entities.Request.TranslationLanguageRequest;
//import com.karzansoft.mcc.Network.Entities.Response.LoginResponse;
//import com.karzansoft.mcc.Network.Entities.Response.RegistrationResponse;
//import com.karzansoft.mcc.Network.ErrorUtils;
//import com.karzansoft.mcc.Network.WebResponse;
//import com.karzansoft.mcc.Network.WebResponseList;
//import com.karzansoft.mcc.Network.WebServiceFactory;
//import com.karzansoft.mcc.R;
//import com.karzansoft.mcc.Utils.AppUtils;
//import com.karzansoft.mcc.Utils.Constant;
//import com.karzansoft.mcc.Utils.Util;
//
//import org.chat21.android.utils.Utils;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//
///**
// * A login screen that offers login via email/password.
// */
//public class LoginActivity extends AppCompatActivity {
//
//    // UI references.
//    private EditText mEmailView;
//    private EditText mPasswordView;
//    private TextView mWelcometxt, mTrialRequesttxt;
//    Button mSignInButton;
//    TextInputLayout userNameContainer, userPasswordContainer, companyContainer;
//    ProgressDialog progressDialog;
//    Handler mHandler;
//    CheckBox remember_me;
//
//    private long smsBlockedTimeMillis = 0;
//    private int smsRenewDurationMillis = 120000; // 2 mins
//
//    public static int MESSAGE_SENT_COUNTS = 0;
//    public static String Guid;
//
//    final String errorMsg = "Due to temporary issue signup could not be completed. Please try later or send us an email to info@speedautosystems.com";
//    final String codeSendingFailed = "Verification Code sending is failed, try later.";
//    final String sMSLimitExceeded = "SMS limit exceeded, try later.";
//    final String networkError = "Can't connect to network!";
//
//    TrialRequestDialog trialRequestDialog = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        if (AppUtils.isRTLLanguageSelected(this))
//            AppUtils.forceRTLIfSupported(this);
//
//        super.onCreate(savedInstanceState);
//
//        // getSupportActionBar().hide();
//
//        setContentView(R.layout.activity_login);
//        mEmailView = (EditText) findViewById(R.id.email);
//        mWelcometxt = (TextView) findViewById(R.id.txt_welcome_sub);
//        mPasswordView = (EditText) findViewById(R.id.password);
//        userNameContainer = (TextInputLayout) findViewById(R.id.userNameContainer);
//        userPasswordContainer = (TextInputLayout) findViewById(R.id.passwordContainer);
//        companyContainer = (TextInputLayout) findViewById(R.id.companyNameContainer);
//        mTrialRequesttxt = (TextView) findViewById(R.id.txt_trial_request);
//        remember_me = (CheckBox) findViewById(R.id.remember_me);
//        mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setMessage("Processing...");
//        progressDialog.setCancelable(false);
//        mHandler = new Handler(Looper.getMainLooper());
//
//        progressDialog.setMessage(AppUtils.getLocalizeString(this, "Processing", "Processing..."));
//        AppUtils.setText(mWelcometxt, AppUtils.getLocalizeString(this, "VehicleMobileInspectionSystem", mWelcometxt.getText().toString()));
//        AppUtils.setText(mSignInButton, AppUtils.getLocalizeString(this, "LogIn", mSignInButton.getText().toString()));
//        AppUtils.setTextHint(companyContainer, AppUtils.getLocalizeString(this, "TenancyName", "Company Code"));
//        AppUtils.setTextHint(userNameContainer, AppUtils.getLocalizeString(this, "Email", "Email"));
//        AppUtils.setTextHint(userPasswordContainer, AppUtils.getLocalizeString(this, "Password", "Password"));
//
//        companyContainer.getEditText().setCompoundDrawablesWithIntrinsicBounds(Util.getTintedDrawable(this, R.drawable.ic_domain_white_24dp, R.color.white), null, null, null);
//        userNameContainer.getEditText().setCompoundDrawablesWithIntrinsicBounds(Util.getTintedDrawable(this, R.drawable.ic_email_black_18dp, R.color.white), null, null, null);
//        userPasswordContainer.getEditText().setCompoundDrawablesWithIntrinsicBounds(Util.getTintedDrawable(this, R.drawable.ic_lock_white_18dp, R.color.white), null, null, null);
//
//        mSignInButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!validateCompany())
//                    return;
//                if (!validateName())
//                    return;
//                if (!validatePassword())
//                    return;
//
//                login();
//                requestFocus(mSignInButton);
//            }
//        });
//
//        if (AppUtils.getAuthToken(this).length() > 0 && Utils.isLiveChatBuild(this)) {
//            Intent i = new Intent(LoginActivity.this, FragmentMainActivity.class);
//            startActivity(i);
//            finish();
//
//        }
//
//        mTrialRequesttxt.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                trialRequestDialog = TrialRequestDialog.newInstance();
//
//                trialRequestDialog.setButtonsClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        RegistrationRequest registrationRequest = trialRequestDialog.getRestrationObject();
//                        String to = formatedNumber(registrationRequest.contactNo);
//                        sendCodeSms(to);
//                    }
//                });
//                trialRequestDialog.show(getSupportFragmentManager(), "TrialRequest");
//            }
//        });
//
//        findViewById(R.id.forgot_password).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ForgotPasswordDialog forgotPasswordDialog = ForgotPasswordDialog.newInstance();
//                forgotPasswordDialog.show(getSupportFragmentManager(), "forgotpassword");
//            }
//        });
//
//        setTextSpaneAble(mTrialRequesttxt, R.color.colorAccent, 7);
//
//        if (AppUtils.isRememberMe(this)) {
//            companyContainer.getEditText().setText(AppUtils.getTenantName(this));
//            userNameContainer.getEditText().setText(AppUtils.getUserName(this));
//        }
//    }
//
//    private void setTextSpaneAble(TextView tv, int col, int chr_count) {
//        Spannable wordtoSpan = new SpannableString(tv.getText());
//
//        wordtoSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, col)), tv.getText().length() - chr_count, tv.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        tv.setText(wordtoSpan);
//    }
//
//    private void setTextSpaneAble(TextView tv) {
//        Spannable wordtoSpan = new SpannableString(tv.getText());
//
//        wordtoSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)), tv.getText().length() - 3, tv.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        tv.setText(wordtoSpan);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1001) {
//            if (resultCode == RESULT_OK) {
//
//                if (data != null) {
//                    String tenant = data.getStringExtra("Tenant");
//                    String userName = data.getStringExtra("UserName");
//                    String password = data.getStringExtra("Password");
//
//                    companyContainer.getEditText().setText(tenant);
//                    userNameContainer.getEditText().setText(userName);
//                    userPasswordContainer.getEditText().setText(password);
//
//                    clearFocus(companyContainer.getEditText());
//                    clearFocus(userNameContainer.getEditText());
//                    clearFocus(userPasswordContainer.getEditText());
//
//                }
//            }
//        }
//    }
//
//    private void login() {
//
//        if (!AppUtils.isNetworkAvailable(this)) {
//            AppUtils.showMessage(AppUtils.getLocalizeString(this, "NetworkError", networkError), mSignInButton, Snackbar.LENGTH_SHORT);
//            return;
//        }
//
//        progressDialog.show();
//        LoginRequest request = new LoginRequest();
//        request.setTenancyName(companyContainer.getEditText().getText().toString());
//        request.setUsernameOrEmailAddress(mEmailView.getText().toString());
//        request.setPassword(mPasswordView.getText().toString());
//        Call<WebResponse<LoginResponse>> loginResponse = WebServiceFactory.getInstance().login(request);
//        loginResponse.enqueue(new Callback<WebResponse<LoginResponse>>() {
//            @Override
//            public void onResponse(Call<WebResponse<LoginResponse>> call, Response<WebResponse<LoginResponse>> response) {
//
//                if (response.isSuccessful() && response.body() != null) {
//
//                    if (response.body().isSuccess()) {
//                        LoginResponse res = response.body().getResult();
//                        Utils.setLiveChatBuild(true, LoginActivity.this);
//                        if (res.getUserName() == null || res.getUserName().length() < 1)
//                            res.setUserName(mEmailView.getText().toString());
//
//                        res.setTenant(companyContainer.getEditText().getText().toString());
//                        res.setRememberMe(true);
//                        AppUtils.saveAuth(LoginActivity.this, res);
//                        AppUtils.saveLoginInfo(LoginActivity.this, res);
//                        getLanguageText();
//                        //loadAccessoriesList();
//                    } else {
//
//                        AppUtils.showMessage("" + response.body().getError().getDetails(), mSignInButton, Snackbar.LENGTH_SHORT);
//                        progressDialog.dismiss();
//                    }
//                } else {
//                    AppUtils.showMessage(AppUtils.getLocalizeString(LoginActivity.this, "InternalServerError", "Internal server error!"), mSignInButton, Snackbar.LENGTH_SHORT);
//                    progressDialog.dismiss();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<WebResponse<LoginResponse>> call, Throwable t) {
//
//                String msg = "";
//                if (t != null)
//                    msg = t.getMessage();
//                AppUtils.showMessage("" + msg, mSignInButton, Snackbar.LENGTH_SHORT);
//                progressDialog.dismiss();
//                t.printStackTrace();
//            }
//        });
//
//    }
//
//    LanguageItem languageItem;
//
//    private void getLanguageText() {
//
//
//        languageItem = AppUtils.getCurrentLanguage(this);
//        if (languageItem == null) {
//            languageItem = new LanguageItem();
//            languageItem.setDisplayName("English");
//            languageItem.setName("en");
//        }
//
//        Call<WebResponse<WebResponseList<LocalizeText>>> webResponseCall = WebServiceFactory.getInstance().getLanguageText(new TranslationLanguageRequest(languageItem.getName()), AppUtils.getAuthToken(LoginActivity.this));
//        webResponseCall.enqueue(new Callback<WebResponse<WebResponseList<LocalizeText>>>() {
//            @Override
//            public void onResponse(Call<WebResponse<WebResponseList<LocalizeText>>> call, Response<WebResponse<WebResponseList<LocalizeText>>> response) {
//
//                if (response.body() != null && response.body().isSuccess() && response.body().getResult() != null) {
//                    if (response.body().getResult().getItems() != null) {
//                        AppUtils.saveLanguageStrings(LoginActivity.this, AppUtils.convertToMap(response.body().getResult().getItems()));
//                        AppUtils.setCurrentLanguage(LoginActivity.this, languageItem);
//                    }
//                }
//
//                startHomeActivity();
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<WebResponse<WebResponseList<LocalizeText>>> call, Throwable t) {
//                startHomeActivity();
//                progressDialog.dismiss();
//            }
//        });
//    }
//
//    private void startHomeActivity() {
//        Intent intent = new Intent(LoginActivity.this, FragmentMainActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//    private void loadAccessoriesList() {
//        progressDialog.show();
//
//
//        Call<WebResponse<WebResponseList<AccessoryItem>>> responseCall = WebServiceFactory.getInstance().getAccessoryItems(AppUtils.getAuthToken(LoginActivity.this));
//
//        responseCall.enqueue(new Callback<WebResponse<WebResponseList<AccessoryItem>>>() {
//            @Override
//            public void onResponse(Call<WebResponse<WebResponseList<AccessoryItem>>> call, Response<WebResponse<WebResponseList<AccessoryItem>>> response) {
//                progressDialog.dismiss();
//
//                if (response.isSuccessful() && response.body() != null) {
//                    if (response.body().isSuccess()) {
//                        ArrayList<AccessoryItem> items = response.body().getResult().getItems();
//                        DatabaseManager.getInstance(getApplicationContext()).addAccessories(items);
//
//                        Intent intent = new Intent(LoginActivity.this, FragmentMainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WebResponse<WebResponseList<AccessoryItem>>> call, Throwable t) {
//                String msg = "";
//                if (t != null)
//                    msg = t.getMessage();
//                AppUtils.showMessage("" + msg, mSignInButton, Snackbar.LENGTH_SHORT);
//                progressDialog.dismiss();
//                t.printStackTrace();
//            }
//        });
//
//
//    }
//
//    private boolean validateName() {
//        mEmailView.requestFocus();
//        if (mEmailView.getText().toString().trim().isEmpty()) {
//            userNameContainer.setError(AppUtils.getLocalizeString(this, "PleaseEnterUserName", "Please enter user name"));
//            requestFocus(mEmailView);
//            return false;
//        } else {
//            userNameContainer.setErrorEnabled(false);
//        }
//        return true;
//    }
//
//    private boolean validatePassword() {
//        mPasswordView.requestFocus();
//        if (mPasswordView.getText().toString().trim().isEmpty()) {
//            userPasswordContainer.setError(AppUtils.getLocalizeString(this, "PleaseEnterPassword", "Please enter password"));
//            requestFocus(mPasswordView);
//            return false;
//        } else {
//            userPasswordContainer.setErrorEnabled(false);
//        }
//        return true;
//    }
//
//    private boolean validateCompany() {
//        companyContainer.getEditText().requestFocus();
//        if (companyContainer.getEditText().getText().toString().trim().isEmpty()) {
//            companyContainer.setError(AppUtils.getLocalizeString(this, "PleaseEnterCompanyName", "Please enter company name"));
//            requestFocus(companyContainer.getEditText());
//            return false;
//        } else {
//            companyContainer.setErrorEnabled(false);
//        }
//        return true;
//    }
//
//    private void requestFocus(View view) {
//        if (view.requestFocus()) {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }
//
//    private void clearFocus(View view) {
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//
//    private void showVerificationDialog(final RegistrationRequest registrationRequest, CodeVerificationFregDto codeVerificationFregDto) {
//
//        final String to = registrationRequest.contactNo;
//        final VerificationDialog verificationDialog = VerificationDialog.newInstance(codeVerificationFregDto);
//
//        verificationDialog.setButtonsClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                switch (view.getId()) {
//
//                    case R.id.resendaccesscodeButton: // resend Code
//                        sendCodeSms(to);
//                        break;
//
//                    case R.id.continueButton: //registration request
//                        progressDialog.show();
//
//                        Call<WebResponse<RegistrationResponse>> signupResponse = WebServiceFactory.getInstance().registerAccount(registrationRequest, AppUtils.getAuthToken(LoginActivity.this));
//                        signupResponse.enqueue(new Callback<WebResponse<RegistrationResponse>>() {
//                            @Override
//                            public void onResponse(Call<WebResponse<RegistrationResponse>> call, Response<WebResponse<RegistrationResponse>> response) {
//                                try {
//                                    progressDialog.dismiss();
//
//                                    if (response.isSuccessful() && response.body() != null) {
//                                        if (response.body().isSuccess()) {
//                                            RegistrationResponse reg = response.body().getResult();
//                                            companyContainer.getEditText().setText(reg.getTenancyName());
//                                            userNameContainer.getEditText().setText(reg.getEmail());
//                                            userPasswordContainer.getEditText().setText(registrationRequest.password);
//                                            clearFocus(companyContainer.getEditText());
//                                            clearFocus(userNameContainer.getEditText());
//                                            clearFocus(userPasswordContainer.getEditText());
//                                            login();
//                                        } else {
//                                            String errtext = errorMsg;
//                                            if (response.body() != null && response.body().getError() != null && response.body().getError().getDetails() != null)
//                                                errtext = response.body().getError().getDetails();
//                                            else if (response.body().getError() != null && response.body().getError().getMessage() != null)
//                                                errtext = response.body().getError().getMessage();
//
//                                            AlertDialogFragment.newInstance("Error in signing up", errtext, "ok", false).show(getSupportFragmentManager(), "error");
//                                        }
//                                    } else {
//                                        String message = "Internal server error!";
//                                        APIError error = ErrorUtils.parseError(response);
//                                        if (error != null && error.message() != null)
//                                            message = error.message();
//                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
//                                    }
//                                } catch (Exception ex) {
//                                    Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
//                                }
//
//                                //registration failed, allow user to verify again if sms sending count is over.
//                                if (response.isSuccessful() == true
//                                        && response.body().isSuccess() == false
//                                        && LoginActivity.MESSAGE_SENT_COUNTS == Constant.VERIFICATION_CODE_MESSAGE_COUNTS) {
//                                    LoginActivity.MESSAGE_SENT_COUNTS--;
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<WebResponse<RegistrationResponse>> call, Throwable t) {
//                                progressDialog.dismiss();
//                                AlertDialogFragment.newInstance("Error in signing up", errorMsg, "ok", false).show(getSupportFragmentManager(), "error");
//
//                            }
//                        });
//
//                        break;
//                }
//            }
//        });
//
//        verificationDialog.show(getSupportFragmentManager(), "verificationDialog");
//    }
//
//    private void sendCodeSms(String to) {
//
//        if (!AppUtils.isNetworkAvailable(getBaseContext())) {
//            AppUtils.showToastMessage(networkError, LoginActivity.this);
//            return;
//        }
//
//        // renew sms limit
//        if (MESSAGE_SENT_COUNTS >= Constant.VERIFICATION_CODE_MESSAGE_COUNTS) {
//            Calendar calendar = Calendar.getInstance();
//
//            if (smsBlockedTimeMillis > 0) {
//                long currentTimeMilli = calendar.getTimeInMillis();
//                if (currentTimeMilli - smsBlockedTimeMillis >= smsRenewDurationMillis) {
//                    MESSAGE_SENT_COUNTS = 0;
//                    smsBlockedTimeMillis = 0;
//                } else {
//                    AppUtils.showToastMessage(sMSLimitExceeded, LoginActivity.this);
//                    return;
//                }
//            } else {
//                smsBlockedTimeMillis = calendar.getTimeInMillis();
//                AppUtils.showToastMessage(sMSLimitExceeded, LoginActivity.this);
//                return;
//            }
//        }
//
//        progressDialog.show();
//
//        Guid = java.util.UUID.randomUUID().toString();
//        SmsCodeDto sendCodeSMSDto = new SmsCodeDto();
//        sendCodeSMSDto.setPhoneNumber(to);
//        sendCodeSMSDto.setGuid(Guid);
//
//        Call<SmsCodeDto> codeSmsResponse = WebServiceFactory.getInstance().sendCodeSMS(sendCodeSMSDto, AppUtils.getAuthToken(LoginActivity.this));
//        codeSmsResponse.enqueue(new Callback<SmsCodeDto>() {
//            @Override
//            public void onResponse(Call<SmsCodeDto> call, Response<SmsCodeDto> response) {
//
//                progressDialog.dismiss();
//
//                try {
//                    if (response.isSuccessful() && response.body() != null) {
//                        if (response.body().getIsCodeSent() == true) {
//
//                            MESSAGE_SENT_COUNTS++;
//
//                            CodeVerificationFregDto codeVerificationFregDto = new CodeVerificationFregDto();
//                            codeVerificationFregDto.codeLenght = response.body().getCodeLength();
//
//                            RegistrationRequest registrationRequest = trialRequestDialog.getRestrationObject();
//                            showVerificationDialog(registrationRequest, codeVerificationFregDto);
//                        } else {
//                            AlertDialogFragment.newInstance("Code Sending Failed", codeSendingFailed, "ok", false).show(getSupportFragmentManager(), "error");
//                        }
//                    } else {
//                        String message = "Internal server error!";
//                        APIError error = ErrorUtils.parseError(response);
//                        if (error != null && error.message() != null)
//                            message = error.message();
//                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
//                    }
//                } catch (Exception ex) {
//                    Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SmsCodeDto> call, Throwable t) {
//                progressDialog.dismiss();
//                AlertDialogFragment.newInstance("Code Sending Failed", codeSendingFailed, "ok", false).show(getSupportFragmentManager(), "error");
//            }
//        });
//    }
//
//    private String formatedNumber(String number) {
//
//        if (!number.startsWith("+"))
//            number = "+" + number;
//
//        return number;
//    }
//}
//
package com.karzansoft.fastvmi.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.karzansoft.fastvmi.DataBase.DatabaseManager;
//import com.karzansoft.mcc.Dialogs.ForgotPasswordDialog;
//import com.karzansoft.mcc.Dialogs.TrialRequestDialog;
import com.karzansoft.fastvmi.Models.AccessoryItem;
import com.karzansoft.fastvmi.Models.LanguageItem;
import com.karzansoft.fastvmi.Models.LocalizeText;
import com.karzansoft.fastvmi.Network.Entities.Request.LoginRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.TranslationLanguageRequest;
import com.karzansoft.fastvmi.Network.Entities.Response.LoginResponse;
import com.karzansoft.fastvmi.Network.WebResponse;
import com.karzansoft.fastvmi.Network.WebResponseList;
import com.karzansoft.fastvmi.Network.WebServiceFactory;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Util;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    TextInputLayout userNameContainer, userPasswordContainer;//,companyContainer;
    Button mSignInButton;
    private TextView mWelcometxt;//,mTrialRequesttxt;
    ProgressDialog progressDialog;
    Handler mHandler;
    CheckBox remember_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppUtils.isRTLLanguageSelected(this))
            AppUtils.forceRTLIfSupported(this);

        super.onCreate(savedInstanceState);

        // getSupportActionBar().hide();

        setContentView(R.layout.activity_login);
        mEmailView = (EditText) findViewById(R.id.email);
        mWelcometxt = (TextView) findViewById(R.id.txt_welcome_sub);
        mPasswordView = (EditText) findViewById(R.id.password);
        userNameContainer = (TextInputLayout) findViewById(R.id.userNameContainer);
        userPasswordContainer = (TextInputLayout) findViewById(R.id.passwordContainer);
//        companyContainer=(TextInputLayout)findViewById(R.id.companyNameContainer);
//        mTrialRequesttxt=(TextView)findViewById(R.id.txt_trial_request);
        remember_me = (CheckBox) findViewById(R.id.remember_me);
        mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        mHandler = new Handler(Looper.getMainLooper());

        progressDialog.setMessage(AppUtils.getLocalizeString(this, "Processing", "Processing..."));
        AppUtils.setText(mWelcometxt, AppUtils.getLocalizeString(this, "VehicleMobileInspectionSystem", mWelcometxt.getText().toString()));
        AppUtils.setText(mSignInButton, AppUtils.getLocalizeString(this, "LogIn", mSignInButton.getText().toString()));
//        AppUtils.setTextHint(companyContainer,AppUtils.getLocalizeString(this,"TenancyName","Company Code"));
        AppUtils.setTextHint(userNameContainer, AppUtils.getLocalizeString(this, "Email", "Email"));
        AppUtils.setTextHint(userPasswordContainer, AppUtils.getLocalizeString(this, "Password", "Password"));

//        companyContainer.getEditText().setCompoundDrawablesWithIntrinsicBounds(Util.getTintedDrawable(this,R.drawable.ic_domain_white_24dp,R.color.white),null,null,null);
        userNameContainer.getEditText().setCompoundDrawablesWithIntrinsicBounds(Util.getTintedDrawable(this, R.drawable.ic_email_black_18dp, R.color.colorPrimary), null, null, null);
        userPasswordContainer.getEditText().setCompoundDrawablesWithIntrinsicBounds(Util.getTintedDrawable(this, R.drawable.ic_lock_white_18dp, R.color.colorPrimary), null, null, null);


        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(!validateCompany())
//                    return;
                if (!validateName())
                    return;
                if (!validatePassword())
                    return;

                login();
                requestFocus(mSignInButton);
            }
        });


        //setTextSpaneAble(mWelcometxt);
        if (AppUtils.getAuthToken(this).length() > 0) {
            Intent i = new Intent(LoginActivity.this, FragmentMainActivity.class);
            startActivity(i);
            finish();
        }

    /*    mTrialRequesttxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*Intent i = new Intent(LoginActivity.this,SignupActivity.class);
                startActivityForResult(i,1001);*//*

                final TrialRequestDialog trialRequestDialog=TrialRequestDialog.newInstance();
                final String errorMsg="Due to temporary issue signup could not be completed. Please try later or send us an email to info@speedautosystems.com";
                trialRequestDialog.setButtonsClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        progressDialog.show();

                        final RegistrationRequest registrationRequest=trialRequestDialog.getRestrationObject();
                        Call<WebResponse<RegistrationResponse>> signupResponse= WebServiceFactory.getInstance().registerAccount(registrationRequest,AppUtils.getAuthToken(LoginActivity.this));

                        signupResponse.enqueue(new Callback<WebResponse<RegistrationResponse>>() {
                            @Override
                            public void onResponse(Call<WebResponse<RegistrationResponse>> call, Response<WebResponse<RegistrationResponse>> response) {
                                progressDialog.dismiss();

                                if(response.isSuccessful() && response.body()!=null) {
                                    if (response.body().isSuccess()) {
                                        RegistrationResponse reg=response.body().getResult();
                                        trialRequestDialog.dismiss();
//                                        companyContainer.getEditText().setText(reg.getTenancyName());
                                        userNameContainer.getEditText().setText(reg.getEmail());
                                        userPasswordContainer.getEditText().setText(registrationRequest.password);
//                                        clearFocus(companyContainer.getEditText());
                                        clearFocus(userNameContainer.getEditText());
                                        clearFocus(userPasswordContainer.getEditText());
                                        // a confirmation email has been sent to your email
                                        //AlertDialogFragment.newInstance("Sign up Successful!","Your account is created. Please continue to login.","ok",false).show(getSupportFragmentManager(),"success");
                                        login();
                                    } else {
                                        String errtext=errorMsg;
                                        if(response.body()!=null && response.body().getError()!=null && response.body().getError().getDetails()!=null)
                                            errtext=response.body().getError().getDetails();
                                        else if (response.body().getError()!=null && response.body().getError().getMessage()!=null)
                                            errtext = response.body().getError().getMessage();
                                        AlertDialogFragment.newInstance("Error in signing up",errtext,"ok",false).show(getSupportFragmentManager(),"error");
                                    }
                                }else {
                                    String message = "Internal server error!";
                                    APIError error= ErrorUtils.parseError(response);
                                    if(error!=null && error.message()!=null)
                                        message=error.message();
                                    Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<WebResponse<RegistrationResponse>> call, Throwable t) {
                                progressDialog.dismiss();
                                AlertDialogFragment.newInstance("Error in signing up",errorMsg,"ok",false).show(getSupportFragmentManager(),"error");

                            }
                        });


                    }
                });
                trialRequestDialog.show(getSupportFragmentManager(),"TrialRequest");
            }
        });*/

//        findViewById(R.id.forgot_password).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ForgotPasswordDialog forgotPasswordDialog=ForgotPasswordDialog.newInstance();
//                forgotPasswordDialog.show(getSupportFragmentManager(),"forgotpassword");
//            }
//        });

//        setTextSpaneAble(mTrialRequesttxt,R.color.colorAccent,7);

        if (AppUtils.isRememberMe(this)) {
//            companyContainer.getEditText().setText(AppUtils.getTenantName(this));
            userNameContainer.getEditText().setText(AppUtils.getUserName(this));
            //remember_me.setChecked(AppUtils.isRememberMe(this));
        }
    }

    private void setTextSpaneAble(TextView tv,int col,int chr_count)
    {
        Spannable wordtoSpan = new SpannableString(tv.getText());

        wordtoSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,col)), tv.getText().length() - chr_count, tv.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setText(wordtoSpan);
    }
    private void setTextSpaneAble(TextView tv)
    {
        Spannable wordtoSpan = new SpannableString(tv.getText());

        wordtoSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.colorPrimary)), tv.getText().length() - 3, tv.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setText(wordtoSpan);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == RESULT_OK) {

                if (data!=null)
                {
//                    String tenant = data.getStringExtra("Default");
                    String userName = data.getStringExtra("UserName");
                    String password = data.getStringExtra("Password");

//                    companyContainer.getEditText().setText(tenant);
                    userNameContainer.getEditText().setText(userName);
                    userPasswordContainer.getEditText().setText(password);

//                    clearFocus(companyContainer.getEditText());
                    clearFocus(userNameContainer.getEditText());
                    clearFocus(userPasswordContainer.getEditText());

                }
            }
        }
    }

    private void login() {

        if (!AppUtils.isNetworkAvailable(this)) {
            AppUtils.showMessage(AppUtils.getLocalizeString(this, "NetworkError", "Can't connect to network!"), mSignInButton, Snackbar.LENGTH_SHORT);
            return;
        }

        progressDialog.show();
        LoginRequest request = new LoginRequest();
        request.setTenancyName(null);
//        request.setTenancyName(companyContainer.getEditText().getText().toString());
        request.setUsernameOrEmailAddress(mEmailView.getText().toString());
        request.setPassword(mPasswordView.getText().toString());
        final Call<WebResponse<String>> loginResponse = WebServiceFactory.getInstance().login(request);
        loginResponse.enqueue(new Callback<WebResponse<String>>() {
            @Override
            public void onResponse(Call<WebResponse<String>> call, Response<WebResponse<String>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isSuccess()) {
                        String res=response.body().getResult();
  /*                      if (res.getUserName() == null || res.getUserName().length() < 1 )
                            res.setUserName(mEmailView.getText().toString().toLowerCase());*/

                        AppUtils.saveAuth(LoginActivity.this, res);
                        AppUtils.saveLoginInfo(LoginActivity.this,res);
                        Intent intent=new Intent(LoginActivity.this,FragmentMainActivity.class);
                        intent.putExtra("IS_LOGIN", true);
                        startActivity(intent);
                        finish();
                        //loadAccessoriesList();
                    } else {
                        if(response.body().getError().getDetails()!=null)
                            AppUtils.showMessage(""+response.body().getError().getDetails(), mSignInButton, Snackbar.LENGTH_LONG);
                        else
                            AppUtils.showMessage(""+response.body().getError().getMessage(), mSignInButton, Snackbar.LENGTH_LONG);
                    }
                } else
                    AppUtils.showMessage("Login Failed!", mSignInButton, Snackbar.LENGTH_LONG);

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<WebResponse<String>> call, Throwable t) {

                t.printStackTrace();
                AppUtils.showMessage("Connection time out!", mSignInButton, Snackbar.LENGTH_LONG);
                progressDialog.dismiss();
            }
        });



 /*
        loginResponse.enqueue(new Callback<WebResponse<Object>>() {
            @Override
            public void onResponse(Call<WebResponse<Object>> call, Response<WebResponse<Object>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isSuccess()) {
//                        Log.d("response_login", response.toString());
                        LoginResponse res = new LoginResponse();
                        String result = response.body().getResult().toString();
                        res.setToken(result);
//                        Utils.setLiveChatBuild(true,LoginActivity.this);
//                        if (res.getUserName() == null || res.getUserName().length()< 1)
                        res.setUserName(mEmailView.getText().toString());

//                        res.setTenant(companyContainer.getEditText().getText().toString());
                        res.setRememberMe(true);
                        AppUtils.saveAuth(LoginActivity.this, res);
                        AppUtils.saveLoginInfo(LoginActivity.this, res);
                        getLanguageText();
                        //loadAccessoriesList();
                    } else {

                        AppUtils.showMessage("" + response.body().getError().getDetails(), mSignInButton, Snackbar.LENGTH_SHORT);
                        progressDialog.dismiss();
                    }
                } else
                {
                    AppUtils.showMessage(AppUtils.getLocalizeString(LoginActivity.this,"InternalServerError","Internal server error!"), mSignInButton, Snackbar.LENGTH_SHORT);
                    progressDialog.dismiss();
                }



            }

            @Override
            public void onFailure(Call<WebResponse<Object>> call, Throwable t) {

                String msg = "";
                if (t != null)
                    msg = t.getMessage();
                AppUtils.showMessage("" + msg, mSignInButton, Snackbar.LENGTH_SHORT);
                progressDialog.dismiss();
                t.printStackTrace();
            }
        });
*/
    }

    LanguageItem languageItem;
    private void getLanguageText(){


        languageItem=AppUtils.getCurrentLanguage(this);
        if(languageItem==null)
        {
            languageItem=new LanguageItem();
            languageItem.setDisplayName("English");
            languageItem.setName("en");
        }

        Call<WebResponse<WebResponseList<LocalizeText>>> webResponseCall=WebServiceFactory.getInstance().getLanguageText(new TranslationLanguageRequest(languageItem.getName()),AppUtils.getAuthToken(LoginActivity.this));
        webResponseCall.enqueue(new Callback<WebResponse<WebResponseList<LocalizeText>>>() {
            @Override
            public void onResponse(Call<WebResponse<WebResponseList<LocalizeText>>> call, Response<WebResponse<WebResponseList<LocalizeText>>> response) {

                if(response.body()!=null && response.body().isSuccess() && response.body().getResult()!=null)
                {
                    if(response.body().getResult().getItems()!=null)
                    {
                        AppUtils.saveLanguageStrings(LoginActivity.this,AppUtils.convertToMap(response.body().getResult().getItems()));
                        AppUtils.setCurrentLanguage(LoginActivity.this,languageItem);
                    }
                }

                startHomeActivity();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<WebResponse<WebResponseList<LocalizeText>>> call, Throwable t) {
                startHomeActivity();
                progressDialog.dismiss();
            }
        });
    }

    private void startHomeActivity()
    {
        Intent intent = new Intent(LoginActivity.this, FragmentMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadAccessoriesList()
    {
        progressDialog.show();


        Call<WebResponse<WebResponseList<AccessoryItem>>> responseCall=WebServiceFactory.getInstance().getAccessoryItems(AppUtils.getAuthToken(LoginActivity.this));

        responseCall.enqueue(new Callback<WebResponse<WebResponseList<AccessoryItem>>>() {
            @Override
            public void onResponse(Call<WebResponse<WebResponseList<AccessoryItem>>> call, Response<WebResponse<WebResponseList<AccessoryItem>>> response) {
                progressDialog.dismiss();

                if(response.isSuccessful() && response.body() !=null)
                {
                    if(response.body().isSuccess())
                    {
                        ArrayList<AccessoryItem> items=response.body().getResult().getItems();
                        DatabaseManager.getInstance(getApplicationContext()).addAccessories(items);

                        Intent intent=new Intent(LoginActivity.this,FragmentMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<WebResponse<WebResponseList<AccessoryItem>>> call, Throwable t) {
                String msg = "";
                if (t!=null)
                    msg = t.getMessage();
                AppUtils.showMessage("" + msg, mSignInButton, Snackbar.LENGTH_SHORT);
                progressDialog.dismiss();
                t.printStackTrace();
            }
        });





    }

    private boolean validateName() {
        mEmailView.requestFocus();
        if (mEmailView.getText().toString().trim().isEmpty()) {
            userNameContainer.setError(AppUtils.getLocalizeString(this,"PleaseEnterUserName","Please enter user name"));
            requestFocus(mEmailView);
            return false;
        } else {
            userNameContainer.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        mPasswordView.requestFocus();
        if (mPasswordView.getText().toString().trim().isEmpty()) {
            userPasswordContainer.setError(AppUtils.getLocalizeString(this,"PleaseEnterPassword","Please enter password"));
            requestFocus(mPasswordView);
            return false;
        } else {
            userPasswordContainer.setErrorEnabled(false);
        }
        return true;
    }
/*

    private boolean validateCompany() {
        companyContainer.getEditText().requestFocus();
        if (companyContainer.getEditText().getText().toString().trim().isEmpty()) {
            companyContainer.setError(AppUtils.getLocalizeString(this,"PleaseEnterCompanyName","Please enter company name"));
            requestFocus(companyContainer.getEditText());
            return false;
        } else {
            companyContainer.setErrorEnabled(false);
        }
        return true;
    }
*/

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private void clearFocus(View view)
    {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

