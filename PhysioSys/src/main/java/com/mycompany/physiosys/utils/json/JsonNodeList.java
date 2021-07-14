package com.mycompany.physiosys.utils.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonNodeList {

    List<Object> nodes;

    public JsonNodeList(List<Object> nodes) {
        this.nodes = nodes;
    }

    public List<JsonNode> getNodes() {
        List<JsonNode> resultNodes = new ArrayList<>();
        for (Object node : nodes) {
            resultNodes.add(new JsonNode((Map<String, Object>) node));
        }
        return resultNodes;
    }
    public List<String> getStrings() {
        List<String> resultNodes = new ArrayList<>();
        for (Object node : nodes) {
            resultNodes.add((String) node);
        }
        return resultNodes;
    }

}
