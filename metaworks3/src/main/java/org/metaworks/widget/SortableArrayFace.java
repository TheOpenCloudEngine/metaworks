package org.metaworks.widget;

import org.metaworks.Face;
import org.metaworks.model.SortableElement;
import org.metaworks.model.SortableList;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by uengine on 2017. 5. 8..
 */
public class SortableArrayFace<T> extends SortableList implements Face<T[]> {
    @Override
    public void setValueToFace(T[] elements) {
        setAddable(true);

        ParameterizedType parameterizedType;

        try {
            parameterizedType = (ParameterizedType) this.getClass().
                    getGenericSuperclass();
        }catch (ClassCastException cce){
            throw new RuntimeException("SortableList must have parameterized type.", cce);
        }

        Class c = ((Class)(parameterizedType).getActualTypeArguments()[0]);
        setElementClassName(c.getName());



        List<SortableElement> list = new ArrayList<SortableElement>();

        if(elements!=null) {
            int i = 0;
            for (T element : elements) {
                SortableElement me = new SortableElement();
                me.setId(String.valueOf(i++));

                me.setValue(element);
                list.add(me);
            }
        }

        setElements(list);

    }

    @Override
    public T[] createValueFromFace() {
        List<T> list = new ArrayList<T>();
        for(SortableElement me : getElements()){
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
