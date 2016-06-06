package org.metaworks.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.Remover;
import org.metaworks.annotation.*;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ArrayFace;
import org.metaworks.widget.Clipboard;
import org.metaworks.widget.ListFace;

import java.util.ArrayList;

public class MetaworksElement implements ContextAware{

    public MetaworksElement(){
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

    @Order(20)
    @Face(displayName="Remove")
    @ServiceMethod(callByContent=true, inContextMenu = true, needToConfirm = true)
    public void remove(@AutowiredFromClient MetaworksList metaworksList){

        ArrayList<MetaworksElement> removalTargets = new ArrayList<MetaworksElement>();

        for(Object metaworksElement: metaworksList.getElements()){
            if(((MetaworksElement)metaworksElement).isSelected() || metaworksElement == this)
                removalTargets.add((MetaworksElement)metaworksElement);
        }

        for(MetaworksElement element : removalTargets){
            metaworksList.getElements().remove(element);
        }

        MetaworksRemoteService.wrapReturn(metaworksList);
    }

    @Order(20)
    @ServiceMethod(callByContent=true, inContextMenu = true, keyBinding = "Ctrl+A")
    public void selectAll(@AutowiredFromClient MetaworksList metaworksList){

        metaworksList.selectAll();

        MetaworksRemoteService.wrapReturn(metaworksList);
    }

    @Order(20)
    @ServiceMethod(callByContent=true, inContextMenu = true, keyBinding = "ESC")
    public void unselectAll(@AutowiredFromClient MetaworksList metaworksList){

        metaworksList.unselectAll();
        MetaworksRemoteService.wrapReturn(metaworksList);
    }

    @Order(20)
    @ServiceMethod(callByContent=true, inContextMenu = true)
    public void toggleSelectAll(@AutowiredFromClient MetaworksList metaworksList){

        metaworksList.toggleSelectAll();
        MetaworksRemoteService.wrapReturn(metaworksList);
    }

    @Order(1)
    @Face(displayName="Edit")
    @ServiceMethod(callByContent = true, inContextMenu = true)
    public void edit(){
        setMetaworksContext(new MetaworksContext());
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
    }

    @ServiceMethod(callByContent = true, inContextMenu = true)
    @Order(10)
    public void up(@AutowiredFromClient MetaworksList metaworksList){
        int index = metaworksList.getElements().indexOf(this);

        if(index>0){
            metaworksList.getElements().remove(this);
            //TODO: quiz 1 (below is not proper since it will clear the type information. Prove why and fix this)
            metaworksList.getElements().add(index - 1, this);
        }

        MetaworksRemoteService.wrapReturn(metaworksList);
    }

    @ServiceMethod(callByContent = true, inContextMenu = true)
    @Order(11)
    public void down(@AutowiredFromClient MetaworksList metaworksList){
        int index = metaworksList.getElements().indexOf(this);

        if(index<metaworksList.getElements().size() - 1){
            metaworksList.getElements().remove(this);
            //TODO: quiz 1 (below is not proper since it will clear the type information. Prove why and fix this)
            metaworksList.getElements().add(index + 1, this);
        }

        MetaworksRemoteService.wrapReturn(metaworksList);
    }

    @ServiceMethod(callByContent = true, inContextMenu = true, target = ServiceMethod.TARGET_SELF/*, mouseBinding = "left"*/)
    @Order(100)
    public void select(){
        setSelected(!isSelected());
        //getMetaworksContext().getHow()
    }


    boolean selected;
        public boolean isSelected() {
            return selected;
        }
        public void setSelected(boolean selected) {
            this.selected = selected;
        }

    @ServiceMethod(callByContent = true, inContextMenu = true, keyBinding = "Ctrl+C")
    @Order(100)
    public void copy(@AutowiredFromClient Clipboard clipboard, @AutowiredFromClient MetaworksList metaworksList){

        ArrayList<MetaworksElement> copiedElements = new ArrayList<MetaworksElement>();

        for(Object metaworksElement: metaworksList.getElements()){
            if(((MetaworksElement)metaworksElement).isSelected())
                copiedElements.add((MetaworksElement)metaworksElement);
        }

        clipboard.setContent(copiedElements);

        MetaworksRemoteService.wrapReturn(clipboard);
    }

    @ServiceMethod(callByContent = true, inContextMenu = true, keyBinding = "Ctrl+P")
    @Order(100)
    public void paste(@AutowiredFromClient Clipboard clipboard, @AutowiredFromClient MetaworksList metaworksList){

        if(clipboard!=null && clipboard.getContent()!=null && clipboard.getContent() instanceof Object[] ){

            ArrayList<MetaworksElement> elements = new ArrayList<MetaworksElement>();

            Object[] elementsInObject = (Object[]) clipboard.getContent();

            for(Object elementInObj : elementsInObject){
                MetaworksElement metaworksElement = (MetaworksElement) elementInObj;

                if(getValue().getClass() == metaworksElement.getValue().getClass()) {
                    elements.add(metaworksElement);
                    //metaworksElement.setSelected(false);
                }
            }

            if(elements!=null || elements.size()>0) {

                int index = metaworksList.getElements().indexOf(this);

                metaworksList.getElements().addAll(index, elements);

                MetaworksRemoteService.wrapReturn(metaworksList);
            }
        }

    }

    @Override
    public boolean equals(Object obj) {
        if(obj!=null && obj instanceof MetaworksElement && ((MetaworksElement) obj).getElementId().equals(getElementId())){
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
