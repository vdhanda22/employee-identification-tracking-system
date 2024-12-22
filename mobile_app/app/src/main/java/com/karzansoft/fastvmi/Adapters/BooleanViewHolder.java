package com.karzansoft.fastvmi.Adapters;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.karzansoft.fastvmi.R;


public class BooleanViewHolder extends RecyclerView.ViewHolder {

    private TextView question;
    private CheckBox checkBox;

    public BooleanViewHolder(View v) {
        super(v);
        question = (TextView) v.findViewById(R.id.booleanquestion);
        checkBox = (CheckBox) v.findViewById(R.id.checkboxb);

    }

    public TextView getQuestion() {
        return question;
    }

    public void setQuestion(TextView question) {
        this.question = question;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

}
