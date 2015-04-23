package org.metaworks.validator;

import org.metaworks.Instance;

public class NotNullValid implements Validator{
	
	public String validate(Object data, Instance instance){
		if(data!=null) return null;
		return ("Required");
	}
	
}
