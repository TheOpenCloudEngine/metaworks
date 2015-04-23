package org.metaworks.validator;

public class ValidationResult{
	String message=null;
	boolean validity=false;
	
	public ValidationResult(String message, boolean isValid){
		this.message=message;
		this.validity=isValid;
	}
	
	public ValidationResult(boolean isValid){
		this.validity=isValid;
	}
	
	String getMessage(){
		return message;
	}
			
	boolean isValid(){
		return validity;
	}
	
}