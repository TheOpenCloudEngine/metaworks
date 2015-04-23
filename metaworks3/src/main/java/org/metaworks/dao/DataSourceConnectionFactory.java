/*
 * Created on 2004. 12. 15.
 */
package org.metaworks.dao;

import javax.sql.*;

import java.io.Serializable;
import java.sql.*;
import javax.naming.*;

/**
 * @author Jinyoung Jang
 */
public class DataSourceConnectionFactory implements ConnectionFactory {
	public Connection getConnection() throws Exception{
		InitialContext ctx = null;
		ctx = new InitialContext();
		
		if(getDataSourceJndiName()==null)
			throw (new Exception("Data Source JNDI name is null. Check whether the JNDI name is null."));
		
		DataSource ds = (javax.sql.DataSource) ctx.lookup(getDataSourceJndiName());
		Connection conn = ds.getConnection();
		
		return conn; 
	}
	
	String dataSourceJndiName;
	
	public String getDataSourceJndiName() {
		return dataSourceJndiName;
	}

	public void setDataSourceJndiName(String string) {
		dataSourceJndiName = string;
	}

}
