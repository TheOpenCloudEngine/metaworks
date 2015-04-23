package org.metaworks;

import org.metaworks.contexts.*;
import java.lang.reflect.Method;
/**
 * column의 속성 정보를 표현하는 Class 
 * 
 * 
 * @version 1.0 2000/2/14 
 * 
 * @example
 * <pre>
 *  	public static void main(String args[]) throws Exception{
 * 
 * 
 * 		Thread.currentThread().getContextClassLoader().loadClass("oracle.jdbc.driver.OracleDriver");
 * 																								// ~/orawin95/network/admin/tnsnames.ora file 참조.
 * 		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@165.186.51.28:1526:iman5", "infodba", "ckddnjs5");
 * 		
 * 		// table을 생성한다. db의 'create'문을 연상
 * 		Table px_part_newparts = new Table(
 * 			"px_part_newparts",	//  table명
 * 						//  column		title		type		iskey
 * 			new FieldDescriptor[]{
 * 				new FieldDescriptor("SEQNO", 		"순번", 	Types.INTEGER,	true),
 * 				new FieldDescriptor("description",	"설명"),
 * 				new FieldDescriptor("developmentdate",	"개발일정", 	Types.DATE)
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
 * 		InputForm newForm = new InputForm(px_part_newparts);
 * 		
 * 		JFrame frame = new JFrame("test");
 * 		frame.setVisible(true);		
 * 		
 * 		
 * 		newForm.postInputDialog(frame);
 * 	}		
 * </pre>
 * @author 장진영 
 * @see com.cwpdm.util.db.Tuple
 */

import org.metaworks.validator.*;
import org.metaworks.inputter.*;

import org.metaworks.viewer.*;

import java.util.*;
import javax.swing.*;
import java.awt.Component;
import java.sql.Types;

public class FieldDescriptor extends AbstractPropertyDescriptor{

	//// 추가 속성들
	protected Hashtable attrs = new Hashtable(){
		public Object put(Object k, Object v){
			return super.put(k.toString().toUpperCase(), v);
		}
		public Object get(Object k){
			return super.get(k.toString().toUpperCase());
		}
		public boolean containsKey(Object k){
			return super.containsKey(k.toString().toUpperCase());
		}
	};

	public final static String ATTR_DEFAULT=	"default";
	public final static String ATTR_LENGTH=		"length";
	public final static String ATTR_REQUIRED=	"required";
	public final static String ATTR_FIXED=		"fixed";
	public final static String ATTR_FOREIGNKEY=	"foreign key";


	protected Inputter	inputter=null;
		/**
		 * 이 필드에 값을 입력받을 때 사용할 Swing Component를 리턴
		 */
		public Component getInputComponent(){
			Inputter inp=getInputter();
			return inp.getComponent();
		}
	
		/**
		 * 이 필드의 값을 입력받을 때 사용할 Inputter 를 세팅
		 */
		public void setInputter(Inputter inp){
			inputter = inp;
		}
	
	protected Viewer	viewer=null;
		public void setViewer(Viewer viewer_){
			this.viewer = viewer_;
		}
		public Viewer getViewer(){
			return this.viewer;
		}
	
	protected Validator[]	validators;
		public Validator[] getValidators() {
			return validators;
		}
		public void setValidators(Validator[] validators) {
			this.validators = validators;
		}
		
	protected Class		classType;
	
	boolean bUpdatable = true;
	boolean bSavable = true;
	boolean bLoadable = true;
	
	private static Hashtable typeMappingHT = new Hashtable();
	static{
		typeMappingHT.put(String.class, new Integer(Types.VARCHAR));
		typeMappingHT.put(Integer.class, new Integer(Types.INTEGER));
		typeMappingHT.put(int.class, new Integer(Types.INTEGER));
		typeMappingHT.put(Date.class, new Integer(Types.DATE));
		typeMappingHT.put(Calendar.class, new Integer(Types.DATE));
		typeMappingHT.put(Boolean.class, new Integer(Types.BOOLEAN));
		typeMappingHT.put(boolean.class, new Integer(Types.BOOLEAN));
	}


