package org.metaworks.inputter;

import java.awt.Component;

import org.metaworks.component.*;
import org.metaworks.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Jinyoung Jang
 */

public class org_metaworks_inputter_InputterAdapterInput extends InputterAdapter{

	InputForm form = null;
	JComboBox typeList;
	JPanel panel;
	InputterAdapter currentValue = null;
		
	public org_metaworks_inputter_InputterAdapterInput(){
		super();
	}
	
	public Component getNewComponent() {
		try{
			currentValue = null;
			
			panel = new JPanel(new BorderLayout());
			
			final JPanel formPanel = new JPanel();
			
			typeList = new JComboBox();{
				//Class[] inputterClasses = ClassBrowser.findClasses(InputterAdapter.class);
				
				ArrayList inputterList = (ArrayList)MetaWorksComponentCenter.deserialize(getClass().getClassLoader().getResourceAsStream("org/metaworks/inputters.xml"));
				
				typeList.addItem(null);
				
				for(Iterator iter = inputterList.iterator(); iter.hasNext();){
					try{
						String inputterClsName = (String)iter.next();
						Class inputterClass = Thread.currentThread().getContextClassLoader().loadClass(inputterClsName);
						
						if(inputterClass.getConstructor(new Class[]{})!=null)
							typeList.addItem(inputterClass);
					}catch(Exception e){
					}
				}
			}
			 
			panel.add("North", typeList);
			panel.add("Center", formPanel);
			
			typeList.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e){
					if(typeList.getSelectedItem()==null){
						form = null;
						formPanel.removeAll();
						formPanel.revalidate();
					}
					
					if(e.getStateChange()!=ItemEvent.SELECTED) return;
					
					form = null;
					formPanel.removeAll();
					
					try{
						Class inputterType = (Class)e.getItem();
						ObjectType objectTable = new ObjectType(inputterType);
						
						objectTable.getFieldDescriptor("Value").setAttribute("hidden", "true");
						
						form = new InputForm(objectTable);
						
						if(currentValue!=null && inputterType==currentValue.getClass()){
							try{
								ObjectInstance rec = (ObjectInstance)objectTable.createInstance();
								rec.setObject(currentValue);
								if(currentValue!=null)
									form.setInstance(rec);
							}catch(Exception e2){
							}
						}
						
						formPanel.add(form);
						formPanel.revalidate();
						
						
					}catch(Exception throwable){
					
					}
				}
			});		
			
			return panel;
		}catch(Throwable t){
			t.printStackTrace();
			
			return new JLabel("currently not available");
		}		
	}
	
	
	public Object getValue() {
		if(form!=null){
			currentValue = null;
			Object value = ((ObjectInstance)form.getInstance()).getObject();
			//form.setRecord(null);
			return value;
		}
		
		return null;
	}

	public void setValue(Object obj) {
		
		try{						
			currentValue = (InputterAdapter)obj; 	
			
			if(form!=null){
				form.setInstance(null);
			}

			typeList.setSelectedItem(obj!=null ? obj.getClass() : null);
		}catch(Exception e){
		}
		
//		super.setValue(obj);
		/*

		if(form!=null){
			return ((ObjectRecord)form.getRecord()).getObject();
		}*/
	}

}