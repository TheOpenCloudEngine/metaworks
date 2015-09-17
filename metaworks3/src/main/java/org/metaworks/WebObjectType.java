package org.metaworks;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.AutowiredToClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Children;
import org.metaworks.annotation.Default;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Group;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Icon;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ImagePath;
import org.metaworks.annotation.KeepAtClient;
import org.metaworks.annotation.Name;
import org.metaworks.annotation.NonEditable;
import org.metaworks.annotation.NonLoadable;
import org.metaworks.annotation.NonSavable;
import org.metaworks.annotation.ORMapping;
import org.metaworks.annotation.Order;
import org.metaworks.annotation.Payload;
import org.metaworks.annotation.Range;
import org.metaworks.annotation.Resource;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Test;
import org.metaworks.annotation.TestContext;
import org.metaworks.annotation.Testing;
import org.metaworks.annotation.TypeSelector;
import org.metaworks.annotation.Validator;
import org.metaworks.annotation.ValidatorContext;
import org.metaworks.annotation.ValidatorSet;
import org.metaworks.dao.Database;
import org.metaworks.dao.IDAO;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.TransactionalDwrServlet;
import org.metaworks.inputter.SelectInput;

import com.thoughtworks.xstream.XStream;

public class WebObjectType {

	Object resource;

	boolean isInterface;

	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	boolean isAlwaysSubmitted;

	public boolean isAlwaysSubmitted() {
		return isAlwaysSubmitted;
	}

	public void setIsAlwaysSubmitted(boolean isAlwaysSubmitted) {
		this.isAlwaysSubmitted = isAlwaysSubmitted;
	}


	boolean designable;

	public boolean isDesignable() {
		return designable;
	}

	public void setDesignable(boolean designable) {
		this.designable = designable;
	}

	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	String displayName;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	List<String> superClasses;

	public List<String> getSuperClasses() {
		return superClasses;
	}

	public void setSuperClasses(List<String> superClasses) {
		this.superClasses = superClasses;
	}

	String[] faceMappingByContext;

	public String[] getFaceMappingByContext() {
		return faceMappingByContext;
	}

	public void setFaceMappingByContext(String[] faceMappingByContext) {
		this.faceMappingByContext = faceMappingByContext;
	}

	List<ServiceMethodContext> serviceMethodContexts;

	public List<ServiceMethodContext> getServiceMethodContexts() {
		return serviceMethodContexts;
	}

	public void setServiceMethodContexts(
			List<ServiceMethodContext> serviceMethodContexts) {
		this.serviceMethodContexts = serviceMethodContexts;
	}

	String faceComponentPath;

	public String getFaceComponentPath() {
		return faceComponentPath;
	}

	public void setFaceComponentPath(String faceComponentPath) {
		this.faceComponentPath = faceComponentPath;
	}
//		
//	String faceHelperPath;
//		public String getFaceHelperPath() {
//			return faceHelperPath;
//		}
//		public void setFaceHelperPath(String faceHelperPath) {
//			this.faceHelperPath = faceHelperPath;
//		}

	String faceForArray;

	public String getFaceForArray() {
		return faceForArray;
	}

	public void setFaceForArray(String faceForArray) {
		this.faceForArray = faceForArray;
	}

	Map<String, String> faceOptions;

	public Map<String, String> getFaceOptions() {
		return faceOptions;
	}

	public void setFaceOptions(Map<String, String> faceOptions) {
		this.faceOptions = faceOptions;
	}

	WebFieldDescriptor fieldDescriptors[];

	public WebFieldDescriptor[] getFieldDescriptors() {
		return fieldDescriptors;
	}

	public void setFieldDescriptors(WebFieldDescriptor[] fieldDescriptors) {
		this.fieldDescriptors = fieldDescriptors;
	}

	Map<String, HashMap> autowiredFields;

	public Map<String, HashMap> getAutowiredFields() {
		return autowiredFields;
	}

	public void setAutowiredFields(Map<String, HashMap> autowiredFields) {
		this.autowiredFields = autowiredFields;
	}

	Map<String, String> onDropTypes;

	public Map<String, String> getOnDropTypes() {
		return onDropTypes;
	}

	public void setOnDropTypes(Map<String, String> onDropTypes) {
		this.onDropTypes = onDropTypes;
	}

	Type metaworks2Type;

	public Type metaworks2Type() {
		return metaworks2Type;
	}

	Class iDAOClass;

	public Class iDAOClass() {
		return iDAOClass;
	}

	WebFieldDescriptor keyFieldDescriptor;

	public WebFieldDescriptor getKeyFieldDescriptor() {
		return keyFieldDescriptor;
	}

	public void setKeyFieldDescriptor(WebFieldDescriptor keyFieldDescriptor) {
		this.keyFieldDescriptor = keyFieldDescriptor;
	}

	public WebObjectType() {}

	public WebObjectType(Class clazz) throws Exception {
		initWithJavaClass(clazz);
	}

