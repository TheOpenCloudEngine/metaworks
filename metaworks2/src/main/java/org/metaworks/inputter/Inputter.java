package org.metaworks.inputter;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public interface Inputter extends java.io.Serializable{

	public Object getValue();
	public void setValue(Object data);
	public Component getComponent();
	public void initialize(Hashtable attrs);	
	public void addActionListener(ActionListener al);
	public boolean isEligibleType(Class type);
}
