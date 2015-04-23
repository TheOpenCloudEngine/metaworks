package org.metaworks.inputter;

import org.metaworks.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class ArrayInput extends MultipleInput{
	
	public ArrayInput(Inputter inputter){
		super(new Type("",
			new FieldDescriptor[]{new FieldDescriptor("")}
		));
		
		table.getFieldDescriptor("").setInputter(inputter);
	}
	
	public Object getValue(){
		Instance[] records = (Instance[])super.getValue();
		
		Object[] values = new Object[records.length];
		for(int i=0; i<records.length; i++) values[i] = records[i].getFieldValueObject("");
		
		return values;
	}
	
	public void setValue(Object val){
		if(val!=null){
			Object[] vals = (Object[])val;			
			Instance[] records = new Instance[vals.length];
			
			for(int i=0; i<vals.length; i++){
				Instance rec = getTable().createInstance(); 
				rec.setFieldValue("", vals[i]);
				records[i] = rec;
			}
			
			super.setValue(records);
		}
		else super.setValue(new Instance[]{});
	}
	
	public static void main(String[] args){
		AppliedTable a = new AppliedTable(
			"test",
			new FieldDescriptor[]{
				new FieldDescriptor("test field")
			}
		);

		a.getFieldDescriptor("test field").setInputter(
		
					new ArrayInput(
						new FileInput()
					)
		);
		
		a.runDefaultApplication();
	}
				
	
}


