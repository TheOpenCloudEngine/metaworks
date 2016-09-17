package org.metaworks.dwr;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.convert.*;
import org.directwebremoting.extend.*;
import org.metaworks.*;
import org.metaworks.dao.Database;
import org.metaworks.dao.IDAO;
import org.metaworks.dao.MetaworksDAO;
import org.metaworks.dao.TransactionContext;
import org.metaworks.model.MetaworksList;

public class MetaworksConverter extends BeanConverter {

	private static final Log log = LogFactory.getLog(MetaworksConverter.class);

	private static final String FACE = "_face_";


	@Override
	protected boolean isAllowedByIncludeExcludeRules(String property) {
		if ("__className".equals(property)) return false;
		if ("__objectId".equals(property)) return false;
		if ("__faceHelper".equals(property)) return false;

		return super.isAllowedByIncludeExcludeRules(property);
	}


	public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException{
		return convertInbound(paramType, data, false);

	}

	/* (non-Javadoc)
	 * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
	 */
	public Object convertInbound(Class<?> paramType, InboundVariable data, boolean forFace) throws ConversionException
	{

		Integer depth = (Integer) TransactionContext.getThreadLocalInstance().getSharedContext("_unmarshall_depth");
		{
			if (depth == null)
				depth = new Integer(0);

			depth = new Integer(depth.intValue() + 1);

			TransactionContext.getThreadLocalInstance().setSharedContext("_unmarshall_depth", depth);
		}




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

				if(value.length() >= 2 && !("null".equals(value))) {
					value = value.substring(1, value.length() - 1);

					Map<String, String> tokens = extractInboundTokens(paramType, value);

					String refName = tokens.get("__className");

					if (refName != null) {
						refName = refName.split(":")[1];

						String className = data.getContext().getInboundVariable(refName).getFormField().getString();

						try {
							paramType = Thread.currentThread().getContextClassLoader().loadClass(className);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					refName = tokens.get(FACE);

					if (refName != null) {
							String[] split = ConvertUtil.splitInbound(refName);
							String splitValue = split[ConvertUtil.INBOUND_INDEX_VALUE];
							String splitType = split[ConvertUtil.INBOUND_INDEX_TYPE];

							InboundVariable nested = new InboundVariable(data.getContext(), null, splitType, splitValue);
							nested.dereference();

							try {
								return convertInboundForFace(nested);    //this will jump to "if(Face.class.isAssignableFrom....)" of convertInbound() method.

							}catch (Exception e){new RuntimeException("Failed to replace with face", e).printStackTrace();}

					}
				}
			}

			if(forFace){
				Face face = (Face) super.convertInbound(paramType, data);

				Object realValue = null;
				if(face!=null) {
					realValue = face.createValueFromFace();
				}

				if(realValue instanceof SerializationSensitive){
					((SerializationSensitive)realValue).afterMWDeserialization();
				}

				return realValue;
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
				if(FaceWrapped.class.isAssignableFrom(paramType)){
					FaceWrapped faceWrapped = (FaceWrapped) super.convertInbound(paramType, data);

					Face face = faceWrapped.getFace();

					Object realValue = null;
					if(face!=null) {
						realValue = face.createValueFromFace();

						if(depth.intValue()==1){
							TransactionContext.getThreadLocalInstance().setSharedContext("_real_callee_object", face);
						}
					}

//					Object realValue = faceWrapped.getValue(); //it is created from (FACEWRAP_1)

					if(realValue instanceof SerializationSensitive){
						((SerializationSensitive)realValue).afterMWDeserialization();
					}

					return realValue;
				}

				else{
					Object value = super.convertInbound(paramType, data);

					if(value instanceof SerializationSensitive){
						((SerializationSensitive)value).afterMWDeserialization();
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

	private Object convertInboundForFace(InboundVariable nested) {
		return convertInbound(Face.class, nested, true);
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
			if(wot!=null) {
				WebFieldDescriptor wfd = wot.getFieldDescriptor(key);

				if (wot != null && wfd != null) {

					//register multilingual properties
					{

						Boolean isMultilingual = (Boolean) wfd.getAttribute("multilingual");

						if(isMultilingual!=null && isMultilingual){
							List<PropertyPointer> multilingualProperties = (List<PropertyPointer>) TransactionContext.getThreadLocalInstance().getSharedContext("multilingualProperties");
							if(multilingualProperties==null){
								multilingualProperties = new ArrayList<PropertyPointer>();
								TransactionContext.getThreadLocalInstance().setSharedContext("multilingualProperties", multilingualProperties);
							}

							PropertyPointer propertyPointer = new PropertyPointer(bean, property.getName());
							multilingualProperties.add(propertyPointer);
						}

					}// end of multilingual property registration //later it would be processed by MetaworksRemoteService.callMetaworksService()

					{//face replacing
						String faceClassForField = (String) wfd.getAttribute("faceclass");
						if (faceClassForField != null && !AllChildFacesAreIgnored.class.getName().equals(faceClassForField)) {
							try {

								Class faceClass = Class.forName(faceClassForField);
								Object faceValue = convert(entry.getValue(), faceClass, data.getContext(), property);

								Object realValue = null;
								if (faceValue != null && faceClass.isAssignableFrom(faceValue.getClass())) {
									realValue = ((Face) faceValue).createValueFromFace();
								}

								//sometimes the converted value is realValue
								if (faceValue != null) {
									//						Object realValue = face.createValueFromFace();

									if(property.getPropertyType().isAssignableFrom(faceValue.getClass())) {
										realValue = faceValue;
									}else{
										String message = "Face Value [" + faceValue + "] has been dropped for property [" + property.getName() + ":" + property.getPropertyType() + "]";
										log.debug(message);
										new Exception(message).printStackTrace();
									}

								}

								property.setValue(bean, realValue);

								continue;

							} catch (Exception e) {
								throw new RuntimeException("Can't replace with face class: " + e.getMessage(), e);
							}
						}
					}



				}
			}


			/**
			 * (FACEWRAP_1)
			 */
//			if(bean instanceof FaceWrapped){
//				if( "face".equals(property.getName())) {
//					Object realValue = convert(entry.getValue(), property.getPropertyType(), data.getContext(), property);
//
//					((FaceWrapped) bean).setValue(realValue);
//				}
//
//				continue;  //no need to set value except value by transformed from property 'face'
//			}


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
				if(paramType == java.lang.StackTraceElement.class){
					return null;
				}

				throw new ConversionException(paramType, "Service Object should have constructor with empty parameter. Add new Constructor with no argument into " + paramType.getName() + ". " + paramType.getName(), e);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				throw new ConversionException(paramType, "Service Object couldn't be instantiated due to : " +  e.getClass(), e);
			}
		}


	}

	public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
	{


		String originalDataClassName = null;

		Face faceForData = null;

		if(data!=null)
			originalDataClassName = data.getClass().getName();

		try {

			if(data instanceof SerializationSensitive){
				((SerializationSensitive)data).beforeMWSerialization();
			}

			boolean faceReplacingEnabled = true;

//			Face facePassedFromParent = (Face) TransactionContext.getThreadLocalInstance().getSharedContext("__faceForProperty");
//
//			if(facePassedFromParent!=null){
//				faceForData = facePassedFromParent;
//				TransactionContext.getThreadLocalInstance().setSharedContext("__faceForProperty", null);
//			}

			WebObjectType wot = null;

			if(faceForData==null)
			try {
				try {
					wot = MetaworksRemoteService.getInstance().getMetaworksType(data.getClass().getName());
				} catch (Exception e2) {
				}

//TODO: DO_NOT_SWAP_WITH_FACE
				OutboundVariable do_not_swap_with_face = outctx.get("DO_NOT_SWAP_WITH_FACE");
				if(do_not_swap_with_face ==null) {
					if (wot != null && wot.getFaceOptions() != null && wot.getFaceOptions().get("faceClass") != null) {

						faceForData = (Face) MetaworksRemoteService.getComponent(Class.forName(wot.getFaceOptions().get("faceClass")));
						MetaworksRemoteService.autowire(faceForData);

						faceForData.setValueToFace(data);


						//data = face;//don't replace anymore.

						wot = MetaworksRemoteService.getInstance().getMetaworksType(data.getClass().getName());


					}

//TODO: DO_NOT_SWAP_WITH_FACE
				}else{

					if(do_not_swap_with_face.getDeclareCode()==null) // means not RECURSIVELY, release the condition.
						outctx.put("DO_NOT_SWAP_WITH_FACE", null);
					else
						faceReplacingEnabled = false;	//means the option is that swapping face is recursively disabled
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
					boolean thisStackDisabledChildFaceSwapping = false;

					Map<String, Property> properties = getPropertyMapFromObject(data, true, false);
					for (Entry<String, Property> entry : properties.entrySet())
					{
						String name = entry.getKey();
						Property property = entry.getValue();

						Object value = property.getValue(data);
						Face faceForProperty = null;

						if(wot!=null && faceReplacingEnabled){
							WebFieldDescriptor wfd = wot.getFieldDescriptor(name);
							if(wfd!=null){

								// trying face exchange
								{
									String faceClassForField = (String) wfd.getAttribute("faceclass");

									if(AllChildFacesAreIgnored.class.getName().equals(faceClassForField)){
										disableFaceSwapping(outctx, true);
										thisStackDisabledChildFaceSwapping = true;
									}else {

										if (faceClassForField != null

											//**** important:  to avoid recursive field swapping
											//										&& !faceClassForField.equals(originalDataClassName)

												) {
											try {
												Face face = (Face) MetaworksRemoteService.getComponent(Class.forName(faceClassForField));
												MetaworksRemoteService.autowire(face);

												if (face instanceof FieldFace) {
													((FieldFace) face).visitHolderObjectOfField(data);
												}

												face.setValueToFace(value);

												if (Collection.class.isAssignableFrom(property.getPropertyType()) || face instanceof MetaworksList || property.getPropertyType().getName().startsWith("java.lang") || property.getPropertyType().isPrimitive() || value == null/*|| property.getPropertyType().getPackage().equals(String.class.getPackage())*/) {

													FaceWrapped faceWrapped = new FaceWrapped();
													faceWrapped.setValue(value);
													faceWrapped.setFace(face);
													faceWrapped.setFaceClass(faceClassForField);

													value = faceWrapped;

													//value = face;//don't replace anymore.
												} else {
													faceForProperty = face;
												}

											} catch (Exception e) {
												throw new Exception("Can't replace with face class: " + e.getMessage(), e);
											}
										} else {

											WebObjectType typeOfField = null;

											try {
												typeOfField = MetaworksRemoteService.getInstance().getMetaworksType(value.getClass().getName());
											} catch (Exception e2) {
											}

											if (typeOfField != null && typeOfField.getFaceOptions() != null && typeOfField.getFaceOptions().get("faceClass") != null) {

												String faceClassName = typeOfField.getFaceOptions().get("faceClass");

												if (!faceClassName.equals(originalDataClassName)) {
													Face face = (Face) MetaworksRemoteService.getComponent(Class.forName(faceClassName));
													MetaworksRemoteService.autowire(face);

													face.setValueToFace(value);

													if (Collection.class.isAssignableFrom(property.getPropertyType()) || face instanceof MetaworksList || property.getPropertyType().isPrimitive() || value == null/* || property.getPropertyType().getPackage().equals(String.class.getPackage())*/) {
														FaceWrapped faceWrapped = new FaceWrapped();
														faceWrapped.setValue(value);
														faceWrapped.setFace(face);

														value = faceWrapped;

//														value = face;//don't replace anymore.
													} else {
														faceForProperty = face;
													}
												} else {
													disableFaceSwapping(outctx, false);

												}
											}
										}
									}
								}// end of face exchange




							}
						}

						if(faceReplacingEnabled) {

							boolean propertyValueIsSameClass = value != null && value.getClass().getName().equals(originalDataClassName);

							if (propertyValueIsSameClass) {
								disableFaceSwapping(outctx, false);
							}
						}

						OutboundVariable nested = getConverterManager().convertOutbound(value, outctx);

						if(faceForProperty!=null){
							if(nested instanceof ObjectOutboundVariable) {

								//							TransactionContext.getThreadLocalInstance().setSharedContext("__faceForProperty", faceForProperty);


								OutboundVariable faceForPropertyOV = getConverterManager().convertOutbound(faceForProperty, outctx);
								((ObjectOutboundVariable) nested).getChildMap().put(FACE, faceForPropertyOV);
							}
						}




						if(thisStackDisabledChildFaceSwapping){

							//restore the status of faceSwappingOption after returning the parent stack call which causes the disabling option to all the child.
							outctx.put("DO_NOT_SWAP_WITH_FACE", null);


						}



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


				if(faceForData!=null) {
					OutboundVariable faceForDataOV = getConverterManager().convertOutbound(faceForData, outctx);
					ovs.put(FACE, faceForDataOV);
				}

				ov.setChildren(ovs);

				return ov;

			}
		} catch (ConversionException e) {
			e.printStackTrace();

			throw e;
		}

	}

	private void disableFaceSwapping(OutboundContext outctx, final boolean recursively) {
		outctx.put("DO_NOT_SWAP_WITH_FACE", new OutboundVariable() {
			@Override
			public OutboundVariable getReferenceVariable() {
				return null;
			}

			@Override
			public String getDeclareCode() {
				return (recursively ? "RECURSIVELY" : null);
			}

			@Override
			public String getBuildCode() {
				return null;
			}

			@Override
			public String getAssignCode() {
				return null;
			}
		});
	}

	private void enableChildFace(OutboundContext outctx, Face face) {
		outctx.put("DO_NOT_SWAP_WITH_FACE", new OutboundVariable() {
			@Override
			public OutboundVariable getReferenceVariable() {
				return null;
			}

			@Override
			public String getDeclareCode() {
				return null;
			}

			@Override
			public String getBuildCode() {
				return null;
			}

			@Override
			public String getAssignCode() {
				return null;
			}
		});
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

				if(value==null){
					nested = null;
				}else if(value instanceof IDAO && ((IDAO) value).getImplementationObject()!=null){
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
