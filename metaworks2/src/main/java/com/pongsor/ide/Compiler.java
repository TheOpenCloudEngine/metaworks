package com.pongsor.ide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;



public class Compiler extends JFrame{

	TextArea ta;
	Button comBut;

	public Compiler (){
		super("compiler");
		
		setLayout(new BorderLayout());
		
		add("Center", ta = new TextArea());
		add("South", comBut = new Button("Compile it"));
		
		comBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				try{
					compile();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
	}

	public void compile() throws Exception{
		Process proc;	
	
		proc= Runtime.getRuntime().exec("javac.exe -classpath c:\\pongsor Test.java");
		
		InputStream in = proc.getInputStream();
		InputStream errin = proc.getErrorStream();
		
		final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		final BufferedReader reader2 = new BufferedReader(new InputStreamReader(errin));
		
		Thread pourer1= new Thread(){
			public void run(){
				String line;
				try{
					while((line = reader.readLine())!=null){
						System.out.println(">"+line);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
					

		Thread pourer2= new Thread(){
			public void run(){
				String line;
				
				try{
					while((line = reader.readLine())!=null){
						System.out.println(">"+line);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		
		pourer1.start();
		pourer2.start();
		
		in.close();
		errin.close();
	}
}