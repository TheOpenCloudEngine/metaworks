package org.metaworks.inputter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;



public class FileInput extends AbstractComponentInputter{

	JTextField path;

	public FileInput(){
		super();
	}
	
/////////// implemetations //////////////

	public Object getValue(){
		return path.getText();
	}
	
	public void setValue(Object obj){
		path.setText("" + obj);	
	}

	public Component getNewComponent(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		path = new JTextField(15);
		
		final JTextField finalPath = path;
		final JButton importBtn = new JButton("Browse");
		
		importBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
			        JFileChooser fc = new JFileChooser();
			        fc.showOpenDialog(null);
			        File f = fc.getSelectedFile();
			        
			        finalPath.setText(f.getAbsolutePath());
			}
		});			
		
		panel.add(path);
		panel.add(importBtn);
		return panel;
	}
	
}
