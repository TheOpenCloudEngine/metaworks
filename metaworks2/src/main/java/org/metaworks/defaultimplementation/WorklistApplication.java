package org.metaworks.defaultimplementation;

import org.metaworks.*;
import javax.swing.*;

public class WorklistApplication extends DefaultApplication{

	public WorklistApplication(){
		super();	
	}	
	
	public void open(){
		Instance rec;
		final int sel = getSelectedIndex();
		if((rec=getSelectedRecord())==null) return;
		
		String appName = (String)rec.getFieldValue("APPLICATION");

		DefaultDesktop.getInstance().launchApplication(appName);		
	}
	
	public void run(){
		try{
			setSQL("select * from " + getType().getName() + " order by dueDate desc");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public JPanel createPanel(){
		JPanel p = super.createPanel();
		getMenuPanel().removeAll();
		
		return p;
	}
}
