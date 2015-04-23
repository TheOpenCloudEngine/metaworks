package org.metaworks.common;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.metaworks.FieldDescriptor;
import org.metaworks.ObjectInstance;
import org.metaworks.WebObjectType;
import org.metaworks.dwr.MetaworksRemoteService;

import org.metaworks.Refresh;

public class MetaworksUtil {

	static public void copyStream(InputStream sourceInputStream, OutputStream targetOutputStream) throws Exception{
		int length = 1024;
		byte[] bytes = new byte[length]; 
		int c; 
		int total_bytes=0;
			
		try{
			while ((c = sourceInputStream.read(bytes)) != -1) { 
					total_bytes +=c; 
					targetOutputStream.write(bytes,0,c); 
			} 
		}finally{
			if (sourceInputStream != null) try { sourceInputStream.close(); } catch (Exception e) {}
			if (targetOutputStream != null) try { targetOutputStream.close(); } catch (Exception e) {}
		}
	}
	
	static public Object[] makeRefreshObjectArray(Object[] array){
		for (int i = 0; i < array.length; i++) {
			array[i] = new Refresh(array[i]);
		}
		
		return array;
	}
	
	static public Object[] putObjectArray(Object[] array, Object object){
		Object[] returnObject = new Object[ array.length + 1];

		for (int i = 0; i < array.length; i++) {
			returnObject[i] = array[i];
		}
		returnObject[returnObject.length - 1] = object;
		
		return returnObject;
	}
	
	public static Class getDesiredTypeByTypeSelector(Object object) throws Exception{
		WebObjectType wot = MetaworksRemoteService.getInstance().getMetaworksType(object.getClass().getName());
		for(FieldDescriptor fd : wot.metaworks2Type().getFieldDescriptors()){
			Map<String, String> typeSelector = (Map<String, String>) fd.getAttribute("typeSelector");
			if(typeSelector!=null){
				ObjectInstance objInst = (ObjectInstance) wot.metaworks2Type().createInstance();
				objInst.setObject(object);
				
				String typeName = (String) objInst.getFieldValue(fd.getName());
				String selectedTypeClassName = typeSelector.get(typeName);
				
				if(selectedTypeClassName==null)
					return null;
				
				return Thread.currentThread().getContextClassLoader().loadClass(selectedTypeClassName);
			}
		}
		
		return null;
	}
	
	public static Object cast(Object object, Class<?> desiredType) throws Exception{
		
		WebObjectType wot = MetaworksRemoteService.getInstance().getMetaworksType(object.getClass().getName());
		WebObjectType desiredWot = MetaworksRemoteService.getInstance().getMetaworksType(desiredType.getName());
		
		ObjectInstance desiredInstance = (ObjectInstance) desiredWot.metaworks2Type().createInstance();

		ObjectInstance objInst = (ObjectInstance) wot.metaworks2Type().createInstance();
		objInst.setObject(object);

		for(FieldDescriptor fd : desiredWot.metaworks2Type().getFieldDescriptors()){
			if(fd.getAttribute("ormapping")==null)
				desiredInstance.setFieldValue(fd.getName(), objInst.getFieldValue(fd.name));
		}

		return desiredInstance.getObject();
	}
}
