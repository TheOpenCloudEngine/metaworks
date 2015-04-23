package org.metaworks.inputter;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import org.metaworks.component.MetaWorksComponentCenter;
import org.metaworks.web.HTML;
import org.metaworks.*;

public class SelectInput extends InputterAdapter{
	
	transient boolean ignoreEvents = false;

	Object [] selections;
	
		public Object[] getSelections(){
			return selections;
		}
			
		public void setSelections(Object[] value){
			selections = value;

			JComboBox comp = (JComboBox)getValueComponent();			
			if(comp==null) return;
			
			comp.removeAllItems();
				
			if(selections!=null){
				try{
					ignoreEvents = true;
					for(int i=0; i<selections.length; i++)
						comp.addItem(selections[i]);
				}finally{
					ignoreEvents = false;
				}
			}
		}
		
		public void addSelection(Object newValue){
			Object[] value = getSelections();
			Object[] temp = new Object[value.length +1];
			System.arraycopy(value, 0, temp, 1, value.length);
			temp[0] = newValue;
				
			setSelections(temp);
		}

		public void setSelectionsWithNull(Object[] value){
			if(value==null) return;
				
			Object[] temp = new Object[value.length +1];
			System.arraycopy(value, 0, temp, 1, value.length);
			temp[0] = null;
				
			setSelections(temp);
		}
	
	Object [] values = null;
	
		public Object[] getValues(){
			return values;
		}
		public void setValues(Object[] value){
			values = value;
		}
		public void setValuesWithNull(Object[] value){
			if(value==null) return;
				
			Object[] temp = new Object[value.length +1];
			System.arraycopy(value, 0, temp, 1, value.length);
			temp[0] = null;

			setValues(temp);
		}
	
	public SelectInput(){
		this(new Object[]{}, null);
	}

	public SelectInput(Object [] selections){
		this(selections, null);
	}

	public SelectInput(Object [] selections, Object [] values){
		super();
		this.selections = selections;
		this.values = values;
	}
	

/////////// implemetations //////////////

	public Object getValue(){
		int selectedIndex=((JComboBox)getValueComponent()).getSelectedIndex();
		
		if(selectedIndex<0) return null;
		
		if(values==null)
			return selections[selectedIndex];
		else
			return values[selectedIndex];			
	}
	
	public void setValue(Object obj){		
//		if(obj==null) return;
		
		Object selected = obj;
		
		Object[] checkingValues = (values == null ? selections : values);
		
		if(checkingValues!=null)
		for(int i=0; i<checkingValues.length; i++){
			if((selected==null && checkingValues[i]==null) || 
					(selected!=null && selected.equals(checkingValues[i]))){
				((JComboBox)getValueComponent()).setSelectedIndex(i);
				
				return;
			}
		}
		
		//addSelection(obj);
	}

	public Component getNewComponent(){
		JComboBox combo;
		if(selections==null)
			combo = new JComboBox();
		else
			combo = new JComboBox(selections);
			
		combo.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e){
					if(e.getStateChange()!=ItemEvent.SELECTED || ignoreEvents) return;
					onValueChanged();
				}
		});

	
		return combo;
	}


///////////////////// for web ///////////////////////	

	public String getInputterHTML(String section, FieldDescriptor fd, Object defaultValue, Map options){
		String html="<select"+HTML.getAttrHTML("name", "_" + section + "_" + fd.getName())+">\n";
		
		for(int i=0; i<selections.length; i++)
			html+="	<option value='"+ (values!=null && values.length > i ? values[i] : selections[i]) +"'>"+selections[i]+"</option>\n";
	
		html+="</select>";
	
		return html;
	}
	
	public Object createValueFromHTTPRequest(java.util.Map parameterValues, String section, String fieldName, Object oldValue){
		return TextInput.createStringFromHTTPRequest(parameterValues, section, fieldName);
	}
	
}
