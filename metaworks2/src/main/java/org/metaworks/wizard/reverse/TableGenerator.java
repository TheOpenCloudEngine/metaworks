package org.metaworks.wizard.reverse;

import org.metaworks.*;
import org.metaworks.component.MetaWorksComponentCenter;
import java.sql.*;

public class TableGenerator{

	public static void main(String[] args) throws Exception{	
		if(args.length<4){
			System.out.println(" usage: TableGenerator <connStr> <uid> <pwd> <prjName>");
			System.out.println(" 		example) TableGenerator jdbc:oracle:thin:@203.241.246.222:1521:ora90 scott tiger legacyMRP");
			return;
		}
	
		Thread.currentThread().getContextClassLoader().loadClass("oracle.jdbc.driver.OracleDriver");			
		Connection conn = (DriverManager.getConnection(args[0], args[1], args[2]));			
	
		OracleDatabaseIntrospector oracleDatabaseIntrospector = new OracleDatabaseIntrospector();
		
		String[] tabnames = oracleDatabaseIntrospector.getTableNames(conn);
		
		String packageName = "tables." + args[3];		
		for(int i=0; i<tabnames.length; i++){
			Type table = oracleDatabaseIntrospector.createTable(conn, tabnames[i], packageName);
			MetaWorksComponentCenter.getInstance().saveComponent(packageName + "." + tabnames[i], table);
		}
	}
}