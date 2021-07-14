package com.mycompany.physiosys.model.rules;

import com.mycompany.physiosys.model.knowledge.KB;
import com.mycompany.physiosys.model.knowledge.items.KnowledgeItem;

import java.util.List;

public class Rule {

    private final List<Condition> conditions;
    private final KnowledgeItem consequence;

    public Rule(List<Condition> conditions, KnowledgeItem consequence) {
        this.conditions = conditions;
        this.consequence = consequence;
    }

    public boolean checkConditions(KB KB, boolean userInput) {
        if (!userInput) {
            return checkWithoutInput(KB);
        } else {
            return checkWithInput(KB);
        }
    }

    private boolean checkWithInput(KB KB) {
        System.out.println("Checking " + consequence.getName() + ":");
        if (KB.getItem(consequence.getName()).isValueSet()) {
            System.out.println("x Concequence already set");
            return false;
        }
        System.out.println("- Concequence not yet set");
        System.out.println("- Checking conditions");
        for (Condition condition : conditions) {
            System.out.println(" - check value of " + condition.getKnowledgeItemName());
            Boolean check = condition.check(KB);
            if (check != null && check) {
                System.out.println("  + Condition met");
                continue;
            }
            System.out.println("  - Condition not met");
            System.out.println(" - Check if value is set and its (expected) origin");
            if (check != null) {
                System.out.println("  x Value already set");
                return false;
            }
            KnowledgeItem item = KB.getItem(condition.getKnowledgeItemName());
            if (item == null) {
                System.out.println(" x Item not available");
                return false;
            }
            System.out.println("  - Value not set");
            switch (item.getOrigin()) {
                case GIVEN:
                case INFERRED:
                    System.out.println("  x Given or inferred value origin expected");
                    return false;
                default:
                    System.out.println("  + Correct value origin expected");

            }
        }
        return true;
    }

    private boolean checkWithoutInput(KB KB) {
        if (KB.getItem(consequence.getName()).isValueSet()) {
            System.out.println("[ctrl] value of " + consequence.getName() + " already set");
            return false;
        }
        for (Condition condition : conditions) {
            Boolean check = condition.check(KB);
            if (check == null || !check) {
                System.out.println("Condition " + condition.getKnowledgeItemName() + ": knowledge item is not set or not the correct value");
                return false;
            }
            System.out.println("Condition " + condition.getKnowledgeItemName() + " is met");
        }
        return true;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public KnowledgeItem getConsequence() {
        return consequence;
    }

}
