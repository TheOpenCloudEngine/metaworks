package org.metaworks.inputter;

import java.awt.Component;
import javax.swing.*;
import com.pongsor.swing.WholeNumberField;


public class NumberInput extends TextInput{

	public NumberInput(){
		super();
	}
	
/////////// implemetations //////////////

	public Component getNewComponent(){
		WholeNumberField numField = new WholeNumberField(0, 5);
		numField.setText("");
		numField.setHorizontalAlignment(JTextField.RIGHT);
		
		return numField;
	}
	
	public Object getValue(){
		String oldVal = (String)super.getValue();
		
		try{
			return ( new Integer(oldVal));
		}catch(Exception e){
			try{
				return ( new Double(oldVal));
			}catch(Exception ex){
				return ex.getMessage();
			}
		}
	}
	
	public Object createValueFromHTTPRequest(java.util.Map parameterValues, String section, String fieldName, Object oldValue){
		Object val = super.createValueFromHTTPRequest(parameterValues, section, fieldName, oldValue);
		
		try{
			val = new Integer(val.toString());
		}catch(Exception e){
			//e.printStackTrace();
		}
		
		return val;
	}
	
	public boolean isEligibleType(Class type) {
		// TODO Auto-generated method stub
		return Number.class.isAssignableFrom(type);
	}

}
