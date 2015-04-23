package org.metaworks;

import java.util.*;
import java.sql.*;
import java.io.*;

/**
 * Record들을 관리하는 메서드들이 있으며, Field정보(FieldDescriptor)와 key field(primary key)등의 정보를 담고 있다가 Record에서 call하면 그 정보들을 조합하여 sql문을 만들어서 날린다.
 * @version 1.0 2000/2/14
 * 
 * @example
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
 * 	}
 * </pre>
 * @author 장진영
 * @see com.pongsor.dosql.Tuple
 */
 
public class Type extends AbstractTupleType implements Serializable, Flyweight{
	String tableName;
	String tableTitle;

	boolean bFullKeyMode = false;

	Connection conn;

//	ReferenceTable referenceTables[];

	/**
	 * 빈 table instance의 생성 (<b>주로</b> 디버깅용도)
	 */
	public Type(){
		super();
		
		//setFieldDescriptor(new FieldDescriptor("","",0));
	}

	/**
	 * @param table DB상의 table 이름
	 */
	public Type(String tableName){
		this();
		
		setName( tableName);
	}
	
	/**
	 * Connection만 가진 table (사용상 주의)
	 * @param con 이 table이 SQL을 처리할 때 사용할 JDBC 커넥션
	 */
	public Type(Connection con){
		this();
		
		setConnection(con);
	}
	
	public Type(String tableName, Connection con){
		this(tableName);
		setConnection(con);
	}

	/**
	 * 가장 흔히 쓰임
	 * @param fields DB Field(column) 정보들
	 */
	public Type(String tableName, FieldDescriptor fields[]/*, ReferenceTable refTables[]*/){
		super((AbstractPropertyDescriptor[])fields);

		this.tableName=tableName;

		//this.referenceTables=refTables;
	}

	public Type(String tableName, FieldDescriptor fields[]/*, ReferenceTable refTables[]*/, Connection conn){
		this(tableName, fields);

		this.conn=conn;
	}
	
	// accessors
	
	/**
	 * table에 제목을 설정한다. 사용치 않아도 무방하며 나중에 자동 UI 생성시 title로 활용된다.
	 */
	public void setTitle(String title){
		tableTitle = title;
	}
	public String getTitle(){
		if(tableTitle==null)
			tableTitle = getName();
		return tableTitle;
	}

	/**
	 * DB상의 table 명을 얻는다.
	 */
	public String getName(){
		return tableName;
	}
	
	/**
	 * DB상의 table 명을 지정한다.
	 */
	public void setName(String tn){
		setTitle(tn);
		tableName = tn;
	}
	
	/**
	 * FullKeyMode를 세팅한다. FullKeyMode 란 table이 가진 모든 필드를 키로 사용하는 처리모드이다. 초기치는 false.
	 */
	public void setFullKeyMode(boolean b){
		bFullKeyMode = b;
	}
	
	/**
	 * 현재 FullKeyMode인지를 얻는다.
	 */
	public boolean isFullKeyMode(){
		return bFullKeyMode;
	}


/////////////// overrides //////////////////////

//	public Tuple create(){
//		return (Tuple)(new Record());

//	}


/////////////// overloads ///////////////////

	/**
	 * Record를 저장. 왠만하면 {@link Instance#save() Record.save()}를 사용키 바람.
	 * @param rec 저장할 record (이 table의 record여야 에러 없이 돌아감)
	 */
	public void save(Instance rec) throws Exception{
//System.out.println("save in table called3");

		String sqlStmt="insert into " + getName();

		String columns="";
		String values="";

		Field fields[] = rec.getAllFields();

		for(int i=0; i<fields.length; i++)
			if(fields[i].getFieldDescriptor().isSavable()){
				
				String separator=(i+1==fields.length ? "":",");
				String wrapper=(fields[i].getType()==Types.VARCHAR ? "'":"");
	
				columns += fields[i].getName() + separator;
	
				System.out.println("==>"+fields[i].getValueObject());
	
				values += fields[i].toSQLExp() + separator;
			}

		sqlStmt += "("+columns+") values("+values+")";

		System.out.println(sqlStmt);
		

		///////////// SQL 처리

		if(conn==null) return;

		Statement stmt=conn.createStatement();
		int colAffected = stmt.executeUpdate(sqlStmt);
		
		stmt.close();

		if(colAffected==0) throw new Exception("can't save");
		

	}