	public void initWithJavaClass(Class actCls) throws Exception {
		ObjectType objectType = new ObjectType(actCls);
		this.metaworks2Type = objectType;
		
		setName(objectType.getName());
		setInterface(actCls.isInterface());
		
		final ArrayList<Class> tryingClasses = new ArrayList<Class>();
		superClasses = new ArrayList<String>();
		Class superCls = actCls;
		while(superCls!=Object.class && superCls!=null && Database.class!=superCls){
			superClasses.add(superCls.getName());
			tryingClasses.add(superCls);
			
			superCls = superCls.getSuperclass();
		}

		//search IDAO in the implementation interfaces
		if(actCls.isInterface() && IDAO.class.isAssignableFrom(actCls)){
			this.iDAOClass = actCls;
		}else{
			
			ArrayList<String> interfaceNames = new ArrayList<String>();
			
			for(String superClassName : superClasses){
				
				Class[] interfaces = Thread.currentThread().getContextClassLoader().loadClass(superClassName).getInterfaces();
				for(int i=0; i<interfaces.length; i++){
					if(IDAO.class.isAssignableFrom(interfaces[i])){
						this.iDAOClass = interfaces[i];
					}
					
					interfaceNames.add(interfaces[i].getName());
					tryingClasses.add(interfaces[i]);
				}
			}
			
			superClasses.addAll(interfaceNames);
		}

		//setting face
		Face typeFace = (Face)getAnnotationDeeply(tryingClasses, null, Face.class);
		
		if(typeFace!=null){
			
			if(typeFace.ejsPathMappingByContext().length > 0){
				setFaceMappingByContext(typeFace.ejsPathMappingByContext());
			}
			
			if(typeFace.options().length > 0){
				Map<String, String> optionMap = new HashMap<String, String>();
				
				for(int i=0; i<typeFace.options().length; i++){
					optionMap.put(typeFace.options()[i], typeFace.values()[i]);
				}
				
				setFaceOptions(optionMap);
			}

			if(typeFace.faceClass() != org.metaworks.Face.class){
				if(getFaceOptions()==null) setFaceOptions(new HashMap<String, String>());
				getFaceOptions().put("faceClass", typeFace.faceClass().getName());
			}

			if(typeFace.faceClassName().length() > 0){
				if(getFaceOptions()==null) setFaceOptions(new HashMap<String, String>());
				getFaceOptions().put("faceClass", typeFace.faceClassName());
			}

			if(typeFace.ejsPath().length() > 0)
				setFaceComponentPath(typeFace.ejsPath());
			
			if(typeFace.ejsPathForArray().length() > 0)
				setFaceForArray(typeFace.ejsPathForArray());
		}


		AutowiredFromClient alwaysSubmitted = (AutowiredFromClient)getAnnotationDeeply(tryingClasses, null, AutowiredFromClient.class);
		if(alwaysSubmitted!=null) {
			setIsAlwaysSubmitted(true);
		}


//		else
//			setFaceComponentPath(getComponentLocationByEscalation(actCls, "faces"));
//	
//		if(this.iDAOClass!=null && getFaceComponentPath()==null){
//			setFaceComponentPath(getComponentLocationByEscalation(iDAOClass, "faces"));
//		}

//		//setting for ejs.js
//		setFaceHelperPath(getComponentLocationByEscalation(actCls, "faces", "ejs.js"));
//		if(this.iDAOClass!=null && getFaceHelperPath()==null){
//			setFaceComponentPath(getComponentLocationByEscalation(iDAOClass, "faces", "ejs.js"));
//		}
		
		
		if(getFaceComponentPath()==null)
			setFaceComponentPath("dwr/metaworks/genericfaces/ObjectFace.ejs");
		
		//System.out.println("Metaworks mapped face file : " +  getFaceComponentPath() + " to class: " + getName());


		//set the table name if specified in the annotation.
		
		try{
			
			javax.persistence.Table ejb3Table = (javax.persistence.Table) getAnnotationDeeply(tryingClasses, null, javax.persistence.Table.class);
			
			org.metaworks.annotation.Table metaworksTable = (org.metaworks.annotation.Table) getAnnotationDeeply(tryingClasses, null, org.metaworks.annotation.Table.class);
			
			if(metaworksTable != null && metaworksTable.name().length() > 0)
				objectType.setName(metaworksTable.name());
			else if(ejb3Table!=null)
				objectType.setName(ejb3Table.name());
			else{
				objectType.setName(getClassNameOnly(actCls));

				if(IDAO.class.isAssignableFrom(actCls) && objectType.getName().startsWith("I"))
					objectType.setName(objectType.getName().substring(1));

			}
				
			org.metaworks.annotation.Face objectFace = (Face) getAnnotationDeeply(tryingClasses, null, Face.class);
			if(objectFace != null && objectFace.displayName().length() > 0)
				setDisplayName(objectFace.displayName());
			else{
				setDisplayName(objectType.getName());
			}
			
		}catch(Exception ex){
			//ex.printStackTrace();
			
		};

		
		//analyzing 'autowiredFromClient' fields
		Field[] fields = actCls.getFields();
		for(int i=0; i<fields.length; i++){
			AutowiredFromClient autowiredFromClient = (AutowiredFromClient) fields[i].getAnnotation(AutowiredFromClient.class);
			
			if(autowiredFromClient!=null){
				
				if(autowiredFromClient.onDrop()){
					if(onDropTypes == null)
						onDropTypes = new HashMap<String, String>();
					
					onDropTypes.put(fields[i].getType().getName(), "{field: '" + fields[i].getName() + "', instr: '" + autowiredFromClient.instruction() + "'}");
					
				}else{
					if(autowiredFields == null)
						autowiredFields = new HashMap<String, HashMap>();

					//TODO: Refactor: why is the entry name 'field' rather, isn't 'className' right?
					HashMap<String, String> autowiredField = new HashMap<String, String>();
					autowiredField.put("field", fields[i].getType().getName());
					autowiredField.put("select", autowiredFromClient.select());

//					if(autowiredFromClient.alwaysOnChildren()){
//						autowiredField.put("alwaysOnChildren", "true");
//					}

					//autowiredFields.put(fields[i].getName(), fields[i].getType().getName());
					autowiredFields.put(fields[i].getName(), autowiredField);
					
				}
			}
		}
		
		
		//analyzing setter/getter bean properties
		WebFieldDescriptor[] webFieldDescriptors = new WebFieldDescriptor[objectType.getFieldDescriptors().length];
		
		//Test testerBeginner = null;
		//Map<String, Test> tests = new HashMap<String, Test>();
	
		//Field Ordering if there's some Order annotation
		FieldDescriptor[] fieldDescriptors = objectType.getFieldDescriptors();
		Arrays.sort(fieldDescriptors, new Comparator<FieldDescriptor>() {
            @Override
            public int compare(FieldDescriptor o1, FieldDescriptor o2) {
                Order or1 = null;
				try {
					or1 = (Order) getAnnotationDeeply( tryingClasses, o1.getName(), Order.class);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                Order or2 = null;
				try {
					or2 = (Order) getAnnotationDeeply( tryingClasses, o2.getName(), Order.class);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                // nulls last
                if (or1 != null && or2 != null) {
                    return or1.value() - or2.value();
                } else
                if (or1 != null && or2 == null) {
                    return -1;
                } else
                if (or1 == null && or2 != null) {
                    return 1;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });

		
		FieldDescriptor keyField = null;
		for(int i=0; i<fieldDescriptors.length; i++){
			
			FieldDescriptor fd = fieldDescriptors[i];
			
			//TODO: change to vector or hashmap the fieldDescriptor. will need to study the field order differences
			//ignores meta-meta data  
//			if(fd.getClassType() == MetaworksContext.class) 
//				continue;
			
			//replacing primitive class type to corresponding object type
			if(fd.getClassType().isPrimitive()){
				
				String clsName = fd.getClassType().getName();
				
				if("int".equals(clsName)){
					fd.setClassType(Integer.class);
				}else if("boolean".equals(clsName)){
					fd.setClassType(Boolean.class);
				}//TODO: consider other primitive class types as well
			}

			
			
			WebViewer viewer = new WebViewer();
			
			boolean isKeyField = false;

			if(getAnnotationDeeply( tryingClasses, fd.getName(), NonSavable.class)!=null)
				fd.setSavable(false);

			if(getAnnotationDeeply(tryingClasses, fd.getName(), Name.class)!=null)
				fd.setAttribute("nameField", new Boolean(true));

			if(getAnnotationDeeply(tryingClasses, fd.getName(), Children.class)!=null)
				fd.setAttribute("children", new Boolean(true));

			if(getAnnotationDeeply(tryingClasses, fd.getName(), Icon.class)!=null)
				fd.setAttribute("icon", new Boolean(true));

			/*
			 * 2013-10-22 jinwon
			 * set field descriptor type
			 */
			org.metaworks.annotation.Field field = (org.metaworks.annotation.Field) getAnnotationDeeply(tryingClasses, fd.getName(), org.metaworks.annotation.Field.class); 
			if(field!=null && field.descriptor() != null && !"".equals(field.descriptor()))
				fd.setAttribute("descriptor." + field.descriptor(), new Boolean(true));				
			
			
			if(getAnnotationDeeply(tryingClasses, fd.getName(), NonLoadable.class)!=null)
				fd.setLoadable(false);

			Group group = (Group)getAnnotationDeeply(tryingClasses, fd.getName(), Group.class);

			if(group != null){
				fd.setAttribute("group", group.name());
			}

			Hidden hidden = (Hidden) getAnnotationDeeply(tryingClasses, fd.getName(), Hidden.class);
			if(hidden !=null){
				/*
				if(hidden.when().length() > 1){
					if(hidden.on())
						fd.setAttribute("hidden.when", hidden.when());
					else
						fd.setAttribute("show.when", hidden.when());
					
				}else
					
				*/
				
				if(hidden.when().length > 0){
					Map whens = new HashMap();
					for(String when : hidden.when()){
						whens.put(when, when);
					}
					
					fd.setAttribute("hidden.when", whens);
				}
				
				if(hidden.where().length > 0){
					Map wheres = new HashMap();
					for(String where : hidden.where()){
						wheres.put(where, where);
					}
					
					fd.setAttribute("hidden.where", wheres);
				}
				
				if(hidden.how().length > 0){
					Map hows = new HashMap();
					for(String how : hidden.how()){
						hows.put(how, how);
					}
					
					fd.setAttribute("hidden.how", hows);
				}
				
				if(hidden.media().length > 0){
					Map medias = new HashMap();
					for(String media : hidden.media()){
						medias.put(media, media);
					}
					
					fd.setAttribute("hidden.media", medias);
				}
				
				if(hidden.when().length == 0 && hidden.where().length == 0 && hidden.how().length == 0 && hidden.media().length == 0)
					fd.setAttribute("hidden", hidden.on());
			}

			if(getAnnotationDeeply(tryingClasses, fd.getName(), AutowiredToClient.class)!=null)
				fd.setAttribute("autowiredToClient", new Boolean(true));

			NonEditable nonEditable = (NonEditable)getAnnotationDeeply(tryingClasses, fd.getName(), NonEditable.class);
			
			// 2012-09-27 when ��곕Ⅸ NonEditable 泥�由� 異�媛�
			if(nonEditable!=null){
				fd.setAttribute("nonEditable", new Boolean(true));
				
				if(nonEditable.when().length > 0){
					Map whens = new HashMap();
					for(String when : nonEditable.when()){
						whens.put(when, when);
					}
					
					fd.setAttribute("nonEditable.when", whens);
				}
				
				if(nonEditable.where().length > 0){
					Map wheres = new HashMap();
					for(String where : nonEditable.where()){
						wheres.put(where, where);
					}
					
					fd.setAttribute("nonEditable.where", wheres);
				}
				
				if(nonEditable.how().length > 0){
					Map hows = new HashMap();
					for(String how : nonEditable.how()){
						hows.put(how, how);
					}
					
					fd.setAttribute("nonEditable.how", hows);
				}
			}
			
			// 2012-04-12 cjw where, how ���蹂� 異�媛�
			Available available = (Available) getAnnotationDeeply(tryingClasses, fd.getName(), Available.class); 
			if(available!=null){
				if(available.when().length > 0){
					Map whens = new HashMap();
					for(String when : available.when()){
						whens.put(when, when);
					}
					
					fd.setAttribute("available.when", whens);
				}				
				
				if(available.where().length > 0){
					Map wheres = new HashMap();
					for(String where : available.where()){
						wheres.put(where, where);
					}
					
					fd.setAttribute("available.where", wheres);				
				}
				
				if(available.how().length > 0){
					Map hows = new HashMap();
					for(String how : available.how()){
						hows.put(how, how);
					}
					
					fd.setAttribute("available.how", hows);
				}
				
				if(available.media().length > 0){				
					Map medias = new HashMap();
					for(String media : available.media()){
						medias.put(media, media);
					}
					
					fd.setAttribute("available.media", medias);
				}
				
				if(available.condition().length > 0){
					Map conditions = new HashMap();
					int conditionSeq=0;
					
					for(String condition : available.condition()){
						conditions.put(conditionSeq, condition);
						conditionSeq++;
					}
					
					fd.setAttribute("available.condition", conditions);
				}				
			}

			
			Resource resourceAnnotation = (Resource) getAnnotationDeeply(tryingClasses, fd.getName(), Resource.class);
			if(resourceAnnotation !=null){

				XStream xstream = new XStream(/*new DomDriver()*/);

				Object resourceForThisField = null;
				if(resource == null){
					InputStream is = null;
					try{
						is = Thread.currentThread().getContextClassLoader().getResourceAsStream(actCls.getName() + ".xml");
						
//						XStream xstream = new XStream(/*new DomDriver()*/);
						resource = xstream.fromXML(is);
					
						ObjectInstance rscInst = (ObjectInstance) objectType.createInstance();
						rscInst.setObject(resource);
						resourceForThisField = rscInst.getFieldValue(fd.getName());

					}catch(Exception e){
						
					}finally{try{is.close();}catch(Exception ex){}}
				}
				
				if(resourceForThisField == null && resourceAnnotation.def().length() > 0){
					resourceForThisField = resourceAnnotation.def();
					if(resourceAnnotation.def().startsWith("<") && resourceAnnotation.def().endsWith(">")){
						resourceForThisField = xstream.fromXML(resourceAnnotation.def());
					}
				}
				
				if(resourceForThisField == null)
					resourceForThisField = fd.getDisplayName();
				
				
				fd.setAttribute("resource", resourceForThisField);
				setDesignable(true);
			}
			
			if(getAnnotationDeeply(tryingClasses, fd.getName(), ImagePath.class)!=null){
				fd.setAttribute("imagePath", new Boolean(true));
			}
			
			Testing tests;
			Test[] testsArr;
			if((tests = (Testing) getAnnotationDeeply(tryingClasses, fd.getName(), Testing.class))!=null){
				testsArr = tests.value();
			}else{
				
				Test test;
				if((test = (Test) getAnnotationDeeply(tryingClasses, fd.getName(), Test.class))!=null){

					testsArr = new Test[]{test};
				}else{
					testsArr = new Test[]{};
				}
				
			}
			
			for(int j=0; j<testsArr.length; j++){

				Test test = testsArr[j];

				
				Map<String, TestContext> existingTestSet = (Map<String, TestContext>) fd.getAttribute("test");
				if(existingTestSet==null){
					existingTestSet = new HashMap<String, TestContext>();
					fd.setAttribute("test", existingTestSet);
				}
				
				TestContext testContext = new TestContext();
				
				if(test.next().length > 0)
					testContext.setNext(test.next());
				
				if(test.scenario().length() > 0)
					testContext.setScenario(test.scenario());
				
				if(test.value().length > 0)
					testContext.setValue(test.value());
				
				if(test.instruction().length > 0)
					testContext.setInstruction(test.instruction());
				
				testContext.setStarter(test.starter());

				
				existingTestSet.put(test.scenario(), testContext);
			}
			
			
			/*
			 * 2012-09-07 jinwon
			 * validation modify
			 */
			ArrayList<ValidatorContext> existingValidatorSet = (ArrayList<ValidatorContext>) fd.getAttribute("validator");
			if(existingValidatorSet == null)
				existingValidatorSet = new ArrayList<ValidatorContext>();				
			
			
			ValidatorSet validators;
			Validator[] validatorArr;
			if((validators = (ValidatorSet) getAnnotationDeeply(tryingClasses, fd.getName(), ValidatorSet.class))!=null){
				validatorArr = validators.value();
			}else{				
				Validator validator;
				if((validator = (Validator) getAnnotationDeeply(tryingClasses, fd.getName(), Validator.class))!=null){
					validatorArr = new Validator[]{validator};
				}else{				
					validatorArr = new Validator[]{};
				}				
			}
						
			for(int j=0; j<validatorArr.length; j++){

				Validator validator = validatorArr[j];
				ValidatorContext validatorContext = new ValidatorContext();
				
				if(validator.name().length() > 0)
					validatorContext.setName(validator.name());
				
				if(validator.message().length() > 0)
					validatorContext.setMessage(validator.message());
				
				if(validator.condition().length() > 0)
					validatorContext.setCondition(validator.condition());

				if(validator.availableUnder().length() > 0)
					validatorContext.setAvailableUnder(validator.availableUnder());

				if(validator.options().length > 0)
					validatorContext.setOptions(validator.options());
				
				//existingValidatorSet.put(validator.name(), validatorContext);
				existingValidatorSet.add(validatorContext);
			}
			
			/*
			 * 2012-09-07 jinwon
			 * JSR-303 (Bean Validation) Basic
			 */
			if(getAnnotationDeeply(tryingClasses, fd.getName(), Valid.class)!=null){
				ValidatorContext validatorContext = new ValidatorContext();
				validatorContext.setAvailableUnder("true");
				
				existingValidatorSet.add(validatorContext);				
			}
			
			AssertTrue assertTrue;
			if((assertTrue = (AssertTrue) getAnnotationDeeply(tryingClasses, fd.getName(), AssertTrue.class))!=null){
				ValidatorContext validatorContext = new ValidatorContext();
				validatorContext.setName(ValidatorContext.VALIDATE_ASSERTTRUE) ;
				
				if(!"{javax.validation.constraints.AssertTrue.message}".equals(assertTrue.message()))
					validatorContext.setMessage(assertTrue.message());
				
				existingValidatorSet.add(validatorContext);
			}
			AssertFalse assertFalse;
			if((assertFalse = (AssertFalse) getAnnotationDeeply(tryingClasses, fd.getName(), AssertFalse.class))!=null){
				ValidatorContext validatorContext = new ValidatorContext();
				validatorContext.setName(ValidatorContext.VALIDATE_ASSERTFALSE) ;

				if(!"{javax.validation.constraints.AssertFalse.message}".equals(assertFalse.message()))
					validatorContext.setMessage(assertFalse.message());
				
				existingValidatorSet.add(validatorContext);
			}
			NotNull notNull;
			if((notNull = (NotNull) getAnnotationDeeply(tryingClasses, fd.getName(), NotNull.class))!=null){
				ValidatorContext validatorContext = new ValidatorContext();
				validatorContext.setName(ValidatorContext.VALIDATE_NOTNULL) ;
				
				if(!"{javax.validation.constraints.NotNull.message}".equals(notNull.message()))
					validatorContext.setMessage(notNull.message());
				
				existingValidatorSet.add(validatorContext);
			}
			Null isNull;
			if((isNull = (Null) getAnnotationDeeply(tryingClasses, fd.getName(), Null.class))!=null){
				ValidatorContext validatorContext = new ValidatorContext();
				validatorContext.setName(ValidatorContext.VALIDATE_NULL) ;
				
				if(!"{javax.validation.constraints.Null.message}".equals(isNull.message()))
					validatorContext.setMessage(isNull.message());
				
				existingValidatorSet.add(validatorContext);
			}			Min min;
			if((min = (Min) getAnnotationDeeply(tryingClasses, fd.getName(), Min.class))!=null){
				ValidatorContext validatorContext = new ValidatorContext();
				validatorContext.setName(ValidatorContext.VALIDATE_MIN) ;
				validatorContext.setOptions(new String[]{String.valueOf(min.value())});
				
				if(!"{javax.validation.constraints.Min.message}".equals(min.message()))
					validatorContext.setMessage(min.message());
				
				existingValidatorSet.add(validatorContext);
			}
			Max max;
			if((max = (Max) getAnnotationDeeply(tryingClasses, fd.getName(), Max.class))!=null){
				ValidatorContext validatorContext = new ValidatorContext();
				validatorContext.setName(ValidatorContext.VALIDATE_MAX) ;
				validatorContext.setOptions(new String[]{String.valueOf(max.value())});
				
				if(!"{javax.validation.constraints.Max.message}".equals(max.message()))
					validatorContext.setMessage(max.message());
				
				existingValidatorSet.add(validatorContext);
			}			
			Size size;
			if((size = (Size) getAnnotationDeeply(tryingClasses, fd.getName(), Size.class))!=null){
				ValidatorContext validatorContext;
				
				// Min
				validatorContext = new ValidatorContext();
				validatorContext.setName(ValidatorContext.VALIDATE_MIN) ;
				validatorContext.setOptions(new String[]{String.valueOf(size.min())});
				
				if(!"{javax.validation.constraints.Size.message}".equals(size.message()))
					validatorContext.setMessage(size.message());
				
				existingValidatorSet.add(validatorContext);
				
				// Max
				validatorContext = new ValidatorContext();
				validatorContext.setName(ValidatorContext.VALIDATE_MIN) ;
				validatorContext.setOptions(new String[]{String.valueOf(size.min())});
				
				if(!"{javax.validation.constraints.Size.message}".equals(size.message()))
					validatorContext.setMessage(size.message());
				
				existingValidatorSet.add(validatorContext);
			}
			Pattern pattern;
			if((pattern = (Pattern) getAnnotationDeeply(tryingClasses, fd.getName(), Pattern.class))!=null){
				ValidatorContext validatorContext = new ValidatorContext();
				validatorContext.setName(ValidatorContext.VALIDATE_REGULAREXPRESSION) ;
				validatorContext.setOptions(new String[]{pattern.regexp() });
				
				if(!"{javax.validation.constraints.Pattern.message}".equals(pattern.message()))
					validatorContext.setMessage(pattern.message());
				
				existingValidatorSet.add(validatorContext);
			}	
			
			if(existingValidatorSet.size() > 0)
				fd.setAttribute("validator", existingValidatorSet);
			
			
			
			
			ORMapping orm;
			if((orm = (ORMapping) getAnnotationDeeply(tryingClasses, fd.getName(), ORMapping.class))!=null){
				fd.setAttribute("ormapping", orm);
			}
			
			
			TypeSelector typeSelector;
			if((typeSelector = (TypeSelector) getAnnotationDeeply(tryingClasses, fd.getName(), TypeSelector.class))!=null){
				Map<String, String> typeSelections = new HashMap<String, String>();
				for(int j=0; j<typeSelector.values().length; j++){
					typeSelections.put(typeSelector.values()[j], typeSelector.classes()[j].getName());
				}
				
				fd.setAttribute("typeSelector", typeSelections);
			}
			
			
			Face face = (Face) getAnnotationDeeply(tryingClasses, fd.getName(), Face.class);
			if(face!=null){
				
				if(face.ejsPath().length() >0 || face.options().length > 0 || face.values().length > 0){
					viewer.setFace(face.ejsPath());
					
					FaceInput faceInput = new FaceInput();
					faceInput.setFace(face);
					fd.setInputter(faceInput);
				}else
					viewer.setFace(getComponentLocationByEscalation(fd.getClassType(), "faces"));
				
				
				if(face.displayName().length() > 0){
					fd.setDisplayName(face.displayName());
				}
				
				if(face.faceClass()!=org.metaworks.Face.class){ //if the face class is meaningful 
					fd.setAttribute("faceclass", face.faceClass().getName());
				}

				if(face.faceClassName().length() > 0){
					if(getFaceOptions()==null) setFaceOptions(new HashMap<String, String>());
					fd.setAttribute("faceClass", face.faceClassName());
				}

			}
		
			Range range = (Range) getAnnotationDeeply(tryingClasses, fd.getName(), Range.class);
			if(range!=null){
				if(range.options().length > 0){
					SelectInput selectInput = new SelectInput(range.options(), range.values());
					
					fd.setInputter(selectInput);
					
					viewer.setFace(getComponentLocationByEscalation( SelectInput.class, "genericfaces"));
				}
				
				if(range.size() > 0){
					fd.setAttribute("size", range.size());
				}
			}


			if(getAnnotationDeeply(tryingClasses, fd.getName(), Id.class)!=null 
					|| getAnnotationDeeply(tryingClasses, fd.getName(), javax.persistence.Id.class)!=null){
				isKeyField = true;
				keyField = fd;
			}
			
			if(getAnnotationDeeply(tryingClasses, fd.getName(), KeepAtClient.class)!=null){
				fd.setAttribute("keepAtClient", true);
			}

			fd.setViewer(viewer);
	
			webFieldDescriptors[i] = new WebFieldDescriptor(fd);
			
			if(isKeyField)
				setKeyFieldDescriptor(webFieldDescriptors[i]);
			
			Default defaultValue =  (Default) getAnnotationDeeply(tryingClasses, fd.getName(), Default.class);
			if(defaultValue != null){
				webFieldDescriptors[i].setDefaultValue(defaultValue.value());
			}
		}
		
		
		
		if(iDAOClass != null){
			if(keyField!=null)
				objectType.setKeyFieldDescriptor(keyField);
			else
				System.err.println("[WARN] Although domain class '" + actCls.getName() + "' look like a database synchronizable object, it has no key field description. Give @org.metaworks.Id for the key field's GETTER[!] method NOT the SETTER.");
		}

		setFieldDescriptors(webFieldDescriptors);
		
		
		//method list

		serviceMethodContexts = new ArrayList<ServiceMethodContext>();


		Map<String, Method> methods = new HashMap<String, Method>();

		for(Method method : actCls.getMethods()){
			if(methods.containsKey(method.getName())){
				if(method.getClass() == actCls) {
					methods.put(method.getName(), method);
				}
			}else{
				methods.put(method.getName(), method);
			}
		}


		for(Method method : methods.values()){


		
//javassist version of metaworks2
//		List<String> methodNameList = new ArrayList<String>();
//		try{
//			Map dupChecker = new HashMap<String, String>();
//			CtMethod[] methods1 = ObjectType.classPool.get(actCls.getName()).getDeclaredMethods();
//			CtMethod[] methods2 = ObjectType.classPool.get(actCls.getName()).getMethods();
//			CtMethod[][] methodGroup = new CtMethod[][]{methods1, methods2};
//			
//			for(int j=0; j<methodGroup.length; j++){
//				CtMethod[] methods = methodGroup[j];
//				for(int i=0; i<methods.length; i++){
//					
//					if(methods[i].getParameterTypes().length>0)
//						continue;
//					
//					String methodName = methods[i].getName();
//					if(dupChecker.containsKey(methodName)) 
//						continue;
//					
//					dupChecker.put(methodName, methodName);
//					methodNameList.add(methodName);
//				}
//			}
//		}catch(Exception e){
//			Method[] methods = actCls.getMethods();
//			for(int i=0; i<methods.length; i++){
//				if(methods[i].getParameterTypes().length>0)
//					continue;
//
//				methodNameList.add(methods[i].getName());
//			}
//		}
//		
//		for(String methodName : methodNameList){
//			
//			Method method = null;
//			try{
//				method = actCls.getMethod(methodName, new Class[]{});
//			}catch(Exception e){
//				continue;
//			}
//end of javassist version of metaworks2
		
		
//			if(annotation==null && iDAOClass != null){
//				try{
//					annotation = iDAOClass.getMethod(method.getName(), new Class[]{}).getAnnotation(ServiceMethod.class);
//					face = iDAOClass.getMethod(method.getName(), new Class[]{}).getAnnotation(Face.class);
//					children = iDAOClass.getMethod(method.getName(), new Class[]{}).getAnnotation(Children.class);
//					name = iDAOClass.getMethod(method.getName(), new Class[]{}).getAnnotation(Name.class);
//					available = iDAOClass.getMethod(method.getName(), new Class[]{}).getAnnotation(Available.class);
//					hidden = iDAOClass.getMethod(method.getName(), new Class[]{}).getAnnotation(Hidden.class);
//					testSet = iDAOClass.getMethod(method.getName(), new Class[]{}).getAnnotation(Testing.class);
//					test = iDAOClass.getMethod(method.getName(), new Class[]{}).getAnnotation(Test.class);
//					
//				}catch(Exception e){
//					
//				}
//			}
//			

			Annotation annotationSomewhat = getAnnotationDeeply(tryingClasses, method.getName(), ServiceMethod.class, false);
			
			if(annotationSomewhat!=null){
				
				ServiceMethod annotation = null;
				ServiceMethodContext smc = new ServiceMethodContext();

				if(annotationSomewhat instanceof ServiceMethodAnnotationWithParameterAnnotations){
					ServiceMethodAnnotationWithParameterAnnotations serviceMethodAnnotationWithParameterAnnotations = (ServiceMethodAnnotationWithParameterAnnotations)annotationSomewhat;
					annotation = serviceMethodAnnotationWithParameterAnnotations.getMethodAnnotation();
					
					//meet opportunity for getting parameter annotations
					Map <String, String> payloads = new HashMap<String, String>();
					Annotation[][] parameterAnnotationsS = serviceMethodAnnotationWithParameterAnnotations.getParameterAnnotations();
					Map<String, Integer> payloadParameterIndexes = new HashMap<String, Integer>();
					
					int parameterIndex = 0;
					for(Annotation[] parameterAnnotations : parameterAnnotationsS){
						for(Annotation parameterAnnotation : parameterAnnotations){
							if(parameterAnnotation instanceof Payload){
							
								String payloadName = ((Payload)parameterAnnotation).value();
								if(payloadName!=null){
									
									payloads.put(payloadName, payloadName);							
									payloadParameterIndexes.put(payloadName, new Integer(parameterIndex));
								}
							}else if(parameterAnnotation instanceof AutowiredFromClient){
								Class paramType = null;
								try {
									paramType = method.getParameterTypes()[parameterIndex];
								}catch (ArrayIndexOutOfBoundsException e){
									e.printStackTrace();
								}
								String payloadName = ServiceMethodContext.WIRE_PARAM_CLS + paramType.getName();

								if(((AutowiredFromClient)parameterAnnotation).select()!=null && ((AutowiredFromClient)parameterAnnotation).select().length() > 0){
									payloadName = payloadName + ":" + ((AutowiredFromClient)parameterAnnotation).select();
								}

								payloads.put(payloadName, payloadName);
								payloadParameterIndexes.put(payloadName, new Integer(parameterIndex));
							}
						}
						
						parameterIndex++;
					}
					
					if(payloads.size()>0){
						smc.setPayload(payloads);
						smc.setMethod(serviceMethodAnnotationWithParameterAnnotations.getMethod());
						smc.setPayloadParameterIndexes(payloadParameterIndexes);
					}
					
				}
				

				
				Face face = (Face) getAnnotationDeeply(tryingClasses, method.getName(), Face.class, false);
				Children children = (Children) getAnnotationDeeply(tryingClasses, method.getName(), Children.class, false);
				Name name = (Name) getAnnotationDeeply(tryingClasses, method.getName(), Name.class, false);
				Available available =  (Available) getAnnotationDeeply(tryingClasses, method.getName(), Available.class, false);
				Hidden hidden =  (Hidden) getAnnotationDeeply(tryingClasses, method.getName(), Hidden.class, false);
				Testing testSet = (Testing) getAnnotationDeeply(tryingClasses, method.getName(), Testing.class, false);
				Test test = (Test) getAnnotationDeeply(tryingClasses, method.getName(), Test.class, false);
				Group group = (Group) getAnnotationDeeply(tryingClasses, method.getName(), Group.class, false);
				
				smc.setMethodName(method.getName());
				smc.setWhen(annotation.when() + "|");
				smc.setWhere(annotation.where() + "|");
				smc.setCallByContent(annotation.callByContent());
				smc.setNeedToConfirm(annotation.needToConfirm());
				smc.setClientSide(annotation.clientSide());
				smc.setTarget(annotation.target());
				smc.setValidate(annotation.validate());
				smc.setRtnCls(method.getReturnType().getName());
				smc.setBindingHidden(annotation.bindingHidden());
				
				if(group != null)
					smc.setGroup(group.name());
				
				/*
				 * 2012-12-06 jinwon
				 * add childrenLoader option
				 */
				smc.setChildrenLoader(annotation.childrenLoader());
				
				if(annotation.loader().length > 0 && annotation.loader()[0].length() > 0){
					if("auto".equals(annotation.loader()[0])){
						smc.setLoader(new String[]{smc.getRtnCls()});
					}else{
						smc.setLoader(annotation.loader());
					}
				}

				if(annotation.cacheClasses().length > 0){
					
					smc.setCacheCls(annotation.cacheClasses());
				}
				
				smc.setLoadOnce(annotation.loadOnce());

				smc.setNameGetter(name!=null? true:false);
				smc.setChildrenGetter(children!=null? true:false);
				smc.setConstructor(annotation.constructor());
				
				if(annotation.except().length > 0){
					Map<String, String> excepList = new HashMap<String, String>();
					for(String except : annotation.except()){
						excepList.put(except, except);
					}
					smc.setExcept(excepList);
				}

				if(annotation.payload().length > 0){
					
					if(smc.getPayload()==null){
						smc.setPayload(new HashMap<String, String>());
					}

					Map<String, String> payloads = smc.getPayload();
						 
					for(String payload : annotation.payload()){
						payloads.put(payload, payload);
					}
					smc.setPayload(payloads);
				}
				
				/*
				 * event binding
				 */
				if(annotation.bindingFor().length > 0){
					
					List<String> bindingForList = new ArrayList<String>();
					for(String binding : annotation.bindingFor()){
						bindingForList.add(binding);
					}
					
					smc.setBindingFor(bindingForList);
				}

				if(annotation.eventBinding().length > 0){
					
					List<String> eventBindingList = new ArrayList<String>();
					for(String binding : annotation.eventBinding()){
						eventBindingList.add(binding);
					}
					
					smc.setEventBinding(eventBindingList);
				}
				
				if(annotation.keyBinding().length > 0){
					
					List<String> keyBindingList = new ArrayList<String>();
					for(String binding : annotation.keyBinding()){
						keyBindingList.add(binding);
					}
					
					smc.setKeyBinding(keyBindingList);
				}
				
				if(annotation.mouseBinding().length() > 0){
					smc.setMouseBinding(annotation.mouseBinding());
				}
				

				smc.setInContextMenu(annotation.inContextMenu());
				
				if(face!=null){
					smc.setDisplayName(face.displayName());
				}/*else{
					smc.setDisplayName(method.getName());
				}*/
				
				if(available!=null && available.when().length > 0){
					StringBuffer whens = new StringBuffer();
					for(String when : available.when()){
						whens.append(when).append("|");
					}
					
					smc.setWhen(whens.toString());
				}
				
				if(available!=null && available.where().length > 0){
					StringBuffer wheres = new StringBuffer();
					for(String where : available.where()){
						wheres.append(where).append("|");
					}
					
					smc.setWhere(wheres.toString());
				}
				
				if(available!=null && available.how().length > 0){
					StringBuffer hows = new StringBuffer();
					for(String how : available.how()){
						hows.append(how).append("|");
					}
					
					smc.setHow(hows.toString());
				}
				
				if(hidden != null){
					smc.setAttributes(new HashMap());
					
					if(hidden.when().length > 0){
						Map whens = new HashMap();
						for(String when : hidden.when()){
							whens.put(when, when);
						}
						
						smc.attributes.put("hidden.when", whens);
					}
					
					if(hidden.where().length > 0){
						Map wheres = new HashMap();
						for(String where : hidden.where()){
							wheres.put(where, where);
						}
						
						smc.attributes.put("hidden.where", wheres);
					}
					
					if(hidden.how().length > 0){
						Map hows = new HashMap();
						for(String how : hidden.how()){
							hows.put(how, how);
						}
						
						smc.attributes.put("hidden.how", hows);
					}
					
					if(hidden.when().length == 0 && hidden.where().length == 0 && hidden.how().length == 0)
						smc.attributes.put("hidden", hidden.on());
				}

				if(available!=null && available.condition().length > 0){
					if(smc.getAttributes()==null){
						smc.setAttributes(new HashMap<String, Object>());
					}
					
					Map conditions = new HashMap();
					int conditionSeq=0;
					
					for(String condition : available.condition()){
						conditions.put(conditionSeq, condition);
						conditionSeq++;
					}
					
					smc.attributes.put("available.condition", conditions);					
				}

				
				Test[] tests = null;
				if(testSet!=null){
					tests = testSet.value();
				}else if(test!=null){
					tests = new Test[]{test};
				}
				
				if(tests!=null){
					
					for(Test theTest : tests){
						
						TestContext testContext = new TestContext();
						
						if(theTest.next().length > 0)
							testContext.setNext(theTest.next());
						
						if(theTest.scenario().length() > 0)
							testContext.setScenario(theTest.scenario());
						
						if(theTest.value().length > 0)
							testContext.setValue(theTest.value());
	
						if(theTest.instruction().length > 0)
							testContext.setInstruction(theTest.instruction());
	
						testContext.setStarter(theTest.starter());
	
	
						
						if(smc.getAttributes()==null){
							smc.setAttributes(new HashMap<String, Object>());
						}
						
						if(!smc.getAttributes().containsKey("test")){
							smc.getAttributes().put("test", new HashMap<String, TestContext>());
						}
						
						HashMap<String, TestContext> testMap = (HashMap<String, TestContext>) smc.getAttributes().get("test");
						
						testMap.put(testContext.getScenario(), testContext);
					}
				}
				
				serviceMethodContexts.add(smc);				
			}
			
			Collections.sort(serviceMethodContexts, new Comparator<ServiceMethodContext>() {
	            @Override
	            public int compare(ServiceMethodContext o1, ServiceMethodContext o2) {
	                Order or1 = null;
					try {
						or1 = (Order) getAnnotationDeeply(tryingClasses, o1.getMethodName(), Order.class, false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                Order or2 = null;
					try {
						or2 = (Order) getAnnotationDeeply( tryingClasses, o2.getMethodName(), Order.class, false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                // nulls last
	                if (or1 != null && or2 != null) {
	                    return or1.value() - or2.value();
	                } else
	                if (or1 != null && or2 == null) {
	                    return -1;
	                } else
	                if (or1 == null && or2 != null) {
	                    return 1;
	                }
	                return o1.getMethodName().compareTo(o2.getMethodName());
	            }
	        });
		}
		
	}
	
	static public Annotation getAnnotationDeeply(ArrayList<Class> tryingClasses, String symbol, Class annotationCls) throws Exception{
		return getAnnotationDeeply(tryingClasses, symbol, annotationCls, true);
	}
	

	static public Annotation getAnnotationDeeply(ArrayList<Class> tryingClasses, String symbol, Class annotationCls, boolean isField) throws Exception{
		//		Class annotationCls = Thread.currentThread().getContextClassLoader().loadClass("org.metaworks." +annotationName);
		Annotation annotation = null;
		//Class[] tryingClasses = {cls, iDAOCls};
		
		for(int i=0; i<tryingClasses.size(); i++){
			Class clazz = tryingClasses.get(i);
			
			if(clazz==null) return null;
			
			if(symbol!=null){
				
				if(isField){
					Method getter;
					try {
						getter = clazz.getMethod("get" + symbol, new Class[]{});
						if(getter!=null)
							annotation = getter.getAnnotation(annotationCls);
						
						if(annotation!=null) 
							return annotation;
					} catch (NoSuchMethodException e) {
					}
					
					try {
						getter = clazz.getMethod("is" + symbol, new Class[]{});
						
						if(getter!=null)
							annotation = getter.getAnnotation(annotationCls);
						
						if(annotation!=null) 
							return annotation;
					} catch (NoSuchMethodException e) {
					}
				}else{

						Method[] methods = clazz.getMethods();
						
						for(Method method:methods){

							if(method.getName().equals(symbol) && (annotation = method.getAnnotation(annotationCls)) != null){
								annotation = method.getAnnotation(annotationCls);

								Annotation[][] parameterAnnotations = method.getParameterAnnotations();
								if(annotationCls == ServiceMethod.class && parameterAnnotations!=null){
									ServiceMethodAnnotationWithParameterAnnotations serviceMethodAnnotationWithParameterAnnotations = new ServiceMethodAnnotationWithParameterAnnotations();
									serviceMethodAnnotationWithParameterAnnotations.setMethodAnnotation((ServiceMethod)annotation);
									serviceMethodAnnotationWithParameterAnnotations.setParameterAnnotations(parameterAnnotations);
									serviceMethodAnnotationWithParameterAnnotations.setMethod(method);
									
									return serviceMethodAnnotationWithParameterAnnotations;
								}else
									return annotation;
								
								
							}
							
						}
					
				}
			}else{//in case that class level's annotation
				annotation = clazz.getAnnotation(annotationCls);

				if(annotation!=null){
					break;
				}
					

			}
		}

		if(symbol==null && tryingClasses.size() > 0){
		if(annotationCls == Face.class){
			final Face face = (Face)annotation;
			
			for(int i=0; i<tryingClasses.size(); i++){
				boolean componentPathHashBeenChanged = false;
				String componentPath = face != null ? face.ejsPath() : ""; 

				if(componentPath.length() == 0){
					componentPath = getComponentLocation(tryingClasses.get(i), "faces", false, false, "ejs");
					componentPathHashBeenChanged = true;
				}
				
				if(componentPath.startsWith("faces"))
					componentPath = componentPath.substring("faces".length()+1);
				
				if(tryToFindComponent("dwr/metaworks/" + componentPath)){
					componentPathHashBeenChanged = true;

					componentPath = "dwr/metaworks/" + componentPath;
				}else if(!tryToFindComponent(componentPath)){
					componentPath = null;
				}
				
				if(!componentPathHashBeenChanged && face!=null)
					return face;
				
				if(componentPath!=null){
					
					final String ejsPath = componentPath;

					return new org.metaworks.annotation.Face() {
						
						@Override
						public Class<? extends Annotation> annotationType() {
							return null;
						}
						
						@Override
						public String[] values() {
							return face!=null ? face.values() : new String[]{};
						}
						
						@Override
						public String[] options() {
							return face!=null ? face.options() : new String[]{};
						}
						
						@Override
						public String[] ejsPathMappingByContext() {
							return face!=null ? face.ejsPathMappingByContext() : new String[]{};
						}
						
						@Override
						public String ejsPathForArray() {
							return face!=null ? face.ejsPathForArray() : "";
						}
						
						@Override
						public String ejsPath() {
							return ejsPath;
						}
						
						@Override
						public String displayName() {
							return face!=null ? face.displayName() : "";
						}

						@Override
						public Class<? extends org.metaworks.Face> faceClass() {
							return face!=null ? face.faceClass() : org.metaworks.Face.class;
						}

						@Override
						public String faceClassName() {
							return face!=null ? face.faceClassName() : "";
						}
					};
				}				
			}
		}
		}
		
		
		
		
		
		return annotation;
	}

	static public String getClassNameOnly(Class activityCls){
		return getClassNameOnly(activityCls.getName());
	}
	
	static public String getClassNameOnly(String clsName){
		return clsName.substring( clsName.lastIndexOf(".")+1);
	}


	static public String getComponentLocation(Class cls, String compType, boolean isDefault, boolean overridesPackage, String extName){
		if(cls.isArray())
			return "genericfaces/ArrayFace." + extName;
		
		//String pkgName = cls.getPackage().getName(); //not work for janino compiled classes
		
		
		String pkgName = null;
		
		if(cls.getName().indexOf('.') > -1){
			pkgName = cls.getName().substring(0, cls.getName().lastIndexOf("."));
			pkgName = pkgName.replaceAll("\\.", "/");
		}
		
		String clsName = getClassNameOnly(cls);
		
//		return pkgName + "." + compType +(isDefault ? ".Default" : ".")+ clsName + compType.substring(0, 1).toUpperCase() + compType.substring(1, compType.length());
		
		return compType + (pkgName!=null?"/"+ pkgName:"") + "/" + clsName + "." + extName;		
	}
	
	static public boolean tryToFindComponent(String componentName){	
		
		//try classResource first
		if(componentName.startsWith("dwr/" + TransactionalDwrServlet.PATH_METAWORKS))
		try{
			InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(componentName.substring(5 + TransactionalDwrServlet.PATH_METAWORKS.length()));
			
			if(resource!=null){
				resource.close();
				return true;
			}
			
		}catch(Exception ex){}
			
		try{
			HttpServletRequest request = TransactionContext.getThreadLocalInstance().getRequest();
			
	        String url = request.getRequestURL().toString();
	        String codebase = url.substring( 0, url.lastIndexOf( "/" ) );
	        URL urlURL = new java.net.URL(codebase);
	       	String host = urlURL.getHost();
	       	int port = urlURL.getPort();
	       	String path = urlURL.getPath();
	       	String contextOnly = path.substring(0, path.substring(1).indexOf("/")+1);
			String protocol = urlURL.getProtocol();
			
			if("/dwr".equals(contextOnly))
				contextOnly = "";
						
			host = "127.0.0.1";
			
			new URL(protocol + "://" + host + ":" + port + contextOnly + "/metaworks/" + componentName).openStream();
			
			return true;
		}catch(Exception e){
			return false;
		}
		
	}

	
	static public String getComponentLocationByEscalation(Class cls, String compType){
		return getComponentLocationByEscalation(cls, compType, "ejs");
	}
	
	static public String getComponentLocationByEscalation(Class cls, String compType, String extName){
		Class copyOfCls = cls;
		
		//try to find proper component by escalation (prior to overriding package)
		String overridingPackageName = null;//GlobalContext.ACTIVITY_DESCRIPTION_COMPONENT_OVERRIDER_PACKAGE;
//		if(overridingPackageName!=null){
			do{
//				String componentClsName = getComponentLocation(copyOfCls, compType, false, true);
	
//				if(tryToFindComponent(componentClsName)) return componentClsName;

				//try to find proper component by escalation (with original package)
				String componentPath = getComponentLocation(copyOfCls, compType, false, false, extName);
				if(tryToFindComponent("dwr/metaworks/" + componentPath)) return "dwr/metaworks/" + componentPath;
				
				if(tryToFindComponent(componentPath)) return componentPath;
			
				copyOfCls = copyOfCls.getSuperclass();
			}while(copyOfCls!=Object.class && copyOfCls !=null);
//		}
		

		return null;//"genericfaces/ObjectFace.ejs";
	}
	
	static public String toUpperStartedPropertyName(String propertyName){
		return propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	}
	
	public WebFieldDescriptor getFieldDescriptor(String propertyName){
		for(WebFieldDescriptor fd : fieldDescriptors){
			if(fd.getName().equals(propertyName))
				return fd;
		}
		
		return null;
	}
	
	public WebFieldDescriptor getFieldDescriptorByAttribute(String attributName){
		for(WebFieldDescriptor fd : this.getFieldDescriptors()){
			if(fd.getAttribute("namefield") != null && (Boolean)fd.getAttribute("namefield"))
				return fd;
		}

		return new WebFieldDescriptor();
	}
}


class ServiceMethodAnnotationWithParameterAnnotations implements Annotation{

	@Override
	public Class<? extends Annotation> annotationType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	ServiceMethod methodAnnotation;
		
		public ServiceMethod getMethodAnnotation() {
			return methodAnnotation;
		}
	
		public void setMethodAnnotation(ServiceMethod methodAnnotation) {
			this.methodAnnotation = methodAnnotation;
		}

	Annotation[][] parameterAnnotations;

		public Annotation[][] getParameterAnnotations() {
			return parameterAnnotations;
		}
	
		public void setParameterAnnotations(Annotation[][] parameterAnnotations) {
			this.parameterAnnotations = parameterAnnotations;
		}

		
	Method method;

		public Method getMethod() {
			return method;
		}
	
		public void setMethod(Method method) {
			this.method = method;
		}
}

