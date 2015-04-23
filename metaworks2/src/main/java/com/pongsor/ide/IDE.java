package com.pongsor.ide;

//import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class IDE extends Frame{

	TextArea ta;

	public IDE(){
		super("compiler");
		
		setLayout(new BorderLayout());
		
		Button comBut;
		
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
	
		proc= Runtime.getRuntime().exec("cmd /c javac.exe -classpath c:\\pongsor Test.java");
		
		System.out.println("proc is null?"+(proc==null));
		
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
				try{
					String line;
				
					while((line = reader.readLine())!=null){
						System.out.println(">"+line);
					}
				}catch(Exception e){
					e.printStackTrace();
				}			}
		};
		
		pourer1.start();
		pourer2.start();
		
		in.close();
		errin.close();	
	}
	
	public static void main(String args[]){
		IDE ide = new IDE();
		
		ide.setVisible(true);
	}
}