	public void load(Instance rec, Object keyObject) throws Exception{
		if(!(keyObject instanceof Instance) && getKeyFieldDescriptor()==null) throw new Exception("primary key is not set");


		String sqlStmt="select ";

		String columns="";
		String values="";

		Field fields[] = rec.getAllFields();


		for(int i=0; i<fields.length; i++)
			if(fields[i].getFieldDescriptor().isLoadable()){		
				columns += ", " + fields[i].getName();
			}

		columns = columns.substring(", ".length());
		
		sqlStmt += columns;
		sqlStmt += " from "+getName();
		sqlStmt += getWhereSQL(keyObject);

		System.out.println(sqlStmt);

		///////////// SQL 처리
		if(conn==null) return;

		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sqlStmt);

		if(rs.next()){
			for(int i=0; i<fields.length; i++){			
				Object data = rs.getObject(fields[i].getName());
				if(data!=null)
					rec.setFieldValue(fields[i].getName(), data); //new String(data));
			}
			
			rs.close();
			stmt.close();
		}else{
			rs.close();
			stmt.close();
			throw new Exception("can't load");
		}
	}



/////////////// implements ///////////////////////

//	public Tuple[] find(TupleProperty keyProperty){
//		return (Tuple[])(new Record[10]);
//		return null;
//	}

	/**
	 * <b>Description copied from interface</b>: TupleType 
	 * tuple을 저장할 때의 operation
	 * @over save in class AbstractTupleType
	 * <b>Tags copied from interface</b>: TupleType 
	 * param tuple - 저장할 tuple instance
	 */
	public void save(Tuple tuple) throws Exception{
		save((Instance)tuple);
	}
	public void load(Tuple tuple, Object keyObject) throws Exception{
		load((Instance)tuple, keyObject);
	}

/////////////// mapping methods //////////////////

	/**
	 * table의 필드정보들을 가진 fieldDescriptor들을 얻는다.
	 */
	public FieldDescriptor [] getFieldDescriptors(){
		AbstractPropertyDescriptor[] props=getPropertyDescriptors();
		FieldDescriptor out[]=new FieldDescriptor[props.length];

		for(int i=0; i<props.length; i++)
			out[i]=(FieldDescriptor)props[i];

		return out;

	}
	
	public void setFieldOrder(String [] fieldNames){
		for(int i=fieldNames.length-1; i>=0; i--){
			FieldDescriptor fd = (FieldDescriptor) propertyDescriptors.get(fieldNames[i]);
			props.remove(fd);
			props.insertElementAt(fd, 0);
		}
	}

	public Hashtable getFieldDescriptorTable(){
		return getPropertyDescriptorTable();
	}

	/**
	 * Key로 사용되고 있는 fieldDescriptor 를 리턴한다.
	 */
	public FieldDescriptor getKeyFieldDescriptor(){
		return (FieldDescriptor)getKeyPropertyDescriptor();
	}
	/**
	 * key field를 세팅한다.
	 */
	public void setKeyFieldDescriptor(FieldDescriptor fd){
		keyPropertyDescriptor = fd;
	}
	public void setKeyField(String name){
		keyPropertyDescriptor = getFieldDescriptor(name);
	}
	
