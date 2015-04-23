package com.pongsor.util.db;

import java.sql.*;

public abstract class AbstractDatabaseObject{

	public AbstractDatabaseObject(){
		init();
	}
	
	public abstract void init();	
	public abstract Connection getConnection() throws Exception;
	
	public ResultSet query(String sqlStmt, Object[] param) throws Exception{
		Statement stmt=getConnection().createStatement();
		ResultSet rs=stmt.executeQuery(sqlStmt);	
			
		stmt.close();
		
		return rs;
	}
}
		
		