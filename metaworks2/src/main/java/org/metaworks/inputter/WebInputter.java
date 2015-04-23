package org.metaworks.inputter;

import java.util.*;
import org.metaworks.inputter.Inputter;
import org.metaworks.FieldDescriptor;

public interface WebInputter extends Inputter{

	public String getInputterHTML(String section, FieldDescriptor fd, Object defaultValue, Map options);
	public String getViewerHTML(String section, FieldDescriptor fd, Object defaultValue, Map options);
	public Object createValueFromHTTPRequest(Map req, String section, String name, Object oldValue);
	public void addScriptTo(Properties scriptSet, String section, FieldDescriptor fd, Object defaultValue, Map options);
}
