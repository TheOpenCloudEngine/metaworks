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

public class Person3{
	
	public static void metaworksCallback_changeMetadata(ObjectType table){
		FieldDescriptor fd;
		
		fd = table.getFieldDescriptor("Name");
		fd.setDisplayName("성명");
		fd.setAttribute("default", "[성][이름]");
		fd.setValidators(new Validator[]{
				new Validator(){

					public String validate(Object data, Instance instance) {
						String name = (String)data;
						if( name.length() < 3){
							return "이름은 3자 이상";	
						}
						
						return null;
					}

				}
		});
		/*fd.setAttribute("dependancy", new EnablingDependancy("Birthday"){

			public boolean enableIf(Object dependencyFieldValue) {
				Date birthday = (Date)dependencyFieldValue;
				return birthday.after(new Date());
			}
			
		});*/

		final Inputter inputterForName = fd.getInputter();


		fd = table.getFieldDescriptor("Birthday");
		final Inputter inputter = fd.getInputter();
		
		inputter.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				Date birthday = (Date)inputter.getValue();
				inputterForName.getComponent().setEnabled(birthday.after(new Date()));
			}
			
		});
		
		fd.setDisplayName("생년월일");
				
	}

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
		Type personTable = new ObjectType(Person3.class);
		Instance personRecord = personTable.createInstance();
		
		InputForm inputFormForPerson = new InputForm(personTable);
		inputFormForPerson.postInputDialog(null);
		
	}

}