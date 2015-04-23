package org.metaworks;

import org.metaworks.ui.*;
import org.metaworks.inputter.*;
import org.metaworks.*;

import java.sql.Connection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MasterSubApplication extends DefaultApplication{

	Application subTableApplication;
	
		public Application getSubTableApplication(){
			return subTableApplication;
		}
			
		public void setSubTableApplication(Application value){
			subTableApplication = value;
		}
	
	Type subTable;
	
		public Type getSubTable(){
			return subTable;
		}
			
		public void setSubTable(Type value){
			subTable = value;
			
			DefaultApplication app2 = new DefaultApplication();
			app2.setType(getSubTable());	
			setSubTableApplication(app2);
		}

	public MasterSubApplication(Type masterTable, Type subTable){
		this();
		
		setType(table);
		setSubTable(subTable);
	}	
	
	public MasterSubApplication(){
		super();
	}	
		
	public JPanel createPanel(){
		JPanel p = new JPanel(new BorderLayout());
		JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, super.createPanel(), getSubTableApplication().createPanel());
		pane.setDividerLocation(100);
		p.add("Center", pane);
	
		return p;
	}
	
	public void setConnection(Connection value){
		super.setConnection(value);
		((DefaultApplication)getSubTableApplication()).setConnection(value);
	}
	
	public void run(){
		super.run();
		//method 'run' should be upper
		((DefaultApplication)getSubTableApplication()).run();
	}

}
