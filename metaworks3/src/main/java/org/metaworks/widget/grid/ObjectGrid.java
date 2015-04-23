package org.metaworks.widget.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.metaworks.FieldDescriptor;
import org.metaworks.ObjectType;
import org.metaworks.WebObjectType;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;


public class ObjectGrid extends Grid{
	Class dataClass;
	
	String className;
		
		public String getClassName() {
			return className;
		}
	
		public void setClassName(String className) {
			this.className = className;
		}

	public ObjectGrid() throws Exception{	
	}
	
	public ObjectType setObjectType(Class dataClass) throws Exception{
		
		setClassName(dataClass.getName());
		
		WebObjectType wot = MetaworksRemoteService.getInstance().getMetaworksType(dataClass.getName());
		ObjectType type = (ObjectType) wot.metaworks2Type();
		
		setKeyColumn(wot.getKeyFieldDescriptor().getName());
		
		columnModel = new HashMap<String, Column>();
		
		List<String> columnNamesInList = new ArrayList<String>();
//		Map<String, Method> getters = new HashMap<String, Method>();
		
		
		for(int i=0; i<type.getFieldDescriptors().length; i++){
			
			
			String fieldName = wot.getFieldDescriptors()[i].getName();
			
			if(!fieldName.equals("metaworksContext")){
				String displayName = wot.getFieldDescriptors()[i].getName();
				
				FieldDescriptor fd = type.getFieldDescriptors()[i];
				
				//if(fd.getAttribute("hidden"))
				columnNamesInList.add(fieldName);
				
				Column column = new Column();
				column.setName(fieldName);
	//			column.setIndex(displayName);
				column.setEditable(fd.isUpdatable());
	//			column.setWidth(fd.getAttribute("size"));
				column.setIndex(fieldName);
				
				columnModel.put(fieldName, column);
			}
			
			
			
//			getters.put(fd.getName(), dataClass.getMethod("get" + fd.getName(), parameterTypes))
		}
		
		columnNames = new String[columnNamesInList.size()];
		columnNamesInList.toArray(columnNames); 
		
		return type;
	}
	
	public void setObjectData(List list) throws Exception{
		Class dataClass = list.get(0).getClass();
		
		ObjectType type = setObjectType(dataClass);
		
		ArrayList<Object> arrData = new ArrayList<Object>();
		for(int i=0; i<list.size(); i++){
			arrData.add(list.get(i));
		}
		
		setData(arrData);
	}

	@ServiceMethod
	public void init() {
		
		ArrayList list = new ArrayList();
		
//		Person p = new Person();
//		p.setAge(10);
//		p.setName("Jinyoung Jang");
//		list.add(p);
//		list.add(p);
//		list.add(p);
//		
		try {
			setObjectData(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
