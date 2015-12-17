package org.metaworks.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.Remover;
import org.metaworks.annotation.*;
import org.metaworks.dwr.MetaworksRemoteService;

public class MetaworksElement implements ContextAware{

    public MetaworksElement(){
        setElementId(java.util.UUID.randomUUID().toString());
    }


    String elementId;
        @Id
        public String getElementId() {
            return elementId;
        }

        public void setElementId(String elementId) {
            this.elementId = elementId;
        }

    public Object value;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

    @Order(1)
    @Face(displayName="Remove")
    @ServiceMethod(callByContent=true, inContextMenu = true)
    public Remover remove(){
        return new Remover(this);
    }


    @Face(displayName="Edit")
    @ServiceMethod(callByContent = true, inContextMenu = true)
    public void edit(){
        setMetaworksContext(new MetaworksContext());
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
    }

    @ServiceMethod(inContextMenu = true)
    public void up(@AutowiredFromClient MetaworksList metaworksList){
        int index = metaworksList.getElements().indexOf(this);

        if(index>0){
            metaworksList.getElements().remove(this);
            //TODO: quiz 1 (below is not proper since it will clear the type information. Prove why and fix this)
            metaworksList.getElements().add(index - 1, this);
        }

        MetaworksRemoteService.wrapReturn(metaworksList);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj!=null && obj instanceof MetaworksElement && ((MetaworksElement) obj).getElementId() == getElementId()){
            return true;
        }

        return false;
    }

    MetaworksContext metaworksContext;
        @Override
        public MetaworksContext getMetaworksContext() {
            return metaworksContext;
        }

        @Override
        public void setMetaworksContext(MetaworksContext metaworksContext) {
            this.metaworksContext = metaworksContext;
        }
}
