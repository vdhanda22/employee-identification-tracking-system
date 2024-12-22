////package com.karzansoft.mcc.Dialogs;
////
////import android.app.Dialog;
////import android.app.ProgressDialog;
////import android.content.Context;
////import android.content.DialogInterface;
////import android.os.Bundle;
////import android.os.CountDownTimer;
////import android.support.annotation.Nullable;
////import android.support.v7.app.AlertDialog;
////import android.support.v7.app.AppCompatDialogFragment;
////import android.text.InputFilter;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.inputmethod.InputMethodManager;
////import android.widget.Button;
////import android.widget.EditText;
////import android.widget.TextView;
////import android.widget.Toast;
////
////import com.karzansoft.mcc.Activities.LoginActivity;
////import com.karzansoft.mcc.Models.CodeVerificationFregDto;
////import com.karzansoft.mcc.Network.APIError;
////import com.karzansoft.mcc.Network.Entities.Request.SmsCodeDto;
////import com.karzansoft.mcc.Network.ErrorUtils;
////import com.karzansoft.mcc.Network.WebServiceFactory;
////import com.karzansoft.mcc.R;
////import com.karzansoft.mcc.Utils.AppUtils;
////import com.karzansoft.mcc.Utils.Constant;
////import com.karzansoft.mcc.Utils.Util;
////
////import retrofit2.Call;
////import retrofit2.Callback;
////import retrofit2.Response;
////
/////**
//// * Created by Yasir on 11/7/2018.
//// */
////
////public class VerificationDialog extends AppCompatDialogFragment implements View.OnClickListener {
////
////    View.OnClickListener clickListener;
////    TextView resendAccessCodeTextView;
////    Button resendAccessCodeButton;
////    Button continueButton;
////    EditText accessCodeEditText;
////    CountDownTimer countDownTimer;
////    ProgressDialog progressDialog;
////    final String sMSLimitExceeded = "SMS limit exceeded, try later.";
////
////    private static VerificationDialog verificationDialog;
////    private static CodeVerificationFregDto codeVerificationFregDto = new CodeVerificationFregDto();
////
////    public static VerificationDialog newInstance(CodeVerificationFregDto codeVerificationFregDto) {
////        VerificationDialog.codeVerificationFregDto.codeLenght = codeVerificationFregDto.codeLenght;
////
////        if (verificationDialog == null) {
////            verificationDialog = new VerificationDialog();
////        }
////
////        Bundle args = new Bundle();
////        verificationDialog.setArguments(args);
////        return verificationDialog;
////    }
////
////    public void setButtonsClickListener(View.OnClickListener clickListener) {
////        this.clickListener = clickListener;
////    }
////
////    @Override
////    public void onCreate(@Nullable Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        progressDialog = new ProgressDialog(getActivity());
////        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////        progressDialog.setMessage("Processing...");
////        progressDialog.setCancelable(false);
////        progressDialog.setMessage(AppUtils.getLocalizeString(getActivity(), "Processing", "Processing..."));
////
////    }
////
////    @Override
////    public Dialog onCreateDialog(Bundle savedInstanceState) {
////
////        LayoutInflater inflater = getActivity().getLayoutInflater();
////        View dialogView = inflater.inflate(R.layout.layout_verification, null);
////        AlertDialog.Builder dialog;
////
////        dialog = new AlertDialog.Builder(getActivity())
////                .setTitle("Activation Code Verification")
////                .setView(dialogView);
////
////        resendAccessCodeTextView = dialogView.findViewById(R.id.resendaccesscodeTextview);
////        resendAccessCodeButton = dialogView.findViewById(R.id.resendaccesscodeButton);
////        continueButton = dialogView.findViewById(R.id.continueButton);
////        accessCodeEditText = dialogView.findViewById(R.id.accesscodeEdittext);
////        accessCodeEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(codeVerificationFregDto.codeLenght)});
////        resendAccessCodeButton.setOnClickListener(this);
////        continueButton.setOnClickListener(this);
////
////        if (LoginActivity.MESSAGE_SENT_COUNTS < Constant.VERIFICATION_CODE_MESSAGE_COUNTS) {
////            startTimer();
////        } else {
////            resendAccessCodeTextView.setVisibility(View.INVISIBLE);
////            resendAccessCodeButton.setVisibility(View.GONE);
////        }
////
////        AlertDialog alertDialog = dialog.create();
////        alertDialog.setCanceledOnTouchOutside(false);
////        return alertDialog;
////    }
////
////    @Override
////    public void onDismiss(DialogInterface dialog) {
////        super.onDismiss(dialog);
////        Util.LogE("onDismiss", "OnDestroyed");
////        try {
////            if (countDownTimer != null) {
////                countDownTimer.cancel();
////            }
////        } catch (Exception ex) {
////            ex.printStackTrace();
////        }
////    }
////
////    private void startTimer() {
////        if (countDownTimer != null) {
////            countDownTimer.cancel();
////        }
////        resendAccessCodeTextView.setVisibility(View.VISIBLE);
////        resendAccessCodeButton.setVisibility(View.GONE);
////
////        countDownTimer = new CountDownTimer(60000, 1000) {
////            public void onTick(long millisUntilFinished) {
////
////                resendAccessCodeTextView.setText("You can resend activation code in" + " " + millisUntilFinished / 1000 + " " + "sec, if it is not received");
////                Util.LogE("Verification Code", "time tick");
////            }
////
////            public void onFinish() {
////                resendAccessCodeTextView.setVisibility(View.GONE);
////                resendAccessCodeButton.setVisibility(View.VISIBLE);
////            }
////        }.start();
////    }
////
////    @Override
////    public void onClick(final View view) {
////
////        switch (view.getId()) {
////            case R.id.resendaccesscodeButton:
////
////                if (LoginActivity.MESSAGE_SENT_COUNTS < Constant.VERIFICATION_CODE_MESSAGE_COUNTS) {
////                    clearFocus(accessCodeEditText);
////                    this.dismiss();
////                    if (clickListener != null) {
////                        clickListener.onClick(view);
////                    }
////                } else {
////                    resendAccessCodeButton.setVisibility(View.INVISIBLE);
////                    AppUtils.showToastMessage(sMSLimitExceeded, getContext());
////                }
////
////                break;
////
////            case R.id.continueButton:
////
////                String enteredCode = accessCodeEditText.getText().toString().trim();
////                if (enteredCode != null && enteredCode.isEmpty() == false) {
////
////                    //send code for verification to server
////                    progressDialog.show();
////
////                    SmsCodeDto sendCodeSMSDto = new SmsCodeDto();
////                    sendCodeSMSDto.setGuid(LoginActivity.Guid);
////                    String Code = accessCodeEditText.getText().toString();
////                    sendCodeSMSDto.setCode(Code);
////
////                    Call<SmsCodeDto> codeSmsResponse = WebServiceFactory.getInstance().verifyCode(sendCodeSMSDto, AppUtils.getAuthToken(getContext()));
////                    codeSmsResponse.enqueue(new Callback<SmsCodeDto>() {
////                        @Override
////                        public void onResponse(Call<SmsCodeDto> call, Response<SmsCodeDto> response) {
////                            progressDialog.dismiss();
////
////                            if (response.isSuccessful() && response.body() != null) {
////
////                                String codeStatus = (String) response.body().getStatus();
////
////                                if (codeStatus == null) {
////                                    Toast.makeText(getContext(), "Code Verification Failed.", Toast.LENGTH_LONG).show();
////                                    return;
////                                }
////
////                                if (codeStatus.equals("success")) {
////                                    AppUtils.showToastMessage("Code Verified.", getActivity());
////                                    clearFocus(accessCodeEditText);
////                                    verificationDialog.dismiss();
////                                    if (clickListener != null) {
////                                        clickListener.onClick(view);
////                                    }
////                                } else if (codeStatus.equals("notMatched")) {
////                                    AppUtils.showToastMessage("Wrong Code.", getActivity());
////                                } else if (codeStatus.equals("codeExpired")) {
////                                    AppUtils.showToastMessage("Code Expired.", getActivity());
////                                } else if (codeStatus.equals("blocked")) {
////                                    AppUtils.showToastMessage("Code blocked, try again.", getActivity());
////                                    verificationDialog.dismiss();
////                                } else if (codeStatus.equals("failed")) {
////                                    AppUtils.showToastMessage("Failed to verify code.", getActivity());
////                                }
////
////                            } else {
////                                String message = "Internal server error!";
////                                APIError error = ErrorUtils.parseError(response);
////                                if (error != null && error.message() != null)
////                                    message = error.message();
////                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
////                            }
////                        }
////
////                        @Override
////                        public void onFailure(Call<SmsCodeDto> call, Throwable t) {
////                            progressDialog.dismiss();
////                            AppUtils.showToastMessage("Failed to verify code.", getActivity());
////                        }
////                    });
////
////                } else {
////                    AppUtils.showToastMessage("Please Enter a code.", getActivity());
////                }
////
////                break;
////        }
////    }
////
////    @Override
////    public void onDestroyView() {
////        super.onDestroyView();
////        Util.LogE("Verification Code", "OnDestroyed");
////    }
////
////    private void clearFocus(View view) {
////        if (view != null && view.hasFocus()) {
////            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
////            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
////        }
////    }
////}
//package com.karzansoft.mcc.Dialogs;
//
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatDialogFragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.karzansoft.mcc.Activities.SignupActivity;
//import com.karzansoft.mcc.R;
//import com.karzansoft.mcc.Utils.AppUtils;
//import com.karzansoft.mcc.Utils.Util;
//
///**
// * Created by Yasir on 11/7/2018.
// */
//
//public class VerificationDialog extends AppCompatDialogFragment implements View.OnClickListener {
//
//    View.OnClickListener clickListener;
//    TextView resendAccessCodeTextView;
//    Button resendAccessCodeButton;
//    Button continueButton;
//    EditText accessCodeEditText;
//    CountDownTimer countDownTimer;
//    //int resendTries ;
//    String randomNumber;
//    ProgressDialog progressDialog;
//
//    public static VerificationDialog newInstance(String randomNumber) {
//        VerificationDialog frag = new VerificationDialog();
//        Bundle args = new Bundle();
//        args.putString("RANDOM_NUMBER",randomNumber);
//        frag.setArguments(args);
//        return frag;
//    }
//
//    public void setButtonsClickListener(View.OnClickListener clickListener)
//    {
//        this.clickListener=clickListener;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        progressDialog=new ProgressDialog(getActivity());
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setMessage("Processing...");
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage(AppUtils.getLocalizeString(getActivity(),"Processing","Processing..."));
//
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        randomNumber = this.getArguments().getString("RANDOM_NUMBER");
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        //languageTexts= AppUtils.getLanguageStrings(getActivity());
//        View dialogView= inflater.inflate(R.layout.layout_verification, null);
//
//        AlertDialog.Builder dialog;
//
//
//        dialog = new AlertDialog.Builder(getActivity())
//
//                .setTitle("Activation Code Verification")
//                .setView(dialogView);
//
//        resendAccessCodeTextView =dialogView.findViewById(R.id.resendaccesscodeTextview);
//        resendAccessCodeButton = dialogView.findViewById(R.id.resendaccesscodeButton);
//        continueButton = dialogView.findViewById(R.id.continueButton);
//        accessCodeEditText = dialogView.findViewById(R.id.accesscodeEdittext);
//
//        resendAccessCodeButton.setOnClickListener(this);
//        continueButton.setOnClickListener(this);
//        if (SignupActivity.NUMBER_OF_RETRIES <=2)
//        {
//            startTimer();
//        }else {
//            resendAccessCodeTextView.setVisibility(View.INVISIBLE);
//            resendAccessCodeButton.setVisibility(View.GONE);
//        }
//        AlertDialog alertDialog = dialog.create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        return alertDialog;
//    }
//
//
//
//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//        Util.LogE("onDismiss","OnDestroyed");
//        try {
//            if (countDownTimer != null) {
//                countDownTimer.cancel();
//            }
//        }catch (Exception ex){ex.printStackTrace();}
//    }
//
//
//
//    private void startTimer()
//    {
//        if(countDownTimer!=null)
//        {
//            countDownTimer.cancel();
//        }
//        resendAccessCodeTextView.setVisibility(View.VISIBLE);
//        resendAccessCodeButton.setVisibility(View.GONE);
//
//        countDownTimer = new CountDownTimer(60000, 1000) {
//            public void onTick(long millisUntilFinished) {
//
//                resendAccessCodeTextView.setText("You can resend activation code in"+ " " + millisUntilFinished / 1000 + " " +"sec, if it is not received");
//                Util.LogE("Verification Code","time tick");
//            }
//
//            public void onFinish() {
//                resendAccessCodeTextView.setVisibility(View.GONE);
//                resendAccessCodeButton.setVisibility(View.VISIBLE);
//            }
//        }.start();
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId())
//        {
//            case R.id.resendaccesscodeButton:
//                //SignupActivity.NUMBER_OF_RETRIES +=1;
//
//                if ( SignupActivity.NUMBER_OF_RETRIES <=2)
//                {
//                    //startTimer();
//
//                    clearFocus(accessCodeEditText);
//                    this.dismiss();
//                    if (clickListener!=null)
//                    {
//                        clickListener.onClick(view);
//                    }
//                }
//                else {
//                    resendAccessCodeButton.setVisibility(View. INVISIBLE);
//                }
//                break;
//            case R.id.continueButton:
//
//                if (randomNumber.equalsIgnoreCase(accessCodeEditText.getText().toString().trim()))
//                {
//                    clearFocus(accessCodeEditText);
//                    this.dismiss();
//                    if (clickListener!=null)
//                    {
//                        clickListener.onClick(view);
//                    }
//                }else {
//
//                    AppUtils.showToastMessage("Invalid activation code",getActivity());
//                }
//
//
//                break;
//        }
//
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Util.LogE("Verification Code","OnDestroyed");
//    }
//
//
//
//    private void clearFocus(View view)
//    {
//        if (view != null && view.hasFocus()) {
//            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//}
