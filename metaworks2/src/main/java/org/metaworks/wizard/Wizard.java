package org.metaworks.wizard;

import org.metaworks.*;
import org.metaworks.inputter.*;
import com.pongsor.ide.*;

import javax.swing.*;
import java.awt.event.*;

import java.io.*;

public class Wizard extends AppliedTable{

	public Wizard(){
		super(
			"DoSQL Wizard - DB application generator",
			new FieldDescriptor[]{
				new FieldDescriptor("fieldName", "Field Name"),
				new FieldDescriptor("type", "type", 0, false, null,
					new SelectInput(
						new String[]{
							"string",
							"int",
							"date"
						},
						new Integer[]{
							new Integer(0),
							new Integer(2),
							new Integer(93)
						}
					)
				),
				new FieldDescriptor("iskey", "is key", 0, false, null,
					new SelectInput(
						new Boolean[]{
							new Boolean(true),
							new Boolean(false)
						}
					)
				),
				new FieldDescriptor("references")
			}
		);
		
		
	}
	
	public String makeSource(){
		Instance recs[]= getRecords();
		
		String temp="";
		
		temp+="import com.pongsor.dosql.*;\n\n";
		
		temp+="public class Test extends AppliedTable{\n";
		temp+="	public Test(){\n";
		temp+="		super(\"test\",\n";
		
		temp+="			new FieldDescriptor[]{\n";
		
		for(int i=0; i<recs.length; i++){
			temp+="\t\t\t\tnew FieldDescriptor(";
			
			String fieldName = (String)recs[i].getFieldValue("fieldName");
			String displayName = (String)recs[i].getFieldValue("displayName");
			
			if(displayName==null) displayName = fieldName;
			
			Integer type = (Integer)recs[i].getFieldValue("type");
			//Inputter inputter = recs[i].getFieldValue("inputter");
			
			String arguments = "\""+fieldName+"\", \""+displayName+"\", "+type;
			
			temp+=arguments+")"+(recs.length==i+1 ? "":",")+"\n";
		
		}
	
		temp+="			}\n";
		temp+="		);\n";
		temp+="	}\n";
		temp+="\n";
		temp+="	public static void main(String args[]){\n";
		temp+="		new Test().runDefaultApplication();\n";
		temp+="	}\n";
		temp+="}\n";
		
		return temp;
	}
	
	public JPanel createPanel(){
		JPanel pan = super.createPanel();
		
		JButton but = new JButton("뚝딱");
		
		pan.add("South", but);
		
		but.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				System.out.println(makeSource());
				
				String src = makeSource();
				
				try{
					FileOutputStream fo = new FileOutputStream("Test.java");
					fo.write(src.getBytes());
					fo.close();
					
					Runner runner = new Runner(){
						public void completed(){
							System.out.println("completed.. running..");
							Runner runner2 = new Runner(){
								public void write(String out){
									System.out.println(out);
								}
							};
							runner2.run("java Test");
						}
						public void write(String out){
							System.out.println(out);
						}
					};
					
					System.out.println("compiling...");
					runner.run("javac Test.java");
					
				}catch(Exception e){e.printStackTrace();}
			}
		});
		
		return pan;
	}
	
	public static void main(String args[]){
		new Wizard().runDefaultApplication();
	}
	

}