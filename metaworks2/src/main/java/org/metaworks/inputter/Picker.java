/*
 * Created on 2004-06-04
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.metaworks.inputter;

/**
 * @author Jinyoung Jang
 */
public interface Picker {	
	public void setValue(Object val);
	public Object getValue();
	public boolean showPicker();
	public java.awt.Component getComponent();
}
