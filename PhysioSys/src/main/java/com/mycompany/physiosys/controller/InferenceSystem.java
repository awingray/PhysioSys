package com.mycompany.physiosys.controller;

import com.mycompany.physiosys.model.knowledge.KB;
import com.mycompany.physiosys.model.knowledge.items.KnowledgeItem;
import com.mycompany.physiosys.model.rules.Rule;
import com.mycompany.physiosys.model.rules.RuleBase;
import com.mycompany.physiosys.model.rules.Condition;
import com.mycompany.physiosys.view.View;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

public class InferenceSystem implements Observer {

    private final RuleBase ruleBase;
    private final KB KB;

    private int currentCounter;
    private boolean goalFound;
    private boolean goalReachable;
    private KnowledgeItem goalItem;
    private View view;

    public InferenceSystem(RuleBase ruleBase, KB KB, View view) {
        this.ruleBase = ruleBase;
        this.KB = KB;
        this.view = view;
        this.currentCounter = 0;
        this.goalFound = false;
        this.goalReachable = true;
        this.goalItem = null;
    }

    public void start() {
        System.out.println("Starting inference...");
        printRB();
        while (goalReachable) {
            printKB();
            forwardChainingWithoutInput();
            if (goalFound) {
                // break out of the loop when the goal is found
                break;
            }
            backwardChainingWithInput();
        }
        view.showResult(goalItem, KB);
    }

    private void printRB() {
        System.out.println("RB: ");
        int ruleCnt = 1;
        for (Rule rule : ruleBase) {
            System.out.println("rule " + ruleCnt);
            System.out.println(" conditions:");
            for (Condition condition : rule.getConditions()) {
                System.out.println(" - " + condition.getKnowledgeItemName());
                if (condition instanceof Condition) {
                    Condition eqCondition = condition;
                    System.out.println("   " + eqCondition.getValue() + " (" + eqCondition.isEqual() + ")");
                }
            }
            System.out.println(" concequence: " + rule.getConsequence().getName() + " (" + rule.getConsequence().getType() + "): " + rule.getConsequence().getValue());
            ruleCnt++;
        }
    }

    private void printKB() {
        System.out.println("KB:");
        for (KnowledgeItem item : KB) {
            System.out.println(item.getName() + " (" + item.getType() + "): " + item.getValue());
        }
    }

    private void forwardChainingWithoutInput() {
        // loop using forward chaining to see if rules can be applied without user input (continue looping until none of the rules can be applied anymore)
        int checkedRuleCounter = 0;
        for (; checkedRuleCounter < ruleBase.size(); currentCounter = (currentCounter + 1) % ruleBase.size()) {
            Rule rule = ruleBase.getRule(currentCounter);
            System.out.println("[ctrl]  Checking conditions for rule " + currentCounter + " (" + rule.getConsequence().getName() +")");
            if (rule.checkConditions(KB, false)) {
                checkedRuleCounter = 0;
                KnowledgeItem item = rule.getConsequence();
                System.out.println("[ctrl]   Rule applied. New value of " + item.getName() + ": " + item.getValue());
                KB.setItem(item);
                if (item.isGoal()) {
                    goalFound = true;
                    goalItem = item;
                    return;
                }
            } else {
                System.out.println("[ctrl]   Rule not applicable without user input");
                checkedRuleCounter++;
            }
        }
    }

    private void backwardChainingWithInput() {
        Queue<Rule> ruleQueue = new LinkedList<>();

        // initialize queue with rules towards goals
        KB.stream().filter((item) -> item.isGoal()).forEach((item) -> {
            ruleBase.stream().filter((rule) -> rule.getConsequence().getName().equals(item.getName())).forEach((rule) -> {
                System.out.println("Add rule for item " + item.getName());
                ruleQueue.add(rule);
            });
        });

        System.out.println("Start bfs");
        // walk through the queue with breadth first search
        Rule currentRule = ruleQueue.poll();
        while (currentRule != null) {
            System.out.println("Current rule consequence: " + currentRule.getConsequence().getName());
            if (currentRule.checkConditions(KB, true)) {
                askQuestion(currentRule);
                return;
            } else {
                for (Condition condition : currentRule.getConditions()) {
                    ruleBase.stream().filter((rule) -> rule.getConsequence().getName().equals(condition.getKnowledgeItemName())).forEach((rule) -> {
                        System.out.println("Add rule for item " + condition.getKnowledgeItemName());
                        ruleQueue.add(rule);
                    });
                }
            }

            // go to next rule
            currentRule = ruleQueue.poll();
        }
        goalReachable = false;
    }

    private void askQuestion(Rule rule) {
        System.out.println("[ctrl]  Updating rule with user data where needed");
        for (Condition condition : rule.getConditions()) {
            KnowledgeItem item = KB.getItem(condition.getKnowledgeItemName());
            if (item != null && !item.isValueSet()) {
                System.out.println("[ctrl] Condition with value origin " + item.getOrigin() + ", value is " + (item.isValueSet() ? "" : "not ") + "set");
                switch (item.getOrigin()) {
                    case INFERRED:
                    case GIVEN:
                        break;
                    default:
                        KnowledgeItem newItem = view.inquire(item.copy());
                        System.out.println("Value of " + newItem.getName() + ": " + newItem.getValue());
                        KB.setItem(newItem);
                        return;
                }
            }
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        backwardChainingWithInput();
    }
}
