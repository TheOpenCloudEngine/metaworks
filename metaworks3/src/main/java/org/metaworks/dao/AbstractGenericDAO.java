package org.metaworks.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import org.metaworks.ContextAware;
import org.metaworks.FieldDescriptor;
import org.metaworks.MetaworksContext;
import org.metaworks.ObjectInstance;
import org.metaworks.ObjectType;
import org.metaworks.WebFieldDescriptor;
import org.metaworks.WebObjectType;
import org.metaworks.annotation.ORMapping;
import org.metaworks.dwr.MetaworksRemoteService;

/**
 * Generic DAO
 *
 * @author <a href="mailto:ghbpark@hanwha.co.kr">Sungsoo Park</a>, Jinyoung Jang
 * @version $Id: AbstractGenericDAO.java,v 1.33 2010/04/08 01:27:21 allbegray Exp $
 */
public abstract class AbstractGenericDAO implements InvocationHandler, IDAO {
//	protected boolean isAutoCommit = true;
	
	protected boolean isConnective = false;
		public boolean isConnective() {
			return isConnective;
		}
		public void setConnective(boolean isConnective) {
			this.isConnective = isConnective;
		}
		
	private boolean isDirty = false;
		public boolean isDirty() {
			return isDirty;
		}

	private boolean isUpdateOnlyWhenDirty = true;
		protected boolean updateOnlyWhenDirty() {
			return isUpdateOnlyWhenDirty;
		}
		public void setUpdateOnlyWhenDirty(boolean isUpdateOnlyWhenDirty) {
			this.isUpdateOnlyWhenDirty = isUpdateOnlyWhenDirty;
		}
		
    protected CachedRowSet rowSet = null;
    
    protected ConnectionFactory connectionFactory;
		public ConnectionFactory getConnectionFactory() {
			return connectionFactory;
		}
		public void setConnectionFactory(ConnectionFactory factory) {
			//if(factory==null) throw new RuntimeException("ConnectionFactory should not be null.");
			
			connectionFactory = factory;
		}
    
    //private HashMap propertyMap;
	protected HashMap cache;
    protected HashMap outFieldMap;
    protected HashMap modifiedFieldMap;
    
    protected boolean isBatchUpdate = false;
    
    String sqlStmt;
		public String getSqlStmt() {
			return sqlStmt;
		}
		public void setSqlStmt(String sqlStmt) {
			this.sqlStmt = sqlStmt;
		}

	String tableName;
		public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		
	String keyField;
		public String getKeyField() {
			return keyField;
		}
		public void setKeyField(String keyField) {
			this.keyField = keyField;
		}

    
	Class daoClass;
		public Class getDaoClass() {
			return daoClass;
		}

	HashMap<String, Class> propertyTypes;
		
	ArrayList<String> propertyNames;
		public ArrayList<String> getPropertyNames() {
			return propertyNames;
		}
		
	Object synchronizedObject;
		public Object getSynchronizedObject() {
			return synchronizedObject;
		}
		public void setSynchronizedObject(Object synchronizedObject) {
			this.synchronizedObject = synchronizedObject;
		}
//	HashMap isOptional;
	




	boolean autoSQLGeneration;
		public boolean isAutoSQLGeneration() {
			return autoSQLGeneration;
		}
		public void setAutoSQLGeneration(boolean autoSQLGeneration) {
			this.autoSQLGeneration = autoSQLGeneration;
		}


