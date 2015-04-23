package org.metaworks.dao;


import java.lang.reflect.Proxy;


/**
 * Generic DAO
 *
 * @author <a href="mailto:ghbpark@hanwha.co.kr">Sungsoo Park</a>, Jinyoung Jang
 * @version $Id: ConnectiveDAO.java,v 1.7 2008/03/31 12:30:59 kmooje Exp $
 */
public class MetaworksDAO extends AbstractGenericDAO {


	protected MetaworksDAO(final ConnectionFactory cf, boolean isConnective, String sqlStmt, Class daoClass, Object synchronizedObject)	throws Exception {
		super(cf, isConnective, sqlStmt, daoClass, synchronizedObject);
	}
	
	public static IDAO createDAOImpl(ConnectionFactory cf, String sqlStmt, Class daoClass) throws Exception{		

		return (IDAO)Proxy.newProxyInstance(
			daoClass.getClassLoader(),
			new Class[]{daoClass},
			new MetaworksDAO(cf, true, sqlStmt, daoClass, null)
		);		
	}	
	
	public static IDAO createDAOImpl(Class daoClass) throws Exception{		
		return createDAOImpl( null, null, daoClass, null);
	}

	public static IDAO createDAOImpl(TransactionContext tc, String sqlStmt, Class daoClass) throws Exception{		
		return createDAOImpl( tc, sqlStmt, daoClass, null);
	}

	public static IDAO createDAOImpl(TransactionContext tc, String sqlStmt, Class daoClass, Object synchronizedObject) throws Exception{		

		return (IDAO)Proxy.newProxyInstance(
			daoClass.getClassLoader(),
			new Class[]{daoClass},
			new MetaworksDAO(tc, true, sqlStmt, daoClass, synchronizedObject)
		);		
	}	

	
	
	
	public static void main(String[] args) throws Exception{

		
		IDAO dao = MetaworksDAO.createDAOImpl(IDAO.class);
		
		dao.moveToInsertRow();
		dao.set("test","test1");
		dao.moveToInsertRow();
		dao.set("test","test2");
		dao.moveToInsertRow();
		dao.set("test","test3");
		
		dao.beforeFirst();
		while(dao.next()){
			System.out.println(dao.get("test"));
		}
		
		
//		
//		rowSet.beforeFirst();
//		rowSet.
//		rowSet.setString("test", "test1");
//		rowSet.moveToInsertRow();
//		rowSet.setString("test", "test2");

	}
}	