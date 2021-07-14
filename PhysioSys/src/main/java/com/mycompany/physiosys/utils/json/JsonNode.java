package com.mycompany.physiosys.utils.json;

import java.util.List;
import java.util.Map;

public class JsonNode {

    Map<String, Object> node;

    public JsonNode(Map<String, Object> node) {
        this.node = node;
    }

    public JsonNode getNode(String key) {
        return new JsonNode((Map<String, Object>) node.get(key));
    }

    public JsonNodeList getNodeList(String key) {
        return new JsonNodeList((List<Object>) node.get(key));
    }

    public String getString(String key) {
        return node.get(key).toString();
    }


    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(this.getString(key));
    }
}
