package com.mycompany.physiosys.model.knowledge;

import com.mycompany.physiosys.model.knowledge.items.KnowledgeItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class KB implements Iterable<KnowledgeItem>{

    private final List<KnowledgeItem> items;

    public KB() {
        this.items = new ArrayList<>();
    }

    public KnowledgeItem getItem(String itemName) {
        for (KnowledgeItem item : items) {
            if (item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }

    public void setItem(KnowledgeItem newItem) {
        String itemName = newItem.getName();
        for (int i = 0; i < items.size(); i++) {
            KnowledgeItem item = items.get(i);

            if (item.getName().equals(itemName)) {
                items.remove(i);
                break;
            }

        }
        items.add(newItem);
    }

    @Override
    public Iterator<KnowledgeItem> iterator() {
        return items.iterator();
    }

    public Stream<KnowledgeItem> stream(){
        return items.stream();
    }
}
