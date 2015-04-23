package org.metaworks.wizard.reverse;

import org.metaworks.*;
import org.metaworks.inputter.*;
import com.pongsor.util.db.*;
import java.sql.*;
import java.util.*;

public class OracleDatabaseIntrospector extends DefaultDatabaseObject implements DatabaseIntrospector{

	private static Hashtable typesForOracleDataTypeNames = new Hashtable();
	static{
		typesForOracleDataTypeNames.put("VARCHAR2", String.class);
		typesForOracleDataTypeNames.put("NUMBER", Integer.class);
		typesForOracleDataTypeNames.put("DATE", java.util.Date.class);
	}
	
	public OracleDatabaseIntrospector(){
		super(null, null, null);
	}
	
	Connection conn;
	public Connection getConnection(){
		return conn;
	}
	protected void setConnection(Connection conn){
		this.conn = conn;
	}
	
	public String[] getTableNames(Connection conn) throws Exception{		
		setConnection(conn);
		return queryColumnString("select tname from tab", null);
	}
	
								    //optional
	public Type createTable(Connection conn, String tableName, String packageName) throws Exception{	
		setConnection(conn);
		Type table = new Type();
		
		table.setName(tableName);
		
		//setting field descriptors
//		Vector fields = new Vector();
		ResultSet rs = query("select column_name, data_type, data_length from user_tab_columns where table_name=?", new Object[]{tableName});
		while(rs.next()){
			FieldDescriptor fd = new FieldDescriptor();
			fd.setName(rs.getString("column_name"));
			
			String strType = rs.getString("data_type");
System.out.println("strType:"+strType);
			Class type = (Class)typesForOracleDataTypeNames.get(strType); 
			
			fd.setType(type);
//			fields.add(fd);
			table.addFieldDescriptor(fd);
		}
		rs.close();
			
		//setting primary key
		rs = query("select cn.column_name as COLUMN_NAME from user_cons_columns cn, user_constraints c where c.constraint_type='P' and c.table_name=? and cn.constraint_name=c.constraint_name", new Object[]{tableName});
		if(rs.next()){
			String pFieldName = (String)rs.getString("COLUMN_NAME");
			table.setKeyField(pFieldName);
		}else{
			table.setFullKeyMode(true);
		}
		rs.close();

		//setting entity relations
		rs = query("select cn.column_name as COLUMN_NAME, c.r_constraint_name as REF_CONST_NAME from user_cons_columns cn, user_constraints c where c.constraint_type='R' and c.table_name=? and cn.constraint_name=c.constraint_name", new Object[]{tableName});
		if(rs.next()){
			String referencingFieldName = (String)rs.getString("COLUMN_NAME");
			String refConstraintName = (String)rs.getString("REF_CONST_NAME");
			rs.close();
System.out.println("referencingFieldName:"+referencingFieldName);
System.out.println("refConstraintName:"+refConstraintName);			
			rs = query("select column_name as COLUMN_NAME, table_name as TABLE_NAME from user_cons_columns where constraint_name=?", new Object[]{refConstraintName});
			if(rs.next()){
				String referencedFieldName = (String)rs.getString("COLUMN_NAME");
				String referencedTableName = (String)rs.getString("TABLE_NAME");
System.out.println("referencedFieldName:"+referencedFieldName);
System.out.println("referencedTableName:"+referencedTableName);			
				Type referencedTable = new Type();
				referencedTable.setComponentName(packageName + "." + referencedTableName);
				
				FieldDescriptor fd = table.getFieldDescriptor(referencingFieldName);
				TableReferenceInput inputter = new TableReferenceInput();
				inputter.setReferenceTable(referencedTable);
				inputter.setReferenceFieldName(referencedFieldName);
				
				fd.setInputter(inputter);
			}
			rs.close();
		}else rs.close();
			
		setConnection(null);	
		
		return table;
	}
		
	public static void main(String[] args) throws Exception{
		Thread.currentThread().getContextClassLoader().loadClass("oracle.jdbc.driver.OracleDriver");			
		Connection conn = (DriverManager.getConnection("jdbc:oracle:thin:@203.241.246.222:1521:ora90", "scott", "tiger"));			
	
		OracleDatabaseIntrospector oracleDatabaseIntrospector = new OracleDatabaseIntrospector();
		
		String[] tabnames = oracleDatabaseIntrospector.getTableNames(conn);
				
		Type bonus = oracleDatabaseIntrospector.createTable(conn, "BONUS", "test");
		FieldDescriptor[] fds = bonus.getFieldDescriptors();
		
		for(int i=0; i<fds.length; i++){
			FieldDescriptor fd = fds[i];
			System.out.print("name:"+fd.getName());
			System.out.println("  type:"+fd.getType());
		}
	}
	
}