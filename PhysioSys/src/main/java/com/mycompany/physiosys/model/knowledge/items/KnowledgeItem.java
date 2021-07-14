package com.mycompany.physiosys.model.knowledge.items;

import com.mycompany.physiosys.model.knowledge.Origin;

public abstract class KnowledgeItem<T> {

    protected final String name;
    protected final Origin origin;
    protected final boolean goal;
    protected final String type;

    protected T value;
    protected String question;
    protected String tip;
    protected String goalResponse;


    public KnowledgeItem(String name, Origin origin, boolean goal) {
        this.name = name;
        this.origin = origin;
        this.goal = goal;
        this.type = getType();

        this.value = null;
        this.question = null;
        this.tip = null;
        this.goalResponse = null;
    }

    public KnowledgeItem(String name, T value, Origin origin, boolean goal) {
        this(name, origin, goal);
        setValue(value);
    }

    public KnowledgeItem(KnowledgeItem<T> item) {
        this(item.name, item.origin, item.goal);
        setValue(item.value);
        setQuestion(item.question);
        setTip(item.tip);
        setGoalResponse(item.goalResponse);
    }

    public abstract String getType();

    public abstract KnowledgeItem<T> copy();

    public final KnowledgeItem<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public final KnowledgeItem<T> setQuestion(String question) {
        this.question = question;
        return this;
    }

    public final KnowledgeItem<T> setTip(String tip) {
        this.tip = tip;
        return this;
    }

    public final KnowledgeItem<T> setGoalResponse(String goalResponse) {
        this.goalResponse = goalResponse;
        return this;
    }

    public String getName() {
        return name;
    }

    public Origin getOrigin() {
        return origin;
    }

    public boolean isGoal() {
        return goal;
    }

    public T getValue() {
        return value;
    }

    public boolean isValueSet() {
        return value != null;
    }

    public String getQuestion() {
        return question;
    }

    public String getTip() {
        return tip;
    }

    public boolean hasTip() {
        return tip != null;
    }

    public String getGoalResponse() {
        if (goalResponse == null) {
            return getDefaultGoalResponse();
        }
        String result = goalResponse;
        return result;
    }

    public String getDefaultGoalResponse() {
        return "Value of goal variable " + name + " is: " + value;
    }

}
