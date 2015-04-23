package org.metaworks.inputter;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * @author Jinyoung Jang
 */
public abstract class PickerInput extends TextInput {
	JTextField tf;
	Picker picker;
	JButton selectButton;
	
	boolean isEditable = false;
		public boolean isEditable() {
			return isEditable;
		}	
		public void setEditable(boolean editable){
			JTextField tf = (JTextField)getValueComponent();
			if(tf!=null)
				tf.setEditable(editable);
			
			isEditable = editable;
		}

	boolean isAbsoluteCreatePicker = false;
		public boolean isAbsoluteCreatePicker() {
			return isAbsoluteCreatePicker;
		}
		public void setAbsoluteCreatePicker(boolean isAbsoluteCreatePicker) {
			this.isAbsoluteCreatePicker = isAbsoluteCreatePicker;
		}
		
	public Component getNewComponent() {
		tf = (JTextField)super.getNewComponent();
			
		JPanel panel = new JPanel(new BorderLayout());
		selectButton = new JButton("...");
		selectButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				try{
					//Picker picker=null;
					if(isAbsoluteCreatePicker()) picker = getNewPicker();	
					else picker = getPicker();	
					
					picker.setValue(getValue());
					
					//locate the component below to the button
					Component pickerComp = picker.getComponent();
					if(pickerComp!=null){	
						Dimension screenSize =
					          Toolkit.getDefaultToolkit().getScreenSize();
						
						Point btnLocationOnScreen = selectButton.getLocationOnScreen();
						Dimension btnSize = selectButton.getSize(); 
						
						int y = (int)(btnLocationOnScreen.y + btnSize.height);										
						int x = (int)(btnLocationOnScreen.x + btnSize.width - pickerComp.getSize().width);
						
						if(y + pickerComp.getHeight()>screenSize.getHeight()-20){
							pickerComp.setLocation(x, y - pickerComp.getHeight() - btnSize.height);
						}else{
							pickerComp.setLocation(x, y);
						}
					}
					
					if(picker.showPicker())
						onSelect();
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});			
		
		panel.add("Center", tf);
		panel.add("East", selectButton);
		
		tf.addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent arg0) {
				if(isEditable()){
					picker.setValue(null);
					PickerInput.this.onValueChanged();
				}
			}

			public void keyReleased(KeyEvent arg0) {
				keyPressed(null);				
			}

			public void keyTyped(KeyEvent arg0) {
				keyPressed(null);				
			}
			
		});
		

		tf.setEditable(isEditable());
			
		return panel;	
	}
	
	public abstract Picker getNewPicker();
	
	public Picker getPicker(){
		if(picker==null)
			picker = getNewPicker();
					
		return picker;
	}

	public Object getValue() {
		Object pickerValue = getPicker().getValue();
		
		if(pickerValue!=null) return pickerValue;
		else return (tf).getText();
	}

	public void setValue(Object obj) {
		getPicker().setValue(obj);
		if(obj!=null)
			super.setValue(obj.toString());
	}
	
	public Component getValueComponent() {
		return tf;
	}
	
	protected void onSelect(){
		setValue(picker.getValue());		
		onValueChanged();
	}

	public boolean isEligibleType(Class type) {
		return true;
	}

}
