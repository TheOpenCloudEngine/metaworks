package com.pongsor.ide;

import java.util.*;
import java.util.jar.*;
import java.lang.reflect.*;


public class ClassFinder{

	public void ClassFinder(){
	
//		Package pack = Package.getPackage("com");
		
//		System.out.println("ddd"+pack.getSpecificationTitle() );
		
		System.out.println("ddd");
		Package packages [] = Package.getPackages();
		
		for(int i=0; i<packages.length; i++){
			System.out.println(packages[i].getSpecificationTitle() );
		}
		
	}
	
	public static void main(String args[]) throws Exception{
		System.out.println("ddd");
		Package packages [] = Package.getPackages();
		
		for(int i=0; i<packages.length; i++){
			System.out.println(packages[i].getName() );
		}
		
		Class integerClass = Properties.class;
		
		Method methods[] = integerClass.getMethods();
		
		for(int i=0; i<methods.length; i++)
			System.out.println(">"+methods[i]);
		
		JarFile jarFile = new JarFile("c:\\jdk\\jaf\\activation.jar");
		
		Enumeration enumeration = jarFile.entries();
		
		while(enumeration.hasMoreElements())
			System.out.println("-"+enumeration.nextElement());
			
			
		System.getProperties().list(System.out);
	}
}
	
		