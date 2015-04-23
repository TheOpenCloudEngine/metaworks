package org.metaworks;

import org.metaworks.ui.*;
import org.metaworks.inputter.*;
import org.metaworks.*;

import java.sql.Connection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class AppliedTable extends AbstractAppliedTable{

	JFrame owner;
	JButton newRecordButton;
	RecordTableModel tableModel;
	JPanel panel=null;
	JTable uiTable=null;
	JPopupMenu popupMenu = null;
	
	
	// ui settings
	
	boolean bPopupEnabled=true, bDoubleClickOpenEnabled=true;
	
	public AppliedTable(){
		super();
	}
	public AppliedTable(String tableName){
		super(tableName);
	}

	public AppliedTable(String tableName, FieldDescriptor fields[]/*, ReferenceTable refTables[]*/){
		super(tableName, fields);
	}

	public AppliedTable(String tableName, FieldDescriptor fields[]/*, ReferenceTable refTables[]*/, Connection conn){
		super(tableName, fields, conn);
	}	
	
	// accessors
	
	public boolean isPopupEnabled(){
		return bPopupEnabled;
	}
	public void setPopupEnabled(boolean b){
		bPopupEnabled = b;
	}
	public boolean isDoubleClickOpenEnabled(){
		return bDoubleClickOpenEnabled;
	}
	public void setDoubleClickOpenEnabled(boolean b){
		bDoubleClickOpenEnabled = b;
	}

	
	// operations

	public JTable createUITable(){
		tableModel= createRecordTableModel();
		uiTable = new JTable(tableModel);
		
		uiTable.addMouseListener(
			new MouseAdapter(){
				public void mouseClicked(MouseEvent me){
					int iClickCount = me.getClickCount();
					
					if(iClickCount==0){
						onClick(new ActionEvent(AppliedTable.this, 0, "clicked"));
					}else
					if(iClickCount==2){
						onDoubleClick(new ActionEvent(AppliedTable.this, 1, "double clicked"));
					}else
			    			maybeShowPopup(me);	
			    	}
		
			        public void mouseReleased(MouseEvent e) {
			            maybeShowPopup(e);
			        }
	
			        private void maybeShowPopup(MouseEvent e) {
			            if(isPopupEnabled() && e.isPopupTrigger()) {
			            	
			            	JTable source = (JTable)e.getSource();
			            	
			            	/// 오른쪽 클릭도 왼쪽 클릭처럼.
			                MouseEvent newME = new MouseEvent(
						source, 
						501,		// Event type
						e.getWhen(),
						MouseEvent.BUTTON1_MASK,	// Left Click
						e.getX(),
						e.getY(), 
						1,	// Click Count
		                                false
		                        );
		                        source.dispatchEvent(newME);
					source.repaint();
					source.validate();         	
			            	
			                getPopupMenu().show(
			                	e.getComponent(),
			                        e.getX(),
			                        e.getY()
			                );
			            }
				}
			}
		);
		
		return uiTable;
	}
	
	public JTable getUITable(){
		if(uiTable == null) uiTable = createUITable();
		
		return uiTable;
	}
	
	public RecordTableModel createRecordTableModel(){
		return new RecordTableModel(this);
	}
	
	public RecordTableModel getRecordTableModel(){
		return (RecordTableModel)getUITable().getModel();
	}
	
	public int getSelectedIndex(){
		int selectedRow=getUITable().getSelectionModel().getMinSelectionIndex();
		return selectedRow;
	}		
	
	public Instance getSelectedRecord(){
		return getRecordTableModel().getRecord(getSelectedIndex());
	}
	public Instance[] getRecords(){
		return getRecordTableModel().getRecords();
	}
	
	public void newRecord(){
		InputForm form = new InputForm(this){
			public void onSaveOK(Instance rec, JDialog dialog){
				addRecord(new Instance(rec));
			}
		};
		createNewDialog(form).setVisible(true);
	}
	
	public JDialog createNewDialog(InputForm form){
//		return form.getInputDialog(getOwner(), null, "저장", "수정", "새 레코드 작성");
		return new InputDialog(form, getOwner());
	}
	public JDialog createOpenDialog(InputForm form){
//		return form.getInputDialog(getOwner(), null, "저장", "수정", "레코드 수정");
		return createNewDialog(form);
	}
	
	public JPopupMenu createPopupMenu(){
		
	    	final JPopupMenu popup = new JPopupMenu();
	    	
		        JMenuItem menuItem;
		        menuItem = new JMenuItem("열기");
			        menuItem.addActionListener(new ActionListener(){
			        	public void actionPerformed(ActionEvent e){
			        		open();
			        	}
			        });
        	        popup.add(menuItem);
        	        
		        menuItem = new JMenuItem("추가");
			        menuItem.addActionListener(new ActionListener(){
			        	public void actionPerformed(ActionEvent e){
			        		newRecord();
			        	}
			        });
        	        popup.add(menuItem);
        	        
/*		        final JMenuItem cutMenuItem = new JMenuItem("잘라내기");
			        cutMenuItem.addActionListener(new ActionListener(){
			        	public void actionPerformed(ActionEvent e){
			        		cut();
			        	}
			        });
        	        popup.add(cutMenuItem);	
        	        
		        final JMenuItem pasteMenuItem = new JMenuItem("붙여넣기");
			        pasteMenuItem.addActionListener(new ActionListener(){
			        	public void actionPerformed(ActionEvent e){
			        		paste();
			        	}
			        });
        	        popup.add(pasteMenuItem);*/	
        	        
		        menuItem = new JMenuItem("삭제");
			        menuItem.addActionListener(new ActionListener(){
			        	public void actionPerformed(ActionEvent e){
			        		delete();
			        	}
			        });
 
	        popup.add(menuItem);
	        
	        return popup;
	}
	
	public JPopupMenu getPopupMenu(){
		if(popupMenu == null) popupMenu = createPopupMenu();
		
		return popupMenu;
	}
	
	public void setOwner(JFrame frame){
		owner = frame;
	}
	
	public JFrame getOwner(){
		return owner;
	}
	
	public void addRecord(Instance rec){
		getRecordTableModel().addRecord(rec);
	}
	public void addRecords(Instance [] recs){
		for(int i=0; i<recs.length; i++)
			addRecord(recs[i]);
	}
	
	
	
	// UI
	
	public JButton createNewRecordButton(String title){
		newRecordButton = new JButton(title);
		newRecordButton.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					newRecord();
				}
			}
		);
		
		return newRecordButton;
	}
	public JButton createNewRecordButton(){
		return createNewRecordButton("새 레코드");
	}
	
	public JButton createRemoveButton(String title){
		newRecordButton = new JButton(title);
		newRecordButton.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					delete();
				}
			}
		);
		
		return newRecordButton;
	}
	public JButton createRemoveButton(){
		return createRemoveButton("삭제");
	}
	
	public JPanel createPanel(){
		panel = new JPanel(new BorderLayout());
		
		panel.add(new JScrollPane(createUITable()), BorderLayout.CENTER);
		
		JPanel menuPanel = new JPanel(new FlowLayout());
			Component newBtn =	createNewRecordButton();
			Component removeBtn = createRemoveButton();
			if( newBtn != null)
				menuPanel.add( newBtn);
			if( removeBtn != null)
				menuPanel.add(removeBtn);	
		panel.add(menuPanel, BorderLayout.NORTH);
		
		
		return panel;
	}
	
	public JPanel getPanel(){
		return panel;
	}
	
	public JFrame createFrame(String title){
		if(title==null)
			title = getTitle() + " Application";
			
		setOwner(new JFrame(title));
		
		owner.getContentPane().add(createPanel());
		
		owner.pack();
		
		return owner;
	}	
	public JFrame createFrame(){
		return createFrame(null);
	}
		
	public void runDefaultApplication(){
		createFrame().setVisible(true);
	}
	
