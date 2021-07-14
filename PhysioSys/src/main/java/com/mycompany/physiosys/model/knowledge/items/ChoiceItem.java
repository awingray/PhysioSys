package com.mycompany.physiosys.model.knowledge.items;

import com.mycompany.physiosys.model.knowledge.Origin;
import java.util.List;

public class ChoiceItem extends KnowledgeItem<Integer> {

    protected final List<String> options;

    public ChoiceItem(String name, List<String> options, Origin origin, boolean goal) {
        super(name, origin, goal);
        this.options = options;
    }

    public ChoiceItem(ChoiceItem item) {
        super(item);
        this.options = item.options;
    }

    public List<String> getOptions() {
        return options;
    }


    public Integer getIndex(String value) {
        return options.indexOf(value);
    }

    @Override
    public String getType() {
        return "choice";
    }


    @Override
    public ChoiceItem copy() {
        return new ChoiceItem(this);
    }

}
