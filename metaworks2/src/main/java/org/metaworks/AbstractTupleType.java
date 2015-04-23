package org.metaworks;

import java.util.*;


	/**
	 * Table의 하부구조(base class)를 이루게 되고 {@link com.pongsor.dosql.Tuple Tuple}에 대한 Catalog 정보(meta-data)로 PropertyDescriptor라는 것을 member로 가진다.
	 * 이 PropertyDescriptor를 기반으로 {@link com.pongsor.dosql.Type Table} class에서는 SQL문을 만들때 column 정보로 활용한다.
	 * @author 장진영
	 */

public abstract class AbstractTupleType implements TupleType{

	AbstractPropertyDescriptor keyPropertyDescriptor;
        /**
         * Property 들의 인덱스 정보(순서)로 찾기 위해 존재
         */                  
	Vector props;
	/**
	 * Property 들을 key로 찾기위해 존재 (2개로 Holding 해도 메모리상의 address만 가지므로 Object는 Property 하나당 하나만 저장된다.
	 */
	Hashtable propertyDescriptors;

	public AbstractTupleType(){
		super();
		props=new Vector();
		propertyDescriptors=new Hashtable();
	}

	public AbstractTupleType(AbstractPropertyDescriptor props[]){
		this();
		setPropertyDescriptors(props);
	}
	
	public void setPropertyDescriptors(AbstractPropertyDescriptor props[]){
//System.out.println("in AbstractTupleType.setPropertyDescriptors ");		
		

		for(int i=0; i<props.length; i++){
			setPropertyDescriptor(props[i]);
		}
	}
	
	public void setPropertyDescriptor(AbstractPropertyDescriptor prop){
//System.out.println("in AbstractTupleType.setPropertyDescriptor, prop is  "+ prop.getName());		

		
		if(prop.isKey()) keyPropertyDescriptor=prop;

		/*props.addElement(prop);*/
		/*propertyDescriptors.*/put(prop.getName(), prop);
	}
	
	/**
	 * For synchronize inner Vector holds Properties for use of array-accessing(for serial access not key-based)
	 */
	protected Object put(Object key, Object obj){
		if(propertyDescriptors.containsKey(key)) return obj;
		
		props.addElement(obj);
		return propertyDescriptors.put(key, obj);
	}

	protected void clear(){
		props.clear();
		propertyDescriptors.clear();
	}
	
	protected Object remove(Object key){		
		Object obj = propertyDescriptors.remove(key);		
		props.remove(obj);	
		return obj;
	}

///////////////// implementations ///////////////////////
	public Tuple create(){
//		return new AbstractTuple(this);
		return null;
	}

	public AbstractPropertyDescriptor getKeyPropertyDescriptor(){
		return keyPropertyDescriptor;
	}


///////////////// appended methods //////////////////////

	public Hashtable getPropertyDescriptorTable(){
		return propertyDescriptors;
	}

	public AbstractPropertyDescriptor [] getPropertyDescriptors(){

		AbstractPropertyDescriptor [] out=new AbstractPropertyDescriptor[props.size()];
		Enumeration keyEnum = props.elements();
		int i=0;

		while(keyEnum.hasMoreElements()){
			out[i++]=(AbstractPropertyDescriptor)keyEnum.nextElement();
		}

		return out;
	}
	
	public AbstractPropertyDescriptor getPropertyDescriptor(String name){
		if(!propertyDescriptors.containsKey(name))
			return null;
			
		return (AbstractPropertyDescriptor)propertyDescriptors.get(name);
	}


////////////////// abstracts ///////////////////////

	abstract public void save(Tuple tuple) throws Exception;
	abstract public void load(Tuple tuple, Object keyObject) throws Exception;


}