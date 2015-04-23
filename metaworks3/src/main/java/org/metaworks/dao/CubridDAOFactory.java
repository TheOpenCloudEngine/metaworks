package org.metaworks.dao;

public class CubridDAOFactory extends MySQLDAOFactory{
		
	public String getDBMSProductName() throws Exception {
		return "CUBRID";
	}	
}
