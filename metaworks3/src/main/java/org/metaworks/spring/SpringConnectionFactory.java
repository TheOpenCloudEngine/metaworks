package org.metaworks.spring;

import java.sql.Connection;

import javax.sql.DataSource;

import org.metaworks.dao.ConnectionFactory;
import org.metaworks.dao.TransactionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * 
 * @author zachary
 * 
 */
public class SpringConnectionFactory implements ConnectionFactory {
    DataSource dataSource;	
	    public DataSource getDataSource() {
			return dataSource;
		}
		public void setDataSource(DataSource dataSource) {
			this.dataSource = dataSource;
		}
		
	public Connection getConnection() {
		
//		Connection conn = (Connection) (TransactionContext.getThreadLocalInstance().getSharedContext("__connection"));
//		
//		
//		if(conn == null){
//			conn = DataSourceUtils.getConnection(dataSource);
//			TransactionContext.getThreadLocalInstance().setSharedContext("__connection", conn);
//		}
		
		Connection	conn = DataSourceUtils.getConnection(dataSource);
//
		return conn; 
    }
	
}
