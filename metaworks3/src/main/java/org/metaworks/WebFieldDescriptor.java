package org.metaworks;

import java.io.Serializable;
import java.util.*;

import org.metaworks.Type;
import org.metaworks.annotation.*;
import org.metaworks.inputter.CalendarInput;
import org.metaworks.inputter.DateInput;
import org.metaworks.inputter.NumberInput;
import org.metaworks.inputter.SelectInput;
import org.metaworks.inputter.TextInput;

public class WebFieldDescriptor implements Serializable{
	
	public WebFieldDescriptor(){}

	public WebFieldDescriptor(org.metaworks.FieldDescriptor oldFd){
		setName(oldFd.getName().substring(0, 1).toLowerCase() + oldFd.getName().substring(1));
		setDisplayName(oldFd.getDisplayName().replaceAll("(.)([A-Z])", "$1 $2"));
		setClassName(oldFd.getClassType().getName());


		if(oldFd.getCollectionClass()!=null){
			setCollectionClass(oldFd.getCollectionClass().getName());
		}


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
					attributes = new Properties();
				
				attributes.put(((String) key).toLowerCase(), attr.toString());//Boolean.valueOf(true));
				//}
			}
		}
		
	}
	
	String name;
	@Order(10)
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

	String displayName;
	@Order(20)
		public String getDisplayName() {
			return displayName;
		}
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

	String className;
	@Order(30)
	@org.metaworks.annotation.Face(options={"vue-component"}, values={"class-selector"})
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}

	String viewFace;
	@Hidden
		public String getViewFace() {
			return viewFace;
		}
		public void setViewFace(String viewFace) {
			this.viewFace = viewFace;
		}

	String inputFace;
	@Hidden
		public String getInputFace() {
			return inputFace;
		}
		public void setInputFace(String editFace) {
			this.inputFace = editFace;
		}

	Object[] options;
	@Hidden
		public Object[] getOptions() {
			return options;
		}
		public void setOptions(Object[] options) {
			this.options = options;
		}

	Object[] values;
	@Hidden
		public Object[] getValues() {
			return values;
		}
		public void setValues(Object[] values) {
			this.values = values;
		}
		
	String defaultValue;
	@Hidden
		public String getDefaultValue() {
			return defaultValue;
		}
	
		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}


	String collectionClass;
	@Hidden
		public String getCollectionClass() {
			return collectionClass;
		}
		public void setCollectionClass(String collectionClass) {
			this.collectionClass = collectionClass;
		}


//	Map<String, Boolean> boolOptions;
//		public Map<String, Boolean> getBoolOptions() {
//			return boolOptions;
//		}
//		public void setBoolOptions(Map<String, Boolean> boolOptions) {
//			this.boolOptions = boolOptions;
//		}
		

	Properties attributes;
	@Hidden
		public Properties getAttributes() {
			return attributes;
		}
		public void setAttributes(Properties attributes) {
			this.attributes = attributes;
		}

	public String getAttribute(String name){
		if(attributes != null)
			return attributes.getProperty(name);
		
		return null;
	}


	public void setAttribute(String propertyName, String s) {

		if(getAttributes()==null) setAttributes(new Properties());

		getAttributes().setProperty(propertyName, s);

	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof WebFieldDescriptor)) return false;

		if(((WebFieldDescriptor)obj).getName().equals(getName())) return true;

		return false;
	}

}