	public FieldDescriptor(String name, String displayName, int type, Object def, int leng, boolean required, boolean fixed){
		super(name, displayName, type);

		attrs.put(ATTR_DEFAULT, def);
		attrs.put(ATTR_LENGTH,	new Integer(leng));
		attrs.put(ATTR_REQUIRED,new Boolean(required));
		attrs.put(ATTR_FIXED,	new Boolean(fixed));
		
		initialize();

	}
	
	public FieldDescriptor(){}
	
	public FieldDescriptor(String name, Object[] properties){
		this(name);
		initializeProperties(properties);
	}
	
	public FieldDescriptor(String name){
		this(name, name);
	}
	
	public FieldDescriptor(String name, String displayName){
		this(name, displayName, 12);
	}
	
	public FieldDescriptor(String name, String displayName, int type){
		this(name, displayName, type, false);
	}

	public FieldDescriptor(String name, String displayName, int type, boolean iskey){
		this(name, displayName, type, iskey, null);
	}

	public FieldDescriptor(String name, String displayName, int type, boolean iskey, FieldAttribute[] fattrs){
		this(name, displayName, type, iskey, fattrs, null);
	}


	public FieldDescriptor(String name, String displayName, int type, boolean iskey, FieldAttribute[] fattrs, Inputter inputter){
		this(name, displayName, type, iskey, fattrs, inputter, null);
	}
	

	public FieldDescriptor(String name, String displayName, int type, boolean iskey, FieldAttribute[] fattrs, Inputter inputter, Validator[] inValidators){
		this(name, displayName, type, iskey, fattrs, inputter, inValidators, null);
	}

	public FieldDescriptor(String name, Class classType, Inputter inputter, Validator[] validators){
		this(name, name, -1, false, null, (inputter !=null ? inputter : new ObjectInput(classType)), validators);
		setType(classType);
	}
	
	public FieldDescriptor(String name, String displayName, int type, boolean iskey, FieldAttribute[] fattrs, Inputter inputter, Validator[] inValidators, Viewer viewer){
		super(name, displayName, type, iskey);


//System.out.println("in FieldDescriptor, inputter is  "+ inputter);		
		//attrs=new Hashtable();
		
		if(inValidators==null)
			this.validators=getDefaultValidators();
		else
			this.validators=inValidators;

		if(validators==null)
			this.validators=new Validator[]{};
			

		if(fattrs!=null)
		for(int i=0; i<fattrs.length; i++){
			Object obj = fattrs[i].getObject();
			
			if(obj!=null){
				attrs.put(fattrs[i].getKey(), obj);
			}
		}
		
		
		try{
			attrs.put("name", name);
		}catch(Exception e){}
		
		try{
			attrs.put("display name", displayName);
		}catch(Exception e){}
		
		try{
			attrs.put("type", new Integer(type));
		}catch(Exception e){}
		
		try{
			attrs.put("is key", new Boolean(iskey));
		}catch(Exception e){}
		try{
		
			attrs.put("validators", inValidators);
		}catch(Exception e){}
		
		try{
			attrs.put("inputter", inputter);
		}catch(Exception e){}

		
		this.inputter=inputter;
		this.viewer = viewer;
		
		initialize();
	}

	public void initialize(){
		if(isKey()){
			setUpdatable(false);
			
			return;
		}
		
		if(!((String)getAttribute("source", "not set")).equals("not set")){
			setUpdatable(false);			
		}
		if((getAttribute("dontcare", null))!=null){
			setUpdatable(false);
			setLoadable(false);
			setSavable(false);			
		}

	}	
		
