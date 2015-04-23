package org.metaworks;

import org.metaworks.ui.*;

import java.sql.Connection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class DefaultApplication extends AbstractApplication implements RecordListener{

	JFrame owner;
	JButton newRecordButton;
	RecordTableModel tableModel;
	JPanel panel=null;
	JTable uiTable=null;
	JPopupMenu popupMenu = null;
	Instance clipboard = null;
	
	JPanel menuPanel;
		public JPanel getMenuPanel(){
			return menuPanel;
		}
		public void setMenuPanel(JPanel value){
			menuPanel = value;
		}
	
	String title;
		public String getTitle(){
			return title;
		}
		public void setTitle(String value){
			title = value;
		}
	
	// ui settings
	
	boolean bPopupEnabled=true, bDoubleClickOpenEnabled=true;
	
	public DefaultApplication(Type table){
		super();
		
		setType(table);
	}	
	
	public DefaultApplication(){
		super();	
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
						onClick(new ActionEvent(DefaultApplication.this, 0, "clicked"));
					}else
					if(iClickCount==2){
						onDoubleClick(new ActionEvent(DefaultApplication.this, 1, "double clicked"));
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
		return new RecordTableModel(getType());
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
	public Instance[] getInstances(){
		return getRecordTableModel().getRecords();
	}
	
	public void newInstance(){
		InputForm form = new InputForm(getType()){
			public void onSaveOK(Instance rec, JDialog dialog){
				//review: referencing or cloning?
				Instance copy = (Instance)rec.clone();
				addInstance(copy);
			}
		};
		createNewDialog(form).show();
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
		        menuItem = new JMenuItem("Open");
			        menuItem.addActionListener(new ActionListener(){
			        	public void actionPerformed(ActionEvent e){
			        		open();
			        	}
			        });
        	        popup.add(menuItem);
        	        
		        menuItem = new JMenuItem("New");
			        menuItem.addActionListener(new ActionListener(){
			        	public void actionPerformed(ActionEvent e){
			        		newInstance();
			        	}
			        });
        	        popup.add(menuItem);
        	        
		        final JMenuItem cutMenuItem = new JMenuItem("Cut");
			        cutMenuItem.addActionListener(new ActionListener(){
			        	public void actionPerformed(ActionEvent e){
			        		cut();
			        	}
			        });
        	        popup.add(cutMenuItem);	
        	        
		        final JMenuItem pasteMenuItem = new JMenuItem("Paste");
			        pasteMenuItem.addActionListener(new ActionListener(){
			        	public void actionPerformed(ActionEvent e){
			        		paste();
			        	}
			        });
        	        popup.add(pasteMenuItem);	
        	        
		        menuItem = new JMenuItem("Remove");
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
	
	public void addInstance(Instance rec){
		getRecordTableModel().addRecord(rec);
	}
	public void addInstances(Instance [] recs){
		for(int i=0; i<recs.length; i++){
			addInstance(recs[i]);
			
		}
	}
		
	
	// UI
	
	public JButton createNewRecordButton(String title){
		newRecordButton = new JButton(title);
		newRecordButton.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					newInstance();
				}
			}
		);
		
		return newRecordButton;
	}
	public JButton createNewRecordButton(){
		return createNewRecordButton("New");
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
		return createRemoveButton("Remove");
	}
	
	public JPanel createPanel(){		
		if(getType() instanceof Flyweight) //for flyweight implementation
			setType((Type)((Flyweight)getType()).getActualObject());
		
		panel = new JPanel(new BorderLayout());		
		panel.add(new JScrollPane(createUITable()), BorderLayout.CENTER);
		
		JPanel menuPanel = createMenuPanel(); 
				
		panel.add(menuPanel, BorderLayout.NORTH);		
		setMenuPanel(menuPanel);		
		
		return panel;
	}
	
	public JPanel createMenuPanel(){
		JPanel menuPanel = new JPanel(new FlowLayout());
			Component newBtn = createNewRecordButton();
			Component removeBtn = createRemoveButton();
			if( newBtn != null)
				menuPanel.add(newBtn);
			if( removeBtn != null)
				menuPanel.add(removeBtn);
		return menuPanel;
	}
	
	public JPanel getPanel(){
		return panel;
	}
	
	public JFrame createFrame(String title){
		if(title==null)
			title = getType().getTitle() + " Application";
			
		setOwner(new JFrame(title));
		
		owner.getContentPane().add(createPanel());
		
		owner.pack();
		
		return owner;
	}	
	public JFrame createFrame(){
		return createFrame(getType().getName());
	}
		
	public void run(){
		try{
			setSQL("select * from " + getType().getName());
		}catch(Exception e){
			e.printStackTrace();	
		}
	}
	
	public void runFrame(){
		JFrame frm = createFrame();
		frm.setVisible(true);
		frm.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				onClose();
			}
		});
		
		run();
	}
	
	public void runDialog(JFrame owner){
		JDialog dlg = new JDialog(owner, getType().getTitle());
		setOwner(owner);		
		dlg.getContentPane().add( createPanel());
		
		dlg.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				onClose();
			}
		});
		
		//dlg.pack();
		dlg.setSize(400,300);
		dlg.setLocationRelativeTo(owner);		
		dlg.show();
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
		newInstance();
	}
		
	public void onClose(){		
	}

/////////////// commands //////////////////
	
	public void setSQL(String sqlStmt) throws Exception{
		setSQL(sqlStmt, null);
	}
	public void setSQL(final String sqlStmt, final String[] titles) throws Exception{
		
		final DefaultApplication fThis = this;
		
		clearAll();
		new Thread(){
			public void run(){
				try{
					getType().findByFullSQL(sqlStmt, titles, false, fThis);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
		
//		addRecords(records);
	}
	public void onAddRecord(Instance rec){
		addInstances(new Instance[]{rec});
	}
	
	public void open(){
		Instance rec;
		final int sel = getSelectedIndex();
		if((rec=getSelectedRecord())==null) return;
		
		InputForm form = new InputForm(getType(), rec){
			public void onSaveOK(Instance rec, JDialog dialog){
				getRecordTableModel().updateRowAt(rec, sel);
				
				dialog.dispose();
			}
		};
		createOpenDialog(form).setVisible(true);
	}

			

	public void cut(){
		clipboard = getSelectedRecord();
		delete();
	}
	
	public void paste(){
		if(clipboard == null) return;
		
		int index = getSelectedIndex();
		getRecordTableModel().insertRowAt(clipboard, index);				
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

		//tm.setColumns(getType().getFieldNames());
		tm.refreshColumns();
	}
	
	Connection connection;
	
		public Connection getConnection(){
			return connection;
		}
			
		public void setConnection(Connection value){
			getType().setConnection(value);
			connection = value;
		}

	public static void main(String args[]) throws Exception{		
		Type table = (Type)org.metaworks.component.MetaWorksComponentCenter.getInstance().getComponent(args[0]);		
		DefaultApplication defaultApplication = new DefaultApplication(table);
		defaultApplication.run();
	}


}
