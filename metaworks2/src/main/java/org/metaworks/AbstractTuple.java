package org.metaworks;


import java.util.*;

public abstract class AbstractTuple extends Hashtable implements Tuple{

	TupleType tupleType;
/*	Hashtable tupleProperties=new Hashtable();*/
	TupleProperty keyProperty;
	
	
	public AbstractTuple(TupleType type){
		super();
		
		try{
			this.tupleType=type;
		}catch(Exception e){e.printStackTrace();}
	}
	
	public AbstractTuple(TupleType type, TupleProperty keyProperty) throws Exception{
		this(type);
		
		load(keyProperty);
	}
	
	public AbstractTuple(TupleType type, TupleProperty[] properties){
		this(type);
		
		setProperties(properties);
	}

			

///////////////// implementations ///////////////////////
		
	public TupleProperty getKeyProperty(){
		return (TupleProperty)get(getTupleType().getKeyPropertyDescriptor().getName());
	}
	
	public TupleProperty[] getAllProperties(){
		TupleProperty[] returns = new TupleProperty[size()];
		
		Enumeration enumKeys=keys();
		
		int i=0;
		
		while(enumKeys.hasMoreElements()){
			String keyStr=(String)enumKeys.nextElement();
			returns[i++]=(TupleProperty)get(keyStr);
		}
		
		return returns;
	}
			
	public void setProperty(TupleProperty prop){
		if(prop==null) return;
		
		put(prop.getName(), prop);
	}

	public void setProperties(TupleProperty[] props){
		for(int i=0; i<props.length; i++)
			setProperty(props[i]);
	}
	
	public void save() throws Exception{
		TupleType type=getTupleType();
		
		type.save(this);
	}
	
	public void load(TupleProperty keyProperty) throws Exception{
		TupleType type=getTupleType();
		
		type.load(this, keyProperty);
	}
	
	public void load(Object keyObject) throws Exception{
		TupleType type=getTupleType();
		type.load(this, keyObject);
	}
	
	
///////////////// appended methods /////////////////////

	public TupleType getTupleType(){
		return tupleType;
	}

	public Hashtable getTuplePropertyTable(){
		return (Hashtable)this;
	}
	
///////////////// abstracts ////////////////////////	

}	
