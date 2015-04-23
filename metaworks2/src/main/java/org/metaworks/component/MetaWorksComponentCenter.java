package org.metaworks.component;

import java.util.*;
import java.io.*;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.sql.*;

public class MetaWorksComponentCenter implements ComponentCenter{

	static Hashtable serializers;
	static Hashtable serviceProviders;
	static Hashtable resources;
	static ComponentCenter instance = null;
	static{
		serializers = new Hashtable();
		resources = new Hashtable();
		serviceProviders = new Hashtable();
	}

	protected MetaWorksComponentCenter(){}

	public static synchronized ComponentCenter getInstance(){
		if(instance!=null)
			return instance;
		else{
			instance = new MetaWorksComponentCenter();
			return instance;
		}
	}
	
	public synchronized void setInstance(ComponentCenter cc){
		instance = cc;
	}

	public static void serialize(Object sourceObj, OutputStream os) throws Exception{
		XMLEncoder e = new XMLEncoder(new BufferedOutputStream(os));
		e.writeObject( sourceObj);
		e.flush();
		//os.flush();
	}
	
	public static Object deserialize(InputStream is) throws Exception{
		Object obj = null;
		XMLDecoder d;
		d = new XMLDecoder( new BufferedInputStream( is));		
		obj = d.readObject();
		d.close();
		is.close();
		
		return obj;
	}
	
	public Object getComponent(String name) throws Exception{
		String compFilePath = getComponentPath(name);
		FileInputStream f = new FileInputStream(compFilePath);
		return deserialize(f);
	}
	
	public void saveComponent(String name, Object comp) throws Exception{
		String compFilePath = getComponentPath(name);
		
		String currPath = ".";
		StringTokenizer tokenizer = new StringTokenizer(name, ".");	
		while(tokenizer.hasMoreTokens()){
			String dirName = tokenizer.nextToken();
			if(tokenizer.hasMoreTokens())
			try{
				currPath = currPath + System.getProperty("file.separator") + dirName;
				new File(currPath).mkdir();
			}catch(Exception e){}
		}
		
		FileOutputStream f = new FileOutputStream(compFilePath);
		serialize(comp, f);

		String pkgName = name.substring(0, name.lastIndexOf("."));
		String compName = name.substring(name.lastIndexOf(".")+1, name.length());
		
		if(!compName.equals("list")){
			ArrayList componentList = null;
			try{
				componentList = (ArrayList)getComponent(pkgName + ".list");
			}catch(Exception e){
				componentList = new ArrayList();
			}
			
			if(!componentList.contains(compName)){
				 componentList.add(compName);
				 saveComponent(pkgName + ".list", componentList); 
			}
		}		
	}
	
	private static String getComponentPath(String name){
		String compFilePath = "." + System.getProperty("file.separator")
			+ name.replace('.', System.getProperty("file.separator").charAt(0)) + ".xml";
					
		return compFilePath;
	}
	
	public String[] getComponentNames(String pkgName){
/*System.out.println("pkgName:"+pkgName);
		File f = new File(pkgName.replace('.', System.getProperty("file.separator").charAt(0)));
System.out.println("comp path:"+f.getAbsolutePath());

		File[] compFiles = f.listFiles();
		
		String [] names = new String[compFiles.length];
		for(int i=0; i<compFiles.length; i++){
			names[i] = compFiles[i].getName();
			names[i] = names[i].substring(0, names[i].indexOf("."));
		}
		
		return names;*/
		
		ArrayList componentList = null;
		try{
			componentList = (ArrayList)getComponent(pkgName + ".list");
		}catch(Exception e){
			componentList = new ArrayList();
		}
		
		String[] names = new String[componentList.size()];
		componentList.toArray(names);
		
		return names;		
	}
	
	static Connection connection;
	
		public Connection getConnection(){
			return connection;
		}
			
		public void setConnection(Connection value){
			connection = value;
		}
			
}