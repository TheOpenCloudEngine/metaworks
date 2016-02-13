package org.metaworks.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.GeneratedValue;

import org.metaworks.FieldDescriptor;
import org.metaworks.MetaworksContext;
import org.metaworks.ObjectInstance;
import org.metaworks.ObjectType;
import org.metaworks.WebFieldDescriptor;
import org.metaworks.WebObjectType;
import org.metaworks.annotation.ORMapping;
import org.metaworks.dwr.MetaworksRemoteService;

public class Database<T extends IDAO> implements IDAO, Serializable, Cloneable{
	
	transient MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			if(metaworksContext==null)
				metaworksContext = new MetaworksContext();
			
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext context) {
			this.metaworksContext = context;
		}

	final public T databaseMe() throws Exception{
//		boolean securityCheck = false;
//		try {
//			securityCheck = TransactionContext.getThreadLocalInstance().isNeedSecurityCheck();
//			TransactionContext.getThreadLocalInstance().setNeedSecurityCheck(false);

			Object keyValue = createKeyObject();
			
			try{
				return (T) get(getClass(), keyValue, this);
			}catch(ClassCastException cce){
				throw new Exception("You probably mis-defined your database class " + getClass().getName() + " extends Database<interface name> not the implemented class name.", cce);
			}catch(NoSuchEntitySQLException noEntity){
				return null; //meaning no such entity!
			}
//		} finally {
//			TransactionContext.getThreadLocalInstance().setNeedSecurityCheck(securityCheck);
//		}
	}
	
	final public T typedDatabaseMe() throws Exception{
//		boolean securityCheck = false;
//		try {
//			securityCheck = TransactionContext.getThreadLocalInstance().isNeedSecurityCheck();
//			TransactionContext.getThreadLocalInstance().setNeedSecurityCheck(false);

			T dbMe = databaseMe();
			
			Class desiredType = getDesiredTypeByTypeSelector(dbMe);
			
			if(desiredType == null) throw new TypeSelectorException();
			
			return (T) cast(dbMe, desiredType);
		
//		} finally {
//			TransactionContext.getThreadLocalInstance().setNeedSecurityCheck(securityCheck);
//		}
	}
	
	
	final public void flushDatabaseMe() throws Exception{
		flush(getClass(), createKeyObject());
	}
	
	final public T createDatabaseMe() throws Exception{
//		boolean securityCheck = false;
//		try {
//			securityCheck = TransactionContext.getThreadLocalInstance().isNeedSecurityCheck();
//			TransactionContext.getThreadLocalInstance().setNeedSecurityCheck(false);
//
		
			return (T) create(createKeyObject(), this);
//		} finally {
//			TransactionContext.getThreadLocalInstance().setNeedSecurityCheck(securityCheck);
//		}
	}
	
	
	public void copyFrom(T fromObj) throws Exception{
		WebObjectType wot = MetaworksRemoteService.getInstance().getMetaworksType(getClass().getName());
		
		ObjectInstance objInst = (ObjectInstance) wot.metaworks2Type().createInstance();
		objInst.setObject(this);
		
		ObjectInstance fromObjInst = (ObjectInstance) wot.metaworks2Type().createInstance();
		fromObjInst.setObject(fromObj);
		
		if(fromObj != null)
			setMetaworksContext(fromObj.getMetaworksContext());
		
		for(int i=0; i<wot.metaworks2Type().getFieldDescriptors().length; i++){
			FieldDescriptor fd = wot.metaworks2Type().getFieldDescriptors()[i];
			
			objInst.setFieldValue(fd.getName(), fromObjInst.getFieldValue(fd.getName()));
		}
	}
	
	final public void syncToDatabaseMe() throws Exception{
//		boolean securityCheck = false;
//		try {
//			securityCheck = TransactionContext.getThreadLocalInstance().isNeedSecurityCheck();
//			TransactionContext.getThreadLocalInstance().setNeedSecurityCheck(false);

			IDAO databaseMe = databaseMe();
			
			WebObjectType wot = MetaworksRemoteService.getInstance().getMetaworksType(databaseMe.getImplementationObject().getDaoClass().getName());
			
			ObjectInstance objInst = (ObjectInstance) wot.metaworks2Type().createInstance();
			objInst.setObject(this);
			
			ObjectInstance databaseObjInst = (ObjectInstance) wot.metaworks2Type().createInstance();
			databaseObjInst.setObject(databaseMe);
	
			for(int i=0; i<wot.metaworks2Type().getFieldDescriptors().length; i++){
				FieldDescriptor fd = wot.metaworks2Type().getFieldDescriptors()[i];
				
				if(fd.isSavable())
					databaseObjInst.setFieldValue(fd.getName(), objInst.getFieldValue(fd.getName()));
			}
		

//		} finally {
//			TransactionContext.getThreadLocalInstance().setNeedSecurityCheck(securityCheck);
//		}

	}

	
	final public void deleteDatabaseMe() throws Exception{
//		boolean securityCheck = false;
//		try {
//			securityCheck = TransactionContext.getThreadLocalInstance().isNeedSecurityCheck();
//			TransactionContext.getThreadLocalInstance().setNeedSecurityCheck(false);

			WebObjectType wot = MetaworksRemoteService.getInstance().getMetaworksType(getClass().getName());
			ObjectType ot = (ObjectType) wot.metaworks2Type();
			ObjectInstance oi = (ObjectInstance) ot.createInstance();
			
			oi.setObject(this);
			
			if(wot.getKeyFieldDescriptor()==null)
				throw new Exception("[WARN] Even though domain class '" + wot.metaworks2Type().getName() + "' is a database synchronizable object, it has no key field description. Give @org.metaworks.Id for the key field's GETTER[!] method NOT the SETTER.");
			
			Object keyValue = oi.getFieldValue(wot.metaworks2Type().getKeyFieldDescriptor().getName());
			
			//TODO: where clause should be generated by the referencing Object connections 
			IDAO forDelete =  Database.sql(IDAO.class, "delete from "+ot.getName()+" where "+wot.getKeyFieldDescriptor().getName()+" = ?" + wot.getKeyFieldDescriptor().getName());
			forDelete.set(wot.getKeyFieldDescriptor().getName(), keyValue);
			int i=forDelete.update();
			
			if(i<1) throw new Exception("No data to delete!");
//		} finally {
//			TransactionContext.getThreadLocalInstance().setNeedSecurityCheck(securityCheck);
//		}
	}

	protected Object createKeyObject() throws Exception {
		WebObjectType wot = MetaworksRemoteService.getInstance().getMetaworksType(getClass().getName());
		if(wot.getKeyFieldDescriptor()==null)
			return null;
		//	throw new Exception("[WARN] Even though domain class '" + wot.metaworks2Type().getName() + "' is a database synchronizable object, it has no key field description. Give @org.metaworks.Id for the key field's GETTER[!] method NOT the SETTER.");
		
		ObjectType ot = (ObjectType) wot.metaworks2Type();
		ObjectInstance oi = (ObjectInstance) ot.createInstance();
		
		oi.setObject(this);
		
		String keyName = wot.metaworks2Type().getKeyFieldDescriptor().getName();		
		Object keyValue = oi.getFieldValue(keyName);

		if(keyValue == null && wot.iDAOClass().getMethod("get"+keyName, null).getAnnotation(GeneratedValue.class) == null)
			throw new Exception("Even though domain class '" + wot.metaworks2Type().getName() + "' is a database synchronizable object, it has no key value.");

		
		return keyValue;
	}

