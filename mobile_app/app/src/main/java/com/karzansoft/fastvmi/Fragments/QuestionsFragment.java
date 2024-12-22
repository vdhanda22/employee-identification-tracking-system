package com.karzansoft.fastvmi.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karzansoft.fastvmi.Adapters.QuestionsViewAdapter;
import com.karzansoft.fastvmi.Models.InspectionDto;
import com.karzansoft.fastvmi.Models.QuestionsTemplate;
import com.karzansoft.fastvmi.Network.Entities.Response.GetQuestionTemplateResponse;
import com.karzansoft.fastvmi.R;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class QuestionsFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView mainview;
    Button mNext;
    List<QuestionsTemplate> nestedQuestionswithgroups;
    int position;
    InspectionDto inspectionquestions;

    public QuestionsFragment() {
        // Required empty public constructor
    }

    public static QuestionsFragment newInstance(GetQuestionTemplateResponse QuestionTemplate, InspectionDto inspectionDto) {
        QuestionsFragment f = new QuestionsFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        args.putLong("id", QuestionTemplate.getId());
        args.putString("name", QuestionTemplate.getName());
        args.putString("questiontemplate", QuestionTemplate.getTemplate());
        args.putString("inspectionDto", gson.toJson(inspectionDto));
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.questions_select, container, false);
        rootView.setBackgroundColor(Color.WHITE);
        mainview = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mNext = (Button) rootView.findViewById(R.id.qa_action_next);
        mainview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<QuestionsTemplate>>() {
        }.getType();
        nestedQuestionswithgroups = gson.fromJson(getArguments().getString("questiontemplate"), collectionType);
        inspectionquestions = gson.fromJson(getArguments().getString("inspectionDto"), InspectionDto.class);
        mNext.setOnClickListener(this);
        bindDataToAdapter(position);
        return rootView;
    }

    private void bindDataToAdapter(int position) {
        List<QuestionsTemplate> questions = nestedQuestionswithgroups.get(position).getChildren();
        getActivity().setTitle(nestedQuestionswithgroups.get(position).getDetail());
        mainview.setAdapter(new QuestionsViewAdapter(getActivity(), questions));
        mainview.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public boolean onBackPressed() {
        if (position > 0) {
            position--;
            bindDataToAdapter(position);
            return true;
        } else
            return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qa_action_next:
                if (position < nestedQuestionswithgroups.size() - 1) {
                    for (int i = 0; i < nestedQuestionswithgroups.get(position).getChildren().size(); i++) {
                        QuestionsTemplate questions = nestedQuestionswithgroups.get(position).getChildren().get(i);
                        if (questions.getCategory() == 2) {
                            if (questions.getValue() == "Choose an item " && !questions.getChoices().isEmpty()) {
                                Toast.makeText(getActivity(), "Select all Items", Toast.LENGTH_LONG).show();
                                return;
                            }
                        } else if (questions.getCategory() == 1) {
                            Log.wtf("hy", String.valueOf(questions.getChildren().size()));

                            for (int j = 0; j < questions.getChildren().size(); j++) {
                                QuestionsTemplate questionsTemplate = questions.getChildren().get(j);
                                if (questionsTemplate.getValue() == "Choose an item " && !questionsTemplate.getChoices().isEmpty()) {
                                    Toast.makeText(getActivity(), "Select all Items", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        }
                    }
                    position++;
                    bindDataToAdapter(position);
                } else if (position == nestedQuestionswithgroups.size() - 1) {
                    setquestionsdata();
                    InspectionNotesFragment fragment = InspectionNotesFragment.newInstance(inspectionquestions);
                    addFragment(fragment, fragment.getClass().getName());
                }
                break;
        }

    }

    private boolean nestedquestions(QuestionsTemplate questions) {
        List<QuestionsTemplate> listquestions = questions.getChildren();
        for (int j = 0; j < listquestions.size(); j++) {
            if (listquestions.get(j).getValue() == null && !listquestions.get(j).getChoices().isEmpty()) {
                Toast.makeText(getActivity(), "Select all options", Toast.LENGTH_LONG);
                return false;
            }
        }
        return true;
    }

    private void setquestionsdata() {
        Gson gson = new Gson();
        inspectionquestions.setSafetyChecks(gson.toJson(nestedQuestionswithgroups));
    }

}
