package org.metaworks.model;


import org.metaworks.annotation.Available;
import org.metaworks.annotation.ServiceMethod;

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

    boolean addable;
        public boolean isAddable() {
            return addable;
        }
        public void setAddable(boolean addable) {
            this.addable = addable;
        }

    String elementClassName;
        public String getElementClassName() {
            return elementClassName;
        }
        public void setElementClassName(String elementClassName) {
            this.elementClassName = elementClassName;
        }


    @ServiceMethod(clientSide = true, target = ServiceMethod.TARGET_NONE)
    @Available(condition = "addable")
    public void addNew(){}


}
