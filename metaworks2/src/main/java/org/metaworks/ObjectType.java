package org.metaworks;

import java.lang.reflect.*;
import org.metaworks.inputter.*;
import org.metaworks.tutorial.objecttable.Person4;

import java.util.*;

public class ObjectType extends Type{
	
	static List defaultInputterPackages = new ArrayList();
	static{
		defaultInputterPackages.add("org.metaworks.inputter"); 
	}
	
	static Map inputterClsCache = new HashMap(50);
	
	boolean isPrimitive = false;
		public boolean isPrimitive() {
			return isPrimitive;
		}
		public void setPrimitive(boolean b) {
			isPrimitive = b;
		}

	Class type = null;
		public Class getClassType(){
			return type;
		}
		
	Hashtable setterMethods;
	    public Hashtable getSetterMethods() {
			return setterMethods;
		}
		public void setSetterMethods(Hashtable setterMethods) {
			this.setterMethods = setterMethods;
		}
		
		
	public ObjectType(Class actCls) throws Exception{
        super(actCls.getName());
        
        type = actCls;
        boolean bThereIsField = false;
        
        if(isPrimitive(type)){
        	FieldDescriptor fd = new FieldDescriptor("value", " ");
        	fd.setType(type);
        	
            setFieldDescriptor(fd);
            
            setPrimitive(true);
        }else{
        	
        	setSetterMethods(new Hashtable());
        	
            Method [] methods = actCls.getMethods();
            
            for(int i=0; i<methods.length; i++){
                    
                    if(     (methods[i].getName().startsWith("get") || methods[i].getName().startsWith("is")) 
                            && (methods[i].getParameterTypes().length==0)
                    ){
                            String fieldName = methods[i].getName();
                            
                            int starting = (methods[i].getName().startsWith("is") ? 2 : 3);
                            fieldName = fieldName.substring(starting, fieldName.length());
                            Class retType = methods[i].getReturnType();
                            
                            try{
                            	Method setter;
                                    if((setter = actCls.getMethod("set" + fieldName, new Class[]{retType})) != null){
                                    	
                                    	setterMethods.put(setter.getName(), setter);

                                    	FieldDescriptor fd = new FieldDescriptor( fieldName, fieldName);
					                	fd.setType(retType);

										if(setter.getDeclaringClass()==actCls){
											fd.setAttribute("extended", true);
										}

										try{
                                        	Inputter inputter = getDefaultInputter(retType);

                                        	fd.setInputter(inputter);
                                        }catch(Exception e){
                                        	//e.printStackTrace();
                                        	
                                        	if(retType== String.class)
                                        		fd.setInputter(new TextInput());
											else
	                                		if(retType.isArray()){
												String elementClsName = retType.getName();
												elementClsName = elementClsName.substring(2, elementClsName.length()-1);
	                                			
	                                			fd.setInputter(new ArrayObjectInput(Thread.currentThread().getContextClassLoader().loadClass(elementClsName)));
	                                		}else{
	                                			fd.setInputter(new ObjectInput(retType));
	                                		}
                                    	}
                                	    
                                	    bThereIsField = true;                                  	
                                        setFieldDescriptor(fd);
                                    }
                            }catch(Exception e){}
                            
                    }
            }
			
			if(!bThereIsField){                
				FieldDescriptor fd = new FieldDescriptor("value", "value");
				fd.setType(type);
				setFieldDescriptor(fd);
				setPrimitive(true);
			}
			
			try{
				Method m = actCls.getMethod("metaworksCallback_changeMetadata", new Class[]{Type.class});
				m.invoke(null, new Object[]{this});
			}catch(java.lang.NoSuchMethodException nsme){
				try{
					Method m = actCls.getMethod("metaworksCallback_changeMetadata", new Class[]{ObjectType.class});
					m.invoke(null, new Object[]{this});
				}catch(java.lang.NoSuchMethodException nsme2){
				}catch(Exception e){
					e.printStackTrace();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
    }
    
    public void save(Instance rec){
    }

    public void update(Instance rec){
            save(rec);
    }
    
    public Instance createInstance(){
    	try{
    		return new ObjectInstance(this/*, type.newInstance()*/);
    	}catch(Exception e){
    		e.printStackTrace();
    		
    		return null;
    	}
    }
    
////// private methods /////////////////

/*        protected static Inputter getDefaultInputter(Class type) throws Exception{
        	String inputterName = type.getName().replace('.', '$');
        
        	Class inputterCls = Thread.currentThread().getContextClassLoader().loadClass("com.hyunse.uflow.processdesigner.inputters." + inputterName);
        	
        	Inputter inputter = inputterCls.newInstance();
        	
        	return inputter;
        }
*/
	public static boolean isPrimitive(Class type){
	
/*		return 	type.isPrimitive() || 
				type == String.class || 
				type == Integer.class || 
				type == Double.class || 
				type == Long.class;*/
				
		//review: totally inefficient			
		return (FieldDescriptor.getDefaultInputter(FieldDescriptor.getMappingTypeOfClass(type)) != null);
	}



	public static void main(String[] args) throws Exception{
//		System.out.println("ObjectTable.newRecord() = " + (new ObjectTable(String.class)).newRecord());
//		System.out.println("ObjectTable.newRecord() = " + (new ObjectType(String.class)).getFieldDescriptors());

		Type personTable = new ObjectType(Person4.class);
		Instance personRecord = personTable.createInstance();
		
		InputForm inputFormForPerson = new InputForm(personTable);
		inputFormForPerson.postInputDialog(null);
		
		System.out.println(((ObjectInstance)inputFormForPerson.getInstance()).getObject());

		
		DefaultApplication defaultApplicationForPerson = new DefaultApplication(personTable);
		defaultApplicationForPerson.runFrame();

		GridApplication gridApplicationForPerson = new GridApplication(personTable);
		gridApplicationForPerson.runFrame();

	}

	public static Inputter getDefaultInputter(Class type) throws Exception{
			
		Class inputterCls = null; 
		Class orgType = type;
		String typeNameOnlyIfArray = null;
    	boolean isArray = type.isArray();  
		
		
		if(!inputterClsCache.containsKey(type)){
			do{ //try to find proper inputter by escalation

				
				String inputterName = type.getName();

				if(inputterName.startsWith("[L")){
					typeNameOnlyIfArray = inputterName.substring(2, inputterName.length()-1);
		        	inputterName = typeNameOnlyIfArray + "ArrayInput";	
				}else
					inputterName = inputterName + "Input";

				inputterName = inputterName.replace('.', '_');
				

//	System.out.println("finding inputter = " + inputterName);	
	
				for(Iterator iter = defaultInputterPackages.iterator(); inputterCls==null && iter.hasNext(); ){
					String pkgName = (String)iter.next();
					
					try{
						inputterCls = Thread.currentThread().getContextClassLoader().loadClass(pkgName + "." + inputterName);
					}catch(Exception e){
					}					
				}

				type = type.getSuperclass();
				//System.out.println("in while");
			}while(inputterCls==null && type!=Object.class);
			
			if(inputterCls!=null)
				inputterClsCache.put(orgType, inputterCls);
			else{
				if(isArray)
					return new ArrayObjectInput(Thread.currentThread().getContextClassLoader().loadClass(typeNameOnlyIfArray));
				else{
					Inputter defInputter = FieldDescriptor.getDefaultInputter(orgType);
					
					if(defInputter==null) //if there's no proper inputter finally, return generic Inputter.
						return new ObjectInput(orgType);
					else
						return defInputter;
				}
				
				//inputterClsCache.put(orgType, ObjectInput.class); //use generic inputter if there's no proper one.
				//throw new Exception("can't find proper inputter.");
				
			}
		}
		
		inputterCls = (Class)inputterClsCache.get(orgType);			
		Inputter inputter = (Inputter)inputterCls.newInstance();
		
//disabled : may occur recursive call
//		if(inputter instanceof ObjectInput){
//			((ObjectInput)inputter).setType(type);
//		}
				
		return inputter;
	}	
	
	public static void addInputterPackage(String pkgName){
		if(!defaultInputterPackages.contains(pkgName))
			defaultInputterPackages.add(pkgName);
	}
	
}