	static RowSetFactory rowSetFactory;
	protected static RowSetFactory getRowSetFactory() {

		if(rowSetFactory==null){
			try {
				rowSetFactory = RowSetProvider.newFactory();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return rowSetFactory;
	}


	protected AbstractGenericDAO(Class daoClass, boolean isConnective) throws Exception{
		initialize(daoClass, isConnective);
	}	


  
	protected AbstractGenericDAO(final String jndiName, boolean isConnective, String sqlStmt, Class daoClass) throws Exception {
		this(
			new ConnectionFactory(){
				public Connection getConnection() throws Exception{
					InitialContext ctx = null;
					ctx = new InitialContext();
					DataSource ds = (javax.sql.DataSource) ctx.lookup(jndiName);
					return ds.getConnection();
				}
			}, 
			isConnective,
			sqlStmt, daoClass, null
		);		
	}
	
//	protected AbstractGenericDAO(TransactionContext tc, String sqlStmt, Class daoClass)	throws Exception {
//		this(
//			new TransactionContextConnectionFactoryAdapter(tc),
//			false,
//			sqlStmt, daoClass
//		);
//		this.tc = tc;
//	}		

//	protected AbstractGenericDAO(final Connection conn, boolean isConnective, String sqlStmt, Class daoClass)	throws Exception {
//		this(
//			new ConnectionFactory(){
//				public Connection getConnection() throws Exception{
//					return conn;
//				}
//			},
//			isConnective,
//			sqlStmt, daoClass
//		);
//	}		

	protected AbstractGenericDAO(ConnectionFactory con, boolean isConnective, String sqlStmt, Class daoClass, Object synchronizedObject) throws Exception {
		initialize(con, isConnective, sqlStmt, daoClass, synchronizedObject);
	}
	
	protected void initialize(ConnectionFactory cf, boolean isConnective, String sqlStmt, Class daoClass, Object synchronizedObject)
		throws Exception {
		
		setSynchronizedObject(synchronizedObject);
		
		setConnectionFactory(cf);

/*		propertyMap = new HashMap();{
			String[] splits = statement.split("?");
			for(int i=0; i<splits.length; i++){
				String token = splits[i];
    		
				String splits2[] = token.split(" ");        		
				String propertyName = splits2[0];
    		
				propertyMap.put(propertyName.toUpperCase(), new Integer(i));
			}
		}*/
//		this.outFieldMap = new HashMap();    
//		this.cache = new HashMap();
		
//		this.selectSqlStmt = selectSqlStmt;
//		this.insertSqlStmt = insertSqlStmt;
//		this.updateSqlStmt = updateSqlStmt;
		
//		this.actualSelectSqlStmt = getActualSqlStmt(selectSqlStmt); 
//		this.actualInsertSqlStmt = getActualSqlStmt(insertSqlStmt);
//		this.actualUpdateSqlStmt = getActualSqlStmt(updateSqlStmt);
		
//		this.daoClass = daoClass;
//		Method[] methods = daoClass.getMethods();
//		propertyTypes = new HashMap(methods.length);
//		for(int i=0; i<methods.length; i++){
//			String propName = methods[i].getName();
//			
//			if(propName.startsWith("get")){
//				propName = propName.substring(3).toUpperCase();
//				propertyTypes.put(propName, methods[i].getReturnType());
//			}
//		}
		setStatement(sqlStmt);
		initialize(daoClass, isConnective);
	}

	
	protected void initialize(Class daoClass, boolean isConnective) throws Exception {
//		this.isAutoCommit = isAutoCommit;
		this.isConnective = isConnective;
		
//		if ( isAutoCommit ) isConnective = false;
		
		this.outFieldMap = new HashMap();    
		
		this.cache = new HashMap(){

			@Override //this will guarantee the cachedRows and cache access will synchronized exactly. 
			public Object get(Object arg0) {
				if(cachedRows==null)
					return super.get(arg0);
				else{
					HashMap currRowCache = cachedRows.get(cursor);
					return currRowCache.get(arg0);
				}
			}

			@Override //this will guarantee the cachedRows and cache access will synchronized exactly. 
			public Object put(Object arg0, Object arg1) {
				if(cachedRows==null)
					return super.put(arg0, arg1);
				else{
					HashMap currRowCache = cachedRows.get(cursor);
					return currRowCache.put(arg0, arg1);
				}
			}
			
		};
		
		this.daoClass = daoClass;

		initializePropertyTypes();
	}
	
	protected void initializePropertyTypes(){
		Method[] methods = daoClass.getMethods();
		propertyTypes = new HashMap<String, Class>(methods.length);
		propertyNames = new ArrayList<String>();
		for(int i=0; i<methods.length; i++){
			String propName = methods[i].getName();
			
			boolean getterStartsGet = propName.startsWith("get");
			boolean getterStartsIs = (getterStartsGet ? false : propName.startsWith("is"));
			
			if((getterStartsGet || getterStartsIs) && methods[i].getParameterTypes().length == 0){
				
				try{
					propName = propName.substring((getterStartsGet ? 3 : 2));

					propertyNames.add(propName.substring(0, 1).toLowerCase() + propName.substring(1));
					
					propName = propName.toUpperCase();
					propertyTypes.put(propName, methods[i].getReturnType());
				}catch(Exception e){
					//ignore if there's no setter having same property naming convention.
					e.printStackTrace();
				}
				
			}
		}		
	}
	
	public Class getPropertyType(String propertyName){
		if(propertyTypes==null){
			initializePropertyTypes();
		}
		
		return propertyTypes.get(propertyName);
	}
	
	public void select() throws Exception {
		
		if(sqlStmt == null && isAutoSQLGeneration()){
			createSelectSql();
		}
		
//		rowSet = null;
		//TODO: need to be cached
		releaseResource();
		
		PreparedStatement pstmt = null;
		ConnectionFactory cf = getConnectionFactory();
		Connection con = cf.getConnection();
		try{
			if(con == null) throw new Exception("Connection is null");
			
			pstmt = con.prepareStatement(getActualSqlStmt(getStatement()));
		
			lateBindProperties(getStatement(), pstmt);


			rowSet = getRowSetFactory().createCachedRowSet();

			
			//System.out.println("=====>");
			//System.out.println(getStatement());
			
			Long startTime = System.nanoTime();
			
			ResultSet rs = pstmt.executeQuery();
			
			//System.out.println("query : " + String.valueOf((System.nanoTime() - startTime) / 1000000));
			
			rowSet.populate(rs);
			
			startTime = System.nanoTime();
			
			//System.out.println("populate : " + String.valueOf((System.nanoTime() - startTime) / 1000000));
			
			rowSet.beforeFirst();
			
			isDirty = false;
			
		}catch(Exception e){
			throw new Exception(e.getMessage() + " when to try sql [" + getStatement() + "] :", e);
		}finally{
			if (rowSet!= null && cf instanceof TransactionContext) {
				TransactionContext tc = (TransactionContext) cf;
				tc.addReleaseResourceListeners(new ReleaseResourceListener() {
					
					@Override
					public void beforeReleaseResource(TransactionContext tx) throws Exception {
						releaseResource();
					}
				});
			}
			
			try{pstmt.close();}catch(Exception e){}
			if ( !isConnective ) {
				checkOkToCloseConnection();
				try{con.close();}catch(Exception e){}
			}
			
		}		
	}
	
	private void checkOkToCloseConnection() throws Exception{
	}
	
	public void createInsertSql() throws Exception{
		final StringBuffer sql_KeyNames = new StringBuffer();
		final StringBuffer sql_ValuePlaceHolders = new StringBuffer();
		
		
		WebObjectType webObjectType = MetaworksRemoteService.getInstance().getMetaworksType(daoClass.getName());
		
		/*
		 * 2013/04/23 add database type(cubrid)
		 */
		final DAOUtil daoUtil = new DAOUtil();
		
		if(rowSet==null){	
			ForLoop loopForCacheKeys = new ForLoop(){
				String sep = "";

				public void logic(Object target) {
					String propertyName = (String)target;

//					webObjectType
					//ignores metaworks signals
					if("METAWORKSCONTEXT".equals(propertyName)) return;
					
					propertyName = daoUtil.replaceReservedKeyword(propertyName);
					
					sql_KeyNames.append(sep + propertyName);
					sql_ValuePlaceHolders.append(sep + "?" + propertyName);
					
					sep =", ";
				}
				
			};
			
			loopForCacheKeys.run(cache.keySet());
			sqlStmt = "insert into " + getTableName() + "("+ sql_KeyNames +") values (" + sql_ValuePlaceHolders + ")";

		}else{				
			String sep = "";
			ResultSetMetaData rsMetaData = rowSet.getMetaData();
			for(int i=1; i<=rsMetaData.getColumnCount(); i++){
				String propertyName = rsMetaData.getColumnName(i);
				
				sql_KeyNames.append(sep + propertyName);
				sql_ValuePlaceHolders.append(sep + "?" + propertyName);
				
				sep =", ";
			}

			sqlStmt = "insert into " + getTableName() + "("+ sql_KeyNames +") values (" + sql_ValuePlaceHolders + ")";

		   	adjustMetaDataIfFetched();
		}
		
	}
	
	public int insert() throws Exception {
		if(sqlStmt == null && isAutoSQLGeneration()){
			createInsertSql();
		}
		
		return update();
	}

	private static Hashtable typeMappingHT = new Hashtable();
	static{
		typeMappingHT.put(Integer.valueOf(Types.VARCHAR), 	String.class);
		typeMappingHT.put(Integer.valueOf(Types.INTEGER), 	Number.class);
		typeMappingHT.put(Integer.valueOf(2), 				Number.class);
		typeMappingHT.put(Integer.valueOf(Types.DATE),		Date.class);
		typeMappingHT.put(Integer.valueOf(Types.BOOLEAN),	Boolean.class);
	}
	protected void adjustMetaDataIfFetched() throws Exception{
		if(rowSet==null) return;
		
		ResultSetMetaData rsMetaData = rowSet.getMetaData();
		for(int i=1; i<=rsMetaData.getColumnCount(); i++){
			String propertyName = rsMetaData.getColumnName(i);
			int type = rsMetaData.getColumnType(i);

			Class propertyCls = (Class)typeMappingHT.get(Integer.valueOf(type));
			if(propertyCls!=null)
				propertyTypes.put(propertyName, propertyCls);
		}
	}
	
	public int update(String sqlStmt) throws Exception {	
		setStatement(sqlStmt);
		return update();
	}
	
	
	private int batchCount = 0;
		public int getBatchCount() {
			return batchCount;
		}

	private PreparedStatement pstmtForBatch = null;
	private Connection connForBatch = null;
	
	public void addBatch() throws Exception {
		isBatchUpdate = true;
		if ( batchCount == 0 ) {
//			if ( getConnectionFactory() == null ) 
//				setConnectionFactory(new ConnectionFactory());
			connForBatch = getConnectionFactory().getConnection();
			pstmtForBatch = connForBatch.prepareStatement(getActualSqlStmt(getStatement()));
		} 
		lateBindProperties(getStatement(), pstmtForBatch);
		pstmtForBatch.addBatch();
		batchCount++;
	}
	
	public int[] updateBatch() throws Exception {
		try{
			if ( batchCount > 0 ) {
				return pstmtForBatch.executeBatch();
			} else {
				int[] btc = new int[0];
				return btc;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("Error when to try sql [" + getStatement() + "] ", e);
			
		}finally{
			try{pstmtForBatch.close();}catch(Exception e){}
			if ( !isConnective ) {
				checkOkToCloseConnection();
				try{connForBatch.close();}catch(Exception e){}
			}
		}			
	}
	
	
	public void createUpdateSql() throws Exception{
		final StringBuffer sql_SetPairs = new StringBuffer();

		if(getTableName()==null || getKeyField()==null)
			throw new Exception("Although Update query is set to be build automatically, the table name or key field is not set.");
		
		/*
		 * 2012-10-25 jinwon
		 * syncToDatabase() 후에 DAO의 NonSavable 필드에 값을 set 할 경우 다시 
		 * update 이 호출되고 NonSavable 필드까지 포함되어 에러가 발생해 이에 대해 처리함
		 */
		WebObjectType webObjectType = MetaworksRemoteService.getInstance().getMetaworksType(daoClass.getName());
		for(FieldDescriptor fd : webObjectType.metaworks2Type().getFieldDescriptors()){
			if(!fd.isSavable()){
				if(modifiedFieldMap.containsKey(fd.name.toUpperCase()))
					modifiedFieldMap.remove(fd.name.toUpperCase());
			}
		}
		
		if(rowSet==null){
			final DAOUtil daoUtil = new DAOUtil();
			
			ForLoop loopForCacheKeys = new ForLoop(){
				String sep = "";

				public void logic(Object target) {
					String propertyName = (String)target;
					
					if(modifiedFieldMap!=null && !modifiedFieldMap.containsKey(propertyName)) return;

					//ignores metaworks signals
					if("METAWORKSCONTEXT".equals(propertyName)) return;

					propertyName = daoUtil.replaceReservedKeyword(propertyName);

					sql_SetPairs.append(sep + propertyName + "=?" + propertyName);
					
					sep =", ";
				}
				
			};
			
			loopForCacheKeys.run(cache.keySet());
			sqlStmt = "update " + getTableName() + " set "+ sql_SetPairs +" where " + getKeyField() + "=?" + getKeyField();

		}else{				
			String sep = "";
			ResultSetMetaData rsMetaData = rowSet.getMetaData();
			for(int i=1; i<=rsMetaData.getColumnCount(); i++){
				String propertyName = rsMetaData.getColumnName(i);				
				
				if(propertyName.equalsIgnoreCase(getKeyField()) || modifiedFieldMap!=null && !modifiedFieldMap.containsKey(propertyName)) continue;
				
				sql_SetPairs.append(sep + propertyName + "=?" + propertyName);				
				sep =", ";
			}

			sqlStmt = "update " + getTableName() + " set "+ sql_SetPairs +" where " + getKeyField() + "=?" + getKeyField();

			if(sql_SetPairs.length() == 0)
				sqlStmt = null;
			
		   	adjustMetaDataIfFetched();
		}
	}
	
	public void createSelectSql() throws Exception{
		
//		ArrayList<String> joinTableList = new ArrayList<String>();
		StringBuffer joinTableSQL = new StringBuffer();
//		ArrayList<String> whereClauses = new ArrayList<String>();
		final StringBuffer whereClauseSQL = new StringBuffer();
		

		
		//Automated join query generation: DAO interface should have keyField setting
		WebObjectType webObjectType = MetaworksRemoteService.getInstance().getMetaworksType(daoClass.getName());
		for(FieldDescriptor fd : webObjectType.metaworks2Type().getFieldDescriptors()){
			if(fd.isLoadable() && !Database.dbPrimitiveTypes.containsKey(fd.getClassType()) && fd.getAttribute("ormapping")==null){
				WebObjectType referenceFieldWOT = MetaworksRemoteService.getInstance().getMetaworksType(fd.getClassType().getName());
				
				if(referenceFieldWOT.getKeyFieldDescriptor()==null || referenceFieldWOT.metaworks2Type().getKeyFieldDescriptor()==null) 
					continue; //means it's impossible to create relation
				
				String referenceTableName = referenceFieldWOT.metaworks2Type().getName();
//				joinTableList.add(referenceTableName);
				joinTableSQL.append(",").append(referenceTableName);
//				whereClauses.add(getTableName()+"."+fd.getName() + "=" + referenceTableName +"." + referenceFieldWOT.metaworks2Type().getKeyFieldDescriptor().getName());
				whereClauseSQL.append(" and ").append(getTableName()).append(".").append(fd.getName()).append("=").append(referenceTableName).append(".").append(referenceFieldWOT.metaworks2Type().getKeyFieldDescriptor().getName());
			}
		}
		
		final String separator = " where ";
		
		if(rowSet==null){
			
			ForLoop loopForCacheKeys = new ForLoop(){
				String sep = separator;// + " and ";

				public void logic(Object target) {
					String propertyName = (String)target;
					
					if(modifiedFieldMap!=null && !modifiedFieldMap.containsKey(propertyName)) return;

					//ignores metaworks signals
					if("METAWORKSCONTEXT".equals(propertyName)) return;


					whereClauseSQL.append(sep + propertyName + "=?" + propertyName);
					
					sep =" and ";
				}
				
			};
			
			loopForCacheKeys.run(cache.keySet());

		}else{				
			String sep = separator;// + " and ";
			ResultSetMetaData rsMetaData = rowSet.getMetaData();
			for(int i=1; i<=rsMetaData.getColumnCount(); i++){
				String propertyName = rsMetaData.getColumnName(i);				
				
				if(propertyName.equalsIgnoreCase(getKeyField()) || modifiedFieldMap!=null && !modifiedFieldMap.containsKey(propertyName)) continue;
				
				whereClauseSQL.append(sep + propertyName + "=?" + propertyName);				
				sep =" and ";
			}

		   	adjustMetaDataIfFetched();
		}
		
		if(whereClauseSQL.length()==0 && getKeyField()!=null) 
			whereClauseSQL.append("where " + getKeyField() + "=?" + getKeyField());

		
		sqlStmt = "select * from " + getTableName() + (joinTableSQL) + whereClauseSQL;
	}
	
	public int update() throws Exception {
		if(sqlStmt == null && isAutoSQLGeneration()){
			createUpdateSql();
		}
		
		if(sqlStmt == null || updateOnlyWhenDirty() && !isDirty) return 0;
			
//		if ( tc != null ) tc.setHasDML(true);
//		if ( getConnectionFactory() == null ) 
//			setConnectionFactory(new ConnectionFactory());
		
		//rowSet = null;

		//TODO: need to be cached
		Connection con = getConnectionFactory().getConnection();
		PreparedStatement pstmt = null;
		
		try{
//			System.out.println("getStatement() : " + getStatement());
			
			pstmt = con.prepareStatement(getActualSqlStmt(getStatement()));
			
			lateBindProperties(getStatement(), pstmt);
			int rowAffected = pstmt.executeUpdate();
			
			return rowAffected;
		}catch(Exception e){
			throw new Exception(e.getMessage() + " when to try sql [" + getStatement() + "] :", e);
		}finally{
			isDirty = false;
			
			try{pstmt.close();}catch(Exception e){}
			if ( !isConnective ) {
				checkOkToCloseConnection();
				try{con.close();}catch(Exception e){}
			}
		}		
	}	
	
	
	public int call() throws Exception {
		CallableStatement cstmt = null;		
		Connection con = getConnectionFactory().getConnection();
		try{
//			rowSet = null;
			releaseResource();
		
			cstmt = con.prepareCall(getActualSqlStmt(getStatement()));
		
			lateBindProperties(getStatement(), cstmt);
			int rowAffected = cstmt.executeUpdate();
		
			for(Iterator iter = outFieldMap.keySet().iterator(); iter.hasNext();){
				String fieldName = (String)iter.next();
				int order = ((Integer)outFieldMap.get(fieldName)).intValue();
				Object value = cstmt.getObject(order);
//				Array array = cstmt.getArray("");
				cache.put(fieldName.toUpperCase(), value);
			}
		
			return rowAffected;			
		}catch(Exception e){
			throw e;
		}finally{
			try{cstmt.close();}catch(Exception e){}
			if ( !isConnective ) {
				checkOkToCloseConnection();
				try{con.close();}catch(Exception e){}
			}
		}		
	}
	
//	public boolean absolute(int row) throws Exception {
//		if(rowSet==null)
//			throw new Exception("This DAO is not selected yet.");
//
//		return rowSet.absolute(row);
//	}

//	public int getRow() throws Exception {
//		if(rowSet==null)
//			throw new Exception("This DAO is not selected yet.");
//
//		return rowSet.getRow();
//	}

	int cursor = 0;
    protected List<HashMap> cachedRows;
		public List<HashMap> getCachedRows() {
			return cachedRows;
		}
		 
	public void moveToInsertRow() throws Exception{
		moveToInsertRow(null);
	}
	
	public void moveToInsertRow(IDAO cache) throws Exception{
		//TODO: this should care all the cases of scenario, user may reuse rowSet to insert something after selecting. then, we have to put changed values to the rowSet instead of cache? how about now?
		if(rowSet==null && cachedRows==null)
			cachedRows = new ArrayList<HashMap>();
		
		cachedRows.add(cache!=null ? cache.getImplementationObject().cache : new HashMap()); //make new space for cache row.
		last();
	}

	
	public void copyFrom(IDAO fromObj) throws Exception{
		
		
		if(rowSet!=null)
			throw new RuntimeException("Selected DAO's properties cannott be changed. ");
		
		/*
		 * rowset 을 복사하지 않아서 ormapping 된 값을 제대로 못들고 오는 현상 발생하여 수정함 
		 */
		ResultSetMetaData rsMetaData = fromObj.getImplementationObject().rowSet.getMetaData();
		for(int i=1; i<=rsMetaData.getColumnCount(); i++){
			String propertyName = rsMetaData.getColumnName(i);
			
			cachedRows.get(cursor).put(propertyName.toUpperCase(), fromObj.getImplementationObject().rowSet.getObject(i));
		}
		
		/*
		WebObjectType wot = MetaworksRemoteService.getInstance().getMetaworksType(daoClass.getName());
				
		ObjectInstance fromObjInst = (ObjectInstance) wot.metaworks2Type().createInstance();
		fromObjInst.setObject(fromObj);

		for(int i=0; i<wot.metaworks2Type().getFieldDescriptors().length; i++){
			FieldDescriptor fd = wot.metaworks2Type().getFieldDescriptors()[i];
			
			Object fieldValue = null;
			
			fieldValue = fromObjInst.getFieldValue(fd.getName());
			
			cachedRows.get(cursor).put(fd.getName().toUpperCase(), fieldValue);
		}*/
	}

	
//	public void copyFrom(IDAO fromObj) throws Exception{
//		
//		if(rowSet!=null)
//			throw new RuntimeException("Selected DAO's properties cannott be changed. ");
//		
//		WebObjectType wot = MetaworksRemoteService.getInstance().getMetaworksType(daoClass.getName());
//				
//		ObjectInstance fromObjInst = (ObjectInstance) wot.metaworks2Type().createInstance();
//		fromObjInst.setObject(fromObj);
//
//		for(int i=0; i<wot.metaworks2Type().getFieldDescriptors().length; i++){
//			FieldDescriptor fd = wot.metaworks2Type().getFieldDescriptors()[i];
//			
//			Object fieldValue = null;
//			if(fromObj instanceof AbstractGenericDAO){
//				try {
//					Method m = daoClass.getMethod("get" + fd.getName());
//				
//					fieldValue = invoke(fromObj, m, new Object[]{});
//				} catch (Throwable e) {
//					// TODO Auto-generated catch block
//					//throw new RuntimeException(e);
//				}
//			}else{
//				fieldValue = fromObjInst.getFieldValue(fd.getName());
//			}
//			
//			cachedRows.get(cursor).put(fd.getName().toUpperCase(), fieldValue);
//		}
//	}
//	


	public void beforeFirst() throws Exception {
		if(rowSet==null && cachedRows==null)
			throw new Exception("This DAO is not selected yet or you never added row by calling moveToInsertRow()");
		
		if(rowSet!=null)
			rowSet.beforeFirst();
		else if(cachedRows!=null)
			cursor = -1;
	}

	public boolean previous() throws Exception {
		if(rowSet==null && cachedRows==null)
			throw new Exception("This DAO is not selected yet or you never added row by calling moveToInsertRow()");

		if(rowSet!=null)
			return rowSet.previous();
		else{
			cursor--;
			if(cursor < 0){
				cursor = 0;
				return false;
			}else
				return true;
		}
			
	}    

    public boolean next() throws Exception {
		if(rowSet==null && cachedRows==null)
			throw new Exception("This DAO is not selected yet or you never added row by calling moveToInsertRow()");

    	if(rowSet!=null)
    		return rowSet.next();
    	else{
    		cursor ++;
    		if(cursor >= cachedRows.size()){
    			cursor = cachedRows.size()-1;
    			
    			return false;
    		}else{
    			return true;
    		}
    	}
    		
    }

	public void afterLast() throws Exception {
		if(rowSet==null && cachedRows==null)
			throw new Exception("This DAO is not selected yet or you never added row by calling moveToInsertRow()");

    	if(rowSet!=null)
    		rowSet.afterLast();
    	else{
    		moveToInsertRow();
    	}
	}
    
	public boolean last() throws Exception {
		if(rowSet==null && cachedRows==null)
			throw new Exception("This DAO is not selected yet or you never added row by calling moveToInsertRow()");

    	if(rowSet!=null)
    		return rowSet.last();
    	else{
    		cursor = cachedRows.size() - 1;
    		return true;
    	}
    		
	}
        
    
    public boolean first() throws Exception {
		if(rowSet==null && cachedRows==null)
			throw new Exception("This DAO is not selected yet or you never added row by calling moveToInsertRow()");

		if(rowSet!=null)
			return rowSet.first();
		else{
			cursor = 0;
			
			return true;
		}
    }
    
    public boolean absolute(int pos) throws Exception {
		if(rowSet==null && cachedRows==null)
			throw new Exception("This DAO is not selected yet or you never added row by calling moveToInsertRow()");

		if(rowSet!=null)
			return rowSet.absolute(pos);
		else{
			if(pos > -1 && pos < cachedRows.size()){
				cursor = pos;
				return true;
			}else{
				return false;
			}
		}
    }    

    public int size() {
		if(rowSet==null && cachedRows==null)
			throw new RuntimeException("This DAO is not selected yet or you never added row by calling moveToInsertRow()");

		if(rowSet!=null)
			return rowSet.size();
		else
			return cachedRows.size();
    }
    
	public String getString(String key) throws Exception {
		if(rowSet==null)
			throw new Exception("This DAO is not selected yet.");

		return rowSet.getString(key);
	}
	public Integer getInt(String key) throws Exception {
		if(rowSet==null)
			throw new Exception("This DAO is not selected yet.");
		return Integer.valueOf(rowSet.getInt(key));
	}
	public Long getLong(String key) throws Exception {
		if(rowSet==null)
			throw new Exception("This DAO is not selected yet.");

		return Long.valueOf(rowSet.getLong(key));		
	}
	public Boolean getBoolean(String key) throws Exception {
		if(rowSet==null)
			throw new Exception("This DAO is not selected yet.");

		return Boolean.valueOf(rowSet.getBoolean(key));			
	}
	
	public Date getDate(String key) throws Exception {
		if(rowSet==null)
			throw new Exception("This DAO is not selected yet.");

		return rowSet.getDate(key);				 
	}

	public Timestamp getTimestamp(String key) throws Exception {
		if(rowSet==null)
			throw new Exception("This DAO is not selected yet.");

		return rowSet.getTimestamp(key);				 
	}

	protected void lateBindProperties(String statement, PreparedStatement pStmt) throws Exception{
		getActualSqlStmt(statement, pStmt);    	
	}
	
	protected String getActualSqlStmt(String statement) throws Exception{
		return getActualSqlStmt(statement, null);
	}

    protected String getActualSqlStmt(String statement, PreparedStatement pstmt) throws Exception{
    	
    	if(statement==null) return null;
    	
     	StringBuffer realSql=new StringBuffer();
    	
		String[] splits = statement.split("\\?");
						
		if(splits.length>0) 
			realSql.append(splits[0]);
			
		outFieldMap.clear();

		if(splits.length>1){
			for(int i=1; i<splits.length; i++){
				String token = splits[i];
		
				String splits2[] = token.split("[ ,);]");        		
				String propertyNamePart = splits2[0];
				String propertyName = propertyNamePart.trim();
				boolean isOut = false;
				
				if(propertyNamePart.startsWith("*")){
					isOut = true;
					propertyName = propertyNamePart.substring(1);
					
					outFieldMap.put(propertyName.toUpperCase(), Integer.valueOf(i));
				}
						
				token = token.replaceFirst((isOut ? "\\*" : "") + propertyName, "\\?");			
				realSql.append(token);
				
///System.out.println("\n\n ===================== real sql =========================\n  " + realSql);
				
				if (pstmt != null) {
					propertyName = propertyName.toUpperCase();
					
					Object value;
					if (rowSet != null)
						value = rowSet.getObject(propertyName);
					else
						value = cache.get(propertyName);
					
					Class type = (Class) propertyTypes.get(propertyName);
					
//					if ( tc != null && tc.getThreadId() != null && tc.isHasDML() ) {
//						apprLogger.warn("sql update value : " + value);
//					}					
					
					boolean failToFindInMethods = false;
					
					if (type == null)
						failToFindInMethods = true;
					if (type == null && value != null)
						type = value.getClass();
					
					if (isOut) {
						CallableStatement cstmt = (CallableStatement) pstmt;
						if (type == String.class) {
							cstmt.registerOutParameter(i, Types.VARCHAR);
						} else if (Number.class.isAssignableFrom(type)) {
							cstmt.registerOutParameter(i, Types.BIGINT);
						} else if (type == java.util.Date.class) {
							cstmt.registerOutParameter(i, Types.DATE);
						}
					} else {
						try{												
							if (type == String.class) {
								pstmt.setString(i, (String) value);
							} else if (type != null && Number.class.isAssignableFrom(type)) {
								if (value != null) {
									Number numberValue;
									if (value instanceof Boolean) {
										numberValue = Long.valueOf(((Boolean)value).booleanValue() ? 1 : 0);
									} else {
										if (value instanceof String)
										try {
											value = Long.valueOf((String)value);
										} catch (Exception e) {
										}
										
										numberValue = (Number)value;
									}
							
									pstmt.setLong(i, numberValue.longValue());
								} else
									pstmt.setNull(i, Types.BIGINT);
								
							} else if (type != null && java.util.Date.class.isAssignableFrom(type)) {
								if (value != null) {
									java.util.Date dateValue = (java.util.Date)value;
						
									long timeInMS = dateValue.getTime(); 
									value = new Timestamp(timeInMS);							 
								}
							
								pstmt.setTimestamp(i, (Timestamp)value);													
							} else if ( type != null && (Boolean.class.isAssignableFrom(type) || boolean.class == type)) {
								Boolean booleanValue = Boolean.valueOf(false);
								if ( value != null) {
									if(value instanceof Boolean)
										booleanValue = (Boolean)value;
									else if(value instanceof Number)
										booleanValue = Boolean.valueOf(((Number)value).intValue() == 1);
								}
								// 0 : false, 1 : true  [default : 0]
								pstmt.setInt(i, (booleanValue.booleanValue())?1:0);
							} else {		
								
/*								if(value instanceof java.util.Date && DAOFactory.getInstance().getDBMSProductName().equals("`")){
									pstmt.setTimestamp(i, new Timestamp(((java.util.Date)value).getTime()));
								}
*/								
								pstmt.setObject(i, value);
							}
							
						}catch(java.sql.SQLException sqlException){
							String additionalInfo = "";
							if(failToFindInMethods && value == null)
								additionalInfo = "Make sure that the property [" + propertyName + "] is declared as a setter/getter.";
																
							throw new Exception("GenericDAO ["+ daoClass.getName() +"] failed to bind value [" + value + "] to field [" + propertyName + "]. " + additionalInfo, sqlException);
						}
					}
				}
			}
		}

		if(MetaworksRemoteService.getInstance().isLowerCaseSQL()){
			return realSql.toString().toLowerCase();
		}
		
		return realSql.toString();
    }
   
    public ArrayList<IDAO> toArrayList() throws Exception{
    	
    	ArrayList<IDAO> arrayList = new ArrayList<IDAO>();
    	
    	
    	beforeFirst();
    	while(next()){
    		IDAO copy = MetaworksDAO.createDAOImpl(getDaoClass());
    		copy.moveToInsertRow();
    		//copy.getImplementationObject().moveToInsertRow(this);
    		copy.getImplementationObject().copyFrom(this);
    		arrayList.add(copy);
    		//copy.beforeFirst();
    	}
    	
    	return arrayList;
    }
    
	public Object invoke(Object proxy, Method m, Object[] args)	throws Throwable{
		
		boolean securityCheck = false;
//		if(TransactionContext.getThreadLocalInstance()!=null){
//			securityCheck = TransactionContext.getThreadLocalInstance().isNeedSecurityCheck();
//			TransactionContext.getThreadLocalInstance().setNeedSecurityCheck(false);
//		}
		
		try{
			String methodName = m.getName();
			
			if(m.getName().equals("getImplementationObject")){
				return getImplementationObject();
			}
			
			boolean startWithGet = m.getName().startsWith("get");
			boolean startsWithIs = m.getName().startsWith("is");
			
			//if getter
			if(startWithGet || startsWithIs){
				
				String propertyNameOrg = methodName.substring(startWithGet ? 3 : 2);
				String propertyName = propertyNameOrg.toUpperCase();
				//propertyName = propertyName;
	
				if(propertyName.length()==0 && args.length==1){//from untyped DAO's getter
					return get((String)args[0]);
				}else{//from typed DAO's getter
					
					if("METAWORKSCONTEXT".equalsIgnoreCase(propertyName))
						return getMetaworksContext();
					
					Object returnValue = null;
					
					
					// It is the chance to convert primitive value to desired object //
					if(!Database.dbPrimitiveTypes.containsKey(m.getReturnType())){
						WebObjectType type = MetaworksRemoteService.getInstance().getMetaworksType(m.getReturnType().getName());
						ObjectType objectType = (ObjectType) type.metaworks2Type();
						ObjectInstance objInst = (ObjectInstance) objectType.createInstance();
						
						if(m.getReturnType().isInterface() && IDAO.class.isAssignableFrom(m.getReturnType())){
							objInst.setObject(MetaworksDAO.createDAOImpl(m.getReturnType()));
						}
						
						boolean atLeastOnceHaveValue = false;
						
						ORMapping ormapping = m.getAnnotation(ORMapping.class);
						if(ormapping!=null){
							
							if(ormapping.availableWhen().length > 0 && !ormapping.availableWhen()[0].equals("")){
								boolean ormappingIsAvailable = false;
								for( int i = 0; i < ormapping.availableWhen().length; i++){
									try{
										String[] propAndValue = ormapping.availableWhen()[i].split("==");
										String availabilityCheckPropName = propAndValue[0].trim().toUpperCase();
										String availabilityCheckValue = propAndValue[1].trim();
	
										Object value = (rowSet!=null ? rowSet.getObject(availabilityCheckPropName) : cache.get(availabilityCheckPropName));
	
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
									return null;
							}
							
							String[] ORMPropNames = ormapping.objectFields();
							for(int i=0; i<ORMPropNames.length; i++){
								
								try{
									
									String propName = ormapping.databaseFields()[i].toUpperCase();

									String mappingPropName = ORMPropNames[i];
									mappingPropName = WebObjectType.toUpperStartedPropertyName(mappingPropName);

									Object value = (rowSet!=null ? rowSet.getObject(propName) : cache.get(propName));
									
									Object converted = desireToConvert(objectType.getFieldDescriptor(mappingPropName).getClassType(), value);
									if(!(converted instanceof NoReturn))
										value = converted;
									
									if(i==0 && value==null && ormapping.objectIsNullWhenFirstDBFieldIsNull()){
										return null;
									}
									
									
									objInst.setFieldValue(mappingPropName, value);
									atLeastOnceHaveValue = true;
									
								}catch(ArrayIndexOutOfBoundsException aob){
									throw new Exception("You have to call 'next()' before getting value of DAO", aob);
								}catch(Exception ex){
								}
							}
							
							if(atLeastOnceHaveValue){
								Object object = objInst.getObject();

								if(object instanceof ContextAware){
									((ContextAware)object).setMetaworksContext(getMetaworksContext());
								}

								if(object instanceof ORMappingListener){
									((ORMappingListener)object).onRelation2Object();
								}
								

								return object;
							}
						}
						
//						//TODO: deprecated due to poor performance
//						for(int i=0; i<objectType.getFieldDescriptors().length; i++){
//							FieldDescriptor fd = objectType.getFieldDescriptors()[i];
//														
//							String actualPropertyName = propertyName + "___" + fd.getName();
//
//							try{
//								String propName = actualPropertyName.toUpperCase();
//								
//								//if cache doesn't contains the property name, it should be ignored since cache.get tries to return null value instead of throwing exception.
//								if(rowSet==null && !cache.containsKey(propName)) 
//									continue;
//								
//								Object
//									propValue = (rowSet!=null ? rowSet.getObject(propName) : cache.get(propName));
//								
//								objInst.setFieldValue(fd.getName(), propValue);
//								atLeastOnceHaveValue = true;
//							}catch(Exception ex){
//							}
//						}
						
						if(atLeastOnceHaveValue){
							Object object = objInst.getObject();
							
							if(object instanceof ContextAware){
								((ContextAware)object).setMetaworksContext(getMetaworksContext());
							}

							if(object instanceof ORMappingListener){
								((ORMappingListener)object).onRelation2Object();
							}

							
							return object;
						}
					}

					if(rowSet!=null) {
						
						
	//					try {
	//					if(rowSet.isClosed()) { 
	//						throw new UEngineException("This DAO has been already closed. If you use TransactionContext, Use DAO during TransactionContext is alive.");
	//					}
	//					}catch (Exception ex) {
	//						ex.printStackTrace();
	//					}
						
						if ( Boolean.class.isAssignableFrom(m.getReturnType()) ) {
							returnValue = Boolean.valueOf( (rowSet.getInt(propertyName)==1)?true:false );
						} else {
							if (m.getName().equals("getInt") ||  
									m.getName().equals("getLong") || 
									m.getName().equals("getBoolean") || 
									m.getName().equals("getDate") || 
									m.getName().equals("getString")) {
								try{
									return m.invoke(this, args);		
								}catch(Exception e){
									throw e.getCause();
								}
							} 
							
							try{
								if(MetaworksRemoteService.getInstance().getMetaworksType(daoClass.getName()).metaworks2Type().getFieldDescriptor(propertyNameOrg).isLoadable())
									returnValue = rowSet.getObject(propertyName);
								else
									returnValue = null;
								
							}catch(Exception e){
								if(MetaworksRemoteService.getInstance().isDebugMode())
									throw new Exception("failed to get property value '" + daoClass.getName() + "." + propertyName + "'", e);
							}
						}
					} else {
						if (args!=null && args.length==1 && args[0] instanceof String && 
								(m.getName().equals("getInt") ||  
								m.getName().equals("getLong") || 
								m.getName().equals("getBoolean") || 
								m.getName().equals("getDate") || 
								m.getName().equals("getString"))
							)
						{
							propertyName = ((String) args[0]).toUpperCase();
						}
						
						returnValue = cache.get(propertyName);
					}

					// processing default value
					if(null == returnValue){
						
						WebFieldDescriptor fd = MetaworksRemoteService.getInstance().getMetaworksType(daoClass.getName()).getFieldDescriptor(propertyName.toLowerCase());
						
						if(fd!=null)
							returnValue = fd.getDefaultValue();
					}

					Object converted = desireToConvert(m.getReturnType(), returnValue);
					
					if(!(converted instanceof NoReturn))
						return converted;
					
					if(returnValue!=null && !m.getReturnType().isAssignableFrom(returnValue.getClass())){
						throw new Exception(daoClass.getName() + " DAO's field type of '"+propertyName+"' is mismatch with the actual table's field.");
					}
					
					return returnValue;
				}
			}else
			//if setter 
			if(m.getName().startsWith("set")){
				String propertyName = methodName.substring(3);
				
				isDirty = true;
				
				
	//			if("writer".equalsIgnoreCase(propertyName)){
	//				System.out.println();
	//			}
				
				// ORMapping option: Object should be mapped into relation fields //
				if(args.length==1 && !Database.dbPrimitiveTypes.containsKey(m.getParameterTypes()[0])){
					

					if(args[0] instanceof ORMappingListener){
						((ORMappingListener)args[0]).onObject2Relation();
					}
					
					ORMapping ormapping = m.getAnnotation(ORMapping.class);
					
					if(ormapping==null){
						try{
							Method getter = getDaoClass().getMethod("get" + propertyName, new Class[]{});
						
							if(getter!=null)
								ormapping = getter.getAnnotation(ORMapping.class);
						}catch(Exception ex){
						}
					}
						
					if(args[0]==null) 
						return null; //means ignore when the object
	
					if(ormapping!=null){
						/*
						 * prevent overwrite value from ormapping field
						 */
						if(ormapping.availableWhen().length > 0 && !ormapping.availableWhen()[0].equals("")){
							boolean ormappingIsAvailable = false;
							for( int i = 0; i < ormapping.availableWhen().length; i++){
								try{
									String[] propAndValue = ormapping.availableWhen()[i].split("==");
									String availabilityCheckPropName = propAndValue[0].trim().toUpperCase();
									String availabilityCheckValue = propAndValue[1].trim();

									Object value = (rowSet!=null ? rowSet.getObject(availabilityCheckPropName) : cache.get(availabilityCheckPropName));

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
								return null;
						}
						
						WebObjectType type = MetaworksRemoteService.getInstance().getMetaworksType(m.getParameterTypes()[0].getName());
						ObjectType objectType = (ObjectType) type.metaworks2Type();
						ObjectInstance objInst = (ObjectInstance) objectType.createInstance();
						
						objInst.setObject(args[0]);
						
						boolean atLeastOnceHaveValue = false;
						
						String[] ORMPropNames = ormapping.objectFields();
						for(int i=0; i<ORMPropNames.length; i++){
							
							String mappingPropName = ORMPropNames[i];
							mappingPropName = WebObjectType.toUpperStartedPropertyName(mappingPropName);
	
							Object value = objInst.getFieldValue(mappingPropName);
	
							String databaseFieldName = ormapping.databaseFields()[i];
							
							//mapping type check
							if(value!=null){
								Class propertyType = (Class) getPropertyType(databaseFieldName.toUpperCase());
								if(propertyType!=null)
									if(!propertyType.isAssignableFrom(value.getClass())){
										throw new Exception("[ORMapping error] the field '" + databaseFieldName + "' cannot be mapped with object field : '" + type.getName() + "." + mappingPropName + "'.");
									}
							}
							
							set(databaseFieldName, value);
							modifiedFieldMap.put(ormapping.databaseFields()[i].toUpperCase(), propertyName);
	
						}
	
						return null;
					}
				}
				
				
				Object value;
				
				if(propertyName.length()==0 && args.length==2){
					propertyName = (String) args[0];
					value = args[1];
					
				}else{//TODO: type check for typed DAO 	
					value = args[0];
				}
				
				set(propertyName, value);
	
				//Synchronize the original value to be set by databaseMe(). that means when you set some value to databaseMe().setXXX then your original value would be changed.
				if(getSynchronizedObject()!=null){
					WebObjectType wot = MetaworksRemoteService.getInstance().getMetaworksType(getSynchronizedObject().getClass().getName());
					ObjectInstance instance = (ObjectInstance) wot.metaworks2Type().createInstance();
					instance.setObject(getSynchronizedObject());
					instance.setFieldValue(propertyName, value);
				}
	
				return null;
	
			}else{
				try{
					return m.invoke(this, args);
					
				}catch(Exception e){
					throw e.getCause();
				}
			}
		}finally{
//			if(TransactionContext.getThreadLocalInstance()!=null)
//				TransactionContext.getThreadLocalInstance().setNeedSecurityCheck(securityCheck);
//
		}
	}
	
	private Object desireToConvert(Class desiredType, Object returnValue) {
		// try to convert an integer value to proper mapping values for types
		if(returnValue instanceof Number){
			Number returnValueInNumber = (Number)returnValue;
			if(desiredType == Long.class || desiredType == long.class ){
				return Long.valueOf(returnValueInNumber.longValue());
			}
			if(desiredType == Integer.class || desiredType == int.class ){
				return Integer.valueOf(returnValueInNumber.intValue());
			}
			if(desiredType == Boolean.class || desiredType == boolean.class ){
				return Boolean.valueOf(returnValueInNumber.intValue() == 1);
			}
		}
		
		// try to null values into proper default primitive types' values
		if(returnValue == null){
			if(desiredType == boolean.class){
				return Boolean.valueOf(false);
			}					
			if(desiredType == int.class){
				return Integer.valueOf(0);
			}					
			if(desiredType == long.class){
				return Long.valueOf(0);
			}					
		}
		
		//	try to parse the string value into integer type.
		if(returnValue instanceof String){ 
			if(desiredType == int.class || Integer.class.isAssignableFrom(desiredType)){
				try{
					return new Integer((String)returnValue);
				}catch(Exception e){
				}
			}	
			
			if(desiredType == long.class || Long.class.isAssignableFrom(desiredType)){
				try{
					return new Long((String)returnValue);
				}catch(Exception e){
				}
			}
		}
		
		//primitive type mappings
		if(returnValue instanceof Boolean && desiredType == boolean.class){
			return (Boolean)returnValue;
		}
		
		if(returnValue instanceof Number && (desiredType == int.class || desiredType == long.class)){
			return returnValue;
		}
		//end
		
		return new NoReturn();
	}
	
	
//	public static IDAO createDAOImpl(String jndiName, String sqlStmt, Class daoClass) throws Exception{		
//		return (IDAO)Proxy.newProxyInstance(
//			daoClass.getClassLoader(),
//			new Class[]{daoClass},
//			new GenericDAO(jndiName, sqlStmt, daoClass)
//		);		
//	}
	
	public AbstractGenericDAO getImplementationObject(){
		return this;
	}

//	/**
//	 * @deprecated
//	 */
//	public static IDAO createDAOImpl(Connection conn, String sqlStmt, Class daoClass) throws Exception{		
//		return (IDAO)Proxy.newProxyInstance(
//			daoClass.getClassLoader(),
//			new Class[]{daoClass},
//			new GenericDAO(conn, sqlStmt, daoClass)
//		);		
//	}
	
//	public static IDAO createDAOImpl(String jndiName, String selectSqlStmt, String insertSqlStmt, String updateSqlStmt, Class daoClass) throws Exception{		
//		return (IDAO)Proxy.newProxyInstance(
//			daoClass.getClassLoader(),
//			new Class[]{daoClass},
//			new GenericDAO(jndiName, selectSqlStmt, insertSqlStmt, updateSqlStmt, daoClass)
//		);		
//	}
//
//	public static IDAO createDAOImpl(ConnectionFactory conn, String selectSqlStmt, String insertSqlStmt, String updateSqlStmt, Class daoClass) throws Exception{		
//		return (IDAO)Proxy.newProxyInstance(
//			daoClass.getClassLoader(),
//			new Class[]{daoClass},
//			new GenericDAO(conn, selectSqlStmt, insertSqlStmt, updateSqlStmt, daoClass)
//		);		
//	}

//	public static IDAO createDAOImpl(ConnectionFactory conn, String sqlStmt, Class daoClass) throws Exception{		
//		return (IDAO)Proxy.newProxyInstance(
//			daoClass.getClassLoader(),
//			new Class[]{daoClass},
//			new GenericDAO(conn, sqlStmt, daoClass)
//		);		
//	}
//
//	public static IDAO createDAOImpl(ConnectionFactory conn, String sqlStmt) throws Exception{		
//		return (IDAO)Proxy.newProxyInstance(
//			IDAO.class.getClassLoader(),
//			new Class[]{IDAO.class},
//			new GenericDAO(conn, sqlStmt, IDAO.class)
//		);
//	}
//
//	public static IDAO createDAOImpl(String sqlStmt, Class daoClass) throws Exception{		
//		return (IDAO)Proxy.newProxyInstance(
//			IDAO.class.getClassLoader(),
//			new Class[]{daoClass},
//			new GenericDAO(new ConnectionFactory(), sqlStmt, daoClass)
//		);
//	}
//
//	public static IDAO createDAOImpl() throws Exception{		
//		return (IDAO)Proxy.newProxyInstance(
//			IDAO.class.getClassLoader(),
//			new Class[]{IDAO.class},
//			new GenericDAO(IDAO.class)
//		);
//	}
//
//	public static IDAO createDAOImpl(Class daoClass) throws Exception{		
//		return (IDAO)Proxy.newProxyInstance(
//			IDAO.class.getClassLoader(),
//			new Class[]{daoClass},
//			new GenericDAO(daoClass)
//		);
//	}
//
//	public static IDAO createDAOImpl(String sqlStmt) throws Exception{		
//		return (IDAO)Proxy.newProxyInstance(
//			IDAO.class.getClassLoader(),
//			new Class[]{IDAO.class},
//			new GenericDAO(new ConnectionFactory(), sqlStmt, IDAO.class)
//		);
//	}

	public Object get(String propertyName) throws Exception {
		try {
			propertyName = propertyName.toUpperCase();
			
			if("METAWORKSCONTEXT".equals(propertyName))
				return getMetaworksContext();
			else
			if(rowSet!=null)
				return rowSet.getObject(propertyName);
			else
				return cache.get(propertyName);
		} catch (Exception e) {
			throw new Exception("Failed to get property '" + propertyName + "' from SQL " + getSqlStmt(), e);
		}
	}

	public Object set(String propertyName, Object value) throws Exception {
		
		if("METAWORKSCONTEXT".equalsIgnoreCase(propertyName)){
			setMetaworksContext((MetaworksContext) value);
			
			return value;
		}		
		
		isDirty = true;
		
//		if("url".equalsIgnoreCase(propertyName)){
//			System.out.println();
//		}

		
		if(modifiedFieldMap==null) modifiedFieldMap = new HashMap()/*{

			@Override
			public Object put(Object key, Object value) {
				if("writer".equalsIgnoreCase(key.toString())){
					System.out.println();
				}
				return super.put(key, value);
			}
			
		}*/;
		
		String upperPropertyName = propertyName.toUpperCase();
		
		if(modifiedFieldMap.containsKey(upperPropertyName) && value==null){ //means already set by ORMapped key
			String theSetterFieldName = (String) modifiedFieldMap.get(upperPropertyName);
			if(!theSetterFieldName.equals(propertyName)) 
				return null;
		}
		
		modifiedFieldMap.put(upperPropertyName, propertyName);
		
		if(rowSet!=null){
			cache.clear();
			ResultSetMetaData rsMetaData = rowSet.getMetaData();
			for(int i=1; i<=rsMetaData.getColumnCount(); i++){
				String propName = rsMetaData.getColumnName(i);
				cache.put(propName.toUpperCase(), rowSet.getObject(i));
			}
			
			//rowSet = null;
			releaseResource();
		}
		
		


		return cache.put(upperPropertyName, value);
	}
	

	public String getStatement() {
		return sqlStmt;//.toUpperCase(); //

	}
	
	public void setStatement(String sql) {
//		System.out.println("set SqlStmt : " + sql);
		this.sqlStmt = sql;
	}

	public RowSet getRowSet(){
		return rowSet;
	}
	


	public String toString(){
		StringBuffer sb = new StringBuffer();
	    Iterator i = propertyTypes.keySet().iterator();
	    while (i.hasNext()) {
	    	String propertyName = (String) i.next();
	    	
	    	Object value = null;
	    	try{
	    		value = get(propertyName);
	    	}catch(Exception e){
	    	}
	    	
	    	if (sb.length() > 0) sb.append(", ");
	    	sb.append(propertyName).append("=").append(value);
		};
		
		return sb.toString();
	}
	
	public void releaseResource() throws Exception {
		if (rowSet != null) {
			rowSet.close();
			rowSet = null;
		}
	}
	
	
	transient MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			if(metaworksContext==null)
				metaworksContext = new MetaworksContext();

			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	
	class NoReturn{}
}
