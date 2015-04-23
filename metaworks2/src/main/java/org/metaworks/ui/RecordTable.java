package org.metaworks.ui;

import org.metaworks.*;

import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class RecordTable extends JTable {
	
	RecordTableModel recTableModel;
	
	public RecordTable(Type dbTable, String[] columns){
		super();
		recTableModel=new RecordTableModel(dbTable, columns);
		
		setModel(recTableModel);
		
		///////  선택시 액션
/*		recTableModel.addMouseListener(new MouseAdapter() {			
		    public void valueChanged(ListSelectionEvent e) {
		        //Ignore extra messages.
		        if (e.getValueIsAdjusting()) return;
				getSelectedRecord();
		        }
		    }
		    
		);*/
		
	}
	
	public Instance getSelectedRecord(){
		ListSelectionModel selModel = getSelectionModel();
		int selIndex=selModel.getMinSelectionIndex();
		
		return ((RecordTableModel)getModel()).getRecord(selIndex);
	}
	
	public JScrollPane getVisiblePanel(){
		
		
		return new JScrollPane(this);
	}		
	
}