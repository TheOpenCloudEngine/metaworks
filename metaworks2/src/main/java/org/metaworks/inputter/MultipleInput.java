package org.metaworks.inputter;

import org.metaworks.*;
import org.metaworks.ui.RecordTableCellEditor;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MultipleInput extends AbstractComponentInputter implements InstanceSensitiveInputter{
	
	final static String SHOW_BUTTONS = "show buttons"; 
	
	Type table;
	
		public Type getTable(){
			return table;
		}

	Properties options;
	
	public MultipleInput(Type table){
		this(table, null);
	}

	public MultipleInput(Type table, Properties options){
		this.table = table;
		this.options = options; 
	}
	
	public Object getValue(){
		return ((ArrayInputComponent)getComponent()).getApplication().getInstances();
	}
	
	public void setValue(Object data){
		DefaultApplication app = ((ArrayInputComponent)getComponent()).getApplication();
		app.clearAll();			
		
		if(data==null){
			return;
		}
		
		app.addInstances((Instance[])data);
	}
	
	public Component getNewComponent(){
		ArrayInputComponent comp =  new ArrayInputComponent();
   		
   		/*comp.getApplication().getUITable().getModel().addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent e) {
				
				MultipleInput.this.fireActionEvent(new ActionEvent(MultipleInput.this, 0, "table model changed"));
			}
   			
   		});*/
		
		comp.getApplication().getUITable().addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				
			}
			public void mouseReleased(MouseEvent e) {
				JTable tbl = (JTable)e.getSource();
				MultipleInput.this.fireActionEvent(new ActionEvent(tbl, 0, "table model changed"));
				
			}
		});
   		
   		comp.setPreferredSize(new Dimension(400,180));
   		return comp;
	}
	
	private class ArrayInputComponent extends JPanel{
		JComponent comp=null;
		DefaultApplication application;
		
			public DefaultApplication getApplication(){
				return application;
			}
				
			public void setApplication(DefaultApplication value){
				application = value;
			}
		

		ArrayInputComponent(){
						
			final DefaultApplication t = new GridApplication(table){						
				public void onDoubleClick(ActionEvent e){
					AppliedTable tt = (AppliedTable)e.getSource();
					Instance rec= tt.getSelectedRecord();
					setValue(rec);
					
				//	postDown();
				}
				
				public JTable createUITable(){
					JTable table = super.createUITable();
					table.setPreferredScrollableViewportSize(new Dimension(200, 150));
					
					return table;
				}
				
				public JPanel createMenuPanel(){					
					if(options!=null && options.getProperty(SHOW_BUTTONS, "yes").equals("no"))
						return new JPanel();
					else
						return super.createMenuPanel();
				}
			};	
			
			setLayout(new BorderLayout());	
			
			add("Center", t.createPanel());			
			setApplication(t);
		}
		
		/*public void initPopupWindow(){
			getPanel().add("Center", comp);
			setPopupTitle("Select");
		}*/
	}
	
	
	public static void main(String[] args){
		AppliedTable a = new AppliedTable(
			"test",
			new FieldDescriptor[]{
				new FieldDescriptor("test field")
			}
		);

		a.getFieldDescriptor("test field").setInputter(
		
					new MultipleInput(
						new Type("test2", new FieldDescriptor[]{new FieldDescriptor("contents")})
					)
		);
		
		a.runDefaultApplication();
	}
	

	public void setInstance(Instance rec, String fieldName) {

		//forward instance setting event.
		FieldDescriptor[] fieldDescriptors = getTable().getFieldDescriptors();
		for(int i=0; i<fieldDescriptors.length; i++){
			
			if(fieldDescriptors[i].getInputter() instanceof InstanceSensitiveInputter){
				((InstanceSensitiveInputter)fieldDescriptors[i].getInputter()).setInstance(rec, fieldName);
			}
		}
		
	}
				
}


