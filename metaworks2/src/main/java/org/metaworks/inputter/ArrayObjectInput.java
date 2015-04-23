/*
 * Created on 2004-08-28
 */
package org.metaworks.inputter;

import org.metaworks.*;

import java.lang.reflect.*;

/**
 * @author Jinyoung Jang
 */
public class ArrayObjectInput extends MultipleInput{
	
	Class type;
	
	public ArrayObjectInput(Class cls) throws Exception{
		super(new ObjectType(cls));
		type = cls;	
	}
	
	public Object getValue(){		
		Instance[] recs = (Instance[])super.getValue();
		Object[] objects = new Object[recs.length];
		for(int i=0; i<recs.length; i++){
			objects[i] = ((ObjectInstance)recs[i]).getObject();
		}
		
		try{
			Object arrInTheType = Array.newInstance(type, objects.length);
			System.arraycopy(objects, 0, arrInTheType, 0, objects.length);
						
			return arrInTheType;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void setValue(Object value){
		try{
			int length = Array.getLength(value);
			Object[] objects = new Object[length];
			System.arraycopy(value, 0, objects, 0, length);
		
			ObjectInstance [] records = new ObjectInstance[length];
			ObjectType table = new ObjectType(type);
	
			for(int i=0; i<length; i++){
				records[i] = new ObjectInstance(table, objects[i]);
			}
		
			super.setValue(records);
		}catch(Exception e){
			//e.printStackTrace();
		}
	}

	
}
