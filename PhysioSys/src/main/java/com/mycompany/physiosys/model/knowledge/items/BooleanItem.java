package com.mycompany.physiosys.model.knowledge.items;

import com.mycompany.physiosys.model.knowledge.Origin;

public class BooleanItem extends KnowledgeItem<Boolean> {

    public BooleanItem(String name, Origin origin, boolean goal) {
        super(name, origin, goal);
    }

    public BooleanItem(BooleanItem item) {
        super(item);
    }

    @Override
    public String getType() {
        return "boolean";
    }

    @Override
    public BooleanItem copy() {
        return new BooleanItem(this);
    }
}