/////////////// user events ///////////////

	public void onClick(ActionEvent eo){
	}
	
	public void onDoubleClick(ActionEvent eo){
		if(isDoubleClickOpenEnabled()) open();
	}

	public void onRightClick(ActionEvent eo){
	}

	public void onDeleteButtonClick(ActionEvent eo){
		delete();
	}
	
	public void onNewRecordButtonClick(ActionEvent eo){
		newRecord();
	}
		

/////////////// commands //////////////////
	
	public void setSQL(String sqlStmt) throws Exception{
		setSQL(sqlStmt, null);
	}
	public void setSQL(String sqlStmt, String[] titles) throws Exception{
		
		Instance [] records = findByFullSQL(sqlStmt, titles);
		
		clearAll();
		
		addRecords(records);
	}
	
	public void open(){
		Instance rec;
		final int sel = getSelectedIndex();
		if((rec=getSelectedRecord())==null) return;
		
		rec.printProperties();
		
		InputForm form = new InputForm(this, rec){
			public void onSaveOK(Instance rec, JDialog dialog){
				getRecordTableModel().updateRowAt(rec, sel);
				
				dialog.dispose();
			}
		};
		createOpenDialog(form).setVisible(true);
	}

			

	public void cut(){}
	public void paste(){
				
	}
	public void delete(){
		Instance rec = getSelectedRecord();
		
		try{
			rec.delete();
			RecordTableModel tm = getRecordTableModel();
			
			tm.removeRowAt(getSelectedIndex());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void clearAll(){
		RecordTableModel tm = getRecordTableModel();
		if(tm != null)
			tm.clearAll();

		tm.setColumns(getFieldNames());
	}

	public static void main(String args[]){
		AppliedTable table = new AppliedTable("ADDRESSBOOK");
		
		try{
			Thread.currentThread().getContextClassLoader().loadClass("oracle.jdbc.driver.OracleDriver");
			table.setConnection("jdbc:oracle:thin:@203.241.246.223:1521:ora90", "scott", "tiger");
			//Thread.currentThread().getContextClassLoader().loadClass("sun.jdbc.odbc.JdbcOdbcDriver");
			//table.setConnection("jdbc:oracle:thin:@165.186.52.2:1526:iman5", "infodba", "infodba");
			
			
			
			
			//table.setFieldDescriptor( new FieldDescriptor("USERID"));
			//table.setFieldDescriptor( new FieldDescriptor("USERNAME"));
			//Record records [] = table.find("");	
			//table.setName("ADDRESSBOOK");
			table.setSQL("SELECT * FROM ADDRESSBOOK");
			table.runDefaultApplication();
				
			Instance records [] = table.findByFullSQL("SELECT * FROM ADDRESSBOOK");
			
			
			//table.getFieldDescriptor("GROUPID").setInputter(new ReferenceInput( table.getConnection(), "SELECT * FROM AddressGroup"));
			
			
			table.addRecords(records);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}


}
