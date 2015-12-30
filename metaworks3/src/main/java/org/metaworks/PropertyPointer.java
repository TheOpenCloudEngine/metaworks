package org.metaworks;

import org.metaworks.dwr.MetaworksRemoteService;

/**
 * Created by jjy on 2015. 12. 30..
 */
public class PropertyPointer {
    Object object;
        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

    String propertyName;
        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

    public PropertyPointer(Object object, String propertyName) {

        setObject(object);
        setPropertyName(propertyName);

    }

    public void setValue(Object value){

        try {
            ObjectInstance instance = createObjectInstance();

            instance.setFieldValue(getPropertyName(), value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Object getValue(){

        try {
            ObjectInstance instance = createObjectInstance();

            return instance.getFieldValue(getPropertyName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private ObjectInstance createObjectInstance() throws Exception {
        Type type = MetaworksRemoteService.getInstance().getMetaworksType(getObject().getClass().getName()).metaworks2Type();
        ObjectInstance instance = (ObjectInstance) type.createInstance();
        instance.setObject(getObject());
        return instance;
    }


}
