package org.metaworks;

import java.util.Hashtable;


/**
 * DB의 한 Row를 저장할 수 있는 Class. 흔히 Database에서 쓰는 말로 "Record"와 같은 뜻으로 생각하면 됩니다.
 * @author 장진영
 * @version 1.0 2000/2/14
 * @see com.pongsor.dosql.Tuple
 */
public class Instance extends AbstractTuple{

	public Instance(){
		this((Type)null);
	}
	/**
	 * @param	table 이 Record의 table(DB)
	 */
	public Instance(Type table){
		super((TupleType)table);
		
		
		///// 초기치 세팅
		
	
		FieldDescriptor fields[] = getFieldDescriptors();
		
		for(int i=0; i<fields.length; i++){
			Hashtable attrs = fields[i].getAttributeTable();
		
			if(attrs.containsKey(FieldDescriptor.ATTR_DEFAULT))
				setFieldValue(fields[i].getName(), attrs.get(FieldDescriptor.ATTR_DEFAULT));
				
			if(attrs.containsKey("source"))
				setFieldValue(fields[i].getName(), null);
			
			if(attrs.containsKey(FieldDescriptor.ATTR_FIXED)){
				
				setFieldValue(fields[i].getName(), attrs.get(FieldDescriptor.ATTR_FIXED));				
			}
		}

	}

	/**
	 * @param	keyObject Loading할 Key 값 - 세팅하면 바로 select문을 만들어 Loading함.<br>예) new Record(addressBook, "장진영") => "장진영"의 주소록 record를 읽어와서 담음
	 */
	public Instance(Type table, Object keyObject) throws Exception{
		this(table);

		FieldDescriptor [] props=table.getFieldDescriptors();

		for(int i=0; i<props.length; i++)
			setFieldValue(
				props[i].getName(),
				null
			);


		load(keyObject);

	}

	/**
	 * @param	fields column 내용을 constructor에서 바로 세팅하기 위함
	 */
	public Instance(Type table, Field[] fields){
		this(table);
		setFields(fields);
	}
	
	/**
	 * @param	rec 복사본을 생성하기 위한 원시 record.  예) new Record(someRecord) -> someRecord의 복사본 생성
	 */
	public Instance(Instance rec){
		this(rec.getType());
		setFields(rec);
	}

///////////////// overrides ///////////////////

	public Object put(Object key, Object val){
		if(getType()!=null){
			setFieldValue(""+key, val);
			
			return val;
		}else{
			return super.put(key, val);
		}
	}	
		


///////////// mappings to the base class //////////////////

	public Field[] getAllFields(){
		TupleProperty[] tupleProps=getAllProperties();

		Field[] fields=new Field[tupleProps.length];

		for(int i=0; i<tupleProps.length; i++)
			fields[i]=(Field)tupleProps[i];

		return fields;
	}


	public Type getType(){
		return (Type)getTupleType();
	}

	public void setField(Field field){
		setProperty((TupleProperty)field);
	}

	public void setFields(Field [] fields){
		for(int i=0; i<fields.length; i++)
			setField(fields[i]);
	}
	

////////////// appended methods /////////////////////

	public void setFields(Instance rec){
		FieldDescriptor fieldDescriptors[] = getFieldDescriptors();
		
		
		String fieldName;
		
		for(int i=0; i<fieldDescriptors.length; i++){
			setFieldValue(fieldName = fieldDescriptors[i].getName(), rec.getFieldValueObject(fieldName));
		}
	}

	/**
	 * Column의 data를 세팅
	 * @param	name column 명(FieldDescriptor에서 사용한 이름)
	 *		data 들어갈 data. 나중에 toString()에 의해 viewing됨
	 */
	public void setFieldValue(String name, Object data){
		TupleProperty prop=new Field(getType(), name, data);
		super.put(prop.getName(), prop);
	}

	public void setFieldValues(String names[], Object[] data){
		for(int i=0; i<data.length; i++)
			setFieldValue(names[i], data[i]);
	}


	public Field getField(String name){
		return (Field)getTuplePropertyTable().get(name);
	}

	/**
	 * 필드 값 얻기
	 * @param	name column(field)명
	 */
	public Object getFieldValueObject(String name){
		Field thisField=getField(name);
		
		if(thisField == null)
			return null;		
		else
			return thisField.getValueObject();
	}
	
	public Object getFieldValue(String name){
		return getFieldValueObject(name);
	}

	/**
	 * 이 Record를 포함 table에서 삭제. Key 값으로 delete문을 만들어 삭제함.
	 */
	public void delete() throws Exception{
		getType().delete(this);
	}

	public void update() throws Exception{
		getType().update(this);
	}
	
	public void update(Object keyObject) throws Exception{
		getType().update(this, keyObject);
	}

	/**
	 * 이 Record의 field 양식들을 얻는다.
	 */
	public FieldDescriptor[] getFieldDescriptors(){
		return getType().getFieldDescriptors();
	}

	/**
	 * 이 Record의 key 값을 얻는다.
	 */
	public Object getKeyFieldValue(){
		return getFieldValueObject( getType().getKeyFieldDescriptor().getName() );
	}
	
	/**
	 * 이 레코드의 내용을 모두 화면에 리스팅한다.(주로 디버깅용도)
	 */
	public void printProperties(){
		System.out.println("- Table name :"+getType().getName());
//		System.out.println("- Key Field  :"+getKeyFieldDescriptor().getName());
		
		FieldDescriptor fields[] = getFieldDescriptors();
		
		for(int i=0; i<fields.length; i++){
			System.out.println("- field["+fields[i].getName()+"] : "+getFieldValue(fields[i].getName()));
		}
	}

	/**
	 * 모든 field 값들을 비교해서 완전히 내용이 같은 record인지를 비교한다.
	 */
	public boolean equals(Instance rec){
		FieldDescriptor fields [] =getFieldDescriptors();
		
		for(int i=0; i<fields.length; i++)
			if(!((""+getFieldValueObject(fields[i].getName())).equals(""+rec.getFieldValueObject(fields[i].getName()))))
				return false;
				
		return true;
	}
	
	
	public String getKeyQueryString(){
		return "m_key_"+getType().getKeyFieldDescriptor().getName()+"="+getKeyFieldValue();
	}
	
/*	public String toString(){
		String fieldName=null;
	
		try{
			fieldName = getTable().getKeyFieldDescriptor().getName();
		}catch(Exception e){}
		
		if(fieldName==null)
			try{
				fieldName = getFieldDescriptors()[0].getName();
			}catch(Exception e){}
			
		if(fieldName!=null)
			return get(fieldName).toString();
		else
			return null;
	}*/
	
}
