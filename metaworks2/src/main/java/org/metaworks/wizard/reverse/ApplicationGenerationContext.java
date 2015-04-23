package org.metaworks.wizard.reverse;


import java.util.*;
import org.metaworks.*;

public class ApplicationGenerationContext{

	Class applicationClass;
	
		public Class getApplicationClass(){
			return applicationClass;
		}
			
		public void setApplicationClass(Class value){
			applicationClass = value;
		}	
	
	Type table;
	
		public Type getTable(){
			return table;
		}
			
		public void setTable(Type value){
			table = value;
		}
		
	Map propertyMap;
	
		public Map getPropertyMap(){
			return propertyMap;
		}
			
		public void setPropertyMap(Map value){
			propertyMap = value;
		}
	
	
		public void addProperty(String propName, Object value){
			if(propertyMap==null) propertyMap = new Hashtable();
			propertyMap.put(propName, value);
		}
		
	String projectName;
	
		public String getProjectName(){
			return projectName;
		}
			
		public void setProjectName(String value){
			projectName = value;
		}
	
	String applicationName;
	
		public String getApplicationName(){
			return applicationName;
		}
			
		public void setApplicationName(String value){
			applicationName = value;
		}
		
	public static void main(String[] args) throws Exception{
		ApplicationGenerationContext applicationGenerationContext = new ApplicationGenerationContext();
		
		applicationGenerationContext.setApplicationClass(Thread.currentThread().getContextClassLoader().loadClass("com.pongsor.dosql.DefaultApplication"));
		applicationGenerationContext.setProjectName("projectName");
		applicationGenerationContext.setApplicationName("tableNameApp");
		Type table = new Type();
		table.setComponentName("tables.projectName.tableName");
		applicationGenerationContext.setTable(table);	
		applicationGenerationContext.addProperty("title", "DoSQL application");	
	
		java.io.FileOutputStream os = new java.io.FileOutputStream(args[0]);
		org.metaworks.component.MetaWorksComponentCenter.serialize(applicationGenerationContext, os);
	}

}