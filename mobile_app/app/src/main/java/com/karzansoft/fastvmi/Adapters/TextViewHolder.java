package com.karzansoft.fastvmi.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.karzansoft.fastvmi.R;

public class TextViewHolder extends RecyclerView.ViewHolder {
    private TextView question;

    public TextViewHolder(View v) {
        super(v);
        question = (TextView) v.findViewById(R.id.textviewgroup);
    }

    public TextView getQuestion() {
        return question;
    }

    public void setQuestion(TextView question) {
        this.question = question;
    }
}
