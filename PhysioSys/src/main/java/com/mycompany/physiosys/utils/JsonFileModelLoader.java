package com.mycompany.physiosys.utils;

import com.mycompany.physiosys.model.knowledge.KB;
import com.mycompany.physiosys.model.knowledge.Origin;
import com.mycompany.physiosys.model.knowledge.items.BooleanItem;
import com.mycompany.physiosys.model.knowledge.items.ChoiceItem;
import com.mycompany.physiosys.model.knowledge.items.KnowledgeItem;
import com.mycompany.physiosys.model.rules.RuleBase;
import com.mycompany.physiosys.model.rules.RuleBuilder;
import com.mycompany.physiosys.model.rules.Condition;
import com.mycompany.physiosys.utils.json.JsonFile;
import com.mycompany.physiosys.utils.json.JsonNode;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonFileModelLoader extends ModelLoader {

    private final Map<String, List> listsMap;
    protected final String fileName;

    private static final Logger log = Logger.getLogger(JsonFileModelLoader.class.getName());

    public JsonFileModelLoader(String fileName) {
        this.fileName = fileName;
        this.listsMap = new HashMap<>();
    }

    public void load(RuleBase ruleBase, KB KB) {
        try {
            log.info("Open Json file");
            JsonFile knowledge = new JsonFile(fileName);
            log.info("Get the Json root node");
            JsonNode root = knowledge.getRootNode();

            // Read the different options list from the file
            // Store these in the HashMap
            setLists(root);

            // Read all the questions from the file
            // Add these to the Knowledge Base (KB)
            setQuestions(root, KB);

            // Read all the goals from the file
            // Add these to the KB
            setGoals(root, KB);

            // Read all the rules from the file
            // Add these to the RuleBase (RB)
            // Add previously unknown knowledge items to the KB
            setRules(root, KB, ruleBase);

        } catch (IOException ex) {
            Logger.getLogger(JsonFileModelLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setLists(JsonNode root) {
        List<JsonNode> lists = root.getNodeList("lists").getNodes();

        for (JsonNode list : lists) {
            String name = list.getString("name");
            List<String> options = list.getNodeList("options").getStrings();
            listsMap.put(name, options);
        }
    }

    private void setQuestions(JsonNode root, KB KB) {
        List<JsonNode> questions = root.getNodeList("questions").getNodes();

        for (JsonNode question : questions) {
            String questionName = question.getString("name");
            String questionType = question.getString("type");
            String questionText = question.getString("question");
            String questionTip = question.getString("tip");
            String questionOptions = question.getString("options");
            KnowledgeItem newItem = new ChoiceItem(questionName, listsMap.get(questionOptions), Origin.CHOICESELECTION, false);
            newItem.setQuestion(questionText);
            if (!"".equals(questionTip)) {
                newItem.setTip(questionTip);
            }

            KB.setItem(newItem);
        }
    }

    private void setGoals(JsonNode root, KB KB) {
        List<JsonNode> goals = root.getNodeList("goals").getNodes();

        for (JsonNode goal : goals) {
            String name = goal.getString("name");
            String response = goal.getString("response");
            KB.setItem(new BooleanItem(name, Origin.INFERRED, true).setGoalResponse(response));
        }
    }

    private void setRules(JsonNode root, KB KB, RuleBase ruleBase) {
        try {
            List<JsonNode> rules = root.getNodeList("rules").getNodes();
            for (JsonNode rule : rules) {
                // We first retrieve the knowledge item
                String consequenceName = rule.getNode("consequence").getString("name");
                KnowledgeItem knowledgeItem = KB.getItem(consequenceName);
                // add the knowledge item to the knowledge base if it isn't already there
                if (knowledgeItem == null) {
                    knowledgeItem = new BooleanItem(consequenceName, Origin.INFERRED, false);
                }
                KB.setItem(knowledgeItem);

                // Then we create a copy of the item to be used as consequence and set its value
                KnowledgeItem consequence = knowledgeItem.copy();
                consequence.setValue(rule.getNode("consequence").getBoolean("value"));

                // Make the consequence the base of the rule and add the condition that the consequence isn't true yet
                RuleBuilder newRule = new RuleBuilder(consequence);

                // A rule can have multiple conditions
                // We add each condition to the existing rule
                List<JsonNode> conditions = rule.getNodeList("conditions").getNodes();
                for (JsonNode condition : conditions) {
                    Condition newCondition = null;
                    String conditionName = condition.getString("name");
                    KnowledgeItem item = KB.getItem(conditionName);
                    if (item == null) {
                        throw new IllegalArgumentException("The knowledge item with the name '" + conditionName + "' isn't added to the knowledge base yet. Add a question, goal or rule consequence with this knowledge item's name");
                    }
                    Object conditionValue = getConditionValue(condition, item);
                    newCondition = new Condition(conditionName, conditionValue);
                    newRule.addCondition(newCondition);
                }
                // After the consequence and all of the conditions have been added,
                // we build the rule and add it to the rule base
                ruleBase.addRule(newRule.build());
            }
        } catch (Exception e) {
        }
    }

    private Object getConditionValue(JsonNode condition, KnowledgeItem item) {
        switch (item.getType()) {
            case "boolean":
                return condition.getBoolean("value");
            case "choice":
                return ((ChoiceItem) item).getIndex(condition.getString("value"));
            default:
                throw new UnsupportedOperationException();
        }
    }
}