//	
//	public void sync() throws Exception{
//		getDatabaseMe()
//	}
	
	/////////////////// stubs. don't care ///////////////////

	@Override
	public void select() throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Not implemented. Use databaseMe() instead.");
	}

	@Override
	public int insert() throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Not implemented. Use databaseMe() instead.");
//		return 0;
	}

	@Override
	public int update() throws Exception {
		throw new Exception("Not implemented. Use databaseMe() instead.");
//		return 0;
	}

	@Override
	public int update(String stmt) throws Exception {
		throw new Exception("Not implemented. Use databaseMe() instead.");
//		return 0;
	}

	@Override
	public int call() throws Exception {
		throw new Exception("Not implemented. Use databaseMe() instead.");
//		return 0;
	}

	@Override
	public void addBatch() throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Not implemented. Use databaseMe() instead.");

	}

	@Override
	public int[] updateBatch() throws Exception {
		throw new Exception("Not implemented. Use databaseMe() instead.");
//		return null;
	}

	@Override
	public void beforeFirst() throws Exception {
		throw new Exception("Not implemented. Use databaseMe() instead.");
		
	}

	@Override
	public boolean previous() throws Exception {
		throw new Exception("Not implemented. Use databaseMe() instead.");
//		return false;
	}

	@Override
	public boolean next() throws Exception {
		throw new Exception("Not implemented. Use databaseMe() instead.");
//		return false;
	}

	@Override
	public boolean first() throws Exception {
		throw new Exception("Not implemented. Use databaseMe() instead.");
//		return false;
	}

	@Override
	public void afterLast() throws Exception {
		throw new Exception("Not implemented. Use databaseMe() instead.");
		
	}

	@Override
	public boolean last() throws Exception {
		throw new Exception("Not implemented. Use databaseMe() instead.");
//		return false;
	}

	@Override
	public boolean absolute(int pos) throws Exception {
		throw new Exception("Not implemented. Use databaseMe() instead.");
//		return false;
	}

	@Override
	public int size() {
		throw new RuntimeException("Not implemented. Use databaseMe() instead.");
//		return 0;
	}

	@Override
	public Object get(String key) throws Exception {
		throw new RuntimeException("Not implemented. Use databaseMe() instead.");
//		return null;
	}

	@Override
	public Object set(String key, Object value) throws Exception {
		throw new RuntimeException("Not implemented. Use databaseMe() instead.");
//		return null;
	}

	@Override
	public String getString(String key) throws Exception {
		throw new RuntimeException("Not implemented. Use databaseMe() instead.");
//		return null;
	}

	@Override
	public Integer getInt(String key) throws Exception {
		throw new RuntimeException("Not implemented. Use databaseMe() instead.");
//		return null;
	}

	@Override
	public Long getLong(String key) throws Exception {
		throw new RuntimeException("Not implemented. Use databaseMe() instead.");
//		return null;
	}

	@Override
	public Boolean getBoolean(String key) throws Exception {
		throw new RuntimeException("Not implemented. Use databaseMe() instead.");
//		return null;
	}

	@Override
	public Date getDate(String key) throws Exception {
		throw new RuntimeException("Not implemented. Use databaseMe() instead.");
//		return null;
	}

	@Override
	public AbstractGenericDAO getImplementationObject() {
//		throw new RuntimeException("Not implemented. Use databaseMe() instead.");
		return null;
	}

	@Override
	public void releaseResource() throws Exception {
		throw new RuntimeException("Not implemented. Use databaseMe() instead.");
		
	}
	@Override
	public void moveToInsertRow() throws Exception {
		throw new RuntimeException("Not implemented. Use databaseMe() instead.");
		
	}
	
	
	
	/////////// static service methods ////////////
	public static IDAO create(Object key, Object defaultValue) throws Exception{

		if(TransactionContext.getThreadLocalInstance()==null)
			throw new NoTransactionException("There is No TransactionContext has been started.");


		WebObjectType webObjectType = MetaworksRemoteService.getInstance().getMetaworksType(defaultValue.getClass().getName());
		ObjectInstance objInst = (ObjectInstance) webObjectType.metaworks2Type().createInstance();
		objInst.setObject(defaultValue);
		//Object keyValue = objInst.getKeyFieldValue();
		
		Class iDAOType = webObjectType.iDAOClass();
		if(iDAOType == null) iDAOType = IDAO.class;
		
		if(webObjectType.metaworks2Type().getKeyFieldDescriptor() != null && key == null){
			GeneratedValue gv = (GeneratedValue) iDAOType.getMethod("get"+ webObjectType.metaworks2Type().getKeyFieldDescriptor().getName(), null).getAnnotation(GeneratedValue.class);
					
			if(gv!=null) {			
				if(gv.generator() != null || !gv.generator().equals("")) {
					Map options = new HashMap();
					options.put("onlySequenceTable", true);
					
					key = UniqueKeyGenerator.issueKey(webObjectType.metaworks2Type().getName(), options, TransactionContext.getThreadLocalInstance().getConnectionFactory());
					
					objInst.setFieldValue(webObjectType.metaworks2Type().getKeyFieldDescriptor().getName(), key);
				}
			}
		}
		//end
		
		// this will add the dao to cache list which would be inserted or updated when the transaction is committed
		// so that all the changes in the database is manipulated in memory space, and updated only the changes to the database.
		IDAO dao = TransactionContext.getThreadLocalInstance().createSynchronizedDAO(
			webObjectType.metaworks2Type().getName(), 
			webObjectType.metaworks2Type().getKeyFieldDescriptor()!=null ? webObjectType.metaworks2Type().getKeyFieldDescriptor().getName() : null, 
			key, 
			iDAOType,
			defaultValue
		);
		
		if(defaultValue==null) return dao;
		
		/*
		if(genKey != null) {
			dao.set(webObjectType.metaworks2Type().getKeyFieldDescriptor().getName(), genKey);
		}
		*/
		
		//set the object value to prepare the where clauses well. this part is not for getting relation tuple to object, is for converting object to tuple.
		for(FieldDescriptor fd : webObjectType.metaworks2Type().getFieldDescriptors()){
			Object fieldValue = objInst.getFieldValue(fd.getName()); 
			
			if(webObjectType.iDAOClass()!=null){
				//if IDAO interface doesn't have this field, it is not a database synchronizable field
				if(MetaworksRemoteService.getInstance().getMetaworksType(webObjectType.iDAOClass().getName()).metaworks2Type().getFieldDescriptor(fd.getName()) == null) 
					continue;
			}
			
			
			
			ObjectInstance databaseObjInst = (ObjectInstance) MetaworksRemoteService.getInstance().getMetaworksType(dao.getImplementationObject().getDaoClass().getName()).metaworks2Type().createInstance();
			databaseObjInst.setObject(dao);

			if(fd.isSavable() && fd.getAttribute("ormapping")!=null){
				ORMapping ormapping = (ORMapping)fd.getAttribute("ormapping");
				
				if(ormapping.availableWhen().length > 0 && !ormapping.availableWhen()[0].equals("")){
					boolean ormappingIsAvailable = false;
					for( int i = 0; i < ormapping.availableWhen().length; i++){
						try{
							String[] propAndValue = ormapping.availableWhen()[i].split("==");
							String availabilityCheckPropName = propAndValue[0].trim();
							String availabilityCheckValue = propAndValue[1].trim();

							Object value = objInst.getFieldValue(WebObjectType.toUpperStartedPropertyName(availabilityCheckPropName));

							Object comparer = null;
							
							if(availabilityCheckValue.startsWith("'")){ 
								availabilityCheckValue = availabilityCheckValue.substring(1, availabilityCheckValue.length() - 1);
								comparer = availabilityCheckValue;
							}else{
								comparer = new Integer(availabilityCheckValue);
							}
														
							ormappingIsAvailable = comparer.equals(value);
							if( ormappingIsAvailable ){
								break;
							}
							
						}catch(Exception e){
							
						}
					}
					if(!ormappingIsAvailable)
						continue;
				}
				
				databaseObjInst.setFieldValue(fd.getName(), fieldValue);
				
			}else if(fd.isSavable() || fd.isKey()){
				if(!dbPrimitiveTypes.containsKey(fd.getClassType())){
					try {						
						ObjectType referenceTableType = (ObjectType) MetaworksRemoteService.getInstance().getMetaworksType(fd.getClassType().getName()).metaworks2Type();
						referenceTableType.getKeyFieldDescriptor();
						ObjectInstance instance = (ObjectInstance) referenceTableType.createInstance();
						instance.setObject(fieldValue);
						
						fieldValue = instance.getKeyFieldValue();
					} catch(NullPointerException e) {						
						new NullPointerException("key field descriptor for automatic object value mapping is null (name : " + fd.getName() + ")").printStackTrace();
					}						
				}
				
				dao.set(fd.getName(), fieldValue);
			}
		}
		
		return dao;
	}
	
	public T castDatabaseMe(Class<T> desiredType) throws Exception{
		return (T) cast(databaseMe(), desiredType);
	}
	
	public static Class getDesiredTypeByTypeSelector(IDAO dao) throws Exception{
//		IDAO dbMe = databaseMe();
		
		WebObjectType webObjectType = MetaworksRemoteService.getInstance().getMetaworksType(dao.getImplementationObject().getDaoClass().getName());
		for(WebFieldDescriptor fd : webObjectType.getFieldDescriptors()){
			Map<String, String> typeSelector = (Map<String, String>) fd.getAttribute("typeselector");
			if(typeSelector!=null){
				String typeName = (String) dao.get(fd.getName());
				if(typeName == null)
					typeName = fd.getDefaultValue();
				
				String selectedTypeClassName = typeSelector.get(typeName);
				
				if(selectedTypeClassName==null)
					return null;
				
				return Thread.currentThread().getContextClassLoader().loadClass(selectedTypeClassName);
			}
		}
		
		return null;
		//throw new TypeSelectorException();
	}
	
	public static IDAO cast(IDAO original, Class<?> desiredType) throws Exception{
		
		WebObjectType webObjectType = MetaworksRemoteService.getInstance().getMetaworksType(desiredType.getName());
		ObjectInstance desiredInstance = (ObjectInstance) webObjectType.metaworks2Type().createInstance();
		
		for(FieldDescriptor fd : webObjectType.metaworks2Type().getFieldDescriptors()){
			if(fd.getAttribute("ormapping")==null)
				desiredInstance.setFieldValue(fd.getName(), original.get(fd.getName()));
		}

		return (IDAO) desiredInstance.getObject();
		
	}
	
	public static IDAO sql(Class<?> classType, String sql) throws Exception{
//		boolean securityCheck = false;
//		try {
//			securityCheck = TransactionContext.getThreadLocalInstance().isNeedSecurityCheck();
//			TransactionContext.getThreadLocalInstance().setNeedSecurityCheck(false);

		
			WebObjectType webObjectType = MetaworksRemoteService.getInstance().getMetaworksType(classType.getName());
			
			IDAO dao = (IDAO) MetaworksDAO.createDAOImpl(
				TransactionContext.getThreadLocalInstance(), 
				sql, 
				webObjectType.iDAOClass() != null ? webObjectType.iDAOClass() : IDAO.class
			);
			
			return dao;
			
//		} finally {
//			TransactionContext.getThreadLocalInstance().setNeedSecurityCheck(securityCheck);
//		}
	}
	
	public T sql(String sql) throws Exception{
		
		return (T) sql(getClass(), sql);
	}
	
	
	public T auto() throws Exception{
		return auto(null);
	}
	
	public T auto(String sql) throws Exception{
		Class classType = getClass();
		WebObjectType webObjectType = MetaworksRemoteService.getInstance().getMetaworksType(classType.getName());
		
		IDAO dao = (IDAO) MetaworksDAO.createDAOImpl(
			TransactionContext.getThreadLocalInstance(), 
			sql, 
			webObjectType.iDAOClass() != null ? webObjectType.iDAOClass() : IDAO.class
		);
		
		dao.getImplementationObject().setTableName(webObjectType.metaworks2Type().getName());
		if(webObjectType.metaworks2Type().getKeyFieldDescriptor()!=null)
			dao.getImplementationObject().setKeyField(webObjectType.metaworks2Type().getKeyFieldDescriptor().getName());
		dao.getImplementationObject().setAutoSQLGeneration(true);
		
		return (T)dao;
	}
	
