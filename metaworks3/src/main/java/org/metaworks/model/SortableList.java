package org.metaworks.model;


import org.metaworks.dwr.SerializationSensitive;

import java.util.List;

/**
 * Created by jangjinyoung on 2016. 12. 31..
 */
public class SortableList{

    List<SortableElement> elements;
        public List<SortableElement> getElements() {
            return elements;
        }
        public void setElements(List<SortableElement> elements) {
            this.elements = elements;
        }

    String title;
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }


}
