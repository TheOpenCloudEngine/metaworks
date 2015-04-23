package org.metaworks;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import org.metaworks.ui.*;

import org.metaworks.inputter.*;
import org.metaworks.query.*;

import java.util.*;

public class QueryForm extends InputForm{


	String queryStr=null;
	Query q;

	public QueryForm(Type table, String qs){
		super();
		
		this.type = table;
		queryStr = qs;
		createForm();
	}
	
	public QueryForm(Type table, Query q){
		super();
		
		this.type = table;
		this.q = q;
		
		createForm();
	}
	
	public void createForm(){
		
		GridBagLayout gridbag;
		gridbag=new GridBagLayout();
		setLayout(gridbag);
                //setLayout(new GridLayout(0,2));


		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.ipadx=10;
		c.ipady=2;

		

		String names[] =getInputFieldNames();

		for(int i=0; i<names.length; i++)		
		{
			
			FieldDescriptor fd = type.getFieldDescriptor(names[i]);
			
			if(fd!=null){

				
		       		fd.getInputter().initialize(fd.getAttributeTable());


				JLabel label;
					label = new JLabel(fd.getDisplayName());
					c.gridx = 0;
					c.gridy = i;
					c.anchor= c.EAST;

		       		 	gridbag.setConstraints(label, c);
	       		 	add(label);


				Component comp;
					comp=fd.getInputComponent();
					c.gridx = 1;
					c.anchor= c.WEST;
		       		 	gridbag.setConstraints(comp, c);
		       		add(comp);

		       	}else
		       		System.out.println("Missing FieldDescriptor '"+ names[i] +"'");
		}

	}
	
	public String getSQL(){
		if(queryStr==null){
			
			return q.toSQLExp(getInstance());
		}
		
		
		StringTokenizer st = new StringTokenizer(queryStr, "$");
		
//		int iCount = st.countTokens();
		
		String makingStr="";
		
		Instance rec = getInstance();
		
		while(st.hasMoreElements()){
			makingStr += st.nextToken();	// 짝수가 아니면 버림 (다음 변수 위치로 옮기게 함)
			
			if(!st.hasMoreElements()) break;
			
			String replacer = st.nextToken();
			
			String value = rec.get(replacer).toString();
			
			if(value != null)
				replacer = value;	// replace
			else
				replacer = "$"+replacer+"$";	// recover
				
			makingStr += replacer;
		}
		
		return makingStr;
	}

	public String [] getInputFieldNames(){

		if(queryStr != null){
	
			
			StringTokenizer st = new StringTokenizer(queryStr, "$");
	
			String inputFields[] = new String[st.countTokens() / 2];
	
			int i=0;
			while(st.hasMoreElements()){
				st.nextToken();	// 짝수가 아니면 버림 (다음 변수 위치로 옮기게 함)
				
				if(!st.hasMoreElements()) break;
				
				String replacer = st.nextToken();
	
				inputFields[i++] = replacer;
			}
	
			return inputFields;
			
		}else{			
			return q.getFieldNames();
		}
	}

}
