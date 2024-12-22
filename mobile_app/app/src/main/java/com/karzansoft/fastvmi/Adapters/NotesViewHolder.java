package com.karzansoft.fastvmi.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.karzansoft.fastvmi.R;

public class NotesViewHolder extends RecyclerView.ViewHolder {
    private TextView Notes;
    private EditText value;

    public NotesViewHolder(View v) {
        super(v);
        Notes = (TextView) v.findViewById(R.id.notes);
        value = (EditText) v.findViewById(R.id.notesvalue);
    }

    public TextView getNotes() {
        return Notes;
    }

    public void setNotes(TextView question) {
        this.Notes = Notes;
    }

    public EditText getValue() {
        return value;
    }

    public void setValue(EditText value) {
        this.value = value;
    }
}
