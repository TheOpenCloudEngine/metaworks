package org.metaworks.query;

import org.metaworks.*;

public class Eval implements Query{
	String evalStr, fieldName;

	
	public Eval(String evalStr, String fieldName){
		this.evalStr = evalStr;
		this.fieldName = fieldName;
	}
	
	public Eval(String evalStr, FieldDescriptor fd){
		this(evalStr, fd.getName());
	}
	
	public Eval(String evalStr, Field f){
		this(evalStr, f.getName());
	}
	
	public String toSQLExp(Instance rec){
		try{		
			Field f = (Field)rec.get(fieldName);
			
			if(f.getValueObject()==null) return null;
						
			String sql = fieldName + evalStr + f.toSQLExp();
			
			return sql;
							
		}catch(Exception e){
			return null;
		}
	}
	
	public String [] getFieldNames(){
		return new String[]{fieldName};
	}
		
	

}