package org.metaworks.wizard.reverse;

import org.metaworks.*;
import java.sql.Connection;

public interface DatabaseIntrospector{
	
	public String[] getTableNames(Connection conn) throws Exception;
	public Type createTable(Connection conn, String tableName, String packageName) throws Exception;
}