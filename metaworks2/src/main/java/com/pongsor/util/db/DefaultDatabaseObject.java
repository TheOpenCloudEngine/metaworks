package com.pongsor.util.db;

import java.sql.*;
import java.util.*;

public class DefaultDatabaseObject extends AbstractDatabaseObject{

	Connection conn = null;
	String connStr, userId, pwd;
	static Hashtable prepStatementsCache = new Hashtable();

	public DefaultDatabaseObject(String connStr, String userId, String pwd){
		super();
		
		this.connStr = connStr;
		this.userId = userId;
		this.pwd = pwd;
	}
	
	public void init(){};
	
	public synchronized Connection getConnection() throws Exception{		
		if(conn==null){		
			Thread.currentThread().getContextClassLoader().loadClass("oracle.jdbc.driver.OracleDriver");			
			conn = (DriverManager.getConnection(connStr, userId, pwd));			
		}		
		return conn;
	}	
	
	public synchronized ResultSet query(String sqlStmt, Object[] param) throws Exception{
		PreparedStatement prepStmt;
		if(prepStatementsCache.containsKey(sqlStmt)){
			prepStmt = (PreparedStatement)prepStatementsCache.get(sqlStmt);
		}
		else{
			prepStmt = getConnection().prepareStatement(sqlStmt);
			prepStatementsCache.put(sqlStmt, prepStmt);
		}
		
		if(param!=null)
		for(int i=0; i<param.length; i++) prepStmt.setObject(i+1, param[i]);

		ResultSet rs = prepStmt.executeQuery();
		
		//prepStmt.close();
		
		return rs;
	}
	
	public Object[] queryColumn(String sqlStmt, Object[] param) throws Exception{
	
		ResultSet rs = query(sqlStmt, param);
		
		Vector results = new Vector();
		while(rs.next()){
			results.add(rs.getObject(1));		
//System.out.println(results.get(results.size()-1));
		}
		
		rs.close();
		
		Object[] temp = new Object[results.size()];		
		results.toArray(temp);
		
		return temp;
	}

	
	public Object queryCell(String sqlStmt, Object[] param) throws Exception{
		ResultSet rs = query(sqlStmt, param);
		
		if(rs.next())
			return rs.getObject(1);
		else
			throw new Exception("no such row");
	}
	
	public String[] queryColumnString(String sqlStmt, Object[] param) throws Exception{
		Object[] results = queryColumn(sqlStmt, param);
		return com.pongsor.util.PrimitiveConverter.objectArrayToStringArray(results);
	}
}
		
		