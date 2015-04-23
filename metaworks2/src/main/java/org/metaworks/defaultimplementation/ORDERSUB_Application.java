package org.metaworks.defaultimplementation;

import org.metaworks.*;
import javax.swing.*;

public class ORDERSUB_Application extends DefaultApplication{

	public ORDERSUB_Application(){
		super();	
	}
	
	public JPanel createPanel(){
		JPanel p = super.createPanel();
		getMenuPanel().add(new JButton("Excel"));
		
		return p;
	}
}
