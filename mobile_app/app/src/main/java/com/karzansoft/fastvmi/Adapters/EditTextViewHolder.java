package com.karzansoft.fastvmi.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.karzansoft.fastvmi.R;

public class EditTextViewHolder extends RecyclerView.ViewHolder {
    private TextView question;
    private EditText value;

    public EditTextViewHolder(View v) {
        super(v);
        question = (TextView) v.findViewById(R.id.edittextquestion);
        value = (EditText) v.findViewById(R.id.edittextvalue);
    }

    public TextView getQuestion() {
        return question;
    }

    public void setQuestion(TextView question) {
        this.question = question;
    }

    public EditText getValue() {
        return value;
    }

    public void setValue(EditText value) {
        this.value = value;
    }
}
