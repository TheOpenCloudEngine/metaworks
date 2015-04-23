package org.metaworks.tutorial;

import org.metaworks.inputter.*;
import org.metaworks.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Types;

public class TableTutorial{

	public static void main(String args[]) throws Exception{

		Thread.currentThread().getContextClassLoader().loadClass("oracle.jdbc.driver.OracleDriver");																							// ~/orawin95/network/admin/tnsnames.ora file ����.
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@203.241.246.223:1521:orcl", "scott", "tiger");
		
		// table�� ���Ѵ�. db�� 'create'���� ����
		Type px_part_newparts = new Type(
			"px_part_newparts",	//  table��
						//  column		title		type		iskey
			new FieldDescriptor[]{
				new FieldDescriptor("SEQNO", 		"���", 	Types.INTEGER,	true),
				new FieldDescriptor("DESCRIPTION",	"����"),
				new FieldDescriptor("DEVELOPMENTDATE",	"��������", 	Types.DATE),
				new FieldDescriptor("DIVISION",		"�����")
			},
			con
		);

		Instance [] rec = px_part_newparts.find("seqno=1");
		
		if(rec.length> 0){
			System.out.println("SEQNO = "+rec[0].get("SEQNO"));
			System.out.println("DESCRIPTION = " + rec[0].get("DESCRIPTION"));
			System.out.println("DEVELOPMENTDATE = " + rec[0].get("DEVELOPMENTDATE"));
			System.out.println("DIVISION = " + rec[0].get("DIVISION"));
		}			
	}	
}