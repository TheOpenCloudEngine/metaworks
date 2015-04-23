package org.metaworks.defaultimplementation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.metaworks.component.*;
import org.metaworks.*;
import java.sql.*;

public class DefaultDesktop extends JPanel{
	
	ComponentCenter componentCenter;
	
		public ComponentCenter getComponentCenter() {
			return componentCenter;
		}
	
		public void setComponentCenter(ComponentCenter center) {
			componentCenter = center;
		}
		
	Connection connection;
	
		public Connection getConnection(){
			return connection;
		}
			
		public void setConnection(Connection value){
			connection = value;
			getComponentCenter().setConnection(value);
		}

	JDesktopPane jDesktopPane;
	
		public JDesktopPane getJDesktopPane(){
			return jDesktopPane;
		}
			
		public void setJDesktopPane(JDesktopPane value){
			jDesktopPane = value;
		}
		
	static DefaultDesktop instance;
	
		public synchronized static DefaultDesktop getInstance(){
			return instance;
		}
			
		public synchronized static void setInstance(DefaultDesktop value){
			instance = value;
		}

	public DefaultDesktop(String projectName, ComponentCenter componentInstance){
//		super("DoSQL Desktop");		
//		super(projectName);	
		super(new BorderLayout());	
		setProjectName(projectName);
		setJDesktopPane(new JDesktopPane());
		setComponentCenter(componentInstance);
		
		add(
			new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createApplicationMenu(), jDesktopPane)
		);	
		
		setInstance(this);		
	}
	
	public DefaultDesktop(String projectName){
		this(projectName, MetaWorksComponentCenter.getInstance());
	}	
	
	String projectName;
	
		public String getProjectName(){
			return projectName;
		}
			
		public void setProjectName(String value){
			projectName = value;
		}
	
	protected Component createApplicationMenu(){
		String names[] = getComponentCenter().getComponentNames("applications." + getProjectName());
		
		JPanel p = new JPanel(new GridLayout(0,1));
		
		for(int i=0; i<names.length; i++){
			String item = names[i];
			JButton b = new JButton(item);
			p.add(b);
			b.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					launchApplication(((JButton)ae.getSource()).getText());
				}
			});
		}
		
		JPanel panel2 = new JPanel(new BorderLayout());
		panel2.add("North", p);
		
		return new JScrollPane(panel2);
	}
	
	protected JInternalFrame launchApplication(String appName){
		try{
			DefaultApplication app = (DefaultApplication)getComponentCenter().getComponent("applications." + getProjectName() + "." + appName);
			JPanel panel=app.createPanel();
			app.setConnection(getConnection());
			app.run();
					
			JInternalFrame jInternalFrame = new JInternalFrame();		
			jInternalFrame.getContentPane().setLayout(new BorderLayout());
			jInternalFrame.getContentPane().add(new JScrollPane(panel));
			getJDesktopPane().add(jInternalFrame);
			jInternalFrame.setTitle(appName);
		 	jInternalFrame.setSize(500, 400); 
		 	jInternalFrame.setClosable(true); 
		 	jInternalFrame.setResizable(true); 
		 	jInternalFrame.setIconifiable(true); 
		 	jInternalFrame.setMaximizable(true);
			jInternalFrame.show();
			
			return jInternalFrame;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}			
	
	public static void main(String[] args) throws Exception{
		JFrame frm = new JFrame(args[0]);

		DefaultDesktop metaWorksDesktop;
		
		if(args.length > 5){
			metaWorksDesktop = new DefaultDesktop(args[0], new URLComponentCenter(args[5]));
		}else{
			metaWorksDesktop = new DefaultDesktop(args[0]);
		}
		
		frm.getContentPane().add(metaWorksDesktop);
		
		if(args.length > 3){
			Thread.currentThread().getContextClassLoader().loadClass("oracle.jdbc.driver.OracleDriver");			
			metaWorksDesktop.setConnection(DriverManager.getConnection(args[1], args[2], args[3]));
		}
		
		if(args.length > 4){
			metaWorksDesktop.launchApplication(args[4]);
		}
		
		
		frm.addWindowListener(new WindowAdapter(){
					public void windowClosing(WindowEvent we){
						System.exit(0);
					}
				});
				
		frm.setSize(800,600);		
		frm.show();
	}		


}