package org.metaworks;

/**
 * Record에 속한 실재 Field(Cell Data) 값을 가진 Class. 주로 Record를 얻은 후, 실재 값을 얻기 위한 용도로 사용되며 새로 생성시켜 사용하는 경우는 없다. 
 * 
 * 
 * @version 1.0 2000/2/14 
 * @author 장진영 
*/

import java.util.Date;
import java.text.*;

public class Field implements TupleProperty{
	
	FieldDescriptor fieldDescriptor;
	Object data;
	
	
	public Field(FieldDescriptor fieldDesc, Object obj){
		fieldDescriptor = fieldDesc;
		data=obj;
	}
	
	public Field(Type table, String fieldName, Object obj){		
		fieldDescriptor = (FieldDescriptor)table.getFieldDescriptorTable().get(fieldName);
		data=obj;
	}
	
		
///////////////// implementations ///////////////////////
	public AbstractPropertyDescriptor getPropertyDescriptor(){
		return (AbstractPropertyDescriptor)getFieldDescriptor();
	}
	
	/**
	 * cell값을 Object 형으로 얻어 리턴
	 * @specified getValueObject in interface TupleProperty
	*/
	public Object getValueObject(){
		
		try{
			Object temp = getFieldDescriptor().getAttribute("fixed");
			if(temp!=null) data= temp;
		}catch(Exception e){}
		
		return data;
	}
	
	public int getType(){
		return getFieldDescriptor().getType();
	}
	
	public String getName(){
		return getFieldDescriptor().getName();
	}
	
///////////////// appended methods //////////////////////

	public FieldDescriptor getFieldDescriptor(){
		return fieldDescriptor;
	}
	
	// only used for "Statement Generation" Mode.
	public String toSQLExp(){
		return getFieldDescriptor().toSQLExp(getValueObject());
	}
	
	public String toString(){
		Object obj = getValueObject();				
//System.out.println("I'm in Field.toString : object type is " + obj.getClass());
		
		if(obj==null)
			return null;
		else 
		if(obj instanceof java.util.Date){
			
//System.out.println("I'm in Field.toString : reconized I'm a Date");
			
	       		SimpleDateFormat displayFormatter = new SimpleDateFormat("yyyy-MM-dd");
			String making = displayFormatter.format((Date)obj);
			
			return making;
		}else
			return obj.toString();
	}

}
		
