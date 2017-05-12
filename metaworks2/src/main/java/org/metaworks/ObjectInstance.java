package org.metaworks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

public class ObjectInstance extends Instance{

	Hashtable setterMethods = new Hashtable();
	boolean isPrimitive = false;	//review: actually this flag means that the object is not a compound type.
	
	Object obj;
	
		public Object getObject(){
/*			if(obj==null)
			try{
				setObject(((ObjectType)getType()).getClassType().newInstance());
			}catch(Exception e){			
			}
*/			
			return obj;
		}
		
		public void setObject(Object value){
			obj = value;
			
			if(!isPrimitive){
				setterMethods.clear();
				Method [] allMethods = ((ObjectType)getType()).getClassType().getMethods();
				for(int i=0; i<allMethods.length; i++){
					Method thisMethod = allMethods[i];
					if(thisMethod.getName().startsWith("set") && thisMethod.getParameterTypes().length==1){
						setterMethods.put(thisMethod.getName(), thisMethod);
					}
				}
			}
			
			
//** cached globally for performance **
			//			if(isPrimitive){			
//				obj = value;
//			}else{
//				setterMethods.clear();
//				Method [] allMethods = ((ObjectType)getType()).getClassType().getMethods();
//				for(int i=0; i<allMethods.length; i++){
//					Method thisMethod = allMethods[i];
//					if(thisMethod.getName().startsWith("set") && thisMethod.getParameterTypes().length==1){
//						setterMethods.put(thisMethod.getName(), thisMethod);
//					}
//				}
//			}
			//***** end ******
		}

////////////////
	public ObjectInstance(ObjectType table, Object obj){
		super(table);
		setObject(obj);

		Class type = table.getClassType();
		isPrimitive = table.isPrimitive();
	}

	public ObjectInstance(ObjectType table){
		this(table, null);
	}
	
	public void setFieldValue(String keyStr, Object val){
		if(isPrimitive){
			setObject(val);
			return;
		}
		
		if(obj==null)
			try{
				setObject(((ObjectType)getType()).getClassType().newInstance());
			}catch(Exception e){			
			}

		try{
			
			Hashtable setterMethods = ((ObjectType)getType()).getSetterMethods();
			
			((Method)(setterMethods).get("set"+keyStr)).invoke(getObject(), new Object[]{val});
		}catch(Exception e){
System.err.println("[Exception in ObjectInstance::set] value = " +val + ", field = " + keyStr);
System.err.println("	type = " + ((ObjectType)getType()).getClassType() + "  exception = " + e.getMessage());

			//
			if(e instanceof IllegalArgumentException){
				Class type = ((ObjectType)getType()).getClassType();
				Class currObjType = getObject().getClass();
				if(type!=currObjType && currObjType.isAssignableFrom(type)){
					setObject(null);
					try {
						//TODO: actually all the fields should be reset
						((Method)(setterMethods).get("set"+keyStr)).invoke(getObject(), new Object[]{val});
					} catch (IllegalArgumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InvocationTargetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}

			}
		}
	}
	
	public Object getFieldValueObject(String keyStr){
		if(isPrimitive) return getObject();
		
		try{
			return getObject().getClass().getMethod("get"+keyStr, new Class[]{}).invoke(getObject(), new Object[]{});
		}catch(Exception e){}

		try{
			return getObject().getClass().getMethod("is"+keyStr, new Class[]{}).invoke(getObject(), new Object[]{});
		}catch(Exception e){}
		
		return null;
	}
	
}