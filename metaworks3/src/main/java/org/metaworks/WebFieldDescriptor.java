package org.metaworks;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.metaworks.Type;
import org.metaworks.inputter.CalendarInput;
import org.metaworks.inputter.DateInput;
import org.metaworks.inputter.NumberInput;
import org.metaworks.inputter.SelectInput;
import org.metaworks.inputter.TextInput;

public class WebFieldDescriptor {
	
	public WebFieldDescriptor(){}

	public WebFieldDescriptor(org.metaworks.FieldDescriptor oldFd){
		setName(oldFd.getName().substring(0, 1).toLowerCase() + oldFd.getName().substring(1));
		setDisplayName(oldFd.getDisplayName());
		setClassName(oldFd.getClassType().getName());
		
		if(oldFd.getViewer()!=null){
			if(oldFd.getViewer() instanceof WebViewer){
				setViewFace(((WebViewer)oldFd.getViewer()).getFace());

			}else
				setViewFace(oldFd.getViewer().getClass().getName());

		}
		
		if(oldFd.getInputter()!=null && oldFd.getInputter() instanceof SelectInput){
			SelectInput selectInput = (SelectInput) oldFd.getInputter();
			setOptions(selectInput.getSelections());
			setValues(selectInput.getValues());
		}
		
		if(oldFd.getInputter() instanceof FaceInput){
			FaceInput faceInput = (FaceInput)oldFd.getInputter();
			
			setInputFace(faceInput.getFace().ejsPath());
			setOptions(faceInput.getFace().options());
			setValues(faceInput.getFace().values());
		}
		else
		if(oldFd.getInputter()!=null 
				&& !(oldFd.getInputter() instanceof TextInput)
				&& !(oldFd.getInputter() instanceof NumberInput)
				&& !(oldFd.getInputter() instanceof DateInput)
				&& !(oldFd.getInputter() instanceof CalendarInput)
		)
			setInputFace(WebObjectType.getComponentLocationByEscalation( oldFd.getInputter().getClass(), "genericfaces"));
		
		if(oldFd.getAttributeTable()!=null){
			for(Object key : oldFd.getAttributeTable().keySet()){
				Object attr = oldFd.getAttribute((String) key);
				//if(attr instanceof Boolean && ((Boolean)attr).booleanValue() == true){
				if(attributes==null)
					attributes = new HashMap<String, Object>();
				
				attributes.put(((String) key).toLowerCase(), attr);//Boolean.valueOf(true));
				//}
			}
		}
		
	}
	
	String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

	String displayName;
		public String getDisplayName() {
			return displayName;
		}
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

	String className;
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}

	String viewFace;
		public String getViewFace() {
			return viewFace;
		}
		public void setViewFace(String viewFace) {
			this.viewFace = viewFace;
		}

	String inputFace;
		public String getInputFace() {
			return inputFace;
		}
		public void setInputFace(String editFace) {
			this.inputFace = editFace;
		}

	Object[] options;
		public Object[] getOptions() {
			return options;
		}
		public void setOptions(Object[] options) {
			this.options = options;
		}

	Object[] values;
		public Object[] getValues() {
			return values;
		}
		public void setValues(Object[] values) {
			this.values = values;
		}
		
	String defaultValue;
		public String getDefaultValue() {
			return defaultValue;
		}
	
		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

//	Map<String, Boolean> boolOptions;
//		public Map<String, Boolean> getBoolOptions() {
//			return boolOptions;
//		}
//		public void setBoolOptions(Map<String, Boolean> boolOptions) {
//			this.boolOptions = boolOptions;
//		}
		

	Map<String, Object> attributes;
		public Map<String, Object> getAttributes() {
			return attributes;
		}
		public void setAttributes(Map<String, Object> attributes) {
			this.attributes = attributes;
		}

	public Object getAttribute(String name){
		if(attributes != null)
			return attributes.get(name);
		
		return null;
	}
}
