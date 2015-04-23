package org.metaworks.query;

import java.util.*;

import org.metaworks.*;
import com.pongsor.util.*;

public abstract class QuerySet extends Vector implements Query{
	
	public QuerySet(Query [] q){
		for(int i=0; q.length > i; i++){
			addElement(q[i]);
		}
	}

	public String toSQLExp(Instance rec){
		
		String making = "";
		Enumeration enumeration = elements();
			
		String separator = getSeparator();
			
		while(enumeration.hasMoreElements()){
			Object obj = ((Query)enumeration.nextElement()).toSQLExp(rec);
			
			if(obj != null)
				making += separator + obj;
		}
		
		if(making.length() > 0)
			making = making.substring(separator.length());
		
		return "("+making+")";
	}
	
	public String [] getFieldNames(){
		Vector nameVec = new Vector();
		
		Enumeration enumeration = elements();			
		String separator = getSeparator();
			
		while(enumeration.hasMoreElements()){
			String names[] = ((Query)enumeration.nextElement()).getFieldNames();
			
			for(int i=0; i<names.length; i++)
				nameVec.addElement(names[i]);
		}
		
		String [] out = PrimitiveConverter.vectorToStringArray(nameVec);
		
		return out;
	}
		
	
	abstract public String getSeparator();
}