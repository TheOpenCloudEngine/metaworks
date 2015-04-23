package org.metaworks.inputter;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.metaworks.InputForm;
import org.metaworks.ObjectInstance;
import org.metaworks.ObjectType;

public class ObjectInput extends InputterAdapter{
	JPanel inputterPanel;
	InputForm objectInputForm; 
	Inputter properInputter;
	Class type;
	
	public ObjectInput(Class type){
		super();
		this.type = type;
	}
	
	public ObjectInput(){
		super();
	}
	
	public Component getNewComponent() {
		inputterPanel = new JPanel(new BorderLayout(0,0));
		setType(type);
		
		return inputterPanel;
	}
	
	public void setType(Class type){
		this.type = type;
		
		if(inputterPanel==null) return; //only when swing mode
		
		if(type!=null){
			ObjectType table;
			
			try{
				properInputter = ObjectType.getDefaultInputter(type);
				if(properInputter.getClass() != this.getClass()){
					inputterPanel.removeAll();
					inputterPanel.add("Center", properInputter.getComponent());
					inputterPanel.revalidate();
					return;
				}else
					properInputter = null;
			}catch(Exception e){				
			}			
			
			try {
				table = new ObjectType(type);
				objectInputForm = new InputForm(table);
				
				objectInputForm.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent e) {
						onValueChanged();
					}
					
				});
		
				inputterPanel.removeAll();
				inputterPanel.add("Center", objectInputForm);
				inputterPanel.revalidate();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			inputterPanel.removeAll();
			inputterPanel.add("Center", new JLabel("No type is set"));
			inputterPanel.revalidate();
		}
	}
	
	public Object getValue() {
		if(properInputter!=null){
			return properInputter.getValue();
		}else
		if(objectInputForm!=null){			
			ObjectInstance objectRec = (ObjectInstance)objectInputForm.getInstance();
			return objectRec.getObject();
		}else{
			return null;
		}
	}

	public void setValue(Object obj) {
		if(properInputter!=null){
			properInputter.setValue(obj);
		}else
		if(objectInputForm!=null){	
			ObjectInstance objRec = (ObjectInstance)((ObjectType)objectInputForm.getType()).createInstance();
			objRec.setObject(obj);			
			objectInputForm.setInstance(objRec);
		}
	}

}