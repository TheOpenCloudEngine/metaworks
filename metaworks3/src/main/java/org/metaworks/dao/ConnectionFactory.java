/*
 * Created on 2004. 12. 15.
 */
package org.metaworks.dao;


import java.io.Serializable;
import java.sql.*;


/**
 * @author Jinyoung Jang
 */
public interface ConnectionFactory extends Serializable {
	public Connection getConnection() throws Exception;
}