	// Only used for "Statement generation" Mode	
	/**
	 * data type에 맞는 SQL 표현 식을 만들어 낸다.  
	 */
	public String toSQLExp(Object data){
		
		////// source 형이면 그 자체로 리턴
		Hashtable attrs;
		if((attrs=getAttributeTable()).containsKey("source")){
			return ""+attrs.get("source");
		}

		if(data==null) return "null";
		if(data instanceof String && ((String)data).equals("")) return "null";

		switch(getType()){
			case Types.VARCHAR:{
				String making = ""+data;
				
				making = making.replace('\'', '`');
				
				return "'"+ making +"'";
			}
			
			case Types.DATE:
			case 93:			
//System.out.println("FieldDescriptor.toSQLExp; Date안쪽 : "+data+"("+data.getClass()+")");
				if(data instanceof String){
					return data.toString();
				}
				
				if(!(data instanceof Date)) return null;
				
				java.sql.Date sqlDate=new java.sql.Date(((java.util.Date)data).getTime());
				
				String out= sqlDate.toString();
				
				return "to_date('"+out+"','YYYY-MM-DD')";
					

			case Types.INTEGER:
			case Types.LONGVARCHAR:
			case Types.BINARY:
			case Types.BIT:
			case Types.BLOB:	return ""+data;
		}

		return ""+data;
	}
	
	


/////////////////// implements /////////////////////////




/////////////////// appended methods ////////////////////

	public void setType(Class cls){
		super.setType(getMappingTypeOfClass(cls));
		setClassType(cls);
	}
	
	/**
	 * field를 표현하는 attribute 를 얻는다.
	 */
	public Object getAttribute(String keyVal){
		return getAttribute(keyVal, null);
	}
	public Object getAttribute(String keyVal, Object def){

		if(!attrs.containsKey(keyVal)) return def;
		
		Object temp=null;
				
		if(keyVal==null) temp = null;		
		else temp = attrs.get(keyVal);
				
		if(temp!= null){
			return temp;
		}
		else{
			return def;
		}
	}
	public void setAttribute(String keyVal, Object value){
		attrs.put(keyVal, value);
	}

	/**
	 * field를 표현하는 attribute 를 얻는다.
	 */
	public Hashtable getAttributeTable(){
		return attrs;
	}





	/**
	 * 이 필드의 값을 입력받을 때 사용될 Inputter를 얻음
	 */
	public Inputter getInputter(){
		/////// 없으면 default로 세트
		if(inputter==null){
			inputter=getDefaultInputter(getType());
			
			if(inputter==null)
				inputter = new TextInput();
		}

		return inputter;
	}

	/**
	 * Inputter가 세팅되어 있지 않은 경우 data type에 맞는 적절한 Inputter를 리턴해 줌
	 */
	public static Inputter getDefaultInputter(int dataType){
		
		switch(dataType){
			case Types.VARCHAR:	return new TextInput();
			
			case Types.DATE:
			case 93:		return new DateInput();
			
			case Types.INTEGER:
			case 2:			return new NumberInput();
			
			case Types.BOOLEAN:
							return new booleanInput();
		//	case Types.LONGVARCHAR:	return new TextAreaInput();
		//	case Types.BINARY:	return new FileInput();
		//	case Types.BIT:		return new CheckInput();
		//	case Types.BLOB:	return new FileInput();
		}

		return null;
	}
	
	public static Inputter getDefaultInputter(Class dataType){
		return getDefaultInputter(getMappingTypeOfClass(dataType));
	}	

	
	public static int getMappingTypeOfClass(Class cls){
		if(typeMappingHT.containsKey(cls)){
			return ((Integer)typeMappingHT.get(cls)).intValue();
		}		
		//return Types.VARCHAR;
		return -1;
	}

	public WebInputter getWebInputter(){
		/////// 없으면 default로 세트
		if(inputter==null || !(inputter instanceof WebInputter)){
			inputter=getDefaultWebInputter();
		}

		return (WebInputter)inputter;
	}
	
	public WebInputter getDefaultWebInputter(){
		
		switch(getType()){
			case Types.VARCHAR:	return new TextInput();
			
			case Types.DATE:
			case 93:		return new CalendarInput();
			
			case Types.INTEGER:
			case 2:			return new NumberInput();
			
			case Types.BOOLEAN:
							return new booleanInput();
		//	case Types.LONGVARCHAR:	return new TextAreaInput();
		//	case Types.BINARY:	return new FileInput();
		//	case Types.BIT:		return new CheckInput();
		//	case Types.BLOB:	return new FileInput();
		}

		return new TextInput();
	}

