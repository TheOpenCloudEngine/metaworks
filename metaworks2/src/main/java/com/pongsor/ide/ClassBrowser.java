package com.pongsor.ide;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;

public class ClassBrowser{


	static Vector classpaths=new Vector();
	
	static{
		String classPath = System.getProperty("java.class.path");
		
		StringTokenizer st = new StringTokenizer(classPath, ";");
		
		while(st.hasMoreElements()){
			String aPath = st.nextToken();
			
			File file = new File(aPath);
			
//			if(file.isDirectory()){
				/*String [] fnames = file.list();
				
				for(int i=0; i<fnames.length; i++)
					System.out.println(">"+fnames[i]);*/
					
				classpaths.add(aPath);
//			}else
//				jarDirs.add(aPath);
		}
	}
//	Vector jarDirs=new Vector();


	
	public static Class [] findClasses(String superclass) throws Throwable{
		Class cls=null;
		cls = Thread.currentThread().getContextClassLoader().loadClass(superclass);	
	
		return findClasses(cls);
	}
	
	public static Class [] findClasses(Class cls) throws Throwable{
		
/*		URLClassLoader cl = (URLClassLoader)ClassBrowser.class.getClassLoader();
		
		URL[] urls = cl.getURLs();
		
		for(int i=0; i<urls.length; i++){
			JarFile jarfile = new JarFile(componentPath);
			ZipEntry entry = jarfile.getEntry("META-INF/activitytypes.xml");

		}*/

		String superPath = cls.getName();	
		superPath = superPath.replace('.', '/');
		superPath = superPath.substring(0, superPath.lastIndexOf("/"));
		
		Vector validClasses = new Vector();
		
		Enumeration enumeration = classpaths.elements();
		
		while(enumeration.hasMoreElements()){
			String path = (String)enumeration.nextElement();
			
			File file = new File(path);
			
			if(file.isDirectory()){
System.out.println(path + "/" +superPath);
				file = new File(path + "/" +superPath);
				
				String [] fnames = file.list(new FilenameFilter(){
					public boolean accept(File dir, String fname){
						return fname.endsWith(".class");
					}
				});
				
				if( fnames == null)
					continue;
					
System.out.println(">>>>>>"+fnames.length);				
				for(int i=0; i<fnames.length; i++){
					String clsName_ = fnames[i].substring(0, fnames[i].lastIndexOf("."));
					
					clsName_=superPath.replace('/','.') + "." + clsName_;
					
					
					
					
					try{
						Class cls_= Thread.currentThread().getContextClassLoader().loadClass(clsName_);
System.out.println(">"+clsName_);
						
						if(cls.isAssignableFrom(cls_)){
							System.out.println("found!");
							
							if(!Modifier.isAbstract(cls_.getModifiers()) && !cls_.isInterface()){
								validClasses.add(cls_);
								
								//System.out.println("valid class :"+cls_.getName());
							}
						}
					}catch(Throwable e){
						System.out.println("err:"+e.getMessage());
					}
				}
			}
		}
		
		Class [] temp = new Class[validClasses.size()];
		validClasses.toArray(temp);
		
		return temp;
	}
	
	// b is same type(implementation) of a?
	public static boolean isSameType(Class a, Class b){
/*		String clsName=null;
		
		// test superclasses
		do{
			Class superclass = b.getSuperclass();
			
			if(superclass==null) break;
			clsName = superclass.getName();

			if(clsName.equals(a.getName())) return true;
			b=b.getSuperclass();
		}while(!clsName.equals("Object"));
		
		// test interfaces
		Class interfaces [] = b.getInterfaces();
		
		for(int i=0; i<interfaces.length; i++){
			clsName = interfaces[i].getName();
System.out.println("interface name is "+clsName);
			if(clsName.equals(a.getName())) return true;
		}			
	
		return false;*/
		
		return a.isAssignableFrom(b);
	}
			
	
	public static void main(String args[]) throws Throwable{
		new ClassBrowser().findClasses(args[0]);
	}	
}