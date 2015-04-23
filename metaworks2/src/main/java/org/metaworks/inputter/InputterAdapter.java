package org.metaworks.inputter;

import java.awt.*;
import java.util.*;

import org.metaworks.inputter.WebInputter;
import org.metaworks.web.HTML;
import org.metaworks.FieldDescriptor;

public class InputterAdapter extends AbstractComponentInputter implements WebInputter{
	
	public InputterAdapter(){
		super();
	}
	

/////////// implemetations //////////////

	public Object getValue(){
		return null;
	}
	
	public void setValue(Object obj){
	}

	public Component getNewComponent(){
		return null;
	}


///////////////////// for web ///////////////////////	

	public String getInputterHTML(String section, FieldDescriptor fd, Object defaultValue, Map options){
		return "";
	}
	
	public Object createValueFromHTTPRequest(Map req, String section, String name, Object oldValue){
		return null;
	}
	
	public Object getValue(String htmlVal){
		return htmlVal;
	}
	
	public void addScriptTo(Properties scriptSet, String section, FieldDescriptor fd, Object defaultValue, Map options){
	}

	public String getViewerHTML(String section, FieldDescriptor fd, Object defaultValue, Map options) {
		if(defaultValue!=null)
			return defaultValue.toString().replaceAll("\r\n|\n","<br/>");
		else
			return "<not set>";
	}
	
	public static String createNameAttribute(String section, String localName){
		return "_" + section + "_" + localName;
	}
	
}
