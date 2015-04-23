package org.metaworks.ui;

import org.metaworks.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;

public class RecordTableModel extends AbstractTableModel {
        String[] columnNames;
        Type table;
        Vector records=new Vector();

	public RecordTableModel(Type table, String columns[]){
		this.table=table;
		setColumns(columns);	
	}
	
	public RecordTableModel(Type table){
		
		Vector fieldNames = new Vector();
		FieldDescriptor[] fds = table.getFieldDescriptors();
		for(int i=0; i<fds.length; i++){
			if(fds[i].getAttribute("hidden")==null && fds[i].getAttribute("hiddenInTable")==null){
				fieldNames.add(fds[i].getName());				
			}
		}
				
		String[] arrFieldNames = new String[fieldNames.size()];
		fieldNames.toArray(arrFieldNames);		
		
		//this(table, table.getFieldNames());
		this.table = table;
		setColumns(arrFieldNames);
		
	}
	
	
	public void setColumns(String [] columns){
		if(columns == null || columns.length==0){
			columns = table.getFieldNames();
		}
		
		columnNames = columns;
		fireTableStructureChanged();
		fireTableChanged(new TableModelEvent(this));
 	}
 	
	public void setColumns(){
		setColumns(null);
 	}
 	
 	public void refreshColumns(){
		if(columnNames == null || columnNames.length==0){
			setColumns();
		}
		setColumns(columnNames);
		
	} 		

        public int getColumnCount() {
/*        	if(table instanceof ObjectType && ((ObjectType)table).getClassType().getName().equals("org.uengine.kernel.ProcessVariable")){
        		System.out.println("zxxx");
        	}
*/
        	return columnNames.length;
        }
        
        public Class getColumnClass(int columnIndex) {
        	int type = table.getFieldDescriptor(columnNames [columnIndex]).getType();
        	
        	switch (type){
        		case 2:	return Integer.class;
        	}
        	
        	return String.class;
        }    	
        
        
        public int getRowCount() {
            return records.size();
        }

        public String getColumnName(int col) {
        	Object obj;
        	
        	if((obj=table.getFieldDescriptorTable().get(columnNames[col]))==null){
        		System.out.println("Column Name 이 잘못 지정되었습니다 : "+columnNames[col]);
        		return columnNames[col];
        	}
        	
            return ((FieldDescriptor)obj).getDisplayName();
        } 
        
        
        /*added*/ public String [] getRealColumnNamesArray(){
            return columnNames;
        }

        public Object getValueAt(int row, int col) {
			Object field = ((Instance)records.elementAt(row)).getFieldValue(columnNames[col]);
			if(field==null) return null;
        	int type = table.getFieldDescriptor(columnNames[col]).getType();
        	switch (type){
        		case 2:	try{
        				return (new Integer(""+field));
        			}catch(Exception e){
        				return null;
        			}
        	}
        	
			return field;
        	
            //return data;
        }
        
        public void addRow(Instance record){
    		records.add(record);
			fireTableRowsInserted(records.size()-1, records.size()-1);	
        }
        
		public void addRecord(Instance record){
			refreshColumns();
	        	
			addRow(record);
		}
	        
        public void addRecords(Instance rec[]){
        	for(int i=0; i<rec.length; i++)
        		addRecord(rec[i]);
        }
        
        public void clearAll(){
        	int iRowCount = records.size();
        	
        	records.clear();
        	
        	fireTableRowsDeleted(0, iRowCount);
        }
        
        public Vector getAllRecordVector(){
        	return records;
        }
        
        public Instance getRecord(int index){
        	return (Instance)records.elementAt(index);
        }
        
        public void removeRowAt(int i){
        	if(i<records.size()){        	
        		records.removeElementAt(i);
        		
        		fireTableRowsDeleted(i, i);
        	}else{
        		System.out.println("can't remove row : Removing index is out of range");
        	}
        }
        
        public void insertRowAt(Instance rec, int i){
        	if(i<records.size()){
        		records.insertElementAt(rec, i);
        		
        		fireTableRowsInserted(i, i);
        	}else
        	if(i==records.size()){
        		addRow(rec);
        	}else
        	{       		
        		System.out.println("can't insert row : inserting index is out of range");
        	}
        }
        
        public void updateRowAt(Instance rec, int i){
        	if(i<records.size()){        	
        		records.setElementAt(rec, i);
        		
        		fireTableRowsUpdated(i, i);
        	}else{
        		System.out.println("can't update row : index is out of range");
        	}
        }
	
	public Vector getRecordVector(){
		return records;
	}
	public Instance[] getRecords(){
		Instance recs[] = new Instance[records.size()];
		records.toArray(recs);
		
		return recs;
	}
		
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
         */

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
                 return false; 
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */      
         
        public void setValueAt(Object value, int row, int col) {
        	try{
        		((Instance)records.elementAt(row)).setFieldValue(columnNames[col], value);
        	}catch(Exception e){
        		System.out.println("RecordTableModel.setValueAt: 값을 세팅할 수 없습니다");
        	}
        }
}