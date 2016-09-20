package org.metaworks.dwr;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import javax.servlet.ServletContext;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;
//import org.javatuples.Tuple;
//import org.javatuples.Unit;
import org.metaworks.*;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.dao.ConnectionFactory;
import org.metaworks.dao.IDAO;
import org.metaworks.dao.TransactionContext;
import org.metaworks.i18n.MultilingualSupport;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MetaworksRemoteService {
	public static final String AUTOWIRED_FROM_CLIENT_CLASS_MAP = "autowiredFromClientClassMap";

	public static long initTimestamp = System.currentTimeMillis();

//    public static ThreadLocal<String> callingObjectTypeName = new ThreadLocal<String>();

	static Hashtable<String, WebObjectType> metadataStorage = new Hashtable<String, WebObjectType>();
	
	protected static MetaworksRemoteService instance;

	protected static void setInstance(MetaworksRemoteService instance) {
		MetaworksRemoteService.instance = instance;
	}
	public static MetaworksRemoteService getInstance() {

		if(instance==null){

				instance = new MetaworksRemoteService();
		}

		return instance;
	}
	
	public static void pushTargetScript(String sessionId, final String script, final Object[] object){
		 Browser.withSession(sessionId, new Runnable(){
			   @Override
			   public void run() {
				   ScriptSessions.addFunctionCall(script, object);
			   }			  
		});
	}
	
	public static void pushTargetScriptFiltered(ScriptSessionFilter filter, final String script, final Object[] object){
		
		Browser.withAllSessionsFiltered(
				filter,
				new Runnable(){
					@Override
					public void run() {
						ScriptSessions.addFunctionCall(script, object);
					}			  
				});
	}
	
	public static void pushClientObjects(final Object[] object){
		 Browser.withAllSessions(new Runnable(){
			   @Override
			   public void run() {
			    ScriptSessions.addFunctionCall("mw3.locateObject", new Object[]{object, null, "body"});
			    ScriptSessions.addFunctionCall("mw3.onLoadFaceHelperScript", new Object[]{});

			   }			  
		});
	}
	
	public static void pushTargetClientObjects(String sessionId, final Object[] object){
		 Browser.withSession(sessionId, new Runnable(){
			   @Override
			   public void run() {
			    ScriptSessions.addFunctionCall("mw3.locateObject", new Object[]{object, null, "body"});
			    ScriptSessions.addFunctionCall("mw3.onLoadFaceHelperScript", new Object[]{});

			   }			  
		});
	}
		
	public static void pushOtherClientObjects(final String sessionId, final Object[] object){
		
		Browser.withAllSessionsFiltered(
				new ScriptSessionFilter(){
					@Override
					public boolean match(ScriptSession session)
					{
						if(session.getId().equals(sessionId))
							return false;
						else
							return true;
					}			
				},

				new Runnable(){
					@Override
					public void run() {
						ScriptSessions.addFunctionCall("mw3.locateObject", new Object[]{object, null, "body"});
						ScriptSessions.addFunctionCall("mw3.onLoadFaceHelperScript", new Object[]{});

					}			  
				});
	}
	
	public static void pushClientObjectsFiltered(ScriptSessionFilter filter, final Object[] object){
		
		Browser.withAllSessionsFiltered(
				filter,

				new Runnable(){
					@Override
					public void run() {
						ScriptSessions.addFunctionCall("mw3.locateObject", new Object[]{object, null, "body"});
						ScriptSessions.addFunctionCall("mw3.onLoadFaceHelperScript", new Object[]{});

					}			  
				});
	}

	public String getApplicationVersion() throws Exception{

		Properties prop = new Properties();

		InputStream manifest = getClass().getClassLoader().getResourceAsStream("/META-INF/MANIFEST.MF");
		if(manifest==null){
			manifest = ServerContextFactory.get().getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF");

		}

		if(manifest ==null) return String.valueOf(initTimestamp); //if all failed, just return the server started time.

		prop.load(manifest);

		String version = prop.getProperty("Implementation-Build");

		if(version == null)
			version = prop.getProperty("Implementation-Version");

		return version;
	}
	
	public void clearMetaworksType(String className) throws Exception {
		
		if("*".equals(className))
			metadataStorage.clear();
		
		if(metadataStorage.containsKey(className))
			metadataStorage.remove(className);
		
		
		
		////////// alert to other session users :  COMET //////////
		
		WebContext wctx = WebContextFactory.get();
		String currentPage = wctx.getCurrentPage();

	   // For all the browsers on the current page:
	   Collection sessions = wctx.getScriptSessionsByPage(currentPage);

	   //TODO: filter other topic's postings;
	   Util utilAll = new Util(sessions);
	   utilAll.addFunctionCall("mw3.clearMetaworksType('"+ className +"')");

	}


	public WebObjectType getMetaworksType(String className) throws Exception {
		try{
				
			//TODO: this is debug mode option
			if(metadataStorage.containsKey(className))
				return metadataStorage.get(className);
			
			
		//	if(className.length() == 0) return null;
			WebObjectType objType = new WebObjectType(Thread.currentThread().getContextClassLoader().loadClass(className));

			
			
			metadataStorage.put(className, objType);

			//TODO: this will be better performance but, it is little different information between realization object and DAO's field 
//			if(objType.iDAOClass()!=null)
//				metadataStorage.put(objType.iDAOClass().getName(), objType);

			
			return objType;
		}catch(Exception e){
			//e.printStackTrace();

			throw new Exception("viewer tried to get metadata for class "+ className +" but failed.", e);
		}
	}
	
	public ArrayList<WebObjectType> getMetaworksTypes(String[] classNames) throws Exception {
		
		ArrayList<WebObjectType> metadataArray = new ArrayList<WebObjectType>();
		
		for(int i=0; i<classNames.length; i++){
			if(classNames[i].endsWith("*")){
				List<Class> classes = findMyTypes(classNames[i]);
				
				for(Class clsInPkg : classes){
					metadataArray.add(getMetaworksType(clsInPkg.getName()));
				}
			}else{
				try{
					metadataArray.add(getMetaworksType(classNames[i]));
				}catch(Exception e){e.printStackTrace();}
			}
		}
		
		return metadataArray;
		
	}
	
	private List<Class> findMyTypes(String basePackage) throws IOException, ClassNotFoundException
	{
	    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	    MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

	    List<Class> candidates = new ArrayList<Class>();
	    String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
	                               resolveBasePackage(basePackage) + ".class";
	    Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
	    for (Resource resource : resources) {
            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
            candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
            
//	        if (resource.isReadable()) {
//	            if (isCandidate(metadataReader)) {
//	                candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
//	            }
//	        }
	    }
	    return candidates;
	}

	private String resolveBasePackage(String basePackage) {
	    return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}

//	private boolean isCandidate(MetadataReader metadataReader) throws ClassNotFoundException
//	{
//	    try {
//	        Class c = Class.forName(metadataReader.getClassMetadata().getClassName());
//	        if (c.getAnnotation(XmlRootElement.class) != null) {
//	            return true;
//	        }
//	    }
//	    catch(Throwable e){
//	    }
//	    return false;
//	}
	
//	protected InvocationContext prepareToCall(String objectTypeName, Object object, String methodName, Map<String, Object> autowiredFields) throws Throwable{
//
//		Class serviceClass = Thread.currentThread().getContextClassLoader().loadClass(objectTypeName);
//		InvocationContext invocationContext = new InvocationContext();
//
//		
//    	//getBeanFactory().getBean(arg0)
//    	
////		callingObjectTypeName.set(objectTypeName);
//		
//		//if the requested value object is IDAO which need to be converted to implemented one so that it can be invoked by its methods
//		//Another case this required is when Spring is used since the spring base object should be auto-wiring operation
//		WebApplicationContext springAppContext = null;
//		if(TransactionalDwrServlet.useSpring) springAppContext = getBeanFactory();
//		Object springBean = null;
//		if(springAppContext!=null)
//		try{
//			//springBean = getBeanFactory().getBean(serviceClass);
//			Map beanMap = springAppContext.getBeansOfType(serviceClass);
//			Set keys = beanMap.keySet();			
//			for (Object key : keys) {
//			    if(springBean != null) {
//			    	System.err.println("====== Warnning : MetaworksRemoteService.callMetaworksService get only one bean object of one class.");
//					break;
//			    }
//			    springBean = beanMap.get(key);
//			}
//		}catch(Exception e){
//			//TODO: check if there's any occurrence of @Autowired in the field list, it is required to check and shows some WARNING to the developer.
//		}
//		
//		if(serviceClass.isInterface() || springBean!=null){
//			String serviceClassNameOnly = WebObjectType.getClassNameOnly(serviceClass);
//			
//			if(serviceClass.isInterface()){
//				serviceClassNameOnly = serviceClassNameOnly.substring(1, serviceClassNameOnly.length());
//				serviceClass = Thread.currentThread().getContextClassLoader().loadClass(serviceClass.getPackage().getName() + "." + serviceClassNameOnly);
//			}
//			
//			if(springAppContext!=null)
//				try{
//					springBean = getBeanFactory().getBean(serviceClass);
//				}catch(Exception e){
//					//TODO: check if there's any occurrence of @Autowired in the field list, it is required to check and shows some WARNING to the developer.
//				}
//
//			
//			WebObjectType wot = getMetaworksType(serviceClass.getName());
//			
//			//Convert to service object from value object (IDAO)
//			ObjectInstance srcInst = (ObjectInstance) wot.metaworks2Type().createInstance();
//			srcInst.setObject(object);
//			ObjectInstance targetInst = (ObjectInstance) wot.metaworks2Type().createInstance();
//			
//			if(springBean!=null){
//				targetInst.setObject(springBean);
//			}
//			
//			for(FieldDescriptor fd : wot.metaworks2Type().getFieldDescriptors()){
//				Object srcFieldValue = srcInst.getFieldValue(fd.getName());
//
//				//MetaworksObject need to initialize the property MetaworksContext if there's no data.
//				if("MetaworksContext".equals(fd.getName()) && srcFieldValue==null && IDAO.class.isAssignableFrom(serviceClass)){
//					srcFieldValue = new MetaworksContext();
//				}
//				
//				boolean isSpringAutowiredField = false;
//				try{
//					isSpringAutowiredField = ((serviceClass.getMethod( "get"+ fd.getName(), new Class[]{})).getAnnotation(Autowired.class) != null);
//				}catch(Exception e){					
//				}
//				
//				if(!isSpringAutowiredField)
//					targetInst.setFieldValue(fd.getName(), srcFieldValue);
//			}
//			
//			object = targetInst.getObject();
//			
//		}
//		
//		//injecting autowired fields from client
//		if(autowiredFields!=null){
//			for(String fieldName : autowiredFields.keySet()){
//				Object autowiringValue = autowiredFields.get(fieldName);
//				serviceClass.getField(fieldName).set(object, autowiringValue);
//			}
//		}
//		
//		Map autowiredObjects = autowire(object, false);
//		
//		TransactionContext tx = TransactionContext.getThreadLocalInstance();
//		if(connectionFactory!=null)
//			tx.setConnectionFactory(getConnectionFactory());
//		
//		invocationContext.setObject(object);
//		invocationContext.setAutowiredObjects(autowiredObjects);
//		invocationContext.setMethodName(methodName);
//		
//		WebObjectType wot = getMetaworksType(serviceClass.getName());
//		for(ServiceMethodContext smc: wot.getServiceMethodContexts()){ //TODO: [Performance] looking in array
//			if(smc.getMethodName().equals(methodName)){
//				invocationContext.setMethod(smc.getMethod());
//			}
//		}
//
//		
//		return invocationContext;
//	}

	private void fireAfterConvertertedEvent(Object object) throws Exception {
		if(object == null || object.getClass().getPackage().getName().equals("java.lang")) return;

		if(object instanceof ConverterEventListener){
			autowire(object);
			((ConverterEventListener)object).beforeConverted();
		}

		ObjectType ot = (ObjectType) getMetaworksType(object.getClass().getName()).metaworks2Type();{
			ObjectInstance instance = (ObjectInstance) ot.createInstance();
			instance.setObject(object);
			for(FieldDescriptor fd : ot.getFieldDescriptors()){
				Object propertyValue = instance.getFieldValue(fd.getName());
				fireAfterConvertertedEvent(propertyValue);
			}
		}
	}

    public Object callMetaworksService(String objectTypeName, Object clientObject, String methodName, Map<String, Object> autowiredFields) throws Throwable{


		Class serviceClass = Thread.currentThread().getContextClassLoader().loadClass(objectTypeName);


		TransactionContext.getThreadLocalInstance().setSharedContext("_calledClassName", objectTypeName);
		TransactionContext.getThreadLocalInstance().setSharedContext("_calledMethodName", methodName);

		//real callee object is set by MetaworksConverter once the called object is replaced with Face.
		Object realCalleeObject = TransactionContext.getThreadLocalInstance().getSharedContext("_real_callee_object");

		if(realCalleeObject!=null)
			clientObject = realCalleeObject;


		//let the clientObject applied for multilingual framework
//		{
//			List<PropertyPointer> multilingualProperties = (List<PropertyPointer>) TransactionContext.getThreadLocalInstance().getSharedContext("multilingualProperties");
//			if(multilingualProperties!=null) {
//
//				for(PropertyPointer propertyPointer : multilingualProperties){
//					if(propertyPointer.getObject() instanceof MultilingualSupport){
//						Object text = propertyPointer.getValue();
//
//						((MultilingualSupport) propertyPointer.getObject()).putMultilingualText("en", propertyPointer.getPropertyName(), (text!=null ? text.toString(): null));
//					}
//
//				}
//
//
//			}
//
//		}


		//replace clientObject with faceClass in case the one of faceClass' method is called since the dataFaceMap is added by MetaworksConverter already.

		//if the requested value object is IDAO which need to be converted to implemented one so that it can be invoked by its methods
		//Another case this required is when Spring is used since the spring base object should be auto-wiring operation
		ApplicationContext springAppContext = null;
		if(TransactionalDwrServlet.useSpring) springAppContext = getBeanFactory();
		Object springBean = null;
		if(springAppContext!=null)
		try{
			//springBean = getBeanFactory().getBean(serviceClass);
			Map beanMap = springAppContext.getBeansOfType(serviceClass);
			Set keys = beanMap.keySet();			
			for (Object key : keys) {
			    if(springBean != null) {
			    	System.err.println("====== Warnning : MetaworksRemoteService.callMetaworksService get only one bean object of one class.");
					break;
			    }
			    springBean = beanMap.get(key);
			}
		}catch(Exception e){
			//TODO: check if there's any occurrence of @Autowired in the field list, it is required to check and shows some WARNING to the developer.
		}

		//firing ConverterEventListener.afterConverted()
		//fireAfterConvertertedEvent(clientObject);  //disabled due to performance issue


		if(serviceClass.isInterface() || springBean!=null){
			String serviceClassNameOnly = WebObjectType.getClassNameOnly(serviceClass);
			
			if(serviceClass.isInterface()){
				serviceClassNameOnly = serviceClassNameOnly.substring(1, serviceClassNameOnly.length());
				serviceClass = Thread.currentThread().getContextClassLoader().loadClass(serviceClass.getPackage().getName() + "." + serviceClassNameOnly);
			}
			
			if(springAppContext!=null)
				try{
					springBean = getBeanFactory().getBean(serviceClass);
				}catch(Exception e){
					//TODO: check if there's any occurrence of @Autowired in the field list, it is required to check and shows some WARNING to the developer.
				}

			
			WebObjectType wot = getMetaworksType(serviceClass.getName());
			
			//Convert to service object from value object (IDAO)
			ObjectInstance srcInst = (ObjectInstance) wot.metaworks2Type().createInstance();
			srcInst.setObject(clientObject);
			ObjectInstance targetInst = (ObjectInstance) wot.metaworks2Type().createInstance();
			
			if(springBean!=null){
				targetInst.setObject(springBean);
			}
			
			for(FieldDescriptor fd : wot.metaworks2Type().getFieldDescriptors()){
				Object srcFieldValue = srcInst.getFieldValue(fd.getName());

				//MetaworksObject need to initialize the property MetaworksContext if there's no data.
				if("MetaworksContext".equals(fd.getName()) && srcFieldValue==null && IDAO.class.isAssignableFrom(serviceClass)){
					srcFieldValue = new MetaworksContext();
				}
				
				boolean isSpringAutowiredField = false;
				try{
					isSpringAutowiredField = ((serviceClass.getMethod( "get"+ fd.getName(), new Class[]{})).getAnnotation(Autowired.class) != null);
				}catch(Exception e){					
				}
				
				if(!isSpringAutowiredField)
					targetInst.setFieldValue(fd.getName(), srcFieldValue);
			}
			
			clientObject = targetInst.getObject();
			
		}
		
		//injecting autowired fields from client
		if(autowiredFields!=null){
			for(String fieldName : autowiredFields.keySet()){
				Object autowiringValue = autowiredFields.get(fieldName);
				
				if(!fieldName.startsWith(ServiceMethodContext.WIRE_PARAM_CLS)) //if the autowired field is not a @AutowiredFromClient in Parameterized call, means normal case in the field injection.
					serviceClass.getField(fieldName).set(clientObject, autowiringValue);
			}
		}
		
		Map autowiredObjects = autowire(clientObject, false);
		
		TransactionContext tx = TransactionContext.getThreadLocalInstance();
		if(connectionFactory!=null)
			tx.setConnectionFactory(getConnectionFactory());
		
//		invocationContext.setObject(object);
//		invocationContext.setAutowiredObjects(autowiredObjects);
//		invocationContext.setMethodName(methodName);
		
		Method theMethod = null;
		boolean parameterizedInvoke = false;
		ServiceMethodContext theSMC = null;
		
		WebObjectType wot = getMetaworksType(serviceClass.getName());
		for(ServiceMethodContext smc: wot.getServiceMethodContexts()){ //TODO: [Performance] looking in array
			if(smc.getMethodName().equals(methodName)){
				theSMC = smc;
				theMethod = smc._getMethod();
				parameterizedInvoke = theSMC._getPayloadParameterIndexes()!=null && theSMC._getPayloadParameterIndexes().size() > 0;
			}
		}
		
		

//		object = invocationContext.getObject();

		///put autowiring objects all including the object from client itself.
		TransactionContext.getThreadLocalInstance().setAutowiringObjectsFromClient(autowiredFields);
		if(TransactionContext.getThreadLocalInstance().getAutowiringObjectsFromClient()==null){
			TransactionContext.getThreadLocalInstance().setAutowiringObjectsFromClient(new HashMap());
		}
		TransactionContext.getThreadLocalInstance().getAutowiringObjectsFromClient().put("this", clientObject);

		//if we failed to find method by class name, just try to get the method from object directly.
		if(theMethod == null && clientObject!=null)
			theMethod = clientObject.getClass().getMethod(methodName, new Class[]{});
		
		try{
			
			Object[] parameters = new Object[theMethod.getParameterTypes().length];
			if(parameterizedInvoke){
				
				for(String key : theSMC._getPayloadParameterIndexes().keySet()){
					int parameterIndex = theSMC._getPayloadParameterIndexes().get(key);
					
					Object fieldValue = null;
					if(key.startsWith(ServiceMethodContext.WIRE_PARAM_CLS)){
						fieldValue = autowiredFields.get(key);
					}else{
						try{
							fieldValue = serviceClass.getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1), new Class[]{}).invoke(clientObject, new Object[]{});
						}catch(Exception ex){
							throw new RuntimeException("Error when to get field value for inserting parametered metaworks object", ex);
						}						
					}
					
					parameters[parameterIndex] = fieldValue;

				}
				
			}
			
			Object returned = theMethod.invoke(clientObject, parameters);
			
			if(theMethod.getReturnType()==void.class) //if void is return type, apply clientobject back to the browser.  
				returned = clientObject;
			
			
	    	Object wrappedReturn = TransactionContext.getThreadLocalInstance().getSharedContext("wrappedReturn");
	    	if(wrappedReturn!=null)
	    		returned = wrappedReturn;

// moved to MetaworksConverter
//			if(returned instanceof SerializationSensitive){
//				((SerializationSensitive) returned).beforeSerialization();
//			}
			
			return returned;

    	} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			if(!(e.getTargetException() instanceof MetaworksException))
				e.printStackTrace();
			
			throw e.getTargetException();
		}
				
	}

    public ApplicationContext getBeanFactory()
    {
        try {
			ServletContext srvCtx = ServerContextFactory.get().getServletContext();

			return WebApplicationContextUtils.getWebApplicationContext(srvCtx);
		} catch (Exception e) {
		//	e.printStackTrace();
			
			return null;
		}
    }
    
    public static boolean metaworksCall(){

		if(TransactionContext.getThreadLocalInstance()==null) return false;

    	StackTraceElement[] stack = Thread.currentThread().getStackTrace();

		String calledClassName = (String) TransactionContext.getThreadLocalInstance().getSharedContext("_calledClassName");
		String calledMethodNAme = (String) TransactionContext.getThreadLocalInstance().getSharedContext("_calledMethodName");

		try {
			Class calledClass = Thread.currentThread().getContextClassLoader().loadClass(calledClassName);
			Class stackClass = Thread.currentThread().getContextClassLoader().loadClass(stack[2].getClassName());

			if(stackClass.isAssignableFrom(calledClass) && stack[2].getMethodName().equals(calledMethodNAme)){
				return true;
			}else{
				return false;
			}

		} catch (Exception e) {
//			e.printStackTrace();

			return false;
		}


    }

    
    public static void wrapReturn(Object object){
    	TransactionContext.getThreadLocalInstance().setSharedContext("wrappedReturn", object);
    }
    public static void wrapReturn(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7){
    	wrapReturn(new Object[]{o1, o2, o3, o4, o5, o6, o7});
    }
    public static void wrapReturn(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6){
    	wrapReturn(new Object[]{o1, o2, o3, o4, o5, o6});
    }
    public static void wrapReturn(Object o1, Object o2, Object o3, Object o4, Object o5){
    	wrapReturn(new Object[]{o1, o2, o3, o4, o5});
    }
    public static void wrapReturn(Object o1, Object o2, Object o3, Object o4){
    	wrapReturn(new Object[]{o1, o2, o3, o4});
    }
    public static void wrapReturn(Object o1, Object o2, Object o3){
    	wrapReturn(new Object[]{o1, o2, o3});
    }
    public static void wrapReturn(Object o1, Object o2){
    	wrapReturn(new Object[]{o1, o2});
    }

	public static void addReturn(Object object){
		Object existingReturns = TransactionContext.getThreadLocalInstance().getSharedContext("wrappedReturn");

		if(existingReturns==null) {
			wrapReturn(object);

			return;
		}

		if(existingReturns instanceof Object[]){
			Object[] existingReturns1 = (Object[]) existingReturns;
			Object[] newReturns = new Object[existingReturns1.length + 1];
			for(int i=0; i<existingReturns1.length; i++){
				newReturns[i] = existingReturns1[i];
			}

			newReturns[existingReturns1.length] = object;

			wrapReturn(newReturns);

			return;

		}

		wrapReturn(existingReturns, object);

	}

    
	public static Map<Class, Object> autowire(Object object) {
		try {
			return getInstance().autowire(object, true);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public Map<Class, Object> autowire(Object object, boolean autowiringFromClient) throws IllegalAccessException {
		Map<Class, Object> autowiredObjects = new HashMap<Class, Object>();

		if(object==null)
			return autowiredObjects;
		
		ApplicationContext springAppContext = null;
		if(TransactionalDwrServlet.useSpring) springAppContext = MetaworksRemoteService.getInstance().getBeanFactory();
		else return autowiredObjects;
		
		if(springAppContext==null)
			return autowiredObjects;
		
		
		Map<Class, Object> autowiringObjectFromClientMapByClassTypes = null;
		if(autowiringFromClient){
			
			if(TransactionContext.getThreadLocalInstance()==null || TransactionContext.getThreadLocalInstance().getAutowiringObjectsFromClient()==null){
				autowiringFromClient = false;
			}else{

				autowiringObjectFromClientMapByClassTypes = getAutowiredFromClientClassMap();
			}
		}
		
		
		for(Field field: object.getClass().getFields()){
			if(field.getAnnotation(Autowired.class)!=null){
				

				Object springBean = null;
				if(springAppContext!=null)
				try{
					//springBean = getBeanFactory().getBean(serviceClass);
					Map beanMap = springAppContext.getBeansOfType(field.getType());
					Set keys = beanMap.keySet();			
					for (Object key : keys) {
					    springBean = beanMap.get(key);
					    
					    if(springBean != null) {
					    	break;
					    }
					}
				}catch(Exception e){
					//TODO: check if there's any occurrance of @Autowired in the field list, it is required to check and shows some WARNING to the developer.
				}
				
				if(springBean!=null){
					field.set(object, springBean);
					autowiredObjects.put(springBean.getClass(), springBean);
				}

			}else if(autowiringFromClient && field.getAnnotation(AutowiredFromClient.class)!=null){
				if(autowiringObjectFromClientMapByClassTypes.containsKey(field.getType())){
					
					Object autowiringFromClientObject = autowiringObjectFromClientMapByClassTypes.get(field.getType());
					field.set(object, autowiringFromClientObject);
					autowiredObjects.put(autowiringFromClientObject.getClass(), autowiringFromClientObject);
				}
				
			}
		}
		
		return autowiredObjects;
	}

	public static Map<Class, Object> getAutowiredFromClientClassMap() {
		if(TransactionContext.getThreadLocalInstance().getSharedContext("autowiredFromClientClassMap") != null){
			return (Map<Class, Object>) TransactionContext.getThreadLocalInstance().getSharedContext("autowiredFromClientClassMap");
		}


		Map<Class, Object> autowiringObjectFromClientMapByClassTypes;
		autowiringObjectFromClientMapByClassTypes = new HashMap<Class, Object>();

		if(TransactionContext.getThreadLocalInstance()!=null && TransactionContext.getThreadLocalInstance().getAutowiringObjectsFromClient()!=null)
		for(Object key : TransactionContext.getThreadLocalInstance().getAutowiringObjectsFromClient().keySet()){
            Object autowiringObjectFromClient = TransactionContext.getThreadLocalInstance().getAutowiringObjectsFromClient().get(key);

            if(autowiringObjectFromClient!=null){
                Class theClass = autowiringObjectFromClient.getClass();

                //register class hierarchy.
                while(theClass != Object.class) {
                    autowiringObjectFromClientMapByClassTypes.put(theClass, autowiringObjectFromClient);
                    theClass = theClass.getSuperclass();
                }
            }
        }

		TransactionContext.getThreadLocalInstance().setSharedContext(AUTOWIRED_FROM_CLIENT_CLASS_MAP, autowiringObjectFromClientMapByClassTypes);

		return autowiringObjectFromClientMapByClassTypes;
	}

	public static <T> T getComponent(Class<T> clazz) {
		T t = null;

		try {
			if(getInstance().getBeanFactory()==null){
				throw new RuntimeException("There's no configured Spring application context. if you invoke this in some Test Code, Please declare \"new TestMetaworksRemoteService(new ClassPathXmlApplicationContext(...));\" in the beginning of the 'setUp()' method in the test.");
			}

			Map beans = getInstance().getBeanFactory().getBeansOfType(clazz);

			int minOrder = 10000000;

			Iterator iterator = beans.values().iterator();
			while(iterator.hasNext()){
				T bean = (T) iterator.next();
				Order order = AnnotationUtils.findAnnotation(bean.getClass(),
				Order.class);


				Integer orderValue = 9999999;

				if(order!=null){
					orderValue = (Integer) AnnotationUtils.getValue(order);
				}

				if(minOrder > orderValue){
					minOrder = orderValue;
					t = bean;
				}
			}

		} catch (NoSuchBeanDefinitionException e) {
			//System.out.printf("No qualifying bean of type [%s] is defined", clazz.toString());

		}

		if(t==null){

			if(getAutowiredFromClientClassMap().containsKey(clazz)){
				t = (T) getAutowiredFromClientClassMap().get(clazz);
			};

		}

		if(t==null)
			try {
				t = clazz.newInstance();
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}

		autowire(t);

		return t;
	}

	ConnectionFactory connectionFactory;
		public ConnectionFactory getConnectionFactory() {
			return connectionFactory;
		}
	
		public void setConnectionFactory(ConnectionFactory connectionFactory) {
			this.connectionFactory = connectionFactory;
		}
		
	boolean debugMode;
		public boolean isDebugMode() {
			return debugMode;
		}
		public void setDebugMode(boolean debugMode) {
			this.debugMode = debugMode;
		}


	private boolean lowerCaseSQL;
		public boolean isLowerCaseSQL() {
			return lowerCaseSQL;
		}
		public void setLowerCaseSQL(boolean lowerCaseSQL) {
			this.lowerCaseSQL = lowerCaseSQL;
		}
}
