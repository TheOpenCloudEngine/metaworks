package org.metaworks.web;

import org.metaworks.*;
import org.metaworks.inputter.WebStringDecoder;

import java.util.*;

public class WebUtil{
	
	public static WebStringDecoder webStringDecoder = null;

  public static Instance createInstance(Type table, Map request, String section, Map validationResult){
	Instance puttingRecord = table.createInstance();
	
	FieldDescriptor[] fds = table.getFieldDescriptors();
System.out.println("fds.length = " + fds.length);
        for(int i=0; i<fds.length; i++) {
        	FieldDescriptor fieldDescriptor = fds[i];
        	
            String name = fieldDescriptor.getName();
System.out.println("name = " + name);                  
			   //review: Somewhat wierd  
//          String value = ((String[])request.get("_"+ section + "_" + name))[0];
            Object objValue = null;
          
            try{
//            	value = new String(value.getBytes("8859_1"), "KSC5601");
//            	if(value.trim().length()==0) value=null;

				objValue = table.getFieldDescriptor(name).getWebInputter().createValueFromHTTPRequest(request, section, name, null);
				
				if(validationResult!=null){
					String[] validation = fds[i].validate(objValue, puttingRecord);
					if(validation!=null)
						validationResult.put(name, validation);
				}
System.out.println("converted val : "+objValue);

            }catch(Exception exc){exc.printStackTrace();}
            
            puttingRecord.setFieldValue(name, objValue);
        }
        
        return puttingRecord;
   }
  
  public static String createScriptBodyFromScriptSet(Properties scriptSet){
	  StringBuffer sb = new StringBuffer();
	  for(Enumeration scriptKeys = scriptSet.keys(); scriptKeys.hasMoreElements(); ){
		  String scriptKey = (String)scriptKeys.nextElement();
		  sb.append(scriptSet.get(scriptKey));
	  }
	  
	  return sb.toString();
  }
  
  static public String decode(String src){
	  if(webStringDecoder!=null)
		  return webStringDecoder.decode(src);
	  else
		  return src;
  }

   
	public static void main(String[] args) throws Exception{
		Hashtable testReq = new Hashtable();
		testReq.put("__Name", new String[]{"jjy"});
		testReq.put("__Address", new String[]{"ssdfasdfA"});
		testReq.put("__Age", new String[]{"123A"});
		
		HashMap validationResult = new HashMap();
		
		ObjectInstance rec = (ObjectInstance)WebUtil.createInstance(new ObjectType(TestClass.class), testReq, "", validationResult);
		
		System.out.println("rec.getObject() = " + rec.getObject());
		
		System.out.println("rec.getClass() = " + rec.getClass());
	}
	
	public static void setWebStringDecoder(WebStringDecoder decoder){
		webStringDecoder = decoder;
	}

}