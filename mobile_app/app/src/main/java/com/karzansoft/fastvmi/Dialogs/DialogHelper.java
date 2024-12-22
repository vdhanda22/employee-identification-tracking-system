package com.karzansoft.fastvmi.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;

/**
 * Created by Yasir on 3/3/2017.
 */
public class DialogHelper {


    public static void showReportsDialog(Context context)
    {
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =  inflater.inflate(R.layout.reports_dialog, null);
        final TextView companyCode=(TextView)view.findViewById(R.id.company_code);
        TextView info=(TextView)view.findViewById(R.id.info);
        TextView pass=(TextView)view.findViewById(R.id.pass);
        info.setText(AppUtils.getLocalizeString(context, "ForReportAndOtherFeaturesPleaseVisitWeb", "" + info.getText().toString()));
        companyCode.setText(AppUtils.getLocalizeString(context, "Email", "Email") + ": " + AppUtils.getUserName(context));
        pass.setText(AppUtils.getLocalizeString(context, "Password", "Password") + ": <" + AppUtils.getLocalizeString(context, "YourMobileApppassword", "your Mobile App password") + ">");
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle(AppUtils.getLocalizeString(context,"Reports","Reports"));
        builder.setView(view);
        builder.setPositiveButton(AppUtils.getLocalizeString(context,"OK","OK"), null);
        builder.show();
    }

    public static void showAlert(Context context, String title, String msg, DialogInterface.OnClickListener listner)
    {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(AppUtils.getLocalizeString(context,"OK","OK"), listner);
        builder.show();
    }

}
