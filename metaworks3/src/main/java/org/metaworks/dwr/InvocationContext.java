package org.metaworks.dwr;

import java.util.Map;

public class InvocationContext {
        
        Object object;
        
                public Object getObject() {
                        return object;
                }
        
                public void setObject(Object object) {
                        this.object = object;
                }
                
        Map<Class, Object> autowiredObjects;
        
                public Map<Class, Object> getAutowiredObjects() {
                        return autowiredObjects;
                }
        
                public void setAutowiredObjects(Map<Class, Object> autowiredObjects) {
                        this.autowiredObjects = autowiredObjects;
                }
        
        
}