package org.metaworks.validator;

import org.metaworks.Instance;

public class NumberValid implements Validator{

/////////////// implementations //////////////////

	public String validate(Object data, Instance instance){
			
		try{
			new Integer(data.toString());
			
			return null;
			
		}catch(Exception e){
			return ("Number is required.");
		}
	}
	
}