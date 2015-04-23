package org.metaworks.tutorial.objecttable;

import java.util.Date;

import org.metaworks.InputForm;
import org.metaworks.ObjectInstance;
import org.metaworks.ObjectType;
import org.metaworks.Instance;
import org.metaworks.Type;

public class Person1{
	

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
		Type personTable = new ObjectType(Person1.class);
		Instance personRecord = personTable.createInstance();
		
		InputForm inputFormForPerson = new InputForm(personTable);
		inputFormForPerson.postInputDialog(null);
		
		System.out.println(((ObjectInstance)inputFormForPerson.getInstance()).getObject());
	}

}