	public WebViewer getWebViewer(){
		/////// 없으면 default로 세트
		if(viewer==null || !(viewer instanceof WebViewer)){
			
System.out.println("returnning default web viewer:"+getName());
System.out.println("viwer is null ? :"+(viewer==null)+" a WebViwer ? "+(viewer instanceof WebViewer));
			viewer=getDefaultWebViewer();
		}


		return (WebViewer)viewer;
	}
	
	public WebViewer getDefaultWebViewer(){
		
		AbstractWebViewer defViewer = new AbstractWebViewer(){
			public String getViewerHTML(Instance rec, String colName){
				return "-"+rec.get(colName);
			}

			@Override
			public void setFace(String face) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getFace() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		return (WebViewer)defViewer;
	}
	

	/**
	 * 이 필드에 입력된 값들을 검사(invalidate)할 적절한 validator 들을 얻는다.
	 */
	public Validator[] getDefaultValidators(){

		switch(getType()){
			case Types.INTEGER:	return new Validator[]{ new NumberValid()};
		}

		return new Validator[]{};	// �ش� ������ Empty
	}

	/**
	 * 입력된 data가 이 필드의 validator들을 통해 validation한다.
	 */
	public String[] validate(Object data, Instance instance){
		Vector errors=new Vector();

		if(validators!=null)
		for(int i=0; i<validators.length; i++){
			String errMsg = validators[i].validate(data, instance);
			if(errMsg!=null){
				errors.addElement(errMsg);
			}
		}

		if(errors.size()==0) return null;

		String [] errstrs=new String[errors.size()];

		int i=0;
		Enumeration enumeration=errors.elements();
		while(enumeration.hasMoreElements()){
			errstrs[i++]=(String)enumeration.nextElement();
		}

		return errstrs;
	}
	
	/**
	 * Update 가능한 필드인가? 
	 */
	public boolean isUpdatable(){
		return bUpdatable;
	}
	public void setUpdatable(boolean yn){
		bUpdatable = yn;
	}
	
	/**
	 * save 가능한 필드인가? 
	 */
	public boolean isSavable(){
		return bSavable;
	}
	
	/**
	 * save 가능여부 세팅 - false로 세팅하면 insert 문장을 만들 때 제외
	 */
	public void setSavable(boolean yn){
		bSavable = yn;
	}
	
	/**
	 * select 가능한 필드인가? 
	 */
	public boolean isLoadable(){
		return bLoadable;
	}
	
	/**
	 * update 가능여부 세팅 - false로 세팅하면 update문장을 만들 때 제외
	 */
	public void setLoadable(boolean yn){
		bLoadable = yn;
	}
	
	public ForeignKey getForeignKey(){
		return (ForeignKey)getAttribute(ATTR_FOREIGNKEY);
	}
	public void setForeignKey(ForeignKey fk){
		setAttribute(ATTR_FOREIGNKEY, fk);
	}
		
	public void initializeProperties(Object[] props){
		FieldDescriptor destination = this;

		Class destCls = destination.getClass();

		Method[] methods = destCls.getMethods();
		Hashtable methodsHT = new Hashtable();
		
		for(int i = 0; i<methods.length; i++){
			if(methods[i].getParameterTypes().length == 1)
				methodsHT.put(methods[i].getName(), methods[i]);
		}

		String propName;
		Object propData;
		for(int i=0; i<props.length; i+=2){
			propName = (String)props[i];
			propData = props[i+1];
			
			propName = propName.substring(0,1).toUpperCase() + propName.substring(1, propName.length());
System.out.println("propName = "+propName + " propData = " +  propData + " propData.class " + propData.getClass());
			
			try{
				Method m = (Method)methodsHT.get("set" + propName);
				
System.out.println("method = "+m + "method.getParameters[0]" + m.getParameterTypes()[0] );

				m.invoke(destination, new Object[]{propData});
			}catch(Exception e){
				destination.setAttribute(propName, propData);
				e.printStackTrace();
			}
		}
	}	

	public Class getClassType() {
		return classType;
	}

	public void setClassType(Class class1) {
		classType = class1;
	}



}
