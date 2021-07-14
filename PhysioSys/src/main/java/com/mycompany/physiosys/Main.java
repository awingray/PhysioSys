package com.mycompany.physiosys;

import com.mycompany.physiosys.controller.InferenceSystem;
import com.mycompany.physiosys.model.knowledge.KB;
import com.mycompany.physiosys.model.rules.RuleBase;
import com.mycompany.physiosys.utils.ModelLoader;
import com.mycompany.physiosys.utils.JsonFileModelLoader;
import com.mycompany.physiosys.view.DummyConsoleView;
import com.mycompany.physiosys.view.View;

public class Main {

    public static void main(String[] args) {
        KB KB = new KB();
        RuleBase ruleBase = new RuleBase();

        View view = new DummyConsoleView();
        ModelLoader.setCurrentLoader(new JsonFileModelLoader("knowled4.json"));
        ModelLoader.loadModel(ruleBase, KB);
        InferenceSystem system = new InferenceSystem(ruleBase, KB, view);
        system.start();


    }

}
