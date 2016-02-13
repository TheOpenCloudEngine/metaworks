package org.metaworks.dao;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author  <a href="mailto:ghbpark@hanwha.co.kr">Sungsoo Park</a>
 * @version    $Id: TransactionContext.java,v 1.35 2009/12/18 04:34:53 curonide Exp $
 */
public class TransactionContext implements ConnectionFactory{
		
	ConnectionFactory delegatedConnectionFactory;
    static ThreadLocal<TransactionContext> local = new ThreadLocal<TransactionContext>();

	static Hashtable connectionPool = new Hashtable();

	

	String connectionGetterStackDump = null;
		public String getConnectionGetterStackDump() {
			return connectionGetterStackDump;
		}
		public void setConnectionGetterStackDump(String connectionGetterStackDump) {
			this.connectionGetterStackDump = connectionGetterStackDump;
		}
		
	Map autowiringObjectsFromClient;
		public Map getAutowiringObjectsFromClient() {
			return autowiringObjectsFromClient;
		}
		public void setAutowiringObjectsFromClient(Map autowiringObjectsFromClient) {
			this.autowiringObjectsFromClient = autowiringObjectsFromClient;
		}

	HttpServletRequest request;
		public HttpServletRequest getRequest() {
			return request;
		}
		public void setRequest(HttpServletRequest request) {
			this.request = request;
		}

	HttpServletResponse response;
		public HttpServletResponse getResponse() {
			return response;
		}
		public void setResponse(HttpServletResponse response) {
			this.response = response;
		}
		
	protected TransactionContext(ConnectionFactory delegatedConnectionFactory){
		this.delegatedConnectionFactory = delegatedConnectionFactory;
	}
	
	public TransactionContext(){
		
		if(getThreadLocalInstance()!=null)
			throw new RuntimeException("There's uncommitted transactionContext remains.");
		
		local.set(this);
	}
	
	public static TransactionContext getThreadLocalInstance(){
		TransactionContext tc = local.get();
		
//		if(tc==null){
//			tc = new TransactionContext();
//			local.set(tc);
//		}
		
		return tc;
	}
	

	List transactionListeners = new ArrayList();
		public void addTransactionListener(TransactionListener tl){
			transactionListeners.add(tl);
		}
		public List getTransactionListeners(){
			return transactionListeners;
		}
		
	List releaseResourceListeners = new ArrayList();
		public void addReleaseResourceListeners(ReleaseResourceListener rrl){
			releaseResourceListeners.add(rrl);
		}
		public List getReleaseResourceListeners(){
			return releaseResourceListeners;
		}

	boolean isManagedTransaction;
		public boolean isManagedTransaction() {
			return isManagedTransaction;
		}
		public void setManagedTransaction(boolean isManagedTransaction) {
			this.isManagedTransaction = isManagedTransaction;
		}
	
	boolean autoCloseConnection = true;
		public boolean isAutoCloseConnection() {
			return autoCloseConnection;
		}
		public void setAutoCloseConnection(boolean autoCloseConnection) {
			this.autoCloseConnection = autoCloseConnection;
		}	


	transient Map sharedContexts = new Hashtable();
		public Object getSharedContext(String contextKey) {
			if(!sharedContexts.containsKey(contextKey)) return null;
			
			return sharedContexts.get(contextKey);
		}
		public void setSharedContext(String contextKey, Object value) {
			if(value == null) 
				sharedContexts.remove(contextKey);
			else
				sharedContexts.put(contextKey, value);
		}	

