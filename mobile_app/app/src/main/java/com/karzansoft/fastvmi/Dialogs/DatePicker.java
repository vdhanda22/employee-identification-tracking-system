package com.karzansoft.fastvmi.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * Created by Yasir on 8/15/2016.
 */
public class DatePicker extends AppCompatDialogFragment {
    DatePickerDialog.OnDateSetListener dateSetListener;
    int day,month,year;
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        day = args.getInt("day");
        month = args.getInt("month");
        year=args.getInt("year");

    }

    public void setCallBack(DatePickerDialog.OnDateSetListener ondateSet) {
        dateSetListener= ondateSet;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
       }

}
