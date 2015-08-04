package org.metaworks.widget;

import org.metaworks.Face;
import org.metaworks.model.MetaworksElement;
import org.metaworks.model.MetaworksList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayFace<T> extends MetaworksList<T> implements Face<T[]> {
    @Override
    public void setValueToFace(T[] elements) {
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
    public T[] createValueFromFace() {
        List<T> list = new ArrayList<T>();
        for(MetaworksElement me : getElements()){
            list.add((T) me.getValue());
        }

        if(list.size()>0) {
            T[] arr = (T[]) Array.newInstance(list.get(0).getClass(), list.size());

            arr = list.toArray(arr);

            return arr;
        }else
            return  null;
    }
}
