package org.metaworks.contexts;

import java.util.*;
import org.metaworks.*;

public class ForeignKey{
	
	public String referenceTableName; //the name of referencing table 
	
		public String getReferenceTableName(){
			return referenceTableName;
		}
			
		public void setReferenceTableName(String value){
			referenceTableName = value;
		}
		 
	public Vector fieldNames;
	
		public Vector getFieldNames(){
			return fieldNames;
		}
			
		public void setFieldNames(Vector value){
			fieldNames = value;	
		}
	
	Properties referenceContexts = new Properties(); //referencing field name would be content and indexed with fieldName
	Hashtable ultimateRefContexts = new Hashtable(); //would be indexed with fieldNames
	
	public void addReference(String fieldName, String refFieldName){
		referenceContexts.put(fieldName, refFieldName);
		fieldNames.add(fieldName);
	}
	
	public String getReferenceFieldName(String fieldName){
		return referenceContexts.getProperty(fieldName);
	}
	
	public String[] getUltimateReferenceContext(String fieldName){
		return (String[])ultimateRefContexts.get(fieldName);
	}
		
	public void optimize(String myTableName, Map tableRepository)
	{
		//review: may results multi-threaded 'Racing' problems
		if(ultimateRefContexts!=null) return;

		//initialize if use
		ultimateRefContexts = new Hashtable();

		Type ultimateRefTable = (Type)tableRepository.get(getReferenceTableName());
		Type myType = (Type)tableRepository.get(myTableName);

		Vector refFieldNames = getFieldNames();
		for(Enumeration enumeration = refFieldNames.elements(); enumeration.hasMoreElements();){
			String fieldName = (String)enumeration.nextElement();
			String refFieldName = getReferenceFieldName(fieldName);
			ForeignKey foreignKey = null;
				
			try
			{
				foreignKey = ultimateRefTable.getFieldDescriptor(refFieldName).getForeignKey();
				
				//case of recursively related	
				foreignKey.optimize(getReferenceTableName(), tableRepository); //recursively optimize

				//copy the pre-derived ultimately referencing table name					
				Object tempUltimateRefCtx = foreignKey.ultimateRefContexts.get(refFieldName);

				if(tempUltimateRefCtx!=null)
					ultimateRefContexts.put(fieldName, tempUltimateRefCtx);

			}
			catch(Exception e)
			{
			
				//case of 1-level relation: the referenceTableName is the ultimately referenced.
				//the end-point of exploring the ultimate referencing context

				//killed: 다중으로 연관된 경우의 필드만 ulti- ht를 담도록 한다. 다중연결인지의 정보를 전달하기 위함
				ultimateRefContexts.put(fieldName, new String[]{getReferenceTableName(), refFieldName});
			}
		}
	}
}

