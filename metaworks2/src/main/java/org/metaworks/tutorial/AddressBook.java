package org.metaworks.tutorial;

import org.metaworks.*;
import org.metaworks.inputter.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Types;

public class AddressBook extends AppliedTable{

	public AddressBook(Connection conn)
	{

		super( "ADDRESSBOOK", new FieldDescriptor[]{
				new FieldDescriptor("COUNT", 		"���",  	Types.INTEGER,	true),
				new FieldDescriptor("GROUPID", 	"�׷��", 	Types.INTEGER,	false, null,
					 new ReferenceInput( conn, "SELECT * FROM AddressGroup")),
				new FieldDescriptor("FRIENDNAME",	"ģ���̸�"),
				new FieldDescriptor("ADDRESS",		"�ּ�"),
				new FieldDescriptor("PHONENUMBER",	"��ȭ��ȣ"),
				new FieldDescriptor("INPUTDATE",	"�Է�����", 	Types.DATE),
				new FieldDescriptor("MEMO",	"�޸�")
			});
		setConnection( conn);
	}

	public static void main(String args[]){
		
		try{
			Thread.currentThread().getContextClassLoader().loadClass("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@203.241.246.223:1521:ora90", "scott", "tiger");
			
			AddressBook addressBook = new AddressBook( con);
			
			Instance records [] = addressBook.find("");
			
			addressBook.runDefaultApplication();
			addressBook.addRecords(records);
							
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}