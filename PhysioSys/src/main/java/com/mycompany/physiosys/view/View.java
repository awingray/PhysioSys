package com.mycompany.physiosys.view;

import com.mycompany.physiosys.model.knowledge.KB;
import com.mycompany.physiosys.model.knowledge.items.KnowledgeItem;

public interface View {

    public <T> KnowledgeItem<T> inquire(KnowledgeItem<T> item);

    public void showResult(KnowledgeItem goal, KB KB);
}
