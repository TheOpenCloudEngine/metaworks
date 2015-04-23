package org.metaworks.inputter;

import org.metaworks.*;
import org.metaworks.component.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import com.pongsor.swing.*;
import com.pongsor.swing.Painter;

import java.sql.*;

public class TableReferenceInput extends AbstractComponentInputter{
	
	Type referenceTable;
	
		public Type getReferenceTable(){
			return referenceTable;
		}
			
		public void setReferenceTable(Type value){
			referenceTable = value;
		}
		
	String referenceFieldName;
	
		public String getReferenceFieldName(){
			return referenceFieldName;
		}
			
		public void setReferenceFieldName(String value){
			referenceFieldName = value;
		}
		
	DefaultApplication referenceTableApplication;
	
		public DefaultApplication getReferenceTableApplication(){
			return referenceTableApplication;
		}

	
	Instance selectedRec=null;
	
	public TableReferenceInput(Type table){
		setReferenceTable(table);
	}
	
	public TableReferenceInput(){
	}
	
	public Object getValue(){
		try{
			return getReferenceTableApplication().getSelectedRecord().getFieldValue(getReferenceFieldName());
		}catch(Exception e){
			return null;
		}
	}
	
	public void setValue(Object data){
		if(data==null) return;		
		selectedRec = (Instance)data;
	
		((AbstractPopupButton)getComponent()).setText(""+data);
	}
	
	public Component getNewComponent(){
		if(isMandatory())
			return new ReferenceInputComponent("Not Set"){
				public void paint(Graphics g){
           				super.paint(g);
           				Painter.paintIsRequired(this, g);
           			}
        		};
		else
		      	return new ReferenceInputComponent("Not Set");      		
	}
	
	private class ReferenceInputComponent extends AbstractPopupButton{
		ReferenceInputComponent(String msg){
			super(msg);
		}
		public void initPopupWindow(){
			DefaultApplication refTabApp = new DefaultApplication(getReferenceTable());			
			referenceTableApplication = refTabApp;
			getPanel().add("Center", refTabApp.createPanel());
			refTabApp.setConnection(MetaWorksComponentCenter.getInstance().getConnection());
			refTabApp.run();
			
			setPopupTitle("Select");
		}
		public void postDown(){
			super.postDown();
			setText(getValue().toString());
		}
	}		    
}


