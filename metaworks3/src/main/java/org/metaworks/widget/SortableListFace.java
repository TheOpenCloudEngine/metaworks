package org.metaworks.widget;

import org.metaworks.Face;
import org.metaworks.model.SortableElement;
import org.metaworks.model.SortableList;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by uengine on 2017. 4. 17..
 */
public class SortableListFace<T> extends SortableList implements Face<List<T>> {
    @Override
    public void setValueToFace(List<T> elements) {
        setAddable(true);

        ParameterizedType parameterizedType;

        try {
            parameterizedType = (ParameterizedType) this.getClass().
                    getGenericSuperclass();
        }catch (ClassCastException cce){
            throw new RuntimeException("MetaworksList must have parameterized type.", cce);
        }

        Class c = ((Class)(parameterizedType).getActualTypeArguments()[0]);
        setElementClassName(c.getName());



        List<SortableElement> list = new ArrayList<SortableElement>();

        if(elements!=null)
            for(T element : elements){
                SortableElement me = new SortableElement();
                me.setValue(element);
                list.add(me);
            }

        setElements(list);
    }

    @Override
    public List<T> createValueFromFace() {
        List<T> list = new ArrayList<T>();
        for(SortableElement me : getElements()){
            list.add((T) me.getValue());
        }
        return list;
    }
}