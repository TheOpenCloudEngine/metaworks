package org.metaworks.tutorial;

import org.metaworks.*;
import com.pongsor.swing.*;
import org.metaworks.inputter.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class sqller extends AppliedTable{

	JTextArea dbMsgTextArea = new JTextArea(){
			public void append(String str){
				super.append(str);
				
				Dimension d = getSize();
				
				scrollRectToVisible(new Rectangle(0,(int)d.getHeight(), 1, (int)d.getHeight()+1));
			}	
	};
	
	Thread currThread=null;
	Instance oldConfig = null;
	

	public sqller(String name){
		super(name);
	}
		
	public JPanel createPanel(){
				JPanel ovPanel = new JPanel(new BorderLayout());

				final JTextArea ta= new JTextArea();
				JButton bt = new JButton("Do SQL!");
				
				bt.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
					
						Thread thread=
						new Thread(){
							public void run(){
								dbMsgTextArea.append("Wait for database to response... \n");
							
								try{
									setSQL(ta.getText());
									
									dbMsgTextArea.append(getRecordTableModel().getRowCount()+" row(s) are selected\n");
								}catch(Exception ex){
									ex.printStackTrace();
							
									dbMsgTextArea.append("-----------\n"+ex.getMessage()+"\n");
								}
								
								currThread = null;
							}
						};
						
						thread.start();
						
						currThread=thread;
						
					}
				});
				
				JPanel commandPanel = new JPanel(new BorderLayout());
				commandPanel.add("Center", new JScrollPane(ta));
				
				
				JButton stopBtn = new JButton("stop");
				stopBtn.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent a){
						if(currThread!=null) currThread.stop();
					}
				});		
						
				JButton configBtn = new JButton("config");
				configBtn.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent a){
						
						InputDialog dialog = new InputDialog(
							new InputForm(oldConfig.getType(), oldConfig){
								public void onSaveOK(Instance newConfig){
									oldConfig.setFields(newConfig);
								}
							},
							null
						);
						dialog.setVisible(true);
						
					}
				});				
				
				JPanel controlPanel = new JPanel(new BorderLayout());
				controlPanel.add("North", bt);
				controlPanel.add("Center", stopBtn);	
				controlPanel.add("South", configBtn);
				
				
				commandPanel.add("East", controlPanel);
				
				ovPanel.add("Center", new JSplitPane(JSplitPane.VERTICAL_SPLIT, super.createPanel(), new JSplitPane(JSplitPane.VERTICAL_SPLIT, commandPanel, new JScrollPane(dbMsgTextArea))));

				getUITable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
							
				return ovPanel;
	}
	
	public void setConnection(String connStr, String uid, String pwd) throws Exception {
		Connection conn=getConnection();
	
		if(conn!=null){
			System.out.println("기존 것 닫기");
			System.setProperty("jdbc.drivers", "oracle.jdbc.driver.OracleDriver");
	
			conn.close();
		}
	
		super.setConnection(connStr, uid, pwd);
	
		oldConfig = new Instance(new ConfigTable());
		oldConfig.put("tableName", sqller.this.getName());
		oldConfig.put("connStr", connStr);
		oldConfig.put("uid", uid);
		oldConfig.put("pwd", pwd);
	}
	
	private class ConfigTable extends Type{
	
		ConfigTable(){
			super(
				"Table 세팅",
				new FieldDescriptor[]{
					new FieldDescriptor("tableName"),
					new FieldDescriptor("connStr"),
					new FieldDescriptor("uid"),
					new FieldDescriptor("pwd")
				}
			);
		}

		public void save(Instance rec) throws Exception{
			sqller.this.setName(""+rec.get("tableName"));
			//sqller.this.setConnection(""+get("connStr"), ""+get("uid"), ""+get("pwd"));
		}
							
		public void update(Instance rec) throws Exception{
			save(rec);
		}
	}

	public static void main(String args[]){
     		System.setProperty("jdbc.drivers", "oracle.jdbc.driver.OracleDriver");
		System.getProperties().list(System.out);
		
		final sqller table = new sqller("Do SQLLer");
		
		try{
		
			table.setConnection("jdbc:oracle:thin:@", "id", "pwd");
			
			table.setFullKeyMode(true);
			
			table.runDefaultApplication();

			table.getOwner().addWindowListener(new WindowAdapter(){
					public void windowClosing(WindowEvent e){
						try{
							table.getConnection().close();
						}catch(Exception ex){}
						System.exit(0);
					}
			});

			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
