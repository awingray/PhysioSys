package com.mycompany.physiosys.model.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class RuleBase implements Iterable<Rule> {

    private final List<Rule> rules;

    public RuleBase() {
        this.rules = new ArrayList<>();
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    @Override
    public Iterator<Rule> iterator() {
        return rules.iterator();
    }

    public Stream<Rule> stream() {
        return rules.stream();
    }

    public int size() {
        return rules.size();
    }

    public Rule getRule(int index) {
        return rules.get(index);
    }

}
