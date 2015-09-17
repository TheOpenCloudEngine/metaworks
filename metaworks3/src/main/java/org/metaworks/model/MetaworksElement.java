package org.metaworks.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.Remover;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Order;
import org.metaworks.annotation.ServiceMethod;

public class MetaworksElement implements ContextAware{

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
