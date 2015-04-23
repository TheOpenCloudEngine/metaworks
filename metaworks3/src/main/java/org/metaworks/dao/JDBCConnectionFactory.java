/*
 * Created on 2004. 12. 15.
 */
package org.metaworks.dao;

import javax.sql.*;

import java.io.Serializable;
import java.sql.*;
import javax.naming.*;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.RadioInput;

/**
 * @author Jinyoung Jang
 */
public class JDBCConnectionFactory  implements ConnectionFactory {
	
	public static void metaworksCallback_changeMetadata(Type type){
		
//		type.setFieldOrder(new String[]{
//			"DriverClassName", "URL", "User Name", "Password"	
//		});
		
	}
	
	public JDBCConnectionFactory(){
	}
	
	public Connection getConnection() throws Exception{
		Thread.currentThread().getContextClassLoader().loadClass(getDriverClass());
		Connection con = DriverManager.getConnection(getConnectionString(), getUserId(), getPassword());		
				
		return con; 
	}
	
	String connectionString;
		public String getConnectionString() {
			return connectionString;
		}
		public void setConnectionString(String value) {
			connectionString = value;
		}

	String userId;
		public String getUserId() {
			return userId;
		}
		public void setUserId(String value) {
			userId = value;
		}

	String password;
		public String getPassword() {
			return password;
		}
		public void setPassword(String value) {
			password = value;
		}

	String driverClass;
		public String getDriverClass() {
			return driverClass;
		}
		public void setDriverClass(String value) {
			driverClass = value;
		}
}
