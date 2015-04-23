package org.metaworks;

/**
 * {@link com.pongsor.dosql.FieldDescriptor FieldDescriptor}의 하부구조(base class)를 이루고 어떤 Property(DB column)에 대한 속성(Meta data)정보를 담는다.
 * (※ 소스가 간단하므로 열어보아도 좋을 듯 하다)
 * @author 장진영
 * @see com.pongsor.dosql.FieldDescriptor
 */
public abstract class AbstractPropertyDescriptor {
	
	public String displayName;
	public String name;
	public int type;
	public boolean iskey=false;
	


	public AbstractPropertyDescriptor(){}
	
	public AbstractPropertyDescriptor(String name, String displayName, int type){
		this.displayName=displayName;
		this.name=name;
		this.type=type;
	}
	
	public AbstractPropertyDescriptor(String name, String displayName, int type, boolean iskey){
		this(name, displayName, type);
		
		this.iskey=iskey;
	}

//////////////// member methods ////////////////////

	/**
	 * property (DB Column)의 실제명 얻기
	 * @return propery명(DB Column명)  예)"address"
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * property (DB Column)의 title 얻기
	 * @return propery title 명(DB Column 의 description)  예)"집주소"
	 */
	public String getDisplayName(){
		if(displayName==null)
			setDisplayName(getName());
		
		return displayName;
	}	
	
	public void setName(String s){
		name=s;
	}
	
	public void setDisplayName(String s){
		displayName=s;
	}

	/**
	 * 자신의 Type (DB domain)얻기
	 * @return 자신의 Type (DB domain)  예) java.sql.Types.VARCHAR = 문자열 type
	 * <br><b>See</b> java.sql.Types
	 */
	public int getType(){
		return type;
	}
	public void setType(int type){
		this.type = type;
	}
	
	/**
	 * 자신이 Key Property(DB primary key column)인지
	 */
	public boolean isKey(){
		return iskey;
	}



}