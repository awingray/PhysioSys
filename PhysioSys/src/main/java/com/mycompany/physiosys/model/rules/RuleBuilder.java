package com.mycompany.physiosys.model.rules;

import com.mycompany.physiosys.model.knowledge.items.KnowledgeItem;

import java.util.ArrayList;
import java.util.List;

public class RuleBuilder {

    private List<Condition> conditions;
    private KnowledgeItem consequence;

    public RuleBuilder(KnowledgeItem consequence) {
        conditions = new ArrayList<>();
        this.consequence = consequence;

    }

    public RuleBuilder addCondition(Condition condition) {
        conditions.add(condition);
        return this;
    }

    public Rule build() {
        return new Rule(conditions, consequence);
    }

}
