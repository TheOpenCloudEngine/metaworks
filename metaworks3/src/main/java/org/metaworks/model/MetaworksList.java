package org.metaworks.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksAlert;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Order;
import org.metaworks.annotation.ServiceMethod;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class MetaworksList<T> implements ContextAware {

    transient MetaworksContext metaworksContext;
        @Override
        public MetaworksContext getMetaworksContext() {
            return metaworksContext;
        }
        @Override
        public void setMetaworksContext(MetaworksContext metaworksContext) {
            this.metaworksContext = metaworksContext;
        }

    private List<MetaworksElement> elements;
        @Order(1)
        public List<MetaworksElement> getElements() {
            return elements;
        }
        public void setElements(List<MetaworksElement> elements) {
            this.elements = elements;
        }

    public MetaworksList(){
        setElements(new ArrayList<MetaworksElement>());
        setMetaworksContext(new MetaworksContext());
    }


    @Order(1)
    @Face(displayName="Add New")
//	@Available(when={EssenciaContext.WHEN_EDIT, EssenciaContext.WHEN_NEW})
    @ServiceMethod(callByContent=true, validate = true)
    public void add(){
        packElements();
        getElements().add(createNewElement());
    }


    public void packElements(){
        List<MetaworksElement> temp = new ArrayList<MetaworksElement>();
        for(MetaworksElement type : getElements()){
            if(type == null){
                continue;
            }
            temp.add(type);
        }
        setElements(temp);
    }

    public MetaworksElement createNewElement(){

        T c;
        try {

            ParameterizedType parameterizedType;

            try {
                parameterizedType = (ParameterizedType) this.getClass().
                        getGenericSuperclass();
            }catch (ClassCastException cce){
                throw new RuntimeException("MetaworksList must have parameterized type.", cce);
            }


            c = (T)((Class)(parameterizedType).getActualTypeArguments()[0]).newInstance();//getConstructor(new Class[]{getClass()}).newInstance(new Object[]{this});

            if(c instanceof ContextAware) {
                if(((ContextAware)c).getMetaworksContext() == null)
                    ((ContextAware)c).setMetaworksContext(new MetaworksContext());

                ((ContextAware)c).getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
            }

            MetaworksElement me = new MetaworksElement();
            me.setMetaworksContext(new MetaworksContext());
            me.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
            me.setElementId(java.util.UUID.randomUUID().toString());
            me.setValue(c);

            return me;

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;

    }
}
