package org.metaworks.inputter;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import org.metaworks.component.MetaWorksComponentCenter;
import org.metaworks.web.HTML;
import org.metaworks.*;

public class CheckBoxInput extends SelectInput{
	
	Object[] selections;
		public Object[] getSelections() {
			return selections;
		}
		public void setSelections(Object[] selections) {
			this.selections = selections;
		}

	Object[] values;
		public Object[] getValues() {
			return values;
		}
		public void setValues(Object[] values) {
			this.values = values;
		}

	public CheckBoxInput(){
		this(new Object[]{}, null);
	}

	public CheckBoxInput(Object [] selections){
		this(selections, null);
	}

	public CheckBoxInput(Object [] selections, Object [] values){
		super();
		this.selections = selections;
		this.values = values;		
	}
	
	transient Vector checkedBox;
	transient Vector selValues;
	

/////////// implemetations //////////////

	public Object getValue(){
		String[] value = new String[checkedBox.size()];
		
		for(int i=0; i<checkedBox.size(); i++){
			value[i] = ((JCheckBox)checkedBox.get(i)).getName();
		}		
		return value;
	}
	
	public void setValue(Object obj){		
		if(obj==null) return;
		
		Object[] selected = (Object[])obj;
		
		Object[] checkingValues = (values == null ? selections : values);
		
		Component[] components = ((JPanel)getComponent()).getComponents();
		
		for(int i=0; i<selected.length; i++){
			for(int j=0; j<components.length; j++){
				if(selected[i].equals(((JCheckBox)components[j]).getName())){
					((JCheckBox)components[j]).setSelected(true);
				}
			}			
		}
		
		addSelection(obj);	
	}

/*	public Component getNewComponent(){
		JCheckBox checkbox;
		if(selections==null)
			checkbox = new JCheckBox();
		else
			checkbox = new JCheckBox(selections);
			
		
		checkbox.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e){
					if(e.getStateChange()!=ItemEvent.SELECTED) return;
					onValueChanged();
				}
		});
	
		return checkbox;
	}*/
	
	public Component getNewComponent(){
		JPanel panel = new JPanel();
		
		CheckListener checkListener = new CheckListener();
		
		if(selections == null){
			JCheckBox checkbox = new JCheckBox();
			panel.add(checkbox);
		}else{
			
			for(int i=0; i<selections.length; i++){
				JCheckBox checkbox = new JCheckBox((String)selections[i]);	
				checkbox.setName((String)values[i]);
				checkbox.addItemListener(checkListener);
				panel.add(checkbox);
			}
			
		}
		
		checkedBox = new Vector();
		
		return panel;
	}
	
	private class CheckListener implements ItemListener { 
		public void itemStateChanged(ItemEvent e) {
			
			if(e.getSource() instanceof JCheckBox){
				JCheckBox checkbox = (JCheckBox) e.getSource();
				boolean checked = checkbox.isSelected();
				
				if(checked){
					checkedBox.add(checkbox);
				}else if(checkedBox.contains(checkbox)){
					checkedBox.remove(checkbox);
				}
			}
			
			onValueChanged();			
		}
    }

}
