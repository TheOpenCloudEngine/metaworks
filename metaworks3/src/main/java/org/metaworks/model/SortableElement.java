package org.metaworks.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Order;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;

import java.util.ArrayList;

/**
 * Created by jangjinyoung on 2016. 12. 31..
 */
@Face(ejsPathForArray="genericfaces/CleanArrayFace.ejs")
public class SortableElement implements ContextAware{

    Object value;
        public Object getValue() {
            return value;
        }
        public void setValue(Object value) {
            if(value instanceof IconViewable)
                value = ((IconViewable) value).createIcon();

            this.value = value;
        }

    String id;
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
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


    @Order(20)
    @Face(displayName="Remove")
    @ServiceMethod(clientSide = true, inContextMenu = true)
    public void remove(){}
}
