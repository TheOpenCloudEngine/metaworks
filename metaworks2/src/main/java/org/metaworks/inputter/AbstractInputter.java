package org.metaworks.inputter;

import java.awt.Component;
import java.util.Hashtable;
import org.metaworks.*;

public abstract class AbstractInputter implements Inputter{
	transient Hashtable attributeTable=null;
	Integer size=null;
	Object def=null;
	String name= null;
	boolean bMandatory=false;	
	
	public AbstractInputter(){
		// do Nothing
	}

	public AbstractInputter(Hashtable attrs){
		initialize(attrs);
	}
	
	public void initialize(Hashtable attrs){
		attributeTable=attrs;
		
		if(attrs.containsKey(FieldDescriptor.ATTR_DEFAULT))
			def=attrs.get(FieldDescriptor.ATTR_DEFAULT);
			
		if(attrs.containsKey("size")){
			size = (Integer)attrs.get("size");
		}
		
		if(attrs.containsKey("mandatory")){
			bMandatory=true;
		}
		if(attrs.containsKey("name")){
			name = (String)attrs.get("name");
		}
	}		
	

	public Integer getSize(){
		return size;
	}
	public int getSizeInt(){
		if(size==null) return 9999999;
		
		return size.intValue();
	}
	public Object getDefault(){
		return def;
	}
	public String getName(){
		return name;
	}
	
	public boolean isMandatory(){
		return bMandatory;
	}
	
	public Object getAttribute(String key){
		if(key==null) return null;
		
		return attributeTable.get(key);
	}

	public boolean isEligibleType(Class type) {
		// TODO Auto-generated method stub
		return true;
	}
}