/////////////// appended methos //////////////////

	/**
	 * where 이하의 SQL문장을 생성한다.
	 * @param keyObject - - 1. Object 로 입력시 : where {keyField} = {keyObject} 식으로 생성
	 * 			- 2. Record 로 입력시 : record에 포함된 field 정보들을 and로 조합해서 SQL 생성
	 */
	public String getWhereSQL(Object keyObject) throws Exception{
		if(keyObject instanceof Instance){
		
			Instance rec = (Instance)keyObject;

			Field fields[] = rec.getAllFields();

			String temp="", sep="";
			for(int i=0; i<fields.length; i++){
				String sqlExp = fields[i].toSQLExp();
			
				temp +=sep+ fields[i].getName() + (sqlExp.equals("null") ? " is " : "=")+ sqlExp;
				sep=" and ";
			}
		
			if(temp.length() > 0) temp=" where "+temp;
				
			return temp;
		}else{
			FieldDescriptor keyFD= getKeyFieldDescriptor();
			return " where "+ keyFD.getName() + "=" + keyFD.toSQLExp(keyObject);
		}
	}


	public void update(Instance rec, Object keyObject) throws Exception{

		if(!(keyObject instanceof Instance) && getKeyFieldDescriptor()==null) throw new Exception("primary key is not set");

		String sqlStmt="update " + getName()+" set ";

		String pairs="";

		Field fields[] = rec.getAllFields();

		for(int i=0; i<fields.length; i++)
			if(getFieldDescriptor(fields[i].getName()).isUpdatable())
			{
				pairs += ", "+ fields[i].getName()+"="+fields[i].toSQLExp();
			}

			sqlStmt+= pairs.substring(2, pairs.length()) + getWhereSQL(keyObject);

		System.out.println(sqlStmt);

		///////////// SQL 처리

		if(conn==null) return;

		Statement stmt=conn.createStatement();
		int colAffected = stmt.executeUpdate(sqlStmt);
		
		stmt.close();

		if(colAffected==0) throw new Exception("can't update");

	}
	
	public void update(Instance rec) throws Exception{
			update(rec, rec.getKeyProperty().getValueObject());
	}

	public void delete(Object keyObject) throws Exception{
		if(conn==null) return;

		if(keyObject instanceof Instance && !isFullKeyMode()) keyObject = ((Instance)keyObject).getKeyProperty().getValueObject();
		if(getKeyFieldDescriptor()==null && !isFullKeyMode()) throw new Exception("primary key is not set");

		FieldDescriptor keyFieldDescriptor = getKeyFieldDescriptor();

		String sql = "delete " + getName() + getWhereSQL(keyObject);

		System.out.println(sql);

  		///////////// SQL 처리
		Statement stmt=conn.createStatement();

		int colAffected = stmt.executeUpdate(sql);
		
		stmt.close();
		
		if(colAffected==0) throw new Exception("can't delete");
	}
	
	/**
	 * 입력 조건문으로 검색한 결과를 record배열로 리턴한다.
	 * @param whereSQL - where 하위의 SQL문 예) "age=5 and parent is not null"
	 */
	public Instance[] find(String whereSQL) throws Exception{
		String sqlStmt="";
		
		sqlStmt += "select *";
		sqlStmt += " from "+getName();
		
		if( !whereSQL.equals("") && whereSQL != null)
		    sqlStmt += " where "+whereSQL;		

		System.out.println(sqlStmt);

		///////////// SQL 처리
		if(conn==null) return new Instance[]{};

		Vector records=new Vector();
		
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sqlStmt);
		
		FieldDescriptor [] fields = getFieldDescriptors();
		while(rs.next()){
			Instance rec = new Instance(this);
			
			for(int i=0; i<fields.length; i++){
				String data = rs.getString(fields[i].getName());
				if(data!=null)
					rec.setFieldValue(fields[i].getName(), new String(data));
			}
			
			records.addElement(rec);
		}
		
		rs.close();
		stmt.close();
		
		if(records.size() > 0){
			Instance temp[] = new Instance[records.size()];
			Enumeration enumeration = records.elements();
			int i=0;
			while(enumeration.hasMoreElements())
				temp[i++] = (Instance)enumeration.nextElement();

			return temp;
		}else{
			throw new Exception("can't load");
		}
	}	
	
	/**
	 * full SQL 문으로 검색한다. [주의] 검색 후에는 field Description 정보가 검색한 column정보로 바뀐다. <br>
	 * 예) findByFullSQL("select * from testtable"); -> 이후엔 'testtable'의 column정보로 field descriptors 가 바뀐다.
	 */
	public Instance[] findByFullSQL(String sqlStmt) throws Exception{
		return findByFullSQL(sqlStmt, null, false);
	}
	public Instance[] findByFullSQL(String sqlStmt, boolean bGetMetaData) throws Exception{
		return findByFullSQL(sqlStmt, null, bGetMetaData);
	}
	/**
	 * @param titles - 검색한 column들에 대한 display names
	 */
	public Instance[] findByFullSQL(String sqlStmt, String [] titles) throws Exception{
		return findByFullSQL(sqlStmt, null, false);
	}
	
	public Instance[] findByFullSQL(String sqlStmt, String [] titles, boolean bGetMetaData) throws Exception{
		return findByFullSQL(sqlStmt, titles, bGetMetaData, null);
	}
	
	public Instance[] findByFullSQL(String sqlStmt, String [] titles, boolean bGetMetaData, RecordListener listener) throws Exception{
		///////////// SQL 처리
		
System.out.println(">"+sqlStmt);
		
		if(conn==null) return new Instance[]{};

		Vector records=new Vector();
		
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sqlStmt);
				

	
		ResultSetMetaData rmeta=rs.getMetaData();
		
		
		if(bGetMetaData){
			// clears all field descriptors...
			clear();
			
	System.out.println("column count is "+rmeta.getColumnCount());
			
			for(int i=1; i<=rmeta.getColumnCount(); i++){
	
				
				String fieldName = new String(rmeta.getColumnName(i));
				int type = rmeta.getColumnType(i);
				int iSize = rmeta.getColumnDisplaySize(i);
				
	System.out.println("tn>"+ rmeta.getTableName(i));
	System.out.println("getCatalogName>"+ rmeta.getCatalogName( i)); 
	System.out.println("getColumnClassName>"+ rmeta.getColumnClassName( i) );
	System.out.println("getColumnDisplaySize>"+ rmeta.getColumnDisplaySize( i) );
	System.out.println("getColumnLabel>"+ rmeta.getColumnLabel( i) );
	System.out.println("getColumnName>"+ rmeta.getColumnName( i) );
	System.out.println("getColumnType>"+ rmeta.getColumnType( i) );
	System.out.println("getColumnTypeName>"+ rmeta.getColumnTypeName( i) );
	System.out.println("getPrecision>"+ rmeta.getPrecision( i) );
	System.out.println("getScale>"+ rmeta.getScale( i) );
	System.out.println("getSchemaName>"+ rmeta.getSchemaName( i) );
	System.out.println("getTableName>"+ rmeta.getTableName( i) );
	System.out.println("isAutoIncrement>"+ rmeta.isAutoIncrement( i) );
	System.out.println("isCaseSensitive>"+ rmeta.isCaseSensitive(i) );
	System.out.println("isCurrency>"+ rmeta. isCurrency( i) );
	System.out.println("isDefinitelyWritable>"+ rmeta. isDefinitelyWritable( i) );
	System.out.println("isNullable>"+ rmeta.isNullable( i ));
	System.out.println("isReadOnly>"+ rmeta. isReadOnly( i) );
	System.out.println("isSearchable>"+ rmeta. isSearchable( i) );
	System.out.println("isSigned>"+ rmeta.isSigned( i) );
	System.out.println(" isWritable>"+ rmeta.isWritable( i) );
				
				
				setFieldDescriptor(
					new FieldDescriptor(fieldName, (titles!=null && titles.length>(i-1) ? titles[i-1] : fieldName), type, false,
						new FieldAttribute[]{
							new FieldAttribute("size", new Integer(iSize))						
						}
					)
				);
				
			}
		}

		FieldDescriptor [] fields = getFieldDescriptors();

		while(rs.next()){
			
			Instance rec = new Instance(this);

			
			for(int i=0; i<fields.length; i++){

				Object data = rs.getObject(fields[i].getName());

				if(data!=null)
					rec.setFieldValue(fields[i].getName(), data);
			}
			
			records.addElement(rec);
			
			if(listener!=null)
				listener.onAddRecord(rec);
		}

		
		rs.close();
		stmt.close();
