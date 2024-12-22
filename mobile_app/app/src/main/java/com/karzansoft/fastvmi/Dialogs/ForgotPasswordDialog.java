//package com.karzansoft.mcc.Dialogs;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.design.widget.TextInputLayout;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatDialogFragment;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.karzansoft.mcc.Adapters.ModelSearchAdapter;
//import com.karzansoft.mcc.Models.VehicleModel;
//import com.karzansoft.mcc.R;
//import com.karzansoft.mcc.Utils.AppUtils;
//import com.karzansoft.mcc.Utils.Util;
//
///**
// * Created by Yasir on 7/21/2016.
// */
//public class ForgotPasswordDialog extends AppCompatDialogFragment implements View.OnClickListener{
//    View.OnClickListener clickListener;
//    public EditText email;
//    TextInputLayout emailContainer;
//    Button btn_cancel,btn_save;
//
//    public static ForgotPasswordDialog newInstance() {
//        ForgotPasswordDialog frag = new ForgotPasswordDialog();
//        return frag;
//    }
//
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        View dialogView= inflater.inflate(R.layout.forgot_password, null);
//
//        AlertDialog.Builder dialog;
//
//
//            dialog= new AlertDialog.Builder(getActivity())
//
//                    .setTitle("Forgot Password?")
//                    .setView(dialogView);
//
//        email=(EditText)dialogView.findViewById(R.id.email);
//        emailContainer=(TextInputLayout)dialogView.findViewById(R.id.emailContainer);
//        btn_cancel=(Button)dialogView.findViewById(R.id.cancel);
//        btn_save=(Button)dialogView.findViewById(R.id.submit);
//        btn_cancel.setOnClickListener(this);
//        btn_save.setOnClickListener(this);
//
//
//        return dialog.create();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId())
//        {
//            case R.id.cancel:
//                clearFocus(email);
//                dismiss();
//                break;
//            case R.id.submit:
//                if( !validateField(emailContainer))
//                    return ;
//                if( !validateEmail(emailContainer))
//                    return ;
//                clearFocus(email);
//                dismiss();
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
//
//        }
//        return true;
//    }
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
//    private void requestFocus(View view) {
//        if (view.requestFocus()) {
//            //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }
//    private void clearFocus(View view)
//    {
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//}
