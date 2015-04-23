package org.metaworks;

/**
 * 주어진 Table의 한 Row를 입력받을 수 있는 Form Panel (Swing JPanel)을 생성한다.  
 * 
 * 
 * @version: 
 * 1.0 2000/2/14 
 * 
 * @example 1) 폼 생성
 * <pre>
 * 	public static void main(String args[]) throws Exception{
 * 
 * 
 * 		Thread.currentThread().getContextClassLoader().loadClass("oracle.jdbc.driver.OracleDriver");
 * 																								// ~/orawin95/network/admin/tnsnames.ora file 참조.
 * 		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@165.186.52.29:1526:iman5", "infodba", "ckddnjs5");
 * 		
 * 		// table을 생성한다. db의 'create'문을 연상
 * 		Table px_part_newparts = new Table(
 * 			"px_part_newparts",	//  table명
 * 						//  column		title		type		iskey
 * 			new FieldDescriptor[]{
 * 				new FieldDescriptor("SEQNO", 		"순번", 	Types.INTEGER,	true),
 * 				new FieldDescriptor("description",	"설명"),
 * 				new FieldDescriptor("developmentdate",	"개발일정", 	Types.DATE),
 * 				new FieldDescriptor("DIVISION",		"사업부")
 * 			},
 * 			con
 * 		);
 * 		
 * 		Record [] rec = px_part_newparts.find("seqno=10000");
 * 		
 * 		if(rec.length> 0){
 * 			System.out.println("description = " + rec[0].get("description"));
 * 			System.out.println("seqno = "+rec[0].get("seqno"));
 * 			
 * 		}			
 * 			
 * 		final InputForm newForm = new InputForm(px_part_newparts, "10000");
 * 		
 * 		JFrame frame = new JFrame("test");
 * 		
 * 		frame.getContentPane().setLayout(new BorderLayout());
 * 		frame.getContentPane().add("Center", newForm);	// ◀◀◀◀◀◀
 * 		
 * 		JButton saveBtn = new JButton("저장");
 * 		frame.getContentPane().add("South", saveBtn);	// ◀◀◀◀◀◀
 * 
 * 		saveBtn.addActionListener(new ActionListener(){
 * 			public void actionPerformed(ActionEvent e){
 * 				try{
 * 					newForm.getRecord().save();
 * 				}catch(Exception ex){}
 * 			}
 * 		});
 * 		
 * 
 * 		frame.pack();
 * 		frame.setVisible(true);
 * 	}
 * </pre>	
 * @example 1) 다이얼 로그를 만들어 입력 폼 띄우기
 * <pre>
 * 		InputForm testForm2 = new InputForm(
 * 			new Table("test2",
 * 				new FieldDescriptor[]{
 * 					new FieldDescriptor("column1"),
 * 					new FieldDescriptor("column2"),
 * 					new FieldDescriptor("column3")
 * 				}
 * 			)
 * 		){
 * 			public void onSaveOK(Record rec, JDialog dialog){
 * 				com.ugsolutions.util.MessageBox.post("'"+ rec.get("column1") +"' 레코드가 저장되었습니다.", "저장완료", 1);
 * 			}
 * 			
 * 			public void onSaveFailed(Exception e, JDialog dialog){
 * 				com.ugsolutions.util.MessageBox.post("저장 실패 했습니다.\n 에러=" + e.getMessage(), "실패", 1);
 * 			}
 * 		};
 * 		testForm2.postInputDialog(frame);
 * </pre>
 * @author 장진영 
 * @See Tuple, Serialized Form
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.metaworks.ui.*;

import org.metaworks.inputter.*;


public class InputForm extends JPanel implements ActionListener{

	protected Type type;
		public void setType(Type type) {
			this.type = type;
		}
		public Type getType(){
			return type;
		}

	protected Instance instance=null;
	protected Map errLabels = new HashMap();
	boolean isModified = false;
		public boolean isModified() {
			return isModified;
		}
	
		public void setModified(boolean isModified) {
			this.isModified = isModified;
		}
		
	boolean sequenciallyTabOrder = true;
		public boolean isSequenciallyTabOrder() {
			return sequenciallyTabOrder;
		}
	
		public void setSequenciallyTabOrder(boolean sequenciallyTabOrder) {
			this.sequenciallyTabOrder = sequenciallyTabOrder;
		}		
		
	boolean isFormCreated;
		public boolean isFormCreated() {
			return isFormCreated;
		}
		public void setFormCreated(boolean isFormCreated) {
			this.isFormCreated = isFormCreated;
		}

	int groupingStyle = GROUPING_TABBED;
		public final static int GROUPING_TABBED = 1;
		public final static int GROUPING_DIVIDED = 0;
		public int getGroupingStyle() {
			return groupingStyle;
		}
		public void setGroupingStyle(int groupingStyle) {
			this.groupingStyle = groupingStyle;
		}

		
	int fieldOrdering = FIELD_ORDERING_REVERSAL;
		public final static int FIELD_ORDERING_SEQUENTIAL = 0;
		public final static int FIELD_ORDERING_REVERSAL = 1;
		public int getFieldOrdering() {
			return fieldOrdering;
		}
		public void setFieldOrdering(int fieldOrdering) {
			this.fieldOrdering = fieldOrdering;
		}
	
	
	Vector actionListeners = new Vector();
		public void addActionListener(ActionListener al){
			actionListeners.add(al);		
		}
		public void fireActionEvent(ActionEvent ae){
			for(Iterator iter = actionListeners.iterator(); iter.hasNext(); ){
				ActionListener al = (ActionListener)iter.next();
				al.actionPerformed(ae);
			}
		}
		public void actionPerformed(ActionEvent e) {
			isModified = true;
			fireActionEvent(e); //forward the child's events to listers.
		}
		
		
	public InputForm(){
		super();
	}
	
	/**
	 * @param table - 입력 폼을 만들 table
	 */
	public InputForm(Type table){
		super();
		this.type=table;
		
		createForm();
	}

	/**
	 * 초기 레코드를 얻음. 수정 폼인 경우만 존재하고 신규 폼인 경우는 null리턴
	 */
	public Instance getSourceInstance(){
		return instance;
	}
	
	public void createForm(){
		boolean groupExist = false;
		ArrayList groups = new ArrayList();
		Hashtable groupMembers = new Hashtable();
		final Hashtable collapseGroupOrNot = new Hashtable();
		final Hashtable groupMemberComponents = new Hashtable(); 

		FieldDescriptor[] fields=getType().getFieldDescriptors();
	
		for(int i=0; i<fields.length; i++){
			if(fields[i].getAttribute("hidden", null)==null){
				String group = (String)fields[i].getAttribute("group", null);
				Boolean collapseGroup = (Boolean)fields[i].getAttribute("collapseGroup", null);
				if(collapseGroup!=null)
					collapseGroupOrNot.put(group, collapseGroup);
				
				if(group==null) 
					group = (getType().getName() != null ? getType().getName() : "");
				else
					groupExist = true;
				
				if(!groupMembers.containsKey(group)){
					groups.add(group);
					groupMembers.put(group, new ArrayList());
					groupMemberComponents.put(group, new ArrayList());
				}
				
				ArrayList members = (ArrayList)groupMembers.get(group);
				members.add(fields[i]);	
			}
			
			Object groupingStyleOption = fields[i].getAttribute("groupingStyle", null);
			if(groupingStyleOption!=null && groupingStyleOption instanceof Number){
				groupingStyle = ((Number)groupingStyleOption).intValue();
			}

			Object fieldOrderingOption = fields[i].getAttribute("fieldOrdering", null);
			if(fieldOrderingOption!=null && fieldOrderingOption instanceof Number){
				fieldOrdering = ((Number)fieldOrderingOption).intValue();
			}
			
		}
		
		if(this.instance == null)
			instance = getType().createInstance();

		for(int i=0; i<fields.length; i++){
			final FieldDescriptor theField = fields[i];
			Inputter inputter=fields[i].getInputter();
			//inputter.initialize(fields[i].getAttributeTable());
			
			if(inputter instanceof InstanceSensitiveInputter){
				((InstanceSensitiveInputter)inputter).setInstance(instance, theField.getName());
			}
			
			final Dependancy dependancy = (Dependancy)fields[i].getAttribute("dependancy", null);
			if(dependancy!=null){
				final FieldDescriptor theDependantFD = type.getFieldDescriptor(dependancy.getDependantFieldName());
				theDependantFD.getInputter().addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent arg0) {
						dependancy.action(theField, theDependantFD);
					}
					
				});
			}
			
			inputter.addActionListener(this);

		}

		if(groupingStyle == GROUPING_TABBED){
			createFormTabbedGroup(groupExist, groups,	groupMembers, collapseGroupOrNot, groupMemberComponents);
		}else{
			createFormDividedGroup(groupExist, groups,	groupMembers, collapseGroupOrNot, groupMemberComponents);
		}	

		
		setFormCreated(true);
		
		if(this.instance != null){
			setInstance(this.instance);
		}
		
	}
	
	protected void createFormTabbedGroup(boolean groupExist,
			ArrayList groups,
			Hashtable groupMembers,
			final Hashtable collapseGroupOrNot,
			final Hashtable groupMemberComponents
			){
		
		setLayout(new BorderLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.ipadx=10;
		c.ipady=5;
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		int from;
		int to;
		int step;
		
		if(fieldOrdering==FIELD_ORDERING_SEQUENTIAL){
			from = 0;
			to = groups.size();
			step = 1;
		}else{
			from = groups.size()-1;
			to = -1;
			step = -1;
		}
				
		for(int i=from; i!=to; i+=step){
			int row = 0;

			final String groupName = (String)groups.get(i);
			ArrayList members = (ArrayList)groupMembers.get(groupName);
			final ArrayList memberComponents = (ArrayList)groupMemberComponents.get(groupName);

			
			JPanel panel = new JPanel();
			
			GridBagLayout gridbag;
			gridbag=new GridBagLayout();
			panel.setLayout(gridbag);
	  
			for(int j=0; j<members.size(); j++){
				row++;
				
				FieldDescriptor prop = (FieldDescriptor)members.get(j);
				
		       	prop.getInputter().initialize(prop.getAttributeTable());
		       	
				JLabel label;{
					label = new JLabel(prop.getDisplayName());
					c.gridx = 0;
					c.gridy = row;
					c.anchor= c.EAST;

	       		 	gridbag.setConstraints(label, c);
	       		 	
					memberComponents.add(label);	
	       		 	panel.add(label);
				}

				Component comp;{				
					comp=prop.getInputComponent();
					if(prop.getAttribute("disabled", null)!=null)
						comp.setEnabled(false);
					c.gridx = 1;
					c.anchor= c.WEST;
	       		 	gridbag.setConstraints(comp, c);

					memberComponents.add(comp);	
	       		 	panel.add(comp);
				}
				
				row++;
				
				JLabel errLabel;{				
					errLabel=new JLabel();
					errLabel.setFont(label.getFont().deriveFont(Font.ITALIC,10.0f));
					errLabel.setForeground(Color.RED);
					errLabel.setVisible(false);						
					c.gridx = 1;
					c.gridy = row;
					c.anchor= c.WEST;
	       		 	gridbag.setConstraints(errLabel, c);
		       		panel.add(errLabel);
		       		errLabels.put(prop.getName(), errLabel);
				}
			}
			
			tabbedPane.add(groupName, new JScrollPane(panel));
		}
		
		add("Center", tabbedPane);
	}
	
	protected void createFormDividedGroup(
			boolean groupExist,
			ArrayList groups,
			Hashtable groupMembers,
			final Hashtable collapseGroupOrNot,
			final Hashtable groupMemberComponents
			){

//		setLayout(new BorderLayout(20, 20));
		

		GridBagLayout gridbag;
		gridbag=new GridBagLayout();
		setLayout(gridbag);
  

//		JPanel panel = new JPanel(gridbag);
		JPanel panel = this;
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.ipadx=10;
		c.ipady=5;

		FieldDescriptor[] props=type.getFieldDescriptors();
		

		final ImageIcon moreArrowIcon = new ImageIcon(
				getClass().getClassLoader().getResource("org/metaworks/images/more-arrow.gif")
			);
		final ImageIcon collapseArrowIcon = new ImageIcon(
				getClass().getClassLoader().getResource("org/metaworks/images/collapse-arrow.gif")
			);
		
		if(groupExist){
			int row = 0;
			for(int i=0; i<groups.size(); i++){
				final String groupName = (String)groups.get(i);
				ArrayList members = (ArrayList)groupMembers.get(groupName);
				final ArrayList memberComponents = (ArrayList)groupMemberComponents.get(groupName);
				boolean collapseOrNot = (collapseGroupOrNot.containsKey(groupName) ? ((Boolean)collapseGroupOrNot.get(groupName)).booleanValue() : false);

				row++;
					
				{JPanel grpLabelPanel = new JPanel(new BorderLayout());
					//grpPanel.setBorder(BorderFactory.createEtchedBorder());
					//grpPanel.setBackground(Color.GRAY);
					
					final JLabel label;
					label = new JLabel(" " + groupName);
					label.setIcon(collapseOrNot ? moreArrowIcon : collapseArrowIcon );
					label.setHorizontalTextPosition(JLabel.LEFT);
					
					label.addMouseListener(new MouseAdapter(){

						public void mouseClicked(MouseEvent e) {
							boolean collapsed = collapseGroupOrNot.containsKey(groupName) ? ((Boolean)collapseGroupOrNot.get(groupName)).booleanValue() : false;
							boolean collapseOrNot = !collapsed;
							
							for(int i=0; i<memberComponents.size(); i++){
								JComponent component = (JComponent)memberComponents.get(i);
								component.setVisible(!collapseOrNot);
							}
							
							label.setIcon(collapseOrNot ? moreArrowIcon : collapseArrowIcon);
							collapseGroupOrNot.put(groupName, new Boolean(collapseOrNot));
						}
						
					});
					
					label.setFont(label.getFont().deriveFont(Font.BOLD,13.0f));
					label.setBackground(Color.GRAY);
					grpLabelPanel.add("West", label);
					grpLabelPanel.add("South", new Separator(false, Separator.STYLE_ETCHED));
					
					JPanel block = new JPanel();
					block.setPreferredSize(new Dimension(10,10));
					grpLabelPanel.add("North", block);
					
					c.weightx = 0.0;
					c.fill = GridBagConstraints.BOTH;
					c.gridwidth = GridBagConstraints.REMAINDER;
					c.gridx = 0;
					c.gridy = row;
					c.anchor= c.WEST;
	       		 	gridbag.setConstraints(grpLabelPanel, c);

	       		 	panel.add(grpLabelPanel);
					c.gridwidth = 1;
					c.fill = GridBagConstraints.NONE;
				}
				
				for(int j=0; j<members.size(); j++){
					row++;
					
					FieldDescriptor prop = (FieldDescriptor)members.get(j);
					
			       	prop.getInputter().initialize(prop.getAttributeTable());
			       	
					JLabel label;{
						label = new JLabel(prop.getDisplayName());
						c.gridx = 0;
						c.gridy = row;
						c.anchor= c.EAST;

		       		 	gridbag.setConstraints(label, c);
		       		 	
		       		 	if(collapseOrNot) label.setVisible(false);
		       		 	
						memberComponents.add(label);	
		       		 	panel.add(label);
					}

					Component comp;{				
						comp=prop.getInputComponent();
						if(prop.getAttribute("disabled", null)!=null)
							comp.setEnabled(false);
						c.gridx = 1;
						c.anchor= c.WEST;
		       		 	gridbag.setConstraints(comp, c);

		       		 	if(collapseOrNot) comp.setVisible(false);
		       		 	
						memberComponents.add(comp);	
		       		 	panel.add(comp);
					}
					
					row++;
					
					JLabel errLabel;{				
						errLabel=new JLabel();
						errLabel.setFont(label.getFont().deriveFont(Font.ITALIC,10.0f));
						errLabel.setForeground(Color.RED);
						errLabel.setVisible(false);						
						c.gridx = 1;
						c.gridy = row;
						c.anchor= c.WEST;
		       		 	gridbag.setConstraints(errLabel, c);
			       		panel.add(errLabel);
			       		errLabels.put(prop.getName(), errLabel);
					}
				}
			}
		}else{
			int row = 0;
			for(int i=0; i<props.length; i++){
				if(!(props[i].getAttribute("hidden", null)!=null)){
					row++;
					
			       	props[i].getInputter().initialize(props[i].getAttributeTable());

					JLabel label;
						label = new JLabel(props[i].getDisplayName());
						c.gridx = 0;
						c.gridy = row;
						c.anchor= c.EAST;

		       		 	gridbag.setConstraints(label, c);
		       		 	panel.add(label);

					Component comp;				
						comp=props[i].getInputComponent();
						if(props[i].getAttribute("disabled", null)!=null)
							comp.setEnabled(false);
						c.gridx = 1;
						c.anchor= c.WEST;
		       		 	gridbag.setConstraints(comp, c);
			       		panel.add(comp);
			       				       		
			       	row++;
			       	
					JLabel errLabel;{				
						errLabel=new JLabel();
						errLabel.setFont(label.getFont().deriveFont(Font.ITALIC,10.0f));
						errLabel.setForeground(Color.RED);
						errLabel.setVisible(false);						
						c.gridx = 1;
						c.gridy = row;
						c.anchor= c.WEST;
		       		 	gridbag.setConstraints(errLabel, c);
			       		panel.add(errLabel);
			       		errLabels.put(props[i].getName(), errLabel);
					}

				}
			}
		}
		

		
	}

	/**
	 * param keyObject - table에서 키 값으로 검색해 수정 폼을 만듦
	 */
	public InputForm(Type table, Object keyObject){

		this(table);

		if(keyObject instanceof Instance)
			instance = (Instance)keyObject;
		else{
			try{
				instance=new Instance(table, keyObject);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}


	public void addPropertyChangeListener(){
		// Beans service
	}

	/**
	 * 사용자가 입력한 폼의 내용을 Record로 리턴함. 얻은 Record는 record.save()하여 바로 DB에 저장가능

	 */
	public Instance getInstance(){
		
		if(this.instance==null)
			instance = getType().createInstance();

		FieldDescriptor[] fields=getType().getFieldDescriptors();

		boolean validationOk = true;
		for(int i=0; i<fields.length; i++){

			Object value = fields[i].getInputter().getValue();

			String [] validationResult = null;
			try{
				validationResult = fields[i].validate(value, instance);
			}catch(Exception e){
			}
			
			String errorMessage="";
         	Object def;
         	
         	JLabel errLabel  = null;
			if(errLabels.containsKey(fields[i].getName())){
				errLabel = (JLabel)errLabels.get(fields[i].getName());
			}
        
			if((def=fields[i].getAttribute("hidden", null))!=null){
				//record.setFieldValue(fields[i].getName(), def);
			}else
			if(validationResult!=null){

				for(int j=0; j<validationResult.length; j++){
					errorMessage += /*fields[i].getDisplayName() + ": "+*/validationResult[j]+"\n";
					System.out.println(validationResult[j]);
				}

				if(errLabel != null){
					errLabel.setText(errorMessage);
					errLabel.setVisible(true);
				}

				Component comp=fields[i].getInputComponent();

				if(comp instanceof JTextField){
					comp.requestFocus();
				}

				validationOk = false;
			}
			else{
				instance.setFieldValue(fields[i].getName(), value);
				if(errLabel != null){
					errLabel.setVisible(false);
				}
			}
		}
		
		if(validationOk)
			return instance;
		else
			return null;
	}
	
	/**
	 * 폼에 값들을 채워 넣음
	 */
	public void setInstance(Instance record){
		
		
		this.instance = record;
		
		if(!isFormCreated()) return;
			
		FieldDescriptor[] fields=getType().getFieldDescriptors();

		for(int i=0; i<fields.length; i++){
			Inputter inputter=fields[i].getInputter();
			//inputter.initialize(fields[i].getAttributeTable());
			
			String fieldName = fields[i].getName();
			
			if(inputter instanceof InstanceSensitiveInputter){
				((InstanceSensitiveInputter)inputter).setInstance(record, fieldName);
			}

			if(record!=null)
				try{
					if(fields[i].getAttribute("hidden", null)==null)
						inputter.setValue(record.getFieldValueObject(fieldName));
				}catch(Exception e){
					new Exception("error occurred when to display the value for field [" + fieldName + "]", e).printStackTrace();
				}
			else{	
				inputter.setValue(null);
			}
		}

	}
	
	public void dispose(){
		FieldDescriptor[] fields=getType().getFieldDescriptors();

		for(int i=0; i<fields.length; i++){
			Inputter inputter=fields[i].getInputter();
			
			if(inputter instanceof AbstractComponentInputter){
				((AbstractComponentInputter)inputter).dispose();
			}else
				inputter.setValue(null);
		}
				
		this.instance = null;
	}



	/**
	 * 폼을 입력받을 수 있는 JDialog를 생성해서 리턴해줌. 이 dialog는 저장, 수정, 취소등 모든 기능이 기본적으로 가능함.
	 * @param
	 * al - 버튼 클릭시 actionListener <br>
	 * saveTitle - 저장 버튼 타이틀 <br>
	 * updateTitle - 수정 버튼 타이틀 <br>
	 * windowTitle - 다이얼로그 윈도 타이틀 <br>
	 */
	public JDialog getInputDialog(JFrame owner, final ActionListener al, String saveTitle, String updateTitle, String windowTitle){
		JDialog dialog=new InputDialog(this, saveTitle, updateTitle, "cancel", windowTitle, owner);
	
		return dialog;
	}

	/**
	 * getInputDialog(...)로 만든 JDialog를 띄움.
	 */
	public JDialog postInputDialog(JFrame owner, String saveTitle, String updateTitle, ActionListener al, String windowTitle){
		
		JDialog temp = getInputDialog(owner, al, saveTitle, updateTitle, windowTitle);
		
		temp.setVisible(true);

		if(instance !=null) setInstance(instance);
		
		return temp;
	}
	
	public JDialog postInputDialog(JFrame owner, String saveTitle, String updateTitle, String windowTitle){
		return postInputDialog(owner, saveTitle, updateTitle, null, windowTitle);
	}
	
	public JDialog postInputDialog(JFrame owner, String saveTitle, String updateTitle, ActionListener al){
		return postInputDialog(owner, saveTitle, updateTitle, al, "New Record...");
	}
	
	public JDialog postInputDialog(JFrame owner, String saveTitle, String updateTitle){
		return postInputDialog(owner, saveTitle, updateTitle, null, "New Record...");
	}
	
	public JDialog postInputDialog(JFrame owner, ActionListener al){
		return postInputDialog(owner, "저장", "수정", al);
	}
	
	public JDialog postInputDialog(JFrame owner){
		return postInputDialog(owner, null);
	}



//////////////////// handler base catridges ////////////////////////
	
	/**
	 * getInputDialog(...)로 만든 dialog 에서 사용자가 저장 버튼을 클릭해 저장에 성공했을 때 불려짐.
	 */
	public void onSaveOK(Instance rec, JDialog dialog){
		setModified(false);
		dialog.dispose();	
		// catridge
	}
	
	/**
	 * getInputDialog(...)로 만든 dialog 에서 사용자가 수정 버튼을 클릭해 저장에 성공했을 때 불려짐.
	 */
	public void onUpdateOK(Instance rec, JDialog dialog){
		setModified(false);
		dialog.dispose();
		// catridge
	}
	
	public void onSaveFailed(Exception e, JDialog dialog){
		// catridge
	}
	
	public void onUpdateFailed(Exception e, JDialog dialog){
		// catridge
	}
	
	/**
	 * getInputDialog(...)로 만든 dialog 에서 사용자가 취소 버튼을 클릭했을 때 불려짐.
	 */
	public void onCancel(){
		// catridge
	}


//////////////// test /////////////////////

	public static void main(String args[]){

	try{
//		Thread.currentThread().getContextClassLoader().loadClass("sun.jdbc.odbc.JdbcOdbcDriver");
//		Connection con = DriverManager.getConnection("jdbc:odbc:WebSQL", "ihas", "ihas");

		Thread.currentThread().getContextClassLoader().loadClass("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@165.186.51.25:1574:cw_dev", "ihas", "ihas");




		final JFrame aFrame=new JFrame("test");



		Type testTable;

		final InputForm form = new InputForm(
			testTable=
			new Type("test",
				new FieldDescriptor[]{

/*
============================================================================================================
	Field Descriptions
------------------------------------------------------------------------------------------------------------
			name	title	type		iskey	attrs	inputter	viewer	validator
------------------------------------------------------------------------------------------------------------

*/
new FieldDescriptor(	"name",	"이름",	Types.VARCHAR,	true,
	new FieldAttribute[]{
		new FieldAttribute(FieldDescriptor.ATTR_DEFAULT, new String("default val")),
		new FieldAttribute(FieldDescriptor.ATTR_LENGTH, new Integer(50))
	},
								new TextInput()
),

new FieldDescriptor(	"age",	"나이",	Types.INTEGER),
new FieldDescriptor(	"gender","성별",Types.VARCHAR)

//=========================================================================================================

				},
				con
			),
			"장진영"
		);


		JButton but=new JButton("save");



		aFrame.getContentPane().add(form, BorderLayout.CENTER);
		aFrame.getContentPane().add(but, BorderLayout.SOUTH);





///////////////////////////// RecordTable test ///////////////////////////////////



		final RecordTableModel recTableModel = new RecordTableModel(testTable, new String[]{"name", "age", "gender"});

		JTable table = new JTable(recTableModel);
		ListSelectionModel selModel = table.getSelectionModel();

		///////  선택시 액션
		selModel.addListSelectionListener(new ListSelectionListener() {

		    public void valueChanged(ListSelectionEvent e) {
		        //Ignore extra messages.
		        if (e.getValueIsAdjusting()) return;

		        ListSelectionModel lsm =
		            (ListSelectionModel)e.getSource();

		        if (lsm.isSelectionEmpty()) {
		            //no rows are selected
		        } else {
		            int selectedRow = lsm.getMinSelectionIndex();
		            //selectedRow is selected

		            form.setInstance(recTableModel.getRecord(selectedRow));
		        }
		    }

		});


	        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
	        //Create the scroll pane and add the table to it.
	        JScrollPane scrollPane = new JScrollPane(table);


		aFrame.getContentPane().add(scrollPane, BorderLayout.EAST);


		but.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					Instance rec=form.getInstance();

					if(rec==null) return;

					rec.save();
					recTableModel.addRow(rec);

				}catch(Exception ex){ex.printStackTrace();}
			}
		});

///////////////////////////// save test ///////////////////////////////////////////////////
/*

		Record testRec=new Record(testTable);
		testRec.setFieldValues(
			new String[]{"name", 			"age", 			"gender"		},
			/////////////========================== ======================= ========================
			new Object[]{new String("장진영"),	new Integer(25),	new String("남자")	}

		);

		try{
			testRec.save();
		}catch(Exception e){
			e.printStackTrace();
		}

*/
///////////////////////////// load test ///////////////////////////////////////////////////

		aFrame.pack();
		aFrame.setVisible(true);

	}catch(Exception e){
		e.printStackTrace();
	}



	}



}
