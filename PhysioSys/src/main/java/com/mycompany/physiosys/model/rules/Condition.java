package com.mycompany.physiosys.model.rules;

import com.mycompany.physiosys.model.knowledge.KB;
import com.mycompany.physiosys.model.knowledge.items.KnowledgeItem;

public class Condition<T> {

    protected final String knowledgeItemName;
    private final T value;
    private final boolean equal;

    public Condition(String knowledgeItemName, T value) {
        this(knowledgeItemName, value, true);
    }

    public Condition(String knowledgeItemName, T value, boolean equal) {
        this.knowledgeItemName = knowledgeItemName;
        this.value = value;
        this.equal = equal;
    }
    public String getKnowledgeItemName() {
        return knowledgeItemName;
    }

    public Boolean check(KB KB) {
        KnowledgeItem item = KB.getItem(knowledgeItemName);
        if (item == null) {
            return null;
        }
        if (!item.isValueSet()) {
            return null;
        }
        System.out.println("Values: " + value + " (expected), " + item.getValue() + " (actual)");
        if (equal) {
            return item.getValue().equals(value);
        }
        return !item.getValue().equals(value);
    }

    public T getValue() {
        return value;
    }

    public boolean isEqual() {
        return equal;
    }

}
