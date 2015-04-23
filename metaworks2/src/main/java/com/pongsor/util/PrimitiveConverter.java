package com.pongsor.util;

import java.util.*;
/**
 * Vector나 Hashtable을 Primitive Array로 변환한다.
 * @author 장진영
 */
public class PrimitiveConverter{

	public static String [] vectorToStringArray(java.util.Vector vec){
		String temp [] = new String [vec.size()];	
		Enumeration enumeration = vec.elements();
		int i=0;
		while(enumeration.hasMoreElements())
			temp[i++] = enumeration.nextElement().toString();
	
		return temp;	
	}
			
	public static String [] hashTableToStringArray(java.util.Hashtable ht){
		String temp [] = new String [ht.size()];
		
		Enumeration enumeration = ht.keys();
		int i=0;
		while(enumeration.hasMoreElements())
			temp[i++] = enumeration.nextElement().toString();
	
		return temp;
	}
	
	public static String [] objectArrayToStringArray(Object[] input){
		String temp [] = new String [input.length];
		
		for(int i=0; i<temp.length; i++){
			temp[i] = (String)input[i];
		}		
	
		return temp;
	}
}		
		