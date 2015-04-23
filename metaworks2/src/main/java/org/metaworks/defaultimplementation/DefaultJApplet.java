/*
 * Created on 2004-03-23
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.metaworks.defaultimplementation;

import org.metaworks.component.*;
import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.net.*;

public class DefaultJApplet extends javax.swing.JApplet {

		public void init(){
			String projectName = 		getParameter("projectName");
			String connectionString = 	getParameter("connectionString");
			String userId = 			getParameter("userId");
			String password = 			getParameter("password");
			String defaultApplication = getParameter("defaultApplication");
			
			getContentPane().setLayout(new BorderLayout());
			
			DefaultDesktop desktop = new DefaultDesktop(projectName, new URLComponentCenter(getCodeBase().toString()));
			
			try{
				if(connectionString!=null && userId!=null && password!=null){
					Thread.currentThread().getContextClassLoader().loadClass("oracle.jdbc.driver.OracleDriver");			
					desktop.setConnection(DriverManager.getConnection(connectionString, userId, password));
					System.out.println("connected to database");
				}
		
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(defaultApplication!=null){
				desktop.launchApplication(defaultApplication);
			}	
					
			getContentPane().add("Center", desktop);
		}
				
		public static void main(String[] args){
			DefaultJApplet applet = new DefaultJApplet(){
				public URL getCodeBase(){
					try{
						return new URL("http://localhost");
					}catch(Exception e){
						return null;
					}
				}
				public String getParameter(String name){
					java.util.Properties prop = new java.util.Properties();
					prop.setProperty("projectName", "legacyERP");
					
					return prop.getProperty(name, null);
				}
			};
			
			applet.init();
			
			JFrame frm = new JFrame();
			frm.getContentPane().add(applet);
			frm.show();			
		}
}
