package org.metaworks.wizard;

import org.metaworks.*;
import org.metaworks.inputter.*;
import com.pongsor.ide.*;

import java.awt.Component;
import java.lang.reflect.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class ArgTable extends AppliedTable{

	int depth=0;
	
	public ArgTable(String className) throws Exception{
		this(Thread.currentThread().getContextClassLoader().loadClass(className));
	}

	public ArgTable(Class cls) throws Exception{
		super();
		
		String clsNm = cls.getName();
		String help[]=null;
		
		Class [] argCls;
		
		setName(clsNm.substring(clsNm.lastIndexOf(".")+1, clsNm.length()));
		
		
		if(!clsNm.equals("java.lang.String") && !clsNm.equals("int") && !clsNm.equals("java.lang.Object")){
			Constructor constrs[] = cls.getConstructors();
			
			int tempLength=0;
			int tempIdx=0;
			for(int i=0; i<constrs.length; i++){				
				if(tempLength < constrs[i].getParameterTypes().length){
					tempLength = constrs[i].getParameterTypes().length;
					tempIdx = i;
				}
			}
			argCls = constrs[tempIdx].getParameterTypes();
			
			try{
				
//System.out.println("get"+clsNm.substring(clsNm.lastIndexOf(".")+1, clsNm.length())+"Help");	
				Method helpMethod = cls.getMethod("get"+clsNm.substring(clsNm.lastIndexOf(".")+1, clsNm.length())+"Help", argCls);
				
				Object[] fillerArgs = new Object[argCls.length];
				
				for(int i=0; i<fillerArgs.length; i++){
					if(argCls[i].isPrimitive()){
						String name = argCls[i].getName();
						
						if(name.equals("int"))					
							fillerArgs[i]=new Integer(0);
						else if(name.equals("boolean"))	
							fillerArgs[i]=new Boolean(false);
						

						
					}
					else
						fillerArgs[i]=null;
				}
				
				help = (String [])helpMethod.invoke(null, fillerArgs);
			}catch(Exception e){e.printStackTrace();};
			
		}else
			argCls = new Class[]{cls};
		
		if(help!=null) setTitle(help[0]);
		
		for(int i=0; i<argCls.length; i++){
			String clsName = argCls[i].getName();
			String title = (help!=null ? help[i+1] : clsName);
			
			if(clsName.equals("java.lang.String") || clsName.equals("java.lang.Object"))
				setFieldDescriptor(new FieldDescriptor("args "+i, title, 0));
			else
			if(clsName.equals("boolean"))
				setFieldDescriptor(new FieldDescriptor("args "+i, title, 0, false, null,
					new SelectInput(new Boolean[]{new Boolean(true), new Boolean(false)})
				));
			else
			if(clsName.equals("int"))
				setFieldDescriptor(new FieldDescriptor("args "+i, title, 2));
			else
				setFieldDescriptor(new FieldDescriptor("args "+i, title, 0, false, null,
					new ClassInputter(argCls[i])
				));
		}
	
	}
	
/*	public static void main(String args[]) throws Exception{
		InputDialog dialog = new InputDialog (new ArgTable("com.pongsor.dosql.FieldDescriptor"));
		dialog.show();
	}*/
	
	public String makeSource(){
		Instance recs[]= getRecords();
		
		String temp="";
		
		temp+="import com.pongsor.dosql.*;\n";
		temp+="import com.pongsor.dosql.inputter.*;\n";
		
		
		temp+="public class Test extends AppliedTable{\n";
		temp+="	public Test(){\n";
		temp+="		super(\"test\",\n";
		
/*		temp+="			new FieldDescriptor[]{\n";
		
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
	
		temp+="			}\n";*/
		depth=3;
		temp+=constructObjectSource(getRecords());
		
		
		temp+="\n		);\n";
		temp+="	}\n";
		
		
		temp+="\n";
		temp+="	public static void main(String args[]){\n";
		temp+="		new Test().runDefaultApplication();\n";
		temp+="	}\n";
		temp+="}\n";
		
		return temp;
	}
	
	public String constructObjectSource(Object obj){
		String tempSource="";

		for(int i=0; i<depth; i++)
			tempSource+="\t";
			
		String indent = tempSource;
			
		depth++;

		if(obj instanceof Instance[]){
			Instance [] recs = (Instance [])obj;
			
			String name = recs[0].getType().getName();
			
			if(name.startsWith("!"))
				name=name.substring(1, name.length());
			
			tempSource += "\n"+tempSource+"new "+name+"[]{";
			
			for(int i=0; i<recs.length; i++){
				tempSource += constructObjectSource(recs[i]) +(i==recs.length-1 ? "":",\n");
			}
			
			tempSource += "\n"+indent+"}";
			
			depth--;
			return tempSource;
		}else
		if(obj instanceof Instance){
			Instance rec = (Instance)obj;
			
			String name = rec.getType().getName();

			if(name.startsWith("!")){
				depth--;
				return constructObjectSource(rec.getFieldValue("args 0"));
			}
			
			tempSource = "\n"+tempSource+"new "+name+"(";//\n\t"+indent;
			
			String sep = "";
			for(int i=0; i<rec.size(); i++){
				Object thisField = rec.getFieldValue("args "+i);

				tempSource += sep + constructObjectSource(thisField);
				sep=", ";
			}
			
			tempSource +="\n"+indent+")";
			
			depth--;
			return tempSource;
		}else{
			depth--;
			String wrapper = (obj instanceof String ? "\"":"");
			return ""+wrapper+obj+wrapper;
		}
	}
	
	public JPanel createPanel(){
		JPanel pan = super.createPanel();
		
		JButton but = new JButton("�ҵ�");
		
		pan.add("South", but);
		
		but.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
//				System.out.println(constructObjectSource(getRecords()));
				
				String src = makeSource();
System.out.println(src);
				
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
	
		
}	

class ClassInputter extends AbstractComponentInputter{

	Class cls;
	Object obj=null;

	public ClassInputter(Class cls){
		this.cls = cls;
	}
	
	public Component getNewComponent(){
		JButton but = new JButton("arg �Է�");
		but.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				
				try{
					String clsName = cls.getName();
					
					org.metaworks.Type table;
					


					if(cls.isArray()){
System.out.println("isArray");
						
						clsName = clsName.substring(2, clsName.length()-1);
						
						cls = Thread.currentThread().getContextClassLoader().loadClass(clsName);
						
						if(cls.isInterface()){
							table = new ClassTable(cls);
						}else{
System.out.println("isobjecg");
							table = new ArgTable(cls);
						}
						
						clsName = clsName.substring(clsName.lastIndexOf(".")+1, clsName.length());
						
						if(cls.isPrimitive() || Modifier.isFinal(cls.getModifiers()) || cls.getName().equals("java.lang.Object"))
							clsName = "!"+clsName;// flagging that the record is primitive
												
						
						AppliedTable appTable = new AppliedTable(clsName){
							public JPanel createPanel(){
								JPanel oldPanel = super.createPanel();
								JButton but = new JButton("�Է¿Ϸ�");
								
								but.addActionListener(new ActionListener(){
									public void actionPerformed(ActionEvent ae){
										setValue(getRecords());
										getOwner().dispose();
									}
								});
														
								oldPanel.add("South", but);
								
								return oldPanel;
							}
						};
						appTable.setFieldDescriptors(table.getFieldDescriptors());
						appTable.runDefaultApplication();
						
					}else{
						if(cls.isInterface()){
							table = new ClassTable(cls);
							InputDialog inputdialog = new InputDialog(table){
								public void onSaveOK(Instance rec){

									try{
										rec.printProperties();
										Class selectedCls = (Class)rec.getFieldValue("select");
										
										System.out.println(""+selectedCls);
									
										org.metaworks.Type table = new ArgTable(selectedCls);
									
										InputDialog inputdialog = new InputDialog(table){
											public void onSaveOK(Instance rec){
												setValue(rec);
											}
										};
										
										inputdialog.show();
									}catch(Exception e){e.printStackTrace();}
								}
							};
							
							inputdialog.show();
							
						}else{
							table = new ArgTable(cls);
							
							final boolean bDirectInput;
							if(cls.isPrimitive() || Modifier.isFinal(cls.getModifiers()) || cls.getName().equals("java.lang.Object"))
								bDirectInput=true;
							else	bDirectInput=false;
							

							InputDialog inputdialog = new InputDialog(table){
								public void onSaveOK(Instance rec){
									if(bDirectInput){
										setValue(rec.getFieldValue("arg 0"));
									}else
										setValue(rec);
								}
							};
							
							inputdialog.show();
						}
					

					}
					
				}catch(Throwable e){e.printStackTrace();}
			}
			
		});
		
		return but;
	}
	
	public Object getValue(){
		return obj;
	}
	public void setValue(Object obj){
		this.obj = obj;
		((JButton)getComponent()).setText(""+obj);
	}
	
	private class ClassTable extends org.metaworks.Type{
		ClassTable(Class baseClass){
			super();
			
			try{
				Class[] classes = ClassBrowser.findClasses(cls);
	
				setFieldDescriptors(
					new FieldDescriptor[]{
						new FieldDescriptor("select","select", 0, false, null,
							new SelectInput(classes)								
						)
					}
				);
			}catch(Throwable e){}
		}
	}
}