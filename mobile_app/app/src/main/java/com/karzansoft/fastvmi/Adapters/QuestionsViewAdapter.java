package com.karzansoft.fastvmi.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.karzansoft.fastvmi.Models.QuestionsTemplate;
import com.karzansoft.fastvmi.R;

import java.util.List;

public class QuestionsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int group = 0, boolen = 1, dropdown = 2, editext = 3, notes = 4;
    RecyclerView childview;
    // The questions to display in your RecyclerView
    private List<QuestionsTemplate> questions;
    private Context mcontext;

    public QuestionsViewAdapter(Context Context, List<QuestionsTemplate> questions) {
        this.questions = questions;
        this.mcontext = Context;
    }

    @Override
    public int getItemCount() {
        return this.questions.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (questions.get(position).getCategory() == 1)
            return group;
        else if (questions.get(position).getType() == 1)
            return boolen;
        else if (questions.get(position).getType() == 2)
            return dropdown;
        else if (questions.get(position).getType() == 3)
            return editext;
        else if (questions.get(position).getType() == 4)
            return notes;
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case boolen:
                View v1 = inflater.inflate(R.layout.booleanviewholder, viewGroup, false);
                viewHolder = new BooleanViewHolder(v1);
                break;
            case dropdown:
                View v2 = inflater.inflate(R.layout.dropdownviewholder, viewGroup, false);
                viewHolder = new dropdownviewholder(v2);
                break;
            case editext:
                View v3 = inflater.inflate(R.layout.edittextlayout, viewGroup, false);
                viewHolder = new EditTextViewHolder(v3);
                break;
            case notes:
                View v5 = inflater.inflate(R.layout.notesviewholder, viewGroup, false);
                viewHolder = new NotesViewHolder(v5);
                break;
            case group:
                View v4 = inflater.inflate(R.layout.textviewholder, viewGroup, false);
                childview = v4.findViewById(R.id.childrecyclerView);
                viewHolder = new TextViewHolder(v4);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                viewHolder = new BooleanViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case boolen:
                BooleanViewHolder vh1 = (BooleanViewHolder) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case dropdown:
                dropdownviewholder vh2 = (dropdownviewholder) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            case editext:
                EditTextViewHolder vh3 = (EditTextViewHolder) viewHolder;
                configureViewHolder3(vh3, position);
                break;
            case notes:
                NotesViewHolder vh5 = (NotesViewHolder) viewHolder;
                configureViewHolder5(vh5, position);
                break;
            case group:
                TextViewHolder vh4 = (TextViewHolder) viewHolder;
                configureViewHolder4(vh4, position);
                break;
        }
    }


    private void configureViewHolder1(final BooleanViewHolder booleanquestion, int position) {
        final QuestionsTemplate question = (QuestionsTemplate) questions.get(position);
        if (question != null) {
            booleanquestion.getQuestion().setText(question.getDetail());
            if (question.getValue() == "true")
                booleanquestion.getCheckBox().setChecked(true);
            else {
                question.setValue("false");
                booleanquestion.getCheckBox().setChecked(false);
            }
            booleanquestion.getCheckBox().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (booleanquestion.getCheckBox().isChecked())
                        question.setValue("true");
                    else
                        question.setValue("false");
                }
            });
        }
    }

    private void configureViewHolder2(final dropdownviewholder dropdownview, int position) {
        final QuestionsTemplate question = (QuestionsTemplate) questions.get(position);
        if (question != null) {
            if (question.getChoices().get(0) != "" && question.getChoices().get(0) != "Choose an item ")
                question.getChoices().add(0, "Choose an item ");
            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                    mcontext, R.layout.support_simple_spinner_dropdown_item, question.getChoices()) {
                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            dropdownview.getQuestion().setText(question.getDetail());
            dropdownview.getOptions().setAdapter(spinnerArrayAdapter);
            if (question.getValue() != null) {
                dropdownview.getOptions().setSelection(spinnerArrayAdapter.getPosition(question.getValue()));
                dropdownview.getOptions().setFocusable(true);
            }
            dropdownview.getOptions().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    question.setValue(dropdownview.getOptions().getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        }
    }

    private void configureViewHolder3(final EditTextViewHolder editTextQuestion, int position) {
        final QuestionsTemplate question = (QuestionsTemplate) questions.get(position);
        if (question != null) {
            editTextQuestion.getQuestion().setText(question.getDetail());
            if (question.getValue() != null && !question.getValue().isEmpty()) {
                editTextQuestion.getValue().setText(question.getValue());
                editTextQuestion.getValue().setFocusable(true);
            } else {
                question.setValue(null);
            }
            editTextQuestion.getValue().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    question.setValue(editTextQuestion.getValue().getText().toString());
                }
            });
        }
    }

    private void configureViewHolder4(final TextViewHolder group, int position) {
        final QuestionsTemplate question = (QuestionsTemplate) questions.get(position);
        if (question != null) {
            group.getQuestion().setText(question.getDetail());
            if (!question.getChildren().equals(null)) {
                List<QuestionsTemplate> childernlist = question.getChildren();
                childview.setAdapter(new QuestionsViewAdapter(mcontext, childernlist));
                RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(mcontext, R.drawable.divider));
                childview.addItemDecoration(dividerItemDecoration);
                childview.setLayoutManager(new LinearLayoutManager(mcontext));

            }
        }
    }

    private void configureViewHolder5(final NotesViewHolder Notes, int position) {
        final QuestionsTemplate question = (QuestionsTemplate) questions.get(position);
        if (question != null) {
            if (question.getValue() != null && !question.getValue().isEmpty()) {
                Notes.getValue().setText(question.getValue());
                Notes.getValue().setFocusable(true);
            } else {
                question.setValue(null);
            }
            Notes.getValue().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    question.setValue(Notes.getValue().getText().toString());
                }
            });
        }
    }
}
