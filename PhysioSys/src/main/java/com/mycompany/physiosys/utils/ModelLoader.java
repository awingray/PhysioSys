package com.mycompany.physiosys.utils;

import com.mycompany.physiosys.model.knowledge.KB;
import com.mycompany.physiosys.model.rules.RuleBase;

public abstract class ModelLoader {

    private static ModelLoader currentLoader;


    public static void setCurrentLoader(ModelLoader newLoader) {
        currentLoader = newLoader;
    }

    public static void loadModel(RuleBase ruleBase, KB KB){
        currentLoader.load(ruleBase, KB);
    }

    public abstract void load(RuleBase ruleBase, KB KB);
}