System.out.println("FindByFullSQL:end");		
		if(records.size() > 0){
			Instance temp[] = new Instance[records.size()];
			Enumeration enumeration = records.elements();
			int i=0;
			while(enumeration.hasMoreElements())
				temp[i++] = (Instance)enumeration.nextElement();

			return temp;
		}else{
			throw new Exception("can't load");
		}
		
		

	}
	

	/**
	 * field name들을 모두 얻는다.
	 */
	public String[] getFieldNames(){
		FieldDescriptor [] fdescs = getFieldDescriptors();

		String [] fnames = new String [fdescs.length];

		for(int i=0; i<fnames.length; i++){
			fnames[i]=fdescs[i].getName();
		}

		return fnames;
	}
	
	/**
	 * Field Descriptor를 얻는다.
	 * @param fieldName - 얻어낼 field descriptor의 field(column)명
	 */
	public FieldDescriptor getFieldDescriptor(String fieldName){
		return (FieldDescriptor)getPropertyDescriptor(fieldName);
	}
	
	/**
	 * field descriptor 를 추가한다.
	 */
	public void setFieldDescriptor(FieldDescriptor fieldDescriptor){
		setPropertyDescriptor(fieldDescriptor);
	}
	public void addFieldDescriptor(FieldDescriptor fieldDescriptor){
		setFieldDescriptor(fieldDescriptor);
	}
	public void removeFieldDescriptor(FieldDescriptor fieldDescriptor){
		if(!propertyDescriptors.contains(fieldDescriptor)) throw new RuntimeException("No such field descriptor '" + fieldDescriptor + "'.");
		
		propertyDescriptors.remove(fieldDescriptor);
		props.remove(fieldDescriptor);		
	}
	public void removeFieldDescriptor(String fieldName){
		FieldDescriptor fd = getFieldDescriptor(fieldName);
		if(fd == null) throw new RuntimeException("No such field descriptor named '" + fieldName + "'.");
		removeFieldDescriptor(fd);		
	}
	public void setFieldDescriptors(FieldDescriptor [] fieldDescriptors){
		setPropertyDescriptors(fieldDescriptors);
	}
	
	public Object getFieldAttribute(String fieldName, String attrKey){
		return getFieldDescriptor(fieldName).getAttributeTable().get(attrKey);
	}
	
	public Object setFieldAttribute(String fieldName, String attrKey, Object obj){
		return getFieldDescriptor(fieldName).getAttributeTable().put(attrKey, obj);
	}
	
	

	public Connection getConnection(){
		return conn;
	}
	
	public Instance createInstance(){
		return new Instance(this);
	}
	
	/**
	 * connection을 설정한다. <br>
	 * 예) setConnection("jdbc:oracle:thin:@165.186.51.28:1526:iman5","infodba","ckddnjs5");
	 */
	public void setConnection(Connection con){
		this.conn = con;
	}
			
	public void setConnection(String connstr, String id, String pw) throws Exception{
//		Thread.currentThread().getContextClassLoader().loadClass("oracle.jdbc.driver.OracleDriver");			
		setConnection(DriverManager.getConnection(connstr, id, pw));
	}		
	
	
	
	/////// Flyweight implements ///////////////////////////
	
	public Object getActualObject(){
		if(getComponentName()!=null){
			try{
				return org.metaworks.component.MetaWorksComponentCenter.getInstance().getComponent(getComponentName());
			}catch(Exception e){
				e.printStackTrace();
				
				return null;
			}
		}else
			return this;
	}
	
	String componentName;
	
		public String getComponentName(){
			return componentName;
		}
			
		public void setComponentName(String value){
			componentName = value;
		}
	
	
	// end /////////////////////////////////////////////////
		
}