package com.karzansoft.fastvmi.Models;

import java.util.ArrayList;
import java.util.List;

public class QuestionsTemplate {

    String detail, value, guid;
    int type, category;
    ArrayList<String> choices;
    List<QuestionsTemplate> children;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public List<QuestionsTemplate> getChildren() {
        return children;
    }

    public void setChildren(List<QuestionsTemplate> children) {
        this.children = children;
    }
}


