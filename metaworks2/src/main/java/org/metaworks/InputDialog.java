package org.metaworks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InputDialog extends JDialog{

	InputForm inputForm = null;
	JPanel toolBar = null;
	Frame owner;
	
	Instance record;
		public Instance getInstance() {
			return record;
		}
		public void setInstance(Instance record) {
			this.record = record;
		}
	
	public InputDialog(InputForm thisForm, String saveTitle, String updateTitle, String cancelTitle, String windowTitle, Frame owner){				
		super(owner, windowTitle, true);
		
		inputForm = thisForm;
		this.owner = owner;
		
		initialize(saveTitle, updateTitle, cancelTitle);
		
		//final InputDialog finalThis = this;
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				dispose();
			}
		});
		
	/*	inputForm.addComponentListener(new ComponentAdapter(){

			public void componentResized(ComponentEvent e) {
				Runnable packAgain = new Runnable(){
					public void run() {
						InputDialog.this.pack();
					}
				};
				
				SwingUtilities.invokeLater(packAgain);
			}
			
		});*/
	}
	
	public InputDialog(InputForm thisForm, Frame owner){
		this(thisForm, null, null, null, "New "+thisForm.getType().getTitle(), owner);
	}
	public InputDialog(org.metaworks.Type table, Frame owner){
		this(new InputForm(table), owner);		
	}
	public InputDialog(org.metaworks.Type table){
		this(table, null);
	}

	
	
////////////// member accessors ////////////////
	public InputForm getInputForm(){
		return inputForm;
	}
	
	public void setInputForm(InputForm inputForm){
		this.inputForm = inputForm;
	}
	
////////////// private methods ////////////////

			
	public static JPanel createToolBar(final InputForm thisForm, final InputDialog thisDialog, String saveTitle, String updateTitle, String cancelTitle){
		
		if(saveTitle==null) saveTitle = "Confirm";
		if(updateTitle==null) updateTitle = "Update";
		if(cancelTitle==null) cancelTitle = "Cancel";
	
		final JButton button;
		
		if(thisForm.instance==null){
			button = new JButton(saveTitle);
			
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					try{
						Instance rec;
						(rec=thisForm.getInstance()).save();
						thisForm.setInstance(null);
						if(thisDialog!=null){
							thisDialog.setInstance(rec);
							thisDialog.onSaveOK(rec); 
						}else{
							if(thisForm!=null){
								thisForm.onSaveOK(rec, null);
							}
						}
	
					}catch(Exception e){
						e.printStackTrace();
						thisDialog.onSaveFailed(e);
					}
				}
			});
		}
		else {
			button = new JButton(updateTitle);
			
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					try{
						Instance rec;
						
						if(thisForm.getType().isFullKeyMode())
							(rec=thisForm.getInstance()).update(thisForm.getSourceInstance());
						else
							(rec=thisForm.getInstance()).update();
							
						thisForm.setModified(false);
						button.setEnabled(false);

						if(thisDialog!=null) thisDialog.onSaveOK(rec);
						else{
							if(thisForm!=null){
								thisForm.onSaveOK(rec, null);
							}
						}
					}catch(Exception e){
						e.printStackTrace();
						if(thisDialog!=null) thisDialog.onSaveFailed(e);
					}
				}
			});
		}
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(button);
		
		JButton cancelButton = new JButton(cancelTitle);
			cancelButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(thisDialog!=null) thisDialog.dispose();
				}
			});		
		buttonPanel.add(cancelButton);		
		
		thisForm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				button.setEnabled(thisForm.isModified());
			}
		});
		//button.setEnabled(thisForm.isModified());
		
		return buttonPanel;
	}
	
	public JPanel getToolBar(){
		if(toolBar==null) toolBar = createToolBar(getInputForm(), this, null, null, null);
		
		return toolBar;
	}

	public void initialize(String saveTitle, String updateTitle, String cancelTitle){
		
		JPanel panel = createPanel(getInputForm(), this, saveTitle, updateTitle, cancelTitle);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel);

		pack();
		setLocationRelativeTo(owner);
	}
	
	static public JPanel createPanel(InputForm thisForm, InputDialog thisDialog, String saveTitle, String updateTitle, String cancelTitle){
		
		if(thisForm.instance != null)
			thisForm.setInstance(thisForm.instance);

		JScrollPane scroll = new JScrollPane(thisForm);
		
		JPanel panel = new JPanel();
		
		panel.setLayout(new BorderLayout());
		panel.add("Center", scroll);
		panel.add("South", createToolBar(thisForm, thisDialog, saveTitle, updateTitle, cancelTitle));
		
		return panel;
	}	
//////////////////// overrides ///////////////////////

	public void dispose(){
		onCancel();
		
		InputForm thisForm = getInputForm();
		thisForm.dispose();
			
		super.dispose();
	}	
	
//////////////////// handler base catridges ////////////////////////

	public void onSaveOK(Instance rec){
		getInputForm().onSaveOK(rec, this);
			
		//It won't dispose the dialog but clear the inputform instead. 
		getInputForm().setInstance(getInputForm().getType().createInstance());
	}
	
	public void onSaveFailed(Exception e){
		getInputForm().onSaveFailed(e, this);
	}

	public void onCancel(){
		getInputForm().onCancel();
	}	

	public void pack(){
		super.pack();
				
		Dimension dim = getSize();
				
		setSize(Math.min(800, (int)dim.getWidth() + 50), Math.min(500, (int)dim.getHeight() + 50));
	}
	

}
