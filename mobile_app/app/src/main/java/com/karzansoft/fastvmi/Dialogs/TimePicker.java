package com.karzansoft.fastvmi.Dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * Created by Yasir on 8/15/2016.
 */
public class TimePicker extends AppCompatDialogFragment {
    TimePickerDialog.OnTimeSetListener ontimeSet;
    private int hours, minutes;
    public void setCallBack(TimePickerDialog.OnTimeSetListener ontime) {
        ontimeSet = ontime;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        hours = args.getInt("hours");
        minutes = args.getInt("minutes");

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new TimePickerDialog(getActivity(),ontimeSet,hours,minutes,false);
    }
}