//		selections.select();
//		//Collection collection = postings.getImplementationObject().toCollection();
//		
//		WebObjectType webObjectType = MetaworksRemoteService.getMetaworksType(classType.getName());
//		
//		ObjectInstance objInst = (ObjectInstance) webObjectType.metaworks2Type().createInstance();
//
//		int i = 0;
//		Object array[] = new Object[selections.size()];
//		while(selections.next()){
//			
//			for(FieldDescriptor fd : webObjectType.metaworks2Type().getFieldDescriptors()){
//				Object fieldValue = selections.get(fd.getName()); 
//				
//				objInst.setFieldValue(fd.getName(), fieldValue);
//			}
//			
//			array[i++] = objInst.getObject();
//			
//		}
//		
//		return array;
//	}
	
	protected static HashMap<Class, Class> dbPrimitiveTypes = new HashMap<Class, Class>();
	static{
		dbPrimitiveTypes.put(String.class, String.class);
		dbPrimitiveTypes.put(Number.class, Number.class);
		dbPrimitiveTypes.put(Integer.class, Integer.class);
		dbPrimitiveTypes.put(int.class, int.class);
		dbPrimitiveTypes.put(boolean.class, boolean.class);
		dbPrimitiveTypes.put(Boolean.class, Boolean.class);
		dbPrimitiveTypes.put(Long.class, Long.class);
		dbPrimitiveTypes.put(Calendar.class, Calendar.class);
		dbPrimitiveTypes.put(Date.class, Date.class);
		dbPrimitiveTypes.put(Timestamp.class, Timestamp.class);
		
		//it is special case
		dbPrimitiveTypes.put(MetaworksContext.class, MetaworksContext.class);
		
	}
	
	public static IDAO get(Class classType, Object key) throws Exception{
		return get(classType, key, null);		
	}
	
	public static IDAO get(Class classType, Object key, Object synchronizedObject) throws Exception{
		
		WebObjectType webObjectType = MetaworksRemoteService.getInstance().getMetaworksType(classType.getName());

		IDAO dao = TransactionContext.getThreadLocalInstance().findSynchronizedDAO(
				webObjectType.metaworks2Type().getName(), 
				webObjectType.metaworks2Type().getKeyFieldDescriptor().getName(), 
				key, 
				webObjectType.iDAOClass() != null ? webObjectType.iDAOClass() : IDAO.class,
				synchronizedObject);
			
		return dao;
		
	}
	
	public static void flush(Class classType, Object key) throws Exception{
		WebObjectType webObjectType = MetaworksRemoteService.getInstance().getMetaworksType(classType.getName());

		TransactionContext.getThreadLocalInstance().flushSynchronizedDAO(
				webObjectType.metaworks2Type().getName(), 
				key
		);
	}
	
	public static Object createReferenceObject(Class classType, IDAO dao) throws Exception{

		if(!Database.dbPrimitiveTypes.containsKey(classType) && classType != MetaworksContext.class){
			WebObjectType webObjectType = MetaworksRemoteService.getInstance().getMetaworksType(classType.getName());
			ObjectInstance objInst = (ObjectInstance) webObjectType.metaworks2Type().createInstance();
			
			Class daoClass = webObjectType.iDAOClass();
			IDAO refDAO = null;
			if(daoClass != null && IDAO.class.isAssignableFrom(daoClass)){
				refDAO = MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(), null, daoClass);
			}
			
			for(FieldDescriptor fd : webObjectType.metaworks2Type().getFieldDescriptors()){
				try{
					if(!fd.isLoadable()) 
						continue;
					
					if(refDAO==null)
						objInst.setFieldValue(fd.getName(), dao.get(fd.getName()));
					else{
						refDAO.set(fd.getName(), dao.get(fd.getName()));
					}
				}catch(Exception e){
					//e.printStackTrace();
					if(MetaworksRemoteService.getInstance().isDebugMode())
						System.err.println("Error to map reference dao's property from " + fd.getName() + " Automated DAO. You may add isLoadable=false annotation to the setter field 'set" + fd.getName() + "()' of class '" + classType.getName() + "'. For example Metaworks lets you expression like following SQL: select * from A, B, C and all the fields from three tables, then Metaworks tries to map all the DAO fields to the mapped class A, B, C with the retrieved selected column names. Metaworks will tryies these everytime, so if you don't want to do this, mark isLoadable = false to the getter method definition.");
				}
			}
			
			if(refDAO!=null) 
				return refDAO;
			
			Object referenceObject = objInst.getObject();
			
			return referenceObject;
		}
		
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null) return false;
		
		try {
			WebObjectType wot = MetaworksRemoteService.getInstance().getMetaworksType(getClass().getName());
			
			WebFieldDescriptor keyfd = wot.getKeyFieldDescriptor();
			
			Object keyFieldValue;
			keyFieldValue = getClass().getField(keyfd.getName()).get(this);

			Object comparatorFieldValue = obj.getClass().getField(keyfd.getName()).get(obj);

			return keyFieldValue.equals(comparatorFieldValue);
			
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
			
	
}
