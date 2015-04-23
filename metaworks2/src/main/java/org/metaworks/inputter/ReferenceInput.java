package org.metaworks.inputter;

import org.metaworks.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import com.pongsor.swing.*;
import com.pongsor.swing.Painter;

import java.sql.*;

public class ReferenceInput extends AbstractComponentInputter{
	
	Connection conn;
	String sqlStmt;
	String key;
	
	Instance selectedRec=null;
	
	public ReferenceInput(Connection con, String sqlStmt){
		this.conn = con;
		this.sqlStmt = sqlStmt;
//		this.key = key;
	}
	
	public Object getValue(){
		return selectedRec;
	}
	
	public void setValue(Object data){
		if(data==null) return;
		
		selectedRec = (Instance)data;
	
		((AbstractPopupButton)getComponent()).setText(""+data);
	}
	
	public Component getNewComponent(){
		if(isMandatory())
			return new ReferenceInputComponent("Not Set"){
				public void paint(Graphics g){
           				super.paint(g);
           				Painter.paintIsRequired(this, g);
           			}
        		};
		else
		      	return new ReferenceInputComponent("Not Set");      		
	}
	
	private class ReferenceInputComponent extends AbstractPopupButton{
	
		JComponent comp=null;

		ReferenceInputComponent(String msg){
	
			super(msg);
						
			final AppliedTable t = new AppliedTable(){						
					public void onDoubleClick(ActionEvent e){
						AppliedTable tt = (AppliedTable)e.getSource();						
						Instance rec= tt.getSelectedRecord();						
						setValue(rec);
						
						postDown();
					}
					
					public JTable createUITable(){
						JTable table = super.createUITable();						
						table.setPreferredScrollableViewportSize(new Dimension(200, 150));
						
						return table;
					}
			};
								
			t.setConnection(conn);
				
			new Thread(){
						public void run(){
							String orgText = getText();
							
							setText("Loading...");
							setEnabled(false);
						
							try{
								Instance [] recs = t.findByFullSQL(sqlStmt);
								
								comp = new JScrollPane(t.getUITable());
				
								t.addRecords(recs);
							}catch(Exception ex){
								ex.printStackTrace();
							}
							
							setText(orgText);
							setEnabled(true);
						}
			}.start();				
		}
		
		public void initPopupWindow(){
			getPanel().add("Center", comp);
			setPopupTitle("Select");
		}
	}		    
}


