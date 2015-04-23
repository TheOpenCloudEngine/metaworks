	package org.metaworks.ui;
	
	import java.awt.*;
	import javax.swing.table.*;
	import javax.swing.*;
	import org.metaworks.*;
	import org.metaworks.inputter.*;
	
	
	
	public class RecordTableCellEditor extends DefaultCellEditor {				
		Inputter inputter;
			public Inputter getInputter(){
				return inputter;
			}
		
		public RecordTableCellEditor(FieldDescriptor fd) {
			super(new JCheckBox());
			
			this.inputter = fd.getInputter();
				                                
			editorComponent = (JComponent)inputter.getComponent();
			setClickCountToStart(2);
		}
				
		protected void fireEditingStopped() {
			super.fireEditingStopped();
	        }
				
	        public Object getCellEditorValue() {
			return inputter.getValue();
	        }
				
		public Component getTableCellEditorComponent(JTable table_, Object value,
						     boolean isSelected, int r, int c) {
						
			//return table.getFieldDescriptor(columnNames[column]).getInputter().getComponent();
			
			return editorComponent;
		}
	}