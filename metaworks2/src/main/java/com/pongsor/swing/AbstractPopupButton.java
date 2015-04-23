package com.pongsor.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class AbstractPopupButton extends JButton implements ActionListener{

	JDialog popupFrame;
	
		public JDialog getPopupFrame(){
			return popupFrame;
		}
			
		public void setPopupFrame(JDialog value){
			popupFrame = value;
		}
		
	JPanel mainPanel;
	
		public JPanel getPanel(){
			return mainPanel;
		}
			
		public void setPanel(JPanel value){
			mainPanel = value;
		}

	boolean bInit = false;
	
	public AbstractPopupButton(String title){
		super(title);
		addActionListener(this);
		setPanel(new JPanel(new BorderLayout()));
	}
	public AbstractPopupButton(){
		this(null);
	}
	
	abstract public void initPopupWindow();
	
	public void postDown(){
		try{
			getPopupFrame().dispose();
		}catch(Exception e){}
	}
	
	public void postUp(){
		if(!bInit) {
			initPopupWindow();
			bInit = true;
		}
		
		JDialog frm = new JDialog();
		setPopupFrame(frm);
		frm.getContentPane().setLayout(new BorderLayout());
		frm.getContentPane().add("Center", getPanel());
		frm.pack();
		Point p = getLocationOnScreen();
		frm.setLocation(p);
		frm.show();
		
		frm.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				postDown();
			}
		});
	}
	
	public void setPopupTitle(String txt){
		setText(txt);
	}
	
	public void actionPerformed(ActionEvent e){
		postUp();
	}
}