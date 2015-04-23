package org.metaworks;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.event.*;
import org.metaworks.ui.RecordTableCellEditor;

public class GridApplication extends DefaultApplication{

	public GridApplication(Type table){
		super(table);
	}

	public JTable createUITable(){
		tableModel= createRecordTableModel();
		uiTable = new GridTable(tableModel, getType());
		
		uiTable.addMouseListener(
			new MouseAdapter(){
					public void mouseClicked(MouseEvent me){
						int iClickCount = me.getClickCount();
						
/*						if(iClickCount==0){
							onClick(new ActionEvent(GridApplication.this, 0, "clicked"));
						}else
						if(iClickCount==2){
							onDoubleClick(new ActionEvent(GridApplication.this, 1, "double clicked"));
						}else
				   			maybeShowPopup(me);	
*/
						if(iClickCount==2)
							open();
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
								501,						// Event type
								e.getWhen(),
								MouseEvent.BUTTON1_MASK,	// Left Click
								e.getX(),
								e.getY(), 
								1,							// Click Count
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
	
	class GridTable extends JTable{
		
		boolean bInitialized = false;
		Type table;
		
		RecordTableCellEditor[] cellEditors;
			public RecordTableCellEditor[] getCellEditors() {
				return cellEditors;
			}
	
			public void setCellEditors(RecordTableCellEditor[] editors) {
				cellEditors = editors;
			}
		boolean[] isCellEditable;
			public boolean[] getIsCellEditable() {
				return isCellEditable;
			}
	
			public void setIsCellEditable(boolean[] bs) {
				isCellEditable = bs;
			}
			
		public GridTable(TableModel tm, Type table){
			super(tm);
			this.table = table;
		}
		
		public void initialize(){
			FieldDescriptor[] fieldDescriptors = table.getFieldDescriptors();
			cellEditors = new RecordTableCellEditor[fieldDescriptors.length];
			isCellEditable = new boolean[fieldDescriptors.length];
			for(int i=0; i<cellEditors.length; i++){
				if(fieldDescriptors[i].isUpdatable()){
					isCellEditable[i] = true;
					cellEditors[i] = new RecordTableCellEditor(fieldDescriptors[i]);
				}
				else{
					isCellEditable[i] = false;
				}
			}
		}
		
		public boolean isCellEditable(int row, int col){
			if(!bInitialized){
				initialize();
				bInitialized = true;
			}
			
			return false;//isCellEditable[col];
		}
	   
		public TableCellEditor getCellEditor(int row, int column) {			
			if(!isCellEditable[column]) return null;
			
			cellEditors[column].getInputter().setValue(tableModel.getValueAt(row, column));
			return cellEditors[column];
		}
		
	}

}