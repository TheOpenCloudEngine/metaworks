package org.metaworks.inputter;
	
import org.metaworks.web.*;
import org.metaworks.*;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import java.awt.*;

/**
 * @author Jinyoung Jang
 */

public class TextAreaInput extends InputterAdapter{

	//JPanel inputter;
	transient JTextArea contents;

	int cols;
		public int getCols() {
			return cols;
		}
		public void setCols(int i) {
			cols = i;
		}

	int rows;
		public int getRows() {
			return rows;
		}
		public void setRows(int i) {
			rows = i;
		}

	
		public TextAreaInput(int cols, int rows ){
			super();
			
			setRows(rows);
			setCols(cols);
		}		
		
		public TextAreaInput( ){
			super();
		}		
	
	public Component getNewComponent(){
		
		//inputter = new JPanel();
		//inputter.setLayout( new BorderLayout());
		
		int cols = getCols();
		int rows = getRows();
		
		if(cols==0) cols=30;
		if(rows==0) rows=5;
		
		contents = new JTextArea(rows, cols);
		
		contents.addCaretListener(new CaretListener(){

			public void caretUpdate(CaretEvent e) {
				onValueChanged();
			}
			
		});
	
		//inputter.add( "Center", new JScrollPane(contents));	
		
		return /*inputter*/new JScrollPane(contents);
	}
	
	public Object getValue(){
		
		if( contents.getText().equals("") )
			return null;
		
		return contents.getText();
	}
	
	public void setValue( Object obj){
		
		if( obj !=null){
			contents.setText( obj.toString());
		}
	}
	
	
///////////////////// for web ///////////////////////	

	public String getInputterHTML(String section, FieldDescriptor fd, Object defaultValue, java.util.Map options){
		StringBuffer sb = new StringBuffer();
		sb.append("<textarea");

		sb.append(HTML.getAttrHTML("name", "_" + section + "_" + fd.getName()));
		sb.append(HTML.getAttrHTML("cols", ""+getCols()));
		sb.append(HTML.getAttrHTML("rows", ""+getRows()));
		
		sb.append(">\n");
		
		if(defaultValue!=null)
			sb.append(defaultValue);
		
		sb.append("</textarea>");
	
		return sb.toString();
	}

	public Object createValueFromHTTPRequest(java.util.Map parameterValues, String section, String fieldName, Object oldValue){
		return TextInput.createStringFromHTTPRequest(parameterValues, section, fieldName);
	}
	
	public boolean isEligibleType(Class type) {
		return String.class == type;
	}


}