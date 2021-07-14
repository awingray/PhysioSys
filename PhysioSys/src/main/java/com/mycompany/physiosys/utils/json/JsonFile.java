package com.mycompany.physiosys.utils.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import com.google.gson.Gson;

public class JsonFile {

    Map<String, Object> root;

    public JsonFile(String filename) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream(filename);
        String result = IOUtils.toString(is);
        is.close();
        this.root = (new Gson().fromJson(result, Map.class));
    }

    public JsonNode getRootNode() {
        return new JsonNode(root);
    }
}
