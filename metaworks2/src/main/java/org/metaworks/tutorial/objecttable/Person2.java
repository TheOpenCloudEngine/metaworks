package org.metaworks.tutorial.objecttable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import org.metaworks.DefaultApplication;
import org.metaworks.FieldDescriptor;
import org.metaworks.GridApplication;
import org.metaworks.InputForm;
import org.metaworks.ObjectInstance;
import org.metaworks.ObjectType;
import org.metaworks.Instance;
import org.metaworks.Type;
import org.metaworks.inputter.Inputter;
import org.metaworks.validator.Validator;

public class Person2{

	String name;
	Date birthday;
	
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static void main(String[] args) throws Exception{
		Type personTable = new ObjectType(Person2.class);
		
		DefaultApplication defaultApplicationForPerson = new DefaultApplication(personTable);
		defaultApplicationForPerson.runFrame();

	}

}