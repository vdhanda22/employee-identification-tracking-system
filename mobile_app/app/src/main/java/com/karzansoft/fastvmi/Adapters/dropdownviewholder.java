package com.karzansoft.fastvmi.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.karzansoft.fastvmi.R;


public class dropdownviewholder extends RecyclerView.ViewHolder {

    private TextView question;
    private Spinner options;

    public dropdownviewholder(View v) {
        super(v);
        question = (TextView) v.findViewById(R.id.dropdownquestion);
        options = (Spinner) v.findViewById(R.id.options);
    }

    public TextView getQuestion() {
        return question;
    }

    public void setQuestion(TextView question) {
        this.question = question;
    }

    public Spinner getOptions() {
        return options;
    }

    public void setOptions(Spinner options) {
        this.options = options;
    }

}