	public IDAO createSynchronizedDAO(String tableName, String keyFieldName, Object keyFieldValue, Class daoType) throws Exception{
		return createSynchronizedDAO(tableName, keyFieldName, keyFieldValue, daoType, true, null);
	}
	public IDAO createSynchronizedDAO(String tableName, String keyFieldName, Object keyFieldValue, Class daoType, Object synchronizedObject) throws Exception{
		return createSynchronizedDAO(tableName, keyFieldName, keyFieldValue, daoType, true, synchronizedObject);
	}
	public IDAO findSynchronizedDAO(String tableName, String keyFieldName, Object keyFieldValue, Class daoType) throws Exception{
		return findSynchronizedDAO( tableName, keyFieldName, keyFieldValue, daoType, null);
	}
	public IDAO findSynchronizedDAO(String tableName, String keyFieldName, Object keyFieldValue, Class daoType, Object synchronizedObject) throws Exception{

		String sharedContextKey = tableName + "@" + keyFieldValue;
		sharedContextKey = sharedContextKey.toUpperCase();
		
		if(getSharedContext(sharedContextKey)!=null){
			IDAO cachedOne = (IDAO)getSharedContext(sharedContextKey);
			
			try{
				cachedOne.first();
			}catch(Exception e){}
			
			return cachedOne;
		}
		
		return createSynchronizedDAO(tableName, keyFieldName, keyFieldValue, daoType, false, synchronizedObject);
	}
	private IDAO createSynchronizedDAO(final String tableName, final String keyFieldName, final Object keyFieldValue, Class daoType, final boolean isNew, Object synchronizedObject) throws Exception{

		if(!isNew && keyFieldValue == null) 
				throw new Exception("keyFieldValue should have value for synchronized DAO for finding");
		
		final IDAO dao = MetaworksDAO.createDAOImpl(this, null, daoType, synchronizedObject);
		dao.getImplementationObject().setTableName(tableName);
		dao.getImplementationObject().setKeyField(keyFieldName);
		
		if(keyFieldName!=null && keyFieldValue!=null)
			dao.set(keyFieldName, keyFieldValue);

		if(!isNew){
			dao.getImplementationObject().createSelectSql();
			dao.select();
			if(!dao.next()) throw new NoSuchEntitySQLException("No Such "+tableName+" where "+keyFieldName+" is " + keyFieldValue);
		}
		
		String sharedContextKey = tableName + "@" + keyFieldValue;
		sharedContextKey = sharedContextKey.toUpperCase();
		
		setSharedContext(sharedContextKey, dao);
		
		TransactionListener flusher = new TransactionListener(){
			boolean newRow = isNew;

			public void beforeCommit(TransactionContext tx) throws Exception {
				if(newRow){
					
					try{
						dao.getImplementationObject().createInsertSql();
						newRow = false;
						dao.update();
					}catch(Exception integrityError){
						throw new Exception("Error when to insert " + dao.getImplementationObject().getTableName() + " where "+keyFieldName+" is " + keyFieldValue, integrityError);
					}
				}else{
					
					if(dao.getImplementationObject().isDirty()){ //only when the data has been updated it will be applied
						dao.getImplementationObject().createUpdateSql();
						dao.update();
					}
				}
				
			}

			public void beforeRollback(TransactionContext tx) throws Exception {
			}

			public void afterCommit(TransactionContext tx) throws Exception {
				// TODO Auto-generated method stub
				
			}

			public void afterRollback(TransactionContext tx) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		addTransactionListener(flusher);
		
		sharedContextKey = tableName + "@" + keyFieldValue + "@flusher";
		sharedContextKey = sharedContextKey.toUpperCase();
		setSharedContext(sharedContextKey, flusher);

		return dao;
	}
		
	public void flushSynchronizedDAO(String tableName, Object keyFieldValue) throws Exception{
		String sharedContextKey = tableName + "@" + keyFieldValue + "@flusher";
		sharedContextKey = sharedContextKey.toUpperCase();
		TransactionListener tl = (TransactionListener) getSharedContext(sharedContextKey);
		
		if(tl==null)
			throw new Exception("No synchronized DAO to flush where " + sharedContextKey);
		
		tl.beforeCommit(this);
		
	}
		
	protected void beforeCommit() throws Exception{
//		for(Iterator iter = transactionListeners.iterator(); iter.hasNext();){
//			TransactionListener tl = (TransactionListener)iter.next();
//			tl.beforeCommit(this);
//		}
		
		final TransactionContext tx = this;
		
		ForLoop beforeCommitLoop = new ForLoop(){

			public void logic(Object target) {
				
				TransactionListener tl = (TransactionListener)target;
				try {
					tl.beforeCommit(tx);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}				
			}			
		};
		
		beforeCommitLoop.run((ArrayList)transactionListeners);
	}

	protected void afterCommit() throws Exception{
//		for(Iterator iter = transactionListeners.iterator(); iter.hasNext();){
//			TransactionListener tl = (TransactionListener)iter.next();
//			tl.afterCommit(this);
//		}
		final TransactionContext tx = this;

		ForLoop afterCommitLoop = new ForLoop(){

			public void logic(Object target) {

				TransactionListener tl = (TransactionListener)target;
				try {
					tl.afterCommit(tx);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}				
			}			
		};
		
		afterCommitLoop.run((ArrayList)transactionListeners);
	}

	protected void afterRollback() throws Exception{
//		for(Iterator iter = transactionListeners.iterator(); iter.hasNext();){
//			TransactionListener tl = (TransactionListener)iter.next();
//			tl.afterRollback(this);
//		}
		
		final TransactionContext tx = this;

		ForLoop afterRollBackLoop = new ForLoop(){

			public void logic(Object target) {

				TransactionListener tl = (TransactionListener)target;
				try {
					tl.afterRollback(tx);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}				
			}			
		};
		
		afterRollBackLoop.run((ArrayList)transactionListeners);
	}
	
	protected void beforeRollback() throws Exception{
//		for(Iterator iter = transactionListeners.iterator(); iter.hasNext();){
//			TransactionListener tl = (TransactionListener)iter.next();
//			tl.beforeRollback(this);
//		}
		
		final TransactionContext tx = this;

		ForLoop beforeRollBackLoop = new ForLoop(){

			public void logic(Object target) {

				TransactionListener tl = (TransactionListener)target;
				try {
					tl.beforeRollback(tx);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}				
			}			
		};
		
		beforeRollBackLoop.run((ArrayList)transactionListeners);
		
	}
	
	public void commit() throws Exception {
		try {
			beforeCommit();
						
			if(connection != null && !connection.isClosed() && (getConnectionFactory()==null || !isManagedTransaction())){
				
					connection.commit();
		
			}
			
		} catch (Exception e) {
			throw e;
		} finally {
			releaseResources();
			afterCommit();
		}
	}
	
	public void rollback() throws Exception{
		try {
			beforeRollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(connection!=null && !connection.isClosed() && (getConnectionFactory()==null || !isManagedTransaction())){
					connection.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			releaseResources();
			afterRollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void releaseResources() throws Exception{
		local.set(null); //clear the thread local
		
		releaseResources(false);
	}
	

	protected void releaseResources(boolean fromRemoveCall) throws Exception{
		
		final TransactionContext tx = this; 

		ForLoop beforeRealeaseLoop = new ForLoop(){

			public void logic(Object target) {

				ReleaseResourceListener tl = (ReleaseResourceListener)target;
				try {
					tl.beforeReleaseResource(tx);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}				
			}			
		};
		
		beforeRealeaseLoop.run((ArrayList)releaseResourceListeners);
		
		
		if(connection != null && !connection.isClosed()){
			if(fromRemoveCall && !isManagedTransaction()){ 
				(new Exception("This thread tries to release resources that is not applied(committed) or cancelled(rolled-back).")).printStackTrace();
			}
 

			try{
				if(isAutoCloseConnection()){
					System.out.println("releaseResources : " + connection.hashCode());
					connection.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}

		}
		
		if(connection != null)
			System.out.println("null connection : " + connection.hashCode());
		
		connection = null;
	}
	


	Connection connection;
	public Connection getConnection() throws Exception {
		if(connection == null || connection.isClosed()){
			//System.out.println(this);
			//System.out.println("getConnection alloc : " + connection);
			if(connectionGetterStackDump==null)
				try {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					(new Exception("Connection leakage code has been detected. uEngine closes the connection:")).printStackTrace(pw);
					connectionGetterStackDump = sw.toString();
					
					sw.close();
					pw.close();
				} catch (IOException e) {
				}
				
			if(getConnectionFactory()==null) throw new Exception("ConnectionFactory has not been provided.");
			connection = getConnectionFactory().getConnection();
		}
		

		if(!isManagedTransaction()){
			connection.setAutoCommit(false);
		}
		
		//System.out.println("getConnection : " + connection.hashCode());
		return connection;
	}
	
	public ConnectionFactory getConnectionFactory(){
		return delegatedConnectionFactory;
	}	
	
	public void setConnectionFactory(ConnectionFactory cf) {
		this.delegatedConnectionFactory = cf;
	}
	
	protected void finalize() throws Throwable {
		if( connection != null && !connection.isClosed() ){			
			if(connectionGetterStackDump!=null){
				System.out.println(connectionGetterStackDump);
			}
		}
		
		super.finalize();
	}

}


