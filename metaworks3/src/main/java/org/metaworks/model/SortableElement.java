package org.metaworks.model;

import org.metaworks.annotation.Face;

/**
 * Created by jangjinyoung on 2016. 12. 31..
 */
@Face(ejsPathForArray="genericfaces/CleanArrayFace.ejs")
public class SortableElement {

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


}
