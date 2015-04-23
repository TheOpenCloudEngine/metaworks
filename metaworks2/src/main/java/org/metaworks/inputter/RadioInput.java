package org.metaworks.inputter;

import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.util.*;

import org.metaworks.web.HTML;
import org.metaworks.*;


public class RadioInput extends InputterAdapter{

	String [] selections;
		public String[] getSelections() {
			return selections;
		}
		public void setSelections(String[] strings) {
			selections = strings;
			if(values==null)
				values = strings;
		}
	
	Object [] values=null;
		public Object[] getValues() {
			return values;
		}
		public void setValues(Object[] values) {
			this.values = values;
		}

	
	transient Vector selButtons;
	transient int selectedIndex;
	
	boolean alignHorizontally = true;
		public boolean isAlignHorizontally() {
			return alignHorizontally;
		}
		public void setAlignHorizontally(boolean alignHorizontally) {
			this.alignHorizontally = alignHorizontally;
		}
		
		
	public RadioInput(){
		this(null);
	}
	
	public RadioInput(String [] selections){
		this(selections, selections);
	}
	
	public RadioInput(String [] selections, Object [] values){
		super();
		this.selections = selections;
		this.values=values;
	}
	

/////////// implemetations //////////////

	public Object getValue(){
		if(selectedIndex<0) return null;
		
		if(values==null)
			return selections[selectedIndex];
		else
			return values[selectedIndex];			
	}
	
	public void setValue(Object selected){
		//if(selected == null) return;
		
		for(int i=0; i<selections.length; i++)
			if((selected==null && values[i]==null) || (selected!=null && selected.equals(values[i]))){
	
				JRadioButton but = ((JRadioButton)selButtons.elementAt(i));
				
				but.setSelected(true);
			
				return;
			}

	}

	public Component getNewComponent(){
//		System.out.println("new");

		JPanel panel = new JPanel(){
			public void setEnabled(boolean enabled) {
				
				super.setEnabled(enabled);
				if(selButtons!=null){
					for(int i=0; i<selButtons.size(); i++){
						((JComponent)selButtons.get(i)).setEnabled(enabled);
					}
				}
			}
		};

		if(isAlignHorizontally()){
			panel.setLayout(new FlowLayout());
		}else{
			BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
			panel.setLayout(bl);
		}

		ButtonGroup buttonGroup = new ButtonGroup();

		selectedIndex=-1;		
	
		selButtons=new Vector();
		
		RadioListener radioListener = new RadioListener();
		
		for(int i=0; i<selections.length; i++){
			JRadioButton radio = new JRadioButton(selections[i]);
			
			radio.setActionCommand(""+i);			
			radio.addActionListener(radioListener);			
			panel.add(radio);
			buttonGroup.add(radio);
			selButtons.add(radio);
		}		
		
		return panel;
	}
	
/////////// private //////////////////
	
    private class RadioListener implements ActionListener { 
        public void actionPerformed(ActionEvent e) {
            selectedIndex= Integer.parseInt(e.getActionCommand());
            
            //System.out.println(selectedIndex);
            onValueChanged();
        }
    }
    
//////////////////// for web ////////////////////////


	public String getInputterHTML(String section, FieldDescriptor fd, Object defaultValue, Map options){
		
		String nameAttr = HTML.getAttrHTML("name", "_" + section + "_" + fd.getName());
		StringBuffer html=new StringBuffer();
		
		for(int i=0; i<selections.length; i++){			
			if(!isAlignHorizontally() && i>0) html.append("<br>");
			html.append("<input type=radio "+nameAttr + " value='"+ selections[i] +"'" + (values[i].equals(defaultValue) ? " checked":"") + ">"+(selections!=null ? selections[i]:values[i])+"\n");
		}
		
		return html.toString();
	}

	public Object createValueFromHTTPRequest(java.util.Map parameterValues, String section, String fieldName, Object oldValue){
		String text = TextInput.createStringFromHTTPRequest(parameterValues, section, fieldName);
		
		for(int i=0; i<selections.length; i++){
			if(selections[i].equals(text))
				return values[i];
		}
		
		return null;
	}
	
	public String getViewerHTML(String section, FieldDescriptor fd, Object defaultValue, Map options) {
		for(int i=0; i<selections.length; i++){			
			if(values[i].equals(defaultValue)) return selections[i];
		}
		
		return "<None>";
	}


}
