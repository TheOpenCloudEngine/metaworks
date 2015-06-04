package org.metaworks.dwr;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.convert.*;
import org.directwebremoting.extend.ArrayOutboundVariable;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.ObjectOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.Property;
import org.metaworks.Face;
import org.metaworks.FieldDescriptor;
import org.metaworks.ObjectInstance;
import org.metaworks.WebFieldDescriptor;
import org.metaworks.WebObjectType;
import org.metaworks.dao.Database;
import org.metaworks.dao.IDAO;
import org.metaworks.dao.MetaworksDAO;
import org.metaworks.dao.TransactionContext;

public class MetaworksConverter extends BeanConverter{

	private static final Log log = LogFactory.getLog(MetaworksConverter.class);






	@Override
	protected boolean isAllowedByIncludeExcludeRules(String property) {
		if("__className".equals(property)) return false;
		if("__objectId".equals(property)) return false;
		if("__faceHelper".equals(property)) return false;

		return super.isAllowedByIncludeExcludeRules(property);
	}





	/* (non-Javadoc)
	 * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
	 */
	public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
	{

		try {
			//System.out.println("--------------------------------------------------------");
			//System.out.println("name : " + paramType.getName() + "-->" + IDAO.class.isAssignableFrom(paramType) + "," + Modifier.isInterface(paramType.getModifiers()) + "," + Modifier.isAbstract(paramType.getModifiers()));

			// TODO : Why is the value of the proxy object of Modifier.isAbstract true? need confirm
			// TODO : confirm other case 
			//IDAO.class.isAssignableFrom(paramType) && !Modifier.isInterface(paramType.getModifiers()) && !Modifier.isAbstract(paramType.getModifiers()) ||
			if(true){
				//System.out.println("convert metaworks");
				if("string".equals(data.getType())){
					paramType = String.class;

					StringConverter stringConverter = new StringConverter();
					return stringConverter.convertInbound(String.class, data);

				}else if("number".equals(data.getType())){

					if(!Number.class.isAssignableFrom(paramType)) {
						//TODO: it will not properly work. we converted all the numbers as integer.
						paramType = Integer.class;
					}

					PrimitiveConverter primitiveConverter = new PrimitiveConverter();

					return primitiveConverter.convertInbound(paramType, data);
//					ObjectConverter objectConverter = new ObjectConverter();
//					return objectConverter.convertInbound(paramType, data);
				}


				//when unknown object from javascript, metaworks need to get the class Information from the JSON's property value '__className'
				String value = data.getValue();

				if(value.length() >= 2 && !("null".equals(value))){
					value = value.substring(1, value.length() - 1);

					Map<String, String> tokens = extractInboundTokens(paramType, value);

					String refName = tokens.get("__className");

					if(refName!=null){
						refName = refName.split(":")[1];

						String className = data.getContext().getInboundVariable(refName).getFormField().getString();

						try {
							paramType = Thread.currentThread().getContextClassLoader().loadClass(className);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

			if( paramType.isArray() || "array".equals(data.getType())){

				ArrayConverter arrayConverter = new ArrayConverter();
				arrayConverter.setConverterManager(this.getConverterManager());

				//TODO: sometimes, the element member is an IDAO but the paramType info is not supplied, in that case, it is also needed to be converted into IDAO.
				if(!paramType.isArray() && IDAO.class.isAssignableFrom(paramType)){
					Class arrParamType = Array.newInstance(paramType, 0).getClass();
					IDAO[] converted = (IDAO[]) arrayConverter.convertInbound(arrParamType, data);

					try {
						IDAO cursoredDAO = MetaworksDAO.createDAOImpl(paramType);

						for(int i=0; i<converted.length; i++){
							if(converted[i].getImplementationObject() == null){
								cursoredDAO.getImplementationObject().moveToInsertRow();;

								WebObjectType wot = MetaworksRemoteService.getInstance().getMetaworksType(converted[i].getClass().getName());

								ObjectInstance fromObjInst = (ObjectInstance) wot.metaworks2Type().createInstance();
								fromObjInst.setObject(converted[i]);

								if(converted[i] != null)
									cursoredDAO .setMetaworksContext(converted[i].getMetaworksContext());

								for(int j=0; i<wot.metaworks2Type().getFieldDescriptors().length; i++){
									FieldDescriptor fd = wot.metaworks2Type().getFieldDescriptors()[j];

									cursoredDAO.set(fd.getName(), fromObjInst.getFieldValue(fd.getName()));
								}
							}else
								cursoredDAO.getImplementationObject().moveToInsertRow(converted[i]);
						}

						return cursoredDAO;

					} catch (Exception e) {
						throw new ConversionException(paramType, e);
					}
				}else{

					if(!paramType.isArray())
						paramType = Object[].class;



					return arrayConverter.convertInbound(paramType, data);
				}


			}else{

				//this will work only for the case that root object is the FaceClass. for the properties, see createUsingSetterInjection
				if(Face.class.isAssignableFrom(paramType)){
					Face face = (Face) super.convertInbound(paramType, data);

					Map<Object,Face> dataFaceMap = (Map<Object, Face>) TransactionContext.getThreadLocalInstance().getSharedContext("dataFaceMap");
					if(dataFaceMap==null){
						dataFaceMap = new HashMap<Object, Face>();
						TransactionContext.getThreadLocalInstance().setSharedContext("dataFaceMap", dataFaceMap);
					}

//					MetaworksRemoteService.autowire(face);
					Object realValue = face.createValueFromFace();

					//System.out.println("----------  face is registered ------");
					dataFaceMap.put(System.identityHashCode(realValue), face); //register for later reference in case of the Face object is used for calling method.

					if(realValue instanceof SerializationSensitive){
						((SerializationSensitive)realValue).afterDeserialization();
					}

					return realValue;
				}else{
					Object value = super.convertInbound(paramType, data);

					if(value instanceof SerializationSensitive){
						((SerializationSensitive)value).afterDeserialization();
					}

					return value;

				}
			}

		} catch (ConversionException e) {
			//System.err.println("[WARN] You may need to give the primitive types'(e.g. int, boolean) default value of in the Javascript object for current Metaworks call.");
			e.printStackTrace();

			throw e;
		}

	}

	@Override
	protected Object createUsingSetterInjection(Class<?> paramType, InboundVariable data, Map<String, String> tokens) throws InstantiationException, IllegalAccessException
	{
		Object bean;
		if (instanceType != null)
		{
			bean = instanceType.newInstance();
		}
		else
		{
			bean = createParameterInstance(paramType);
		}

		// We should put the new object into the working map in case it
		// is referenced later nested down in the conversion process.
		if (instanceType != null)
		{
			data.getContext().addConverted(data, instanceType, bean);
		}
		else
		{
			data.getContext().addConverted(data, paramType, bean);
		}

		Map<String, Property> properties = getPropertyMapFromObject(bean, false, true);

		WebObjectType wot = null;
		try {
			try{
				wot = MetaworksRemoteService.getInstance().getMetaworksType(paramType.getName());
			}catch(Exception e2){}

		} catch (Exception e1) {
		}


		for (Entry<String, String> entry : tokens.entrySet())
		{
			String key = entry.getKey();

			// TODO: We don't URL decode method names we probably should. This is $dwr
			// TODO: We should probably have stripped these out already
			if (key.startsWith("%24dwr"))
			{
				continue;
			}

			Property property = properties.get(key);
			if (property == null)
			{
				log.warn("Missing setter: " + paramType.getName() + ".set" + Character.toTitleCase(key.charAt(0)) + key.substring(1) + "() to match javascript property: " + key + ". Check include/exclude rules and overloaded methods.");
				continue;
			}


			//Replacing with Face class if applicable
			WebFieldDescriptor wfd = wot.getFieldDescriptor(key);

			if(wot!=null && wfd!=null){
				String faceClassForField = (String) wfd.getAttribute("faceclass");
				if(faceClassForField!=null){
					try{

						Class faceClass = Class.forName(faceClassForField);
						Object realValue = convert(entry.getValue(), faceClass, data.getContext(), property);

						if(faceClass.isAssignableFrom(realValue.getClass())){
							realValue = ((Face)realValue).createValueFromFace();
						}
//						Object realValue = face.createValueFromFace();

						property.setValue(bean, realValue);

						continue;

					}catch(Exception e){
						throw new RuntimeException("Can't replace with face class: " + e.getMessage(), e);
					}
				}
			}


			Object output = convert(entry.getValue(), property.getPropertyType(), data.getContext(), property);
			property.setValue(bean, output);
		}
		return bean;
	}


	protected Object createParameterInstance(Class<?> paramType) throws InstantiationException, IllegalAccessException
	{
		if( paramType.isInterface() && IDAO.class.isAssignableFrom(paramType) ){
			IDAO iDAOInstance;
			try {
				iDAOInstance = MetaworksDAO.createDAOImpl(paramType);
				return iDAOInstance;

			} catch (Exception e) {

				throw new RuntimeException(e);
			}

		}else{
			try {
				return paramType.newInstance();
			} catch(java.lang.InstantiationException e){
				throw new ConversionException(paramType, "Service Object should have constructor with empty parameter. Add new Constructor with no argument into " + paramType.getName() + ". 臾몄��瑜� ��닿껐�����ㅻ㈃ ��ㅼ�� ��대����ㅼ�� ���洹�癒쇳�멸�� ��������� ������ �����깆��瑜� 異�媛���댁＜��몄��--> " + paramType.getName(), e);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				throw new ConversionException(paramType, "Service Object couldn't be instantiated due to : " +  e.getClass(), e);
			}
		}


	}

	public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
	{
		try {

			if(data instanceof SerializationSensitive){
				((SerializationSensitive)data).beforeSerialization();
			}

			WebObjectType wot = null;
			try {
				try{
					wot = MetaworksRemoteService.getInstance().getMetaworksType(data.getClass().getName());
				}catch(Exception e2){}

				if(wot!=null && wot.getFaceOptions()!=null && wot.getFaceOptions().get("faceClass")!=null){

					Face face = (Face) Class.forName(wot.getFaceOptions().get("faceClass")).newInstance();
					MetaworksRemoteService.autowire(face);
					face.setValueToFace(data);
					data = face;
				}
			} catch (Exception e1) {
				throw new RuntimeException("Can't replace with face class: " + e1.getMessage(), e1);
			}

			//sometimes due to weird operation of DWR
			if(data.getClass().isArray()){
				ArrayConverter arrayConverter = new ArrayConverter();
				arrayConverter.setConverterManager(this.getConverterManager());
				return arrayConverter.convertOutbound(data, outctx);
			}

			if(data instanceof IDAO && ((IDAO) data).getImplementationObject()!=null){

				IDAO dao = (IDAO)data;

				ArrayOutboundVariable ov = new ArrayOutboundVariable(outctx);
				outctx.put(data, ov);

				// Convert all the data members
				List<OutboundVariable> ovs = new ArrayList<OutboundVariable>();

				try{
					if(dao.getImplementationObject().getRowSet()!=null || dao.getImplementationObject().getCachedRows()!=null){
						dao.beforeFirst();

						OutboundVariable nested = null;

						while(dao.next()){
							nested = iDaoConvertOutbound(dao, outctx);
							ovs.add(nested);
						}

						//disabled since 'add' button mechanism

						//				        if(ovs.size()<=1){
						//				        	if(nested!=null)
						//				        		return nested; //return single object if there's only one object. client (Javascript) should consider object.length is undefined or not to recognize whether the data is array or not
						//				        }
					}else{
						return iDaoConvertOutbound(dao, outctx);
					}

				}catch(Exception e){
					throw new ConversionException(((IDAO) data).getImplementationObject().getClass(), e.getMessage());
				}

				// if(ovs.size()>0)
				// Group the list of converted objects into this OutboundVariable
				ov.setChildren(ovs);

				//TODO: since this part doesn't return __className property, the array type cannot recognize the dao's class type?

				return ov;

			}else{
				// Where we collect out converted children
				Map<String, OutboundVariable> ovs = new TreeMap<String, OutboundVariable>();

				// We need to do this before collecting the children to save recursion
				ObjectOutboundVariable ov = new ObjectOutboundVariable(outctx, data.getClass(), getJavascript());
				outctx.put(data, ov);

				try
				{
					Map<String, Property> properties = getPropertyMapFromObject(data, true, false);
					for (Entry<String, Property> entry : properties.entrySet())
					{
						String name = entry.getKey();
						Property property = entry.getValue();

						Object value = property.getValue(data);

						if(wot!=null){
							WebFieldDescriptor wfd = wot.getFieldDescriptor(name);
							if(wfd!=null){
								String faceClassForField = (String) wfd.getAttribute("faceclass");
								if(faceClassForField!=null){
									try{
										Face face = (Face) Class.forName(faceClassForField).newInstance();
										MetaworksRemoteService.autowire(face);
										face.setValueToFace(value);
										value = face;
									}catch(Exception e){
										throw new Exception("Can't replace with face class: " + e.getMessage(), e);
									}
								}
							}
						}


						OutboundVariable nested = getConverterManager().convertOutbound(value, outctx);

						ovs.put(name, nested);
					}
				}
				catch (ConversionException ex)
				{
					throw ex;
				}
				catch (Exception ex)
				{
					throw new ConversionException(data.getClass(), ex);
				}

				OutboundVariable classNameOV = getConverterManager().convertOutbound(data.getClass().getName(), outctx);

				ovs.put("__className", classNameOV);
				ov.setChildren(ovs);

				return ov;

			}
		} catch (ConversionException e) {
			e.printStackTrace();

			throw e;
		}

	}


	public OutboundVariable iDaoConvertOutbound(Object data, OutboundContext outctx) throws Exception
	{
		Map<String, OutboundVariable> ovs = new TreeMap<String, OutboundVariable>();

		// We need to do this before collecting the children to save recursion
		ObjectOutboundVariable ov = new ObjectOutboundVariable(outctx, data.getClass(), getJavascript());
		outctx.put(data, ov);

		IDAO dao = (IDAO)data;

		WebObjectType webObjectType = MetaworksRemoteService.getInstance().getMetaworksType(((IDAO) data).getImplementationObject().getDaoClass().getName());
		Class daoClass = dao.getImplementationObject().getDaoClass();

		Map<String, Property> properties = new HashMap<String, Property>();
		for(WebFieldDescriptor property : webObjectType.getFieldDescriptors()){
			String propertyName = property.getName();
			Object value;
			try {
				String upperPropertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

				Class propertyClass = null;
				Method getterMethod = null;
				try{
					propertyClass = (getterMethod = daoClass.getMethod("get" + upperPropertyName)).getReturnType();
				}catch(Exception e){
				}

				if(propertyClass==null)
					try{
						propertyClass = (getterMethod = daoClass.getMethod("is" + upperPropertyName)).getReturnType();
					}catch(Exception e){
					}

				//							if("initiator".equals(propertyName))
				//								System.out.println();

				value = getterMethod.invoke(dao, new Object[]{});
				//value = dao.get(propertyName);


				if(!propertyClass.isPrimitive() &&
						propertyClass!=String.class &&
						(value==null || !propertyClass.isAssignableFrom(value.getClass()))){


					Class<?> propClass = Thread.currentThread().getContextClassLoader().loadClass(property.getClassName());

					boolean needToProhibitRecursivelyGenerateReferenceObject = dao.getImplementationObject().getDaoClass().isAssignableFrom(propClass);

					if(!needToProhibitRecursivelyGenerateReferenceObject) {
						value = Database.createReferenceObject(propClass, dao);
					}
				}

				OutboundVariable nested = null;

				//primitive default value mapping
				//							if(value==null){
				//
				//								if(propertyClass!=null){
				//									if(propertyClass == String.class || propertyClass == MetaworksContext.class || propertyClass == Date.class){
				//										//do nothing. null is ok.
				//									}else
				//									if(propertyClass == int.class)
				//										value = new Integer(0);
				//									else if(propertyClass == boolean.class)
				//										value = new Boolean(false);
				//									else if(propertyClass == Long.class){
				//										value = new Long(0);
				//									}else if(Number.class.isAssignableFrom(propertyClass)){
				//										value = new Integer(0);
				//									}else if(Date.class.isAssignableFrom(propertyClass)){
				//										value = new Date();
				//									}else{
				//										//new Exception("please add more primitive type's default value mapping for " + propertyClass).printStackTrace();
				//									}
				//								}
				//							}

				if(value==null){
					nested = null;
				}else if(value instanceof IDAO && ((IDAO) value).getImplementationObject()!=null){
					//
					//								if(Thread.currentThread().getStackTrace().length > 200){
					//System.out.println(" nasted value's dao type is " + value.getClass());
					//								}
					//
					nested = this.convertOutbound(value, outctx);
				}else{
					nested = getConverterManager().convertOutbound(value, outctx);
				}

				if(nested!=null)
					ovs.put(propertyName, nested);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		//if there's TypeSelector setting exist, try to give cast information as possible as desired.
		Class type = Database.getDesiredTypeByTypeSelector(dao);
		if(type==null)
			type = daoClass;

		OutboundVariable classNameOV = getConverterManager().convertOutbound(type.getName(), outctx);
		ovs.put("__className", classNameOV);

		ov.setChildren(ovs);

		return ov;


	}


}
