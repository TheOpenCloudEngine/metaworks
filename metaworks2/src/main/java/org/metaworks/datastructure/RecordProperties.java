package org.metaworks.datastructure;

/**
 * Record 를 Properties로 변환 
 * 
 * 
 * @version 1.0 2000/2/14 
 * @author 장진영 
*/

import java.util.Properties;
import org.metaworks.*;

public class RecordProperties extends Properties{

	public RecordProperties(Instance rec){
		super();
	
		Field [] fields = rec.getAllFields();
		
		for(int i=0; i<fields.length; i++){
			Object data;
			if((data = fields[i].getValueObject()) != null)
				setProperty(fields[i].getName(), data.toString());
		}
	}
}
		