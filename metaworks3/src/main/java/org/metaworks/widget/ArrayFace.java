package org.metaworks.widget;

import org.metaworks.Face;
import org.metaworks.model.MetaworksElement;
import org.metaworks.model.MetaworksList;

import java.util.ArrayList;
import java.util.List;

public class ArrayFace<T> extends MetaworksList<T> implements Face<List<T>> {
    @Override
    public void setValueToFace(List<T> elements) {
        List<MetaworksElement> list = new ArrayList<MetaworksElement>();

        if(elements!=null)
        for(T element : elements){
            MetaworksElement me = new MetaworksElement();
            me.setValue(element);
            list.add(me);
        }

        setElements(list);
    }

    @Override
    public List<T> createValueFromFace() {
        List<T> list = new ArrayList<T>();
        for(MetaworksElement me : getElements()){
            list.add((T) me.getValue());
        }
        return list;
    }